/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.readystatus;

/**
 *
 * @author Torjus
 */
public class SuccessMessages {
    
    private String successMessage;
    private int inputgpsid;
    
    public SuccessMessages(String successMessage) {
        this.successMessage = successMessage;
    }
    
    public SuccessMessages(String successMessage, int inputgpsid) {
        this.successMessage = successMessage;
        this.inputgpsid = inputgpsid;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public int getInputgpsid() {
        return inputgpsid;
    }

    public void setInputgpsid(int inputgpsid) {
        this.inputgpsid = inputgpsid;
    }
    
}
