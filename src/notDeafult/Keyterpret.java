package notDeafult;

import java.awt.FontFormatException;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.*;

public class Keyterpret extends logic { // This class contains all the code necessary to implement the rules of the game, 
    //Eg: What happens when the player interacts with certain elements such as an 'X' or a Wall
    //How to interpret The Keys
    //Keyterpret

    public Keyterpret() throws IOException {
        populateSquares(); //Makes sure every time the class is called there is no null data
    }

    public boolean usercango(int x, int y) throws IOException, FontFormatException, SQLException { //This method checks, with high efficacy, whether the user can go in a certain direction

        readElements(); //Elements only need to be read here as they are their data is only dealt with first hand by this method 

        if (elementsArr[y][x] == null) {
            return true; //Player can always move through empty space
        }

        if (elementsArr[y][x].getElement().equals("WALL")) {
            if (StartGame.spades > 0) { //If the player doesn't have enough spades, the player can't go through a wall
                wallConseq(x, y); //If the player has enough then due to the "Consequence of hitting the wall" the player will lose a spade
                return true;
            } else {

                lose("You ran out of spades"); //As described in the rules the player looses when running out of spades and encountering a wall.

                return false;

            }
        }

        if (elementsArr[y][x].getElement().equals("SPADE")) {
            spadeConseq(x, y); //users gain a spade when through them
            return true;
        }

        if (elementsArr[y][x].getElement().equals("X")) {

            Xconseq(); //The player always looses when hitting an X and can therefore not move
            return false;
        }

        return false; //Necessary for method to believe it can return something
    }

    public void wallConseq(int x, int y) {

        int spades;
        spades = StartGame.spades;
        StartGame.spades = spades - 1; //code for the consequence described above

    }

    public void spadeConseq(int x, int y) {
        int spades;
        spades = StartGame.spades;
        StartGame.spades = spades + 1;
        elementsArr[y][x] = null;
        StartGame.kill.add(new KillList(x, y)); //Adds the particular element to the kill list, thereby erasing the spade as the user moves through it

    }

    public void Xconseq() throws IOException, FontFormatException, SQLException {

        lose("YOU HIT an X, YOU LOSE");
    }

    public void reset() throws IOException {

        //This method is called when the player reaches the end of a screen. It is used to generate the next level
        StartGame.kill.clear(); //The kill list is cleared so the previously eliminated co-ordinates no longer affect future levels.
        StartGame.x = 4;//Default Positions are set
        StartGame.y = 2;
        populateElements(); //A new randomly generate map is made
        readElements(); //The map is read from the Text File

    }

    public void lose(String m) throws IOException, FontFormatException, SQLException {

        /*
        
         In order to cope with the multiple scenarios of "losing" or even just completely restarting the game board, Many Different Parameters must be considered
         For example if i want to just Dispose the gameFrame and end all process i can send Lose("reset"), this will reset the board without telling the user
         But also if the user hits an X or a wall I can send the reason why the user lost , as a parameter, In which case the user will be given the option of restarting
         Whereas with the reset, the method can automatically skip asking the user permission to reset.
        
         This ultimately creates a highly efficient system as only one method is needed to solve 3-4 different scenarios. 
        
         */
        dataBase db = new dataBase();
        db.addScore();

        int reply = JOptionPane.YES_OPTION;

        if (m.equals("reset")) {
            reply = JOptionPane.YES_OPTION;

        } else {
            reply = JOptionPane.showConfirmDialog(null, m + " Would you like to retry", "Retry", JOptionPane.YES_NO_OPTION);
        }

        if (reply == JOptionPane.NO_OPTION) {
            System.exit(0);
        } else {
            populateElements();
            readElements();
            StartGame.kill.clear();
            StartGame.x = 4;
            StartGame.y = 2;
            StartGame.score = 0;
            StartGame.spades = 0;

        }

    }

}
