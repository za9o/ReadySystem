/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.readystatus.rest;

import com.readystatus.ErrorMessage;
import com.readystatus.FileHandling;
import com.readystatus.IDGenerator;
import com.readystatus.HandleGameStart;
import com.readystatus.SQLConnectionHandler;
import com.readystatus.SuccessMessages;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.ws.rs.GET;
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
@Path("/get")
public class JSONServiceGetMethods {

    private static final String APK_FILE_PATH = "D:\\GitHubRepos\\AndroidApp\\IntentExample2\\app\\build\\outputs\\apk\\app-debug.apk";
    private URL SOUND_FILE_PATH = this.getClass().getClassLoader().getResource("Sound/Air-Horn.wav");
    FileHandling fileHandling = new FileHandling();
    SQLConnectionHandler sqlConnect = new SQLConnectionHandler();
    IDGenerator idCreator = new IDGenerator();
    //HandleGameStart preGameHandler = new HandleGameStart();

    @GET
    @Path("/checkconnectivity")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConnectivity() throws JSONException, IOException {

        return Response.status(200).entity("Connected").build();
    }

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeamStatusJSON() throws JSONException, IOException {

//        JSONObject torjus = fileHandling.readContentsOfFile(FILE_PATH);
        JSONObject teamStatusJson = sqlConnect.getTeamStatusValuesDB();

//        Teams teams = new Teams();
//        teams.setBlueTeamReady(torjus.getString(BLUE));
//        teams.setRedTeamReady(torjus.getString(RED));
//        torjus.put(RED, teams.getRedTeamStatus());
//        torjus.put(BLUE, teams.getBlueTeamStatus());
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(teamStatusJson).build();
    }

    @GET
    @Path("/getapk")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile() {
        File file = new File(APK_FILE_PATH);
        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=BattleNetTracker.apk");
        return response.build();
    }

    @GET
    @Path("/getgpsposition")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGpsPositionAll() throws JSONException, IOException {

        JSONArray GPSMode = sqlConnect.getGPSPositionAllUnits();
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(GPSMode).build();
    }
    @GET
    @Path("/gamestatus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGameStatus() {
        
        
        return Response.status(200).entity(sqlConnect.getGameStatusDB()).build();

//        if (sqlConnect.areBothTeamsReadyDB()) {
//            return Response.status(200).entity(new SuccessMessages("Game started")).build();
//        } else {
//            return Response.status(400).entity(new ErrorMessage("Game has not started")).build();
//        }
    }

}
