package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBC {
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    public JDBC() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:2222/w1499580_0?zeroDateTimeBehavior=convertToNull", "w1499580", "zX01H2Ksl6Qw");
            statement = connection.createStatement();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void enterScore(String username, int score) {
        try {
            String sql = "insert HighScore" + " (Username, Score)" + " values('" + username + "','" + score + "');";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Highscore> getScores() {
        List<Highscore> scores = new ArrayList<Highscore>();
        try {
            resultSet = statement.executeQuery("SELECT * FROM HighScore ORDER BY Score DESC ;");
            while (resultSet.next()) {
                Highscore h = new Highscore();
                h.setHighScoreID(resultSet.getInt(1));
                h.setUserName(resultSet.getString(2));
                h.setScore(resultSet.getInt(3));
                scores.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }
}
