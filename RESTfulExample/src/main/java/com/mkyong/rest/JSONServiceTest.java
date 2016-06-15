/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mkyong.rest;

import com.mkyong.Teams;
import com.mkyong.Track;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import static java.nio.file.Files.delete;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class JSONServiceTest {

    private static final String RED = "Red";
    private static final String BLUE = "Blue";
    private static final String FILE_PATH = "D:\\test\\teamstatus.txt";

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrackInJSON() throws JSONException, IOException {

        JSONObject torjus = readContentsOfFile(FILE_PATH);

        Teams teams = new Teams();
        //teams.setBlueTeamReady();
        teams.setBlueTeamReady(torjus.getString(BLUE));
        teams.setRedTeamReady(torjus.getString(RED));
        //resetTeamStatus();
        return Response.status(200).entity(teams).build();
    }
    
    @POST
    @Path("/reset")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetStatus() throws JSONException, IOException {
           resetTeamStatus();
        return Response.status(201).build();
    }

    @POST
    @Path("/status")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTrackInJSON(Teams team) throws FileNotFoundException, UnsupportedEncodingException, JSONException, IOException {

        JSONObject ayla = new JSONObject();

        JSONObject linn = new JSONObject();
        linn.put(RED, team.getRedTeamStatus());
        linn.put(BLUE, team.getBlueTeamStatus());

        File file = new File(FILE_PATH);
        if (file.exists()) {
            ayla = readContentsOfFile("D:\\test\\teamstatus.txt");
            writeToFile("D:\\test\\originalFile.txt", ayla, false);
        } else {
            ayla.put(BLUE, "false");
            ayla.put(RED, "false");
        }

        if ("true".equalsIgnoreCase(team.getBlueTeamStatus())) {
            ayla.put(BLUE, "true");
        }

        if ("true".equalsIgnoreCase(team.getRedTeamStatus())) {
            ayla.put(RED, "true");
        }

        writeToFile(FILE_PATH, ayla, false);
        String result = "Team status : " + "\n" + team;
        return Response.status(201).entity(result).build();
    }

    public JSONObject readContentsOfFile(String fileName) throws JSONException {

        JSONObject torjus = null;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String sCurrentLine = null;

            while ((sCurrentLine = br.readLine()) != null) {
                torjus = new JSONObject(sCurrentLine);
//                    torjus.put("Yellow", "false");
//                    torjus.put("Yellow", "true");

//                String blueReady = torjus.getString(BLUE);
//                String redReady = torjus.getString(RED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return torjus;
    }

    public void writeToFile(String filePath, JSONObject writeThis, boolean appendToFile) throws IOException {
        
        FileWriter fileWriter = new FileWriter(filePath, appendToFile);

        try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(writeThis.toString());
        }
    }
    
    public void resetTeamStatus() throws JSONException, IOException {
        JSONObject reset = new JSONObject();
        
        reset.put(RED, "false");
        reset.put(BLUE, "false");
        writeToFile(FILE_PATH, reset, false);
    }
}
