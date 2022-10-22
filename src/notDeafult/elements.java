package notDeafult;

import java.awt.*;

/**
 * Created by User on 7/23/2014.
 */
public class elements {

    private String element;
    private int x;
    private int y;
    private int i;//Type of element

    public elements(int x, int y, int i) {
        this.element = genElement(i);
        this.x = x; //dimensions
        this.y = y; //dimensions
        this.i = i;
    }

    public void draw(Graphics g) {

        //Change Colours of elements depending on their proporties
        if (getElement().equals("X")) {
            g.setColor(Color.red); //Red X
            g.setFont(new Font("minecraftia", Font.BOLD, 25));
        }

        if (getElement().equals("WALL")) {
            g.setColor(Color.LIGHT_GRAY); //Grey Wall
            g.setFont(new Font("minecraftia", Font.PLAIN, 15));
        }

        if (getElement().equals("SPADE")) {
            g.setColor(Color.cyan); //Blue Spade
            g.setFont(new Font("minecraftia", Font.PLAIN, 15));

        }

        g.drawString(element, x + 100, y + 70); //Draw the elements in the center of blocks

    }

    public String genElement(int i) {
        String names[] = {"X", "WALL", "WALL", "SPADE", "WALL"}; //THis Array represents the probablity of an element being drawn,
        // The more i values the element spans,
        //The higher the probability. In this case it is more likely for a wall to be chosen 
        element = names[i]; //Find the corresponding element
        return element;
    }

    public String getElement() {
        return element;
    }

    public int getI() {
        return i;
    }

    public String toString() {
        return x + " " + y + " " + i + " ";//TOString used only for file Writing
    }
}
