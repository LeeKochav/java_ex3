package gameClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.mysql.jdbc.ResultSetMetaData;

/**
 * This class represents a simple example of using MySQL Data-Base. Use this
 * example for writing solution.
 *
 * @author boaz.benmoshe
 *
 */
public class DataBase {
    public static final String jdbcUrl = "jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
    public static final String jdbcUser = "student";
    public static final String jdbcUserPassword = "OOP2020student";
    private static Connection connection = null;
    private static Statement statement = null;

    private static ResultSet doQuery(String query) {
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        }

        catch (SQLException sqle) {
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("Vendor Error: " + sqle.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }



    private static void closeQuery(ResultSet resultSet) {
        try {
            resultSet.close();
            connection.close();
            statement.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static int gamesPlayed(int id) {
        ResultSet resultSet = doQuery("select * from Logs where userID = " + id);
        int count = 0;
        try {
            while (resultSet.next()) {
                count++;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        closeQuery(resultSet);
        return count;
    }

    public static TreeMap<Integer, String> myBestResults(int id) {
        String query = "SELECT * FROM Logs as logs inner join (" + "SELECT max(score) as score, levelID FROM Logs"
                + " where userID = " + id + " group by levelID" + ") as groupedLogs"
                + " on logs.levelID = groupedLogs.levelID and logs.score = groupedLogs.score" + " where userID = " + id
                + " order by logs.levelID asc";
        ResultSet resultSet = doQuery(query);
        TreeMap<Integer, String> tp = new TreeMap<Integer, String>();
        try {

            while (resultSet.next()) {
                String value = "" + resultSet.getInt("userID") + "," + resultSet.getInt("levelID") + ","
                        + resultSet.getInt("score") + "," + resultSet.getInt("moves") + "," + resultSet.getDate("time");
                tp.put(resultSet.getInt("levelID"), value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeQuery(resultSet);
        return tp;
    }

    public static TreeMap<String, String> gameBestResults() {
        String query = "SELECT * FROM Logs as logs inner join ("
                + "SELECT max(score) as score, levelID, userID FROM Logs" + "	group by levelID,userID"
                + ") as groupedLogs"
                + " on logs.userID = groupedLogs.userID and logs.levelID = groupedLogs.levelID and logs.score = groupedLogs.score"
                + " order by logs.userID desc,logs.levelID asc";
        ResultSet resultSet = doQuery(query);
        TreeMap<String, String> tp = new TreeMap<String, String>();
        try {

            while (resultSet.next()) {
                String value = "" + resultSet.getInt("userID") + "," + resultSet.getInt("levelID") + ","
                        + resultSet.getInt("score") + "," + resultSet.getInt("moves") + "," + resultSet.getDate("time");
                tp.put(resultSet.getInt("userID") + "," + resultSet.getInt("levelID"), value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeQuery(resultSet);
        return tp;
    }
}