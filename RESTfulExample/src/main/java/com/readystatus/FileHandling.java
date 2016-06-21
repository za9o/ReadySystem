/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.readystatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Torjus
 */
public class FileHandling {

    public JSONObject readContentsOfFile(String fileName) throws JSONException {

        JSONObject torjus = null;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String sCurrentLine = null;

            while ((sCurrentLine = br.readLine()) != null) {
                torjus = new JSONObject(sCurrentLine);
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
}
