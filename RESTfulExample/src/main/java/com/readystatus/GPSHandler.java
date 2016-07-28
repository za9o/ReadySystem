/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.readystatus;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Torjus
 */
public class GPSHandler {

    public int[] idArray = new int[100];
    JSONObject GPSIDJson = new JSONObject();
    SQLConnectionHandler sqlConnect = new SQLConnectionHandler();
    FileHandling fileHandling = new FileHandling();
    JSONObject GPSMode = new JSONObject();
    int GPSCounter;
    int newFreeGPSID;

//    public JSONObject IDgenerator() throws IOException {
//
//        for (int i = 1; i < idArray.length; i++) {
//            try {
//                GPSMode = sqlConnect.getGPSIDDB(GPSCounter);
//
//                if (GPSMode.has("gpsid")) {
//                    System.out.println("GPSID exists! " + GPSMode);
//                    GPSCounter++;
//                    GPSMode.remove("gpsid");
//                    IDgenerator();
//                } else if (!GPSMode.has("gpsid")) {
//                    System.out.println("ID Given to new unit" + GPSCounter);
//                    GPSMode.put("gpsid", GPSCounter);
//                    sqlConnect.insertGPSIDDB(GPSCounter,"test", "password");
//                    break;
//                    //return GPSMode;
//                }
//                break;
//            } catch (JSONException ex) {
//                Logger.getLogger(GPSHandler.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
//        return GPSMode;
//    }
    
        public int IDgenerator() throws IOException {

        for (int i = 1; i < idArray.length; i++) {
            try {
                GPSMode = sqlConnect.getGPSIDDB(GPSCounter);

                if (GPSMode.has("gpsid")) {
                    System.out.println("GPSID exists! " + GPSMode);
                    GPSCounter++;
                    GPSMode.remove("gpsid");
                    IDgenerator();
                } else if (!GPSMode.has("gpsid")) {
                    System.out.println("ID Given to new unit" + GPSCounter);
                    newFreeGPSID = GPSCounter;
                    //GPSMode.put("gpsid", GPSCounter);
                    //sqlConnect.insertGPSIDDB(GPSCounter,"test", "password");
                    break;
                    //return GPSMode;
                }
                break;
            } catch (JSONException ex) {
                Logger.getLogger(GPSHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return newFreeGPSID;
    }
}
