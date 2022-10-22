package notDeafult;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.*;

public class SpadesAndWalls {

    public static void main(String[] args) throws IOException, FontFormatException, SQLException {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Minecraftia.ttf")));
        //the pixelated 'Minecraftia' font is registered here so all Frames have access to it.

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            //The nimbus look is set
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //I noticed one of the biggest performance issues in the game occurred in the latency affecting the initial connection to a database
        //As the first connection to the dataBase is normally as the user starts playing the game, it can feel like a long wait for the game to start
        //I therefore decided to move the initial connection to the First action of the program as the time taken to connect feels more like 
        //time waiting for the program to start
        //All subsequent connections suffer n latency issues after initial connecting here
        dataBase db = new dataBase();
        db.getScores();
        MainMenu mn = new MainMenu();
        mn.setVisible(true);

    }

}
