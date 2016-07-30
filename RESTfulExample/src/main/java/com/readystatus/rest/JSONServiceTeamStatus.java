/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.readystatus.rest;

import com.readystatus.Teams;
import com.readystatus.SQLConnectionHandler;
import com.readystatus.FileHandling;
import com.readystatus.GPSHandler;
import com.readystatus.GPSValues;
import com.readystatus.track;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Torjus
 */
@Path("/json/teams")
public class JSONServiceTeamStatus {

    private static final String RED = "Red";
    private static final String BLUE = "Blue";
    private static final String FILE_PATH = "D:\\test\\teamstatus.txt";
    FileHandling fileHandling = new FileHandling();
    SQLConnectionHandler sqlConnect = new SQLConnectionHandler();
    GPSHandler idCreator = new GPSHandler();

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeamStatusJSON() throws JSONException, IOException {

//        JSONObject torjus = fileHandling.readContentsOfFile(FILE_PATH);
        JSONObject torjus = sqlConnect.getTeamStatusValuesDB();

//        Teams teams = new Teams();
//        teams.setBlueTeamReady(torjus.getString(BLUE));
//        teams.setRedTeamReady(torjus.getString(RED));
//        torjus.put(RED, teams.getRedTeamStatus());
//        torjus.put(BLUE, teams.getBlueTeamStatus());
        System.out.println(torjus);
        return Response.status(200).entity(torjus).build();
    }

    @GET
    @Path("/setgpsid")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGPSID() throws JSONException, IOException {

        JSONObject GPSMode = idCreator.IDgenerator();
        return Response.status(200).entity(GPSMode).build();
    }

    @GET
    @Path("/getgpsposition")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGpsPositionAll() throws JSONException, IOException {

        JSONArray GPSMode = sqlConnect.getGPSPositionAllUnits();
        return Response.status(200).entity(GPSMode).build();
    }

    @POST
    @Path("/setgpsname")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setGPSNameJSON(GPSValues gpsValues) throws JSONException {
        //TODO
        //Should be used to set the GPS name, either before or after the GPSID has been set. 
        return Response.status(201).build();
    }

    @POST
    @Path("/setgpsposition")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGPSPositionJSON(GPSValues gpsValues) throws JSONException {
        sqlConnect.updateGPSPositionDB(gpsValues.getGPSID(), gpsValues.getLatitude(), gpsValues.getLongitude());

        String result = "Track saved : " + gpsValues;
        return Response.status(201).entity(result).build();
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
            System.out.println("blue team status true");
            teamStatusJson.put(BLUE, "true");
            sqlConnect.updateTeamStatusDB("blueTeamStatus", 1);
        }

        if ("true".equalsIgnoreCase(team.getRedTeamStatus())) {
            System.out.println("red team status true");
            teamStatusJson.put(RED, "true");
            sqlConnect.updateTeamStatusDB("redTeamStatus", 1);
        }

        fileHandling.writeToFile(FILE_PATH, teamStatusJson, false);
        String result = "Team status : " + "\n" + team;
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
        fileHandling.writeToFile(FILE_PATH, reset, false);
    }
}
