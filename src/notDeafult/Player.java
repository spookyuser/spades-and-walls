package notDeafult;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author c
 */
public class Player {
//The Player object is very similar to the rectangle object, the only difference being in the way each object is drawn.
//As can be seen below the Paint methods differ between the rectangle and player object

    Rectangle player;
    int width;
    int height;

    public Player() {

    }

    public Player(int x, int y) {
        this.width = 80;
        this.height = 80;
        //The +80 and +30 Moves the entire Grid to the centre of the frame
        this.player = new Rectangle(x + 80, y + 30, width, height);
    }

    public void draw(Graphics g) {
        g.setColor(Color.green);
        g.fillRect(player.x, player.y, width, height);
        //The difference lies only in the fact that the player is a filled rectangle
        //calling the .fill method.
        //while the rectangle calls the .Draw, thereby only giving an outline of a square. 

    }

}
