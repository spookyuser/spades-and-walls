package notDeafult;

import java.sql.*;
import java.util.Vector;

public class dataBase {
    /*Due to many conflicts between newer Java versions and the ordinary ODBC java connection driver, a different driver must be used
     by the name of UcanAcess.
     This file has automatically been added to the Lib folder of this project, so no user input should even be required
     However for more information Visit:
    
     http://ucanaccess.sourceforge.net/site.html
     */

    public static final String driver = "net.ucanaccess.jdbc.UcanaccessDriver"; // connection varaiable
    //Url to file using relative file paths
    public static final String url = "jdbc:ucanaccess://highscores.mdb;showschema=true;ignorecase=true;lockmdb=true";
    public Connection connection;
    public PreparedStatement statement;
    public ResultSet resultSet;

    public Connection DatabaseConnection() {
        //The following executes eventide any databAse command is made
        try {
            Class.forName(driver); //Load the driver
            System.out.println("Successfully loaded driver");
        } catch (ClassNotFoundException c) {
            System.out.println("Unable to load driver");
            c.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(url); //Load URL
            System.out.println("Successfully loaded connection");
            return connection;
        } catch (Exception e) {
            System.out.println("Failed to get connection");
            e.printStackTrace();
        }
        return connection; //If all works return the connection
    }

    public Vector getScores() throws SQLException {

        //This method was almost directly taken from - http://chang.advits.com/populate-data-from-database-into-jtable-in-netbeans
        Vector<Vector<String>> scoresVector = new Vector<Vector<String>>(); //Creating new VEctor

        String stmt = "SELECT * FROM scores ORDER BY score DESC limit 10;"; //Statement to display top 10 scores
        connection = DatabaseConnection(); //Load Connection
        statement = connection.prepareStatement(stmt); //Using Prepared Statements prepare the query
        resultSet = statement.executeQuery(); //Execute the query 

        while (resultSet.next()) {
            //Interpret the results of query into a form readable on a table
            Vector<String> scores = new Vector<String>(); //Reinitialise the Vector
            //In each iteration of the result loop the following must be done
            String userName = resultSet.getString("userName");//Get the userName
            int score = resultSet.getInt("score"); //Get the score
            scores.add(userName); //Add the username to the scores vector
            scores.add(Integer.toString(score)); //Add the Score to the scores vector
            scoresVector.add(scores); //Add the scores vector to the initial ScoresVEctor

        }

        if (connection != null) { //Once the connection is complete end the method
            connection.close();
        }

        return scoresVector; //Return the vector to the HighScore Table

    }

    public void addScore() throws SQLException {
        //Method to add scores, THis method is called whenever the user's game ends for any reason
        try {

            //the sql insert statement
            String query = " insert into scores (userName, score)"
                    + " values (?, ?)";

            // create the sql insert preparedstatement
            connection = DatabaseConnection();
            connection.commit();
            PreparedStatement preparedStmt = connection.prepareStatement(query); //prepare query with the following variables
            preparedStmt.setString(1, StartGame.name);//the name
            preparedStmt.setInt(2, StartGame.score); //the score

            // execute the preparedstatement
            preparedStmt.executeUpdate();

            connection.close(); //close connection
        } catch (SQLException e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }

    }

    public void clearScores() throws SQLException {
        //method to clear all scores
        connection = DatabaseConnection();
        String query = "DELETE FROM scores"; //query
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.executeUpdate(); //delete all scores

        connection.close();

    }

}
