package notDeafult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartGame {

    static Keyterpret kt;

    static {
        try {
            kt = new Keyterpret(); //In order to comply with Static conventions the object must be declared in this form
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

// The following variables need to be accessed by more than one class but also, For example a reset method might need to change all values.
//But importantly by declaring these variables at the beginning of the startGame method
//It becomes impossible for the variables to constantly reset to what they were originally declared. 
//This was an enormous problem before the class used statics, as to reference any variable required creating an object out of the class
//which would always reinitialise the variables beck to the default values

    public static int x = 4; //The users X-coordinate
    public static int y = 2; //Y-Coordinate
    public static int spades = 0;   //Amount of spades
    public static int score = 0; //Score
    public static ArrayList<KillList> kill = new ArrayList<KillList>();

    /*The concept of the kill list addresses an unforeseen issue when reading data from a textfile.
     Because the textfile only stores the first randomly generated version of the board. any changes the user makes by interacting would
     either have to selectively removed from the textfile. Or in my solution. 
     read from the textfile and then having a list of elements of the array which must be immediately reset to 0
     */

    public static String name;

    public StartGame() throws FontFormatException, IOException {

        final Gameframe fr = new Gameframe();

        fr.jPanel3.setFocusable(true); //required for KeyListner
        fr.jPanel3.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) { //I have selected the KeyPressed event as it seems most user friendly

                if (e.getKeyCode() == KeyEvent.VK_UP) { //if the user wants to go up
                    try {
                        up(); //use the up() method to allocate necessary tasks and figure out whether the method should return a
                        //true, the user can move up
                        //or false the user cannot

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (FontFormatException ex) {
                        Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    fr.jPanel3.repaint(); //refresh all changes made if the user moves through any of the Arrays

                }

                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    try {
                        left(); //Same as above for the left movement
                    } catch (IOException ex) {
                        Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (FontFormatException ex) {
                        Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ArrayIndexOutOfBoundsException d) {
                    }
                    //If a user tries to move through any edges of the board this error occurs, 
                    //However the boolean nature of the algorithm detecting whether a user can perform a move polymorphicly does not allow the user to move off screen
                    //Therefore no Error handling is necessary
                    fr.jPanel3.repaint();
                }

                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    try {

                        right(); //right movement

                    } catch (IOException ex) {
                        Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (FontFormatException ex) {
                        Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ArrayIndexOutOfBoundsException d) {
                    }//as above
                    fr.jPanel3.repaint();
                }

            }
        });

        fr.pack();
        fr.setVisible(true);

    }

    public boolean up() throws IOException, FontFormatException, SQLException {//Using BOOLEANS classes allows the code to BREAK after the move has been executed 
        if (x != 0) { //If the user hasn't reached the end of the board

            if (kt.usercango((x - 1), y)) { // Send the block the user will be going to, not the block the user is currently occupying

                x = x - 1; //-1 from the users x position
                score++; //Increase score
                return true;
            } else {
                return false;
            }
        }
        if (x == 0) {
            kt.reset(); //If user reaches the end of a level, generate a new level

        }
        return true;

    }

    public boolean left() throws IOException, FontFormatException, SQLException {

        if (kt.usercango(x, y - 1)) {
            if (y != 0) {

                y = y - 1; //Positional change
                return true; //Break Points
            }
        } else {
            return false;
        }

        return true;

    }

    public boolean right() throws IOException, FontFormatException, SQLException {

        if (kt.usercango(x, y + 1)) {
            if (y != 3) {//If Y is not the edge of the board
                y = y + 1;
                return true;
            }
        } else {
            return false;
        }
        return true;
    }

}
