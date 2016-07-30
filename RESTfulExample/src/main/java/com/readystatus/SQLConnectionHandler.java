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
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class SQLConnectionHandler {

    static final String DRIVER = "org.mariadb.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mariadb://localhost:3306/paintballsystem";
    JSONObject teamStatusValuesJson = new JSONObject();
    JSONObject GPSJson = new JSONObject();
    JSONArray gpsPosAllJsonArray = new JSONArray();

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    public SQLConnectionHandler() {

    }

    public void updateTeamStatusDB(String teamName, int teamStatus) {

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

    public void updateGPSPositionDB(int GPSID, String Latitude, String Longitude) {

        PreparedStatement prepStmt = null;
        try {
            Class.forName(DRIVER);
            connection = setupConnection();
            double latitude = Double.parseDouble(Latitude);
            double longitude = Double.parseDouble(Longitude);

//            String query = "INSERT INTO `gpsposition` SET `latitude`=" + latitude + ",`longitude`=" + longitude + " NOW() WHERE `GPSID`=?";
            String query = "INSERT INTO `gpsposition`(`longitude`, `latitude`, `gps_id`, `timestamp`) VALUES (?,?,?,NOW())";
            prepStmt = connection.prepareStatement(query);
            prepStmt.setDouble(1, latitude);
            prepStmt.setDouble(2, longitude);
            prepStmt.setInt(3, GPSID);
            prepStmt.executeQuery();

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

    public JSONObject getTeamStatusValuesDB() throws JSONException {
        try {

            Class.forName(DRIVER);
            connection = setupConnection();
            statement = connection.createStatement();
            resultSet = statement
                    .executeQuery("SELECT blueTeamStatus, redTeamStatus FROM team2");

            while (resultSet.next()) {
                TeamStatusValues teamStatusValue = new TeamStatusValues();
                teamStatusValue.setBlueTeamStatus(resultSet.getInt("blueTeamStatus"));
                teamStatusValue.setRedTeamStatus(resultSet.getInt("redTeamStatus"));
                teamStatusValuesJson.put("BlueTeamStatus", teamStatusValue.getBlueTeamStatus());
                teamStatusValuesJson.put("RedTeamStatus", teamStatusValue.getRedTeamStatus());

                System.out.println("team status value json: " + teamStatusValuesJson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
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

    public JSONObject getGSPIDDB(int i) throws JSONException {
        try {

            Class.forName(DRIVER);
            connection = setupConnection();
            statement = connection.createStatement();
            resultSet = statement
                    .executeQuery("SELECT gpsid FROM gps WHERE gpsid=" + i);

            while (resultSet.next()) {
                GPSValues gps = new GPSValues();
                gps.setGPSID(resultSet.getInt("gpsid"));
                GPSJson.put("gpsid", gps.getGPSID());

                System.out.println("GPSID checked: " + GPSJson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return GPSJson;
    }

    public JSONArray getGPSPositionAllUnits() throws JSONException {
        try {

            Class.forName(DRIVER);
            connection = setupConnection();
            statement = connection.createStatement();
            resultSet = statement
                    .executeQuery("SELECT gps_id, latitude, longitude\n"
                            + "FROM gpsposition\n"
                            + "GROUP BY gps_id DESC");

            while (resultSet.next()) {
                String longitude_json = resultSet.getString("longitude");
                String latitude_json = resultSet.getString("latitude");
                String gpsid_json = resultSet.getString("gps_id");
                JSONObject jobj = new JSONObject();
                jobj.put("gpsid", gpsid_json);
                jobj.put("latitude", latitude_json);
                jobj.put("longitude", longitude_json);
                gpsPosAllJsonArray.put(jobj);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gpsPosAllJsonArray;
    }

    public void insertGPSIDDB(int GPSID, String name) {

        PreparedStatement prepStmt = null;
        try {
            Class.forName(DRIVER);
            connection = setupConnection();

            String query = "INSERT INTO `gps`(`gpsid`, `name`) VALUES (?,?)";
            prepStmt = connection.prepareStatement(query);
            prepStmt.setInt(1, GPSID);
            prepStmt.setString(2, name);
            prepStmt.executeQuery();

            System.out.println("query: " + query);
            System.out.println("teamStatus: " + GPSID);
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

    private Connection setupConnection() throws SQLException {
        Connection connection;
        connection = DriverManager.getConnection(DATABASE_URL, "root",
                "123123");
        return connection;
    }

    public void resetTeamStatusDB() {

        PreparedStatement prepStmt = null;
        try {
            Class.forName(DRIVER);
            connection = setupConnection();

            String query = "UPDATE `team2` SET `blueTeamStatus`='0',`redTeamStatus`='0' WHERE `blueTeamStatus`='1' OR `redTeamStatus`='1'";
            System.out.println("Query: " + query);
            prepStmt = connection.prepareStatement(query);
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
