package com.readystatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sun.jmx.snmp.daemon.CommunicationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class SQLConnection {

    static final String DRIVER = "org.mariadb.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mariadb://localhost:3306/paintballsystem";
    JSONObject teamStatusValuesJson = new JSONObject();

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    public SQLConnection() {

    }

    public void insertIntoDB(String teamName, int teamStatus) {

        PreparedStatement prepStmt = null;
        int teamStatusInverted;
        try {
            Class.forName(DRIVER);
            connection = setupConnection();

            if (teamStatus == 1) {
                teamStatusInverted = 0;
            } else {
                teamStatusInverted = 1;
            }
            String query = "UPDATE `team2` SET `" + teamName + "`=? WHERE `" + teamName + "`=?";
            prepStmt = connection.prepareStatement(query);
            prepStmt.setInt(1, teamStatus);
            prepStmt.setInt(2, teamStatusInverted);
//            prepStmt.setString(teamStatus, query);
//            prepStmt.execute();
            prepStmt.executeQuery();

            System.out.println("teamName: " + teamName);
            System.out.println("teamStatus: " + teamStatus);
            System.out.println("team status added to database");

        } catch (CommunicationException comexception) {
            System.err.println("Unable to connect 2");
            comexception.printStackTrace();
            return;

        } catch (Exception comexception) {
            System.err.println("Unable to connect - "
                    + "communication or access error?");
            comexception.printStackTrace(); // Will return the fill communications link failure message
            return;

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public JSONObject getTeamStatusValues() throws JSONException {
        try {

            Class.forName(DRIVER);
            connection = setupConnection();
            statement = connection.createStatement();
            resultSet = statement
                    .executeQuery("SELECT blueTeamStatus, redTeamStatus FROM team2");

            while (resultSet.next()) {
                TeamStatusValue teamStatusValue = new TeamStatusValue();
                teamStatusValue.setBlueTeamStatus(resultSet.getInt("blueTeamStatus"));
                teamStatusValue.setRedTeamStatus(resultSet.getInt("redTeamStatus"));
                teamStatusValuesJson.put("BlueTeamStatus", teamStatusValue.getBlueTeamStatus());
                teamStatusValuesJson.put("RedTeamStatus", teamStatusValue.getRedTeamStatus());

                System.out.println("team status value json: " + teamStatusValuesJson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
//        return teamStatusValuesList;
        return teamStatusValuesJson;
    }

    private Connection setupConnection() throws SQLException {
        Connection connection;
        connection = DriverManager.getConnection(DATABASE_URL, "root",
                "123123");
        return connection;
    }

    public void resetDB() {

        PreparedStatement prepStmt = null;
        try {
            Class.forName(DRIVER);
            connection = setupConnection();

            String query = "UPDATE `team2` SET `blueTeamStatus`='0',`redTeamStatus`='0' WHERE `blueTeamStatus`='1' OR `redTeamStatus`='1'";
            System.out.println("Query: " + query);
            prepStmt = connection.prepareStatement(query);
            //prepStmt.setInt(1, teamStatus);
            //prepStmt.setInt(2, blueTeamReady);
            prepStmt.execute();

        } catch (CommunicationException comexception) {
            System.err.println("Unable to connect 2");
            comexception.printStackTrace();
            return;

        } catch (Exception comexception) {
            System.err.println("Unable to connect - "
                    + "communication or access error?");
            comexception.printStackTrace(); // Will return the fill communications link failure message
            return;

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
