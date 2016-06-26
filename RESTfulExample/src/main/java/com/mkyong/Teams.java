/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mkyong;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Torjus
 */
public class Teams {

    //@JsonProperty("blueTeam")
    String blueTeamReady;

    //@JsonProperty("redTeam")
    String redTeamReady;

    PrintWriter writer;

    public String getBlueTeamStatus() {
        if(blueTeamReady == null) {
            blueTeamReady = "false";
        }
        return blueTeamReady;
    }

    public String getRedTeamStatus() {
        if(redTeamReady == null) {
            redTeamReady = "false";
        }
        return redTeamReady;
    }

    public void setBlueTeamReady(String b) {
        this.blueTeamReady = b;

    }

    public void setRedTeamReady(String r) {
        this.redTeamReady = r;
    }

    @Override
    public String toString() {

        return "Blue team status: " + blueTeamReady + "\n"
                + "Red team status: " + redTeamReady;
    }
}
