/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mkyong.rest;

import com.mkyong.Teams;
import com.mkyong.FileHandling;
import com.mkyong.GameStatus;
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
    FileHandling fileHandling = new FileHandling();

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrackInJSON() throws JSONException, IOException {

        JSONObject torjus = fileHandling.readContentsOfFile(FILE_PATH);

        Teams teams = new Teams();
        teams.setBlueTeamReady(torjus.getString(BLUE));
        teams.setRedTeamReady(torjus.getString(RED));
        return Response.status(200).entity(teams).build();
    }

    @GET
    @Path("/gamestatus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGameStatusJSON() {

        GameStatus gameStatus = new GameStatus();
        String gamestatus = gameStatus.getGameStatus();
        return Response.status(200).entity(gamestatus).build();
    }

    @POST
    @Path("/reset")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response resetStatusJSON() throws JSONException, IOException {
        resetTeamStatus();
        return Response.status(201).build();
    }

    @POST
    @Path("/gamestatus")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setGameStatusJSON(GameStatus gameStatus) {
        JSONObject gameStatusFromServer = new JSONObject();

        gameStatus.getGameStatus();
        gameStatus.setGameStatus(gameStatus.getGameStatus());

        String result = gameStatus.toString();
        return Response.status(201).entity(result).build();
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
            ayla = fileHandling.readContentsOfFile("D:\\test\\teamstatus.txt");
            fileHandling.writeToFile("D:\\test\\originalFile.txt", ayla, false);
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

        fileHandling.writeToFile(FILE_PATH, ayla, false);
        String result = "Team status : " + "\n" + team;
        return Response.status(201).entity(result).build();
    }

    public void resetTeamStatus() throws JSONException, IOException {
        JSONObject reset = new JSONObject();

        reset.put(RED, "false");
        reset.put(BLUE, "false");
        fileHandling.writeToFile(FILE_PATH, reset, false);
    }
}
