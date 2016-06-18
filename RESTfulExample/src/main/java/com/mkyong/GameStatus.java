/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mkyong;

import java.io.PrintWriter;

/**
 *
 * @author Torjus
 */
public class GameStatus {
    
    //@JsonProperty("redTeam")
    String gameStatus;

    PrintWriter writer;


    public String getGameStatus() {
//        if(gameStatus == null) {
//            gameStatus = "false";
//        }
        return gameStatus;
    }


    public void setGameStatus(String r) {
        this.gameStatus = r;
    }

    @Override
    public String toString() {

        return "Game status: " + gameStatus;
    }
}
