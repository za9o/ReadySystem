/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.readystatus.rest;

import com.readystatus.ErrorMessage;
import com.readystatus.Teams;
import com.readystatus.SQLConnectionHandler;
import com.readystatus.FileHandling;
import com.readystatus.IDGenerator;
import com.readystatus.GPSValues;
import com.readystatus.HandleGameStart;
import com.readystatus.Security;
import com.readystatus.SuccessMessages;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.spec.InvalidKeySpecException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Torjus
 */
@Path("/post")
public class JSONServicePostMethods {

    private static final String RED = "Red";
    private static final String BLUE = "Blue";
    private static final String FILE_PATH = "D:\\test\\teamstatus.txt";
    private URL SOUND_FILE_PATH = this.getClass().getClassLoader().getResource("Sound/Air-Horn.wav");
    private static final String WAITINGStatus = "Waiting";
    FileHandling fileHandling = new FileHandling();
    SQLConnectionHandler sqlConnect = new SQLConnectionHandler();
    IDGenerator idCreator = new IDGenerator();
    Security security = new Security();
    //HandleGameStart preGameHandler = new HandleGameStart();

    @POST
    @Path("/setgpsposition")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGPSPositionJSON(GPSValues gpsValues) throws JSONException {

        sqlConnect.updateGPSPositionDB(gpsValues.getGPSID(), gpsValues.getTeamvalue(), gpsValues.getLatitude(), gpsValues.getLongitude());
        String result = "gps position saved : " + gpsValues;
        return Response.status(201).entity(result).build();
    }

    @POST
    @Path("/registernewuser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewUser(GPSValues gpsValues) throws JSONException, IOException, InvalidKeySpecException {
        int gpsId = idCreator.idForNewUser();
        byte[] salt = security.generateNewSalt();
        
        if (sqlConnect.insertNewUserDB(gpsId, gpsValues.getUsername(), security.generatePasswordNewUser(gpsValues.getPassword(), salt), salt)) {
            return Response.status(201).entity(new SuccessMessages("User created", gpsId)).build();
        } else {
            return Response.status(400).entity(new ErrorMessage("Username already in use")).build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(GPSValues gpsValues) throws JSONException, IOException, InvalidKeySpecException {
//        int gpsId = sqlConnect.loginDB(gpsValues.getUsername(), gpsValues.getPassword());
        int gpsId = -1;
        
        if (sqlConnect.checkIfUsernameExists(gpsValues.getUsername())){
            String salt = sqlConnect.getSaltFromDB(gpsValues.getUsername());

            String[] byteValues = salt.substring(1, salt.length() - 1).split(",");
            byte[] saltConvertedToBytes = new byte[byteValues.length];

            for (int i = 0, len = saltConvertedToBytes.length; i < len; i++) {
                saltConvertedToBytes[i] = Byte.parseByte(byteValues[i].trim());
            }

            boolean correctPassword = security.isExpectedPassword(gpsValues.getPassword().toCharArray(), saltConvertedToBytes, sqlConnect.getPasswordFromDB(gpsValues.getUsername()));
            if (correctPassword) {
                gpsId = sqlConnect.getGPSIDDB(gpsValues.getUsername());
            }
            if (gpsId > -1) {
                return Response.status(201).entity(new SuccessMessages("Correct login ", gpsId)).build();
            } else {
                return Response.status(400).entity(new ErrorMessage("Wrong credentials")).build();
            }
        } else {
            return Response.status(400).entity(new ErrorMessage("Username does not exist")).build();
        }
    }

    @POST
    @Path("/status")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response readTeamStatusFromClientsInJSON(Teams team) throws FileNotFoundException, UnsupportedEncodingException, JSONException, IOException {

        JSONObject teamStatusJson = new JSONObject();

        JSONObject newTeamStatusJson = new JSONObject();
        newTeamStatusJson.put(RED, team.getRedTeamStatus());
        newTeamStatusJson.put(BLUE, team.getBlueTeamStatus());

        File file = new File(FILE_PATH);
        if (file.exists()) {
            teamStatusJson = fileHandling.readContentsOfFile("D:\\test\\teamstatus.txt");
            fileHandling.writeToFile("D:\\test\\originalFile.txt", teamStatusJson, false);
        } else {
            teamStatusJson.put(BLUE, "false");
            teamStatusJson.put(RED, "false");
        }

        if ("true".equalsIgnoreCase(team.getBlueTeamStatus())) {
            teamStatusJson.put(BLUE, "true");
            sqlConnect.updateTeamStatusDB("blueTeamStatus", 1);
            new HandleGameStart(0, 1);
        }

        if ("true".equalsIgnoreCase(team.getRedTeamStatus())) {
            teamStatusJson.put(RED, "true");
            sqlConnect.updateTeamStatusDB("redTeamStatus", 1);
            new HandleGameStart(0, 1);
        }

        fileHandling.writeToFile(FILE_PATH, teamStatusJson, false);
        String result;
        if (sqlConnect.areBothTeamsReadyDB()) {
            result = "true";
            //preGameHandler.startGame();
            new HandleGameStart(10, 2);
        } else {
            sqlConnect.updateGameStatusDB(WAITINGStatus);
            result = "false";
        }

        return Response.status(201).entity(result).build();
    }

    @POST
    @Path("/reset")
    @Consumes(MediaType.TEXT_PLAIN + ";charset=UTF-8")
    public Response resetStatusJSON() throws JSONException, IOException {
        resetTeamStatus();
        return Response.status(201).build();
    }

    public void resetTeamStatus() throws JSONException, IOException {
        JSONObject reset = new JSONObject();

        reset.put(RED, "false");
        reset.put(BLUE, "false");
        sqlConnect.resetTeamStatusDB();
        sqlConnect.updateGameStatusDB(WAITINGStatus);
        fileHandling.writeToFile(FILE_PATH, reset, false);
    }
}
