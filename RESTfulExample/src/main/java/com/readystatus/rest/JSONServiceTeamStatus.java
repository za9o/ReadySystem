/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.readystatus.rest;

import com.readystatus.Teams;
import com.readystatus.SQLConnection;
import com.readystatus.FileHandling;
import com.readystatus.TeamStatusValue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
@Path("/json/teams")
public class JSONServiceTeamStatus {

    private static final String RED = "Red";
    private static final String BLUE = "Blue";
    private static final String FILE_PATH = "D:\\test\\teamstatus.txt";
    FileHandling fileHandling = new FileHandling();
    SQLConnection sqlConnect = new SQLConnection();
    TeamStatusValue teams = new TeamStatusValue();

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrackInJSON() throws JSONException, IOException {

//        JSONObject torjus = fileHandling.readContentsOfFile(FILE_PATH);
        JSONObject torjus = sqlConnect.getTeamStatusValues();

//        Teams teams = new Teams();
//        teams.setBlueTeamReady(torjus.getString(BLUE));
//        teams.setRedTeamReady(torjus.getString(RED));
//        torjus.put(RED, teams.getRedTeamStatus());
//        torjus.put(BLUE, teams.getBlueTeamStatus());
        System.out.println(torjus);
        return Response.status(200).entity(torjus).build();
    }

    @POST
    @Path("/reset")
//    @Consumes(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN + ";charset=UTF-8")
    public Response resetStatusJSON() throws JSONException, IOException {
        resetTeamStatus();
        return Response.status(201).build();
        //return Response.ok().header("Content-Type", "text/plain;charset=UTF-8").build();
    }

    @POST
    @Path("/status")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response readTeamStatusFromClientsInJSON(Teams team) throws FileNotFoundException, UnsupportedEncodingException, JSONException, IOException {

        JSONObject ayla = new JSONObject();

        JSONObject linn = new JSONObject();
        linn.put(RED, team.getRedTeamStatus());
        linn.put(BLUE, team.getBlueTeamStatus());

        File file = new File(FILE_PATH);
        if (file.exists()) {
            ayla = fileHandling.readContentsOfFile("D:\\test\\teamstatus.txt");
            fileHandling.writeToFile("D:\\test\\originalFile.txt", ayla, false);
        } else {
            ayla.put(BLUE, "false");
            ayla.put(RED, "false");
        }

        if ("true".equalsIgnoreCase(team.getBlueTeamStatus())) {
            System.out.println("blue team status true");
            ayla.put(BLUE, "true");
            sqlConnect.insertIntoDB("blueTeamStatus", 1);
        }

        if ("true".equalsIgnoreCase(team.getRedTeamStatus())) {
            System.out.println("red team status true");
            ayla.put(RED, "true");
            sqlConnect.insertIntoDB("redTeamStatus", 1);
        }

        fileHandling.writeToFile(FILE_PATH, ayla, false);
        String result = "Team status : " + "\n" + team;
        return Response.status(201).entity(result).build();
    }

    public void resetTeamStatus() throws JSONException, IOException {
        JSONObject reset = new JSONObject();

        reset.put(RED, "false");
        reset.put(BLUE, "false");
        sqlConnect.resetDB();
        fileHandling.writeToFile(FILE_PATH, reset, false);
    }
}
