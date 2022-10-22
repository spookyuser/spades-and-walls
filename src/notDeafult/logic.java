package notDeafult;

import java.awt.Graphics;
import java.io.*;
import java.util.*;

public class logic {
    /*
    
     This class contains what could be considered the main engines of the program
     in it, the methods necessary to create and alter the Object Arrays that build up the grid of 
     the main gameplay screen - are first instantiated.
    
     */

    private Graphics g; //The Graphics Variable instantiated
    Random r = new Random(); //The Random class instantiated

    public logic() {
        //All 3 of the object arrays are initialised, these being the object that makes the outline of the grid
        //The object holding the position of the player
        //The object holding the elements stored inside the blocks.
        //Each Array is set to a [4][5] dimension, this is the size of the grid. Therefore each intersection of the 2D array represents a block
        rectArr = new rectangle[4][5]; //Outlines Grid
        playerArr = new Player[4][5]; //Stores player position in grid
        elementsArr = new elements[4][5]; //Stores elements in grid

    }

    rectangle[][] rectArr;
    Player[][] playerArr;
    elements[][] elementsArr;

    int rectSize = 80; //Size of rectangles

    public void populateSquares() {
        //The purpose of this method is to add a 2D block to each element of the rectArr and thereby make the outline of the game grid
        for (int col = 0; col < rectArr.length; col++) { //Loop for length of the first dimension of the array
            for (int row = 0; row < rectArr[0].length; row++) { //Loop for length of second dimension
                if (row == StartGame.x && col == StartGame.y) { //If the block contains a player, fill the block instead of outline
                    playerArr[col][row] = new Player(col * rectSize, row * rectSize); //Send positions of where to draw the square as a parameter

                } else {

                    rectArr[col][row] = new rectangle(col * rectSize, row * rectSize); //else simply outline the block, send positions
                }

            }

        }

    }

    public void populateElements() throws IOException {
        /*
         The purpose of this method is to randomly generate the elements contained within the blocks.
         It might be called to generate a new level or when a new game is selected
         Either way it hinges on writing the ordinal generated map/level to a text file, that other methods can then read from
         This is especially hard as the algorithm for printing a 2D array to text file is significantly different to that of a 1 dimensional array
         This file can also be copied to save a game. 
         The file simply stores the Elements object this being the x,y co-ordinates and the element contained in that co-ordinate
         */
        PrintWriter pw = new PrintWriter(new FileWriter("elements.txt")); //Create file
        for (int col = 0; col < rectArr.length; col++) { //Loop for 1st dimension
            for (int row = 0; row < rectArr[0].length; row++) {//loop for second
                int rndNO = r.nextInt(6 - 0) + 0; //Create a random number corresponding to the number each element is assigned. an X might be a 0 therefore 
                //Whenever a 0 is randomly generated an X will be placed in the ElementsArr

                if (rndNO <= 3) {//There are 3 possible elements X, Spade, Wall
                    elementsArr[col][row] = new elements(((col * rectSize) - 5), row * rectSize, rndNO);
                    //Add it to the Array
                    pw.print(elementsArr[col][row].toString());
                    //Write it to the file
                } else {
                    pw.print("null ");
                    //IF there is to be a blank block, print null
                }

            }

            pw.println(""); //Go to the next line for each iteration of the second dimension array

        }

        pw.close();
    }

    public void paintSquares(Graphics g) {
        //The method used to actually paint the outlines and filled in blocks previously populated n populateSquares 
        for (int col = 0; col < rectArr.length; col++) {//First Dimension 
            for (int row = 0; row < rectArr[0].length; row++) {//Second Dimension
                if (row == StartGame.x && col == StartGame.y) {//Check player positions
                    playerArr[col][row].draw(g);//Draw Player
                } else {
                    rectArr[col][row].draw(g);//Draw outline
                }
            }
        }
    }

    public void assignElements(Graphics g) {
        //This is used to paint the Xs, Spades and Walls seen on the grid
        for (int col = 0; col < rectArr.length; col++) {//First Dimension
            for (int row = 0; row < rectArr[0].length; row++) {//Second Dimension

                try {//Try Paint all elements, However if the element is null. It must be an empty block in which case it does not need to be painted
                    //And the NullException must simply be caught

                    elementsArr[col][row].draw(g);//Paint Element

                } catch (NullPointerException e) {
                    //Do nothing
                }

            }
        }

    }

    public void readElements() throws IOException {
        //THis is used to Read the Elements File

        File file = new File("elements.txt");//Read Elements
        Scanner inp = new Scanner(file);//Using a Scanner
        //Before reading the elements the current array must be emptied
        for (int z = 0; z < rectArr.length; z++) {//Empty First Dimension
            for (int v = 0; v < rectArr[0].length; v++) {//Empty Second

                elementsArr[z][v] = null;

            }
        }
        //Once Empty the New array Information can be read from the File
        for (int col = 0; col < 4; col++) {//First Dimension
            for (int row = 0; row < 5; row++) {//Second Dimension
                if (inp.hasNext()) {//IF there are more objects to read

                    try {
                        String s = inp.next();//Read the next object
                        Integer.parseInt(s);//Try parse the next piece of text as an Integer,
                        //If it is null the Integer passing will fail and it can be inferenced the block was empty
                        //Else it must be a number in which the case the next 2 numbers must make up he object
                        int x = Integer.parseInt(s); //X co-ord
                        int y = Integer.parseInt(inp.next());//y co-ord
                        int i = Integer.parseInt(inp.next());//Element

                        elementsArr[col][row] = new elements(x, y, i);//Add new element
                        killElements();//Call the kill elements method that sets previously captured spades to null, as the textfile cannot be edited

                    } catch (NumberFormatException nfe) {
                        //Element is null,
                        //Do nothing
                    }

                }

            }

            inp.nextLine();//Next Dimension

        }

        inp.close();

    }

    public void killElements() {
        //To kill all elements that have previously been captured
        for (int i = 0; i < StartGame.kill.size(); i++) {//Loop for length of kill Object
            //Get the object x value      Get object y value           Set To null
            elementsArr[StartGame.kill.get(i).getY()][StartGame.kill.get(i).getX()] = null;
        }

    }

}
