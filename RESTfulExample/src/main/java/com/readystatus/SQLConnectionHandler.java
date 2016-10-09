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
import java.sql.BatchUpdateException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class SQLConnectionHandler {

    static final String DRIVER = "org.mariadb.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mariadb://localhost:3306/paintballsystem";
    public int userID;
    JSONObject gameStatusJson = new JSONObject();
    JSONObject teamStatusValuesJson = new JSONObject();
    JSONObject GPSJson = new JSONObject();
    JSONObject usernameJson = new JSONObject();
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

    public void updateGPSPositionDB(int GPSID, int teamvalue, String Latitude, String Longitude) {

        PreparedStatement prepStmt = null;
        try {
            Class.forName(DRIVER);
            connection = setupConnection();
            double latitude = Double.parseDouble(Latitude);
            double longitude = Double.parseDouble(Longitude);

            String query = "INSERT INTO `gpsposition`(`longitude`, `latitude`, `teamvalue`, `gps_id`, `timestamp`) VALUES (?,?,?,?,NOW())";
            prepStmt = connection.prepareStatement(query);
            prepStmt.setDouble(1, longitude);
            prepStmt.setDouble(2, latitude);
            prepStmt.setInt(3, teamvalue);
            prepStmt.setInt(4, GPSID);
            prepStmt.executeQuery();

            System.out.println("User id: " + GPSID + "posted position, latitude: " + Latitude + " and Longitude: " + Longitude + " teamvalue: " + teamvalue);

        } catch (CommunicationException comexception) {
            System.err.println("Unable to connect 2");
            comexception.printStackTrace();
            return;

        } catch (BatchUpdateException batchUpdateException) {
            System.err.println("BatchUpdate Exception - "
                    + "is the gpsid registered in the database?");
            batchUpdateException.printStackTrace(); // Will return the fill communications link failure message
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

    public JSONObject getGPSIDDB(int i) throws JSONException {
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
                    .executeQuery("SELECT gps_id, latitude, longitude, teamvalue\n"
                            + "FROM gpsposition\n"
                            + "GROUP BY gps_id DESC");

            while (resultSet.next()) {
                String longitude_json = resultSet.getString("longitude");
                String latitude_json = resultSet.getString("latitude");
                int teamvalue = resultSet.getInt("teamvalue");
                String gpsid_json = resultSet.getString("gps_id");
                JSONObject jobj = new JSONObject();
                jobj.put("gpsid", gpsid_json);
                jobj.put("latitude", latitude_json);
                jobj.put("longitude", longitude_json);
                jobj.put("teamvalue", teamvalue);
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

    public boolean insertNewUserDB(int GPSID, String name, String password, byte[] salt) {
        PreparedStatement prepStmt = null;
        try {
            Class.forName(DRIVER);

            if (checkIfUsernameIsAvailableDB(name)) {

                System.out.println("username is available");
                connection = setupConnection();
                String query = "INSERT INTO `gps`(`gpsid`, `name`, `password`,`salt`) VALUES (?,?,?,?)";
                prepStmt = connection.prepareStatement(query);
                prepStmt.setInt(1, GPSID);
                prepStmt.setString(2, name);
                prepStmt.setString(3, password);
                prepStmt.setString(4, Arrays.toString(salt));
                prepStmt.executeQuery();
                return true;
            } else {
                System.out.println("NOT AVAILABLE");
                return false;
            }

        } catch (CommunicationException comexception) {
            System.err.println("Unable to connect 2");
            comexception.printStackTrace();
            return false;

        } catch (Exception comexception) {
            System.err.println("Unable to connect - "
                    + "communication or access error?");
            comexception.printStackTrace(); // Will return the fill communications link failure message
            return false;

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

    public boolean checkIfUsernameIsAvailableDB(String username) {
        try {

            Class.forName(DRIVER);
            String foundUsername = "";
            connection = setupConnection();
            statement = connection.createStatement();
            resultSet = statement
                    .executeQuery("SELECT `name` FROM `gps` WHERE `name`='" + username + "'");

            System.out.println("first inside");
            while (resultSet.next()) {
                foundUsername = resultSet.getString("name");
            }

            if (username.equals(foundUsername)) {
                return false;
            } else {
                return true;
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
        return false;
    }

//    public int loginDB(String username, String password) {
//
//        try {
//
//            Class.forName(DRIVER);
////            String foundUsername = "";
////            String foundPassword = "";
//            int foundGPSID = -1;
//            connection = setupConnection();
//            statement = connection.createStatement();
//            resultSet = statement
//                    .executeQuery("SELECT `gpsid`, `name`, `password` FROM `gps` WHERE `name`='" + username + "' AND `password`='" + password + "'");
//
//            while (resultSet.next()) {
//                if (!resultSet.isLast()) {
//                    throw new SQLException("Found more than one user matching username and password.");
//                }
////                
////                foundUsername = resultSet.getString("name");
////                foundPassword = resultSet.getString("password");
//                foundGPSID = resultSet.getInt("gpsid");
//            }
//            return foundGPSID;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(SQLConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return -1;
//    }
    public boolean areBothTeamsReadyDB() {

        try {

            Class.forName(DRIVER);
            connection = setupConnection();
            statement = connection.createStatement();
            resultSet = statement
                    .executeQuery("SELECT `blueTeamStatus`, `redTeamStatus` FROM `team2` WHERE `blueTeamStatus`='1' AND `redTeamStatus`='1'");

            while (resultSet.next()) {
                if (!resultSet.isLast()) {
                    throw new SQLException("Found more than one user matching username and password.");
                }
                return true;
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
        return false;
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

    public void updateGameStatusDB(String gameStatus) {

        PreparedStatement prepStmt = null;
        try {
            Class.forName(DRIVER);
            connection = setupConnection();

            String query = "UPDATE `gameinfo` SET `gamestatus`=\"" + gameStatus + "\"";
            prepStmt = connection.prepareStatement(query);
            //prepStmt.setString(1, gameStatus);
            prepStmt.executeQuery();

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

    public JSONObject getGameStatusDB() {

        PreparedStatement prepStmt = null;
        try {
            Class.forName(DRIVER);
            connection = setupConnection();

            statement = connection.createStatement();
            resultSet = statement
                    .executeQuery("SELECT `gamestatus` FROM `gameinfo`");

            while (resultSet.next()) { //4
                gameStatusJson.put("gamestatus", resultSet.getString("gamestatus"));

            }

        } catch (CommunicationException comexception) {
            System.err.println("Unable to connect 2");
            comexception.printStackTrace();

        } catch (Exception comexception) {
            System.err.println("Unable to connect - "
                    + "communication or access error?");
            comexception.printStackTrace(); // Will return the fill communications link failure message

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gameStatusJson;
    }

    public String getSaltFromDB(String username) {

        try {

            Class.forName(DRIVER);
            connection = setupConnection();
            statement = connection.createStatement();
            resultSet = statement
                    .executeQuery("SELECT `salt` FROM `gps` WHERE `name`='" + username + "'");

            while (resultSet.next()) {
                if (!resultSet.isLast()) {
                    throw new SQLException("Found more than one user matching username and password.");
                }
                String foundSalt = resultSet.getString("salt");
                return foundSalt;
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
        return null;
    }

    public String getPasswordFromDB(String username) {

        try {

            Class.forName(DRIVER);
            connection = setupConnection();
            statement = connection.createStatement();
            resultSet = statement
                    .executeQuery("SELECT `password` FROM `gps` WHERE `name`='" + username + "'");

            while (resultSet.next()) {
                if (!resultSet.isLast()) {
                    throw new SQLException("Found more than one user matching username and password.");
                }
                String foundPassword = resultSet.getString("password");
                return foundPassword;
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
        return "";
    }

    public int getGPSIDDB(String username) {

        try {

            Class.forName(DRIVER);
//            String foundUsername = "";
//            String foundPassword = "";
            int foundGPSID = -1;
            connection = setupConnection();
            statement = connection.createStatement();
            resultSet = statement
                    .executeQuery("SELECT `gpsid`, `name` FROM `gps` WHERE `name`='" + username + "'");

            while (resultSet.next()) {
                if (!resultSet.isLast()) {
                    throw new SQLException("Found more than one user matching username and password.");
                }
//                
//                foundUsername = resultSet.getString("name");
//                foundPassword = resultSet.getString("password");
                foundGPSID = resultSet.getInt("gpsid");
            }
            return foundGPSID;

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
        return -1;
    }

    public boolean checkIfUsernameExists(String username) {

        try {

            Class.forName(DRIVER);
            connection = setupConnection();
            statement = connection.createStatement();
            resultSet = statement
                    .executeQuery("SELECT `name` FROM `gps` WHERE `name`='" + username + "'");

            while (resultSet.next()) {
                if (!resultSet.isLast()) {
                    throw new SQLException("Found more than one user matching username and password.");
                }
                return true;
            }

        } catch (SQLException | ClassNotFoundException e) {
            
            Logger.getLogger(SQLConnectionHandler.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
