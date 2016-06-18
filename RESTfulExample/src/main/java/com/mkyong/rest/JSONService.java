package com.mkyong.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mkyong.Track;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@Path("/json/metallica")
public class JSONService {

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Track getTrackInJSON() {

        Track track = new Track();
        track.setTitle("Enter Sandman");
        track.setSinger("Metallica");

        return track;

    }

    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTrackInJSON(Track track) throws FileNotFoundException, UnsupportedEncodingException {
        
        /*File file = new File("C:\test\the-file-name.txt");
        if(!file.exists()) {
            file.mkdirs();
        }*/
        
        PrintWriter writer = new PrintWriter("D:\\test\\the-file-name.txt", "UTF-8");
        writer.println(track.getSinger());
        writer.println(track.getTitle());
        writer.close();
        

        String result = "Track saved : " + track;
        return Response.status(201).entity(result).build();

    }

    @GET
    @Path("/get2")
    @Produces(MediaType.APPLICATION_JSON)
    public Track getTrackFromStored() {

        Track track = new Track();
        track.setTitle("Enter Sandman");
        track.setSinger("Metallica");

        return track;

    }
}
