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
public class rectangle {
//The basic outline of a rectangle making up the grid

    Rectangle rect;
    int width;
    int height;

    public rectangle() {
    }

    public rectangle(int x, int y) {

        this.width = 80; //Setting the size
        this.height = 80;
        this.rect = new Rectangle(x + 80, y + 30, width, height);//Moving the object to the centre 
    }

    public void draw(Graphics g) {
        g.setColor(Color.green);
        //.drawRect only draws the outline
        g.drawRect(rect.x, rect.y, width, height);

    }

}
