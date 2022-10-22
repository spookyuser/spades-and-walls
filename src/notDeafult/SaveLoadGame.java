package notDeafult;

import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils; //This 3rd party Jar library allows me to easily delete folders. without having to create 
//a complex additional method similar to this http://javatutorialhq.com/java/example-source-code/io/nio/delete-directory/

public class SaveLoadGame {

    public SaveLoadGame() {
    }

    public String save() throws IOException {
        //The Save Game method
        String saveNum = getSaveNum(); //This calls a method that returns how many Saved games the user has made and what number the next should be
        String filePath = "/elements.txt"; //Path to the file that needs to be backed up and saved
        String pathInit = "saves/" + StartGame.name + "/" + StartGame.name + saveNum; //Path to the destination of the file needing to be copied

        //The above Paths are now converted to Files
        File source = new File("elements.txt");
        File dir = new File(pathInit);
        File dest = new File(pathInit + filePath);

        if (!dir.isDirectory()) {
            dir.mkdirs();
            //This allows for the complete deletion of directories without the program encountering any errors. For example
            //When all saves are deleted the entire save folder is simply deleted. 
            //This can be done because of this method
        }

        Files.copy(source.toPath(), dest.toPath()); //The actual command to copy the file

        //Additional data not contained in the "elements" file is now written to the positScore.txt as can be seen, this includes all
        //statistical areas of the game as well as the password
        PrintWriter pw = new PrintWriter(new FileWriter(pathInit + "/positScore.txt"));
        pw.print(StartGame.x + "#" + StartGame.y + "#" + StartGame.score + "#" + StartGame.spades);
        pw.close();

        return StartGame.name + saveNum;
        //The Filename is returned and displayed to the user
    }

    public String getSaveNum() {

        int count = 0;

        File dir = new File("saves/" + StartGame.name);
        String[] children = dir.list();
        if (children == null) {//If the dir is empty
            count = 0;
        } else {
            count = children.length; //If it is not empty it equals to the length of the list
        }

        //http://www.dzone.com/snippets/java-listing-files-or    
        return Integer.toString(count);
        //Return his number as a String so it can be easily written into the File

    }

    public String[] getSubdir(String s) {
        //THis method is mostly/only used by the jComboBoxes in the Load game class
        //It simple allows for a combo box to pass a directory to this method. And for the method
        //to return all subdirectories in the given directory, in the form of an Array matching he ComboBox
        File f = new File(s);
        ArrayList<String> directory = new ArrayList<String>(Arrays.asList(f.list())); //the most notable command, converting a directory to List
        String directoryArr[] = new String[directory.size()];
        directoryArr = directory.toArray(directoryArr);
        return directoryArr; //return the Array 
    }

    public void loadGame(String name, String save) throws IOException, FontFormatException, InterruptedException {

        //The more complicated loadGame method loads the game.
        String path = "saves/" + name + "/" + save + "/"; //Path to the game needing to be loaded
        String pswd = ""; //password variable initialised
        String inp = ""; //Input variable used to check against password - initialised
        File check = new File(path);

        //Check the File exists else end
        if (check.exists()) {
            int reply = JOptionPane.YES_OPTION; //allows for the yes no option to only be effective if the user enters the password incorrectly
            //like in the save method the elements file is copied however in reverse
            //instead of backing up the current game's elements
            //the previously backed up file is now restored
            File source = new File(path + "elements.txt"); //file to copy
            File dest = new File("elements.txt");   //file to replace

            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING); //execute replace command

            StartGame sg = new StartGame(); //Initialise the game here so the variables cannot be reset to zero by resetting the game later
            BufferedReader fr = new BufferedReader(new FileReader(path + "positScore.txt"));
            //create a reader to read from the second textfile.
            //a specific read method is needed for this as the only time this file is utilised is saving and loading.
            String line = fr.readLine();

            StringTokenizer stk = new StringTokenizer(line, "#");
            //Using Static Variables the game can now directly change the information contained in the StartGAme class
            StartGame.x = Integer.parseInt(stk.nextToken());
            StartGame.y = Integer.parseInt(stk.nextToken());
            StartGame.score = Integer.parseInt(stk.nextToken());
            StartGame.spades = Integer.parseInt(stk.nextToken());
            StartGame.name = name;

            if (stk.hasMoreTokens()) {//If the text file has more tokens it must therefore be password protected
                pswd = stk.nextToken(); //Set the password to this next token

                while (!verifyPswd(pswd, inp)) {

                    if (reply == JOptionPane.NO_OPTION) {
                        System.exit(0); //If the user replies NO to trying again, Exit the game. This is skipped the first time as
                        //reply is automatically set to YES
                    } else {
                        inp = JOptionPane.showInputDialog("Enter Password"); //Get user Input
                        verifyPswd(pswd, inp); //Check if the password is correct
                    }

                    if (!verifyPswd(pswd, inp)) { //If it's not display the Incorrect password screen and repeat until either correct or 
                        reply = JOptionPane.showConfirmDialog(null, "Incorrect Please Try again", "Title", JOptionPane.YES_NO_OPTION);
                    }
                }

                JOptionPane.showMessageDialog(null, "Correct"); //Once the password is correct print correct

            }

            fr.close();
        }

    }

    public void deleteGame(String path) throws IOException {
        //Folder to delete is sent as parameter
        File f = new File(path);
        //As mentioned before i now make use of the 3rd party Apache library to delete directories.
        //This is because Java is not able to natively delete empty folders
        FileUtils.deleteDirectory(new File(path)); //Folder deleted

    }

    public String addPassword(String name, String save) throws FileNotFoundException, IOException {

        String path = "saves/" + name + "/" + save + "/";
        //To which file must the password be added
        File source = new File(path + "positScore.txt");

        if (!passExists(source)) { //Check if the file already contains a password, if so do not allow the user to add another
            String pswd = (String) JOptionPane.showInputDialog("Enter New Password");
            if (pswd != null) { //IF the cancel button is not clicked

                PrintWriter pw = new PrintWriter(new FileWriter(path + "positScore.txt", true)); //Append the text file positScore with
                pw.print("#" + pswd); //The password and a token
                pw.close();
                return "Password '" + pswd + "' added"; //Return the password name

            } else {
                //If the user clicks cancel simply return to previous screen
            }
        }

        return "File Already Contains Password"; //If the file contains a password return that it contains a password

    }

    public boolean verifyPswd(String Pswd, String inp) {
        //A user input and password to check against are sent as parameters
        if (inp.equals(Pswd)) { //If they equal, return true
            return true;
        } else {
            return false; //else return false
        }

    }

    public boolean passExists(File Source) throws FileNotFoundException, IOException {
        //Method to check if the file already contains a passwords
        int count = 0; //Set amount of String tokenizers to 0
        BufferedReader fr = new BufferedReader(new FileReader(Source)); //Read the file sent as a Parameter
        String line = fr.readLine();

        StringTokenizer stk = new StringTokenizer(line, "#");
        while (stk.hasMoreTokens()) { //Count how many Tokens there are in the file
            count++;
            stk.nextToken();
        }

        fr.close();

        if (count == 4) {//If there are 4 Tokens. There must not be a password
            return false;
        }
        if (count > 4) {//If there are more than 4. THere must be a password
            return true;
        }
        return true; //Satisfying java

    }

}
