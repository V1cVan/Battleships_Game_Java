package main;

import backend.Board;
import backend.Player;
import frontend.SettingsGui;
import javax.swing.SwingUtilities;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.*;
import java.lang.Thread;
import java.util.SortedMap;

/*
 * The game of Battleships.
 * @author  Victor van Wymeersch - R0690930
 * @since   2020/12/05
 */

/**
 * The BattleshipsMain handles the main functionality of the game during play.
 * It is responsible for handling each of the player objects and their corresponding board objects,
 *      who's turn it is during play, reading and writing the leaderboard textfile, determining who won the game,
 *      managing the operations associated with attacking a coordinate, and running the game/GUI.
 * The Battleships main class connects the frontend of the application to it's backend.
 * All methods and variables in the battleships main class are static as there are not multiple instances of the game.
 */

public class BattleshipsMain {
    // Game objects during play
    private static Board boardPlayerOne;
    private static Board boardPlayerTwo;
    private static Player playerOne;
    private static Player playerTwo;
    private static boolean isPlayerOnesTurn;                        // Defines who's turn it is
    private static boolean isGameOver = false;
    private static SortedMap<Integer, String> playersOnLeaderboard  // Variable containing leaderboard
            = new TreeMap<>(Collections.reverseOrder());
    /* You can adjust this boolean to disable the Gui and play from the console instead.
       (game functionality is fully present without GUI interface) */
    private final static boolean playWithGUI = true;

    public static void initBoards(int[] boardSizePlayerOne, int[] boardSizePlayerTwo){
        /*
         * Initialises the two game boards and assigns them to local board objects.
         * @param board sizes of each player. int[] = (xDimension, yDimension)
         * @return None
         */
        boardPlayerOne = new Board(boardSizePlayerOne);
        boardPlayerTwo = new Board(boardSizePlayerTwo);
    }

    public static void initPlayers(String playerOneName, String playerTwoName,
                                   boolean isPlayerOneDisadvantaged, boolean isPlayerTwoDisadvantaged){
        /*
         * Initialises the players based on the settings received from the GUI and assigns them the local player objects
         * @param Names of each of the players and whether or not they get a disadvantage for going first.
         * @return None
         */
        playerOne = new Player(playerOneName, isPlayerOneDisadvantaged);
        playerTwo = new Player(playerTwoName, isPlayerTwoDisadvantaged);
    }

    public static int[] placeShips(boolean isPlaceShipsFromFile, String playerOneFileName, String playerTwoFileName){
        /*
         * Places the ships on the board based either randomly, or from user defined text files based on the options
         *      chosen on the setting GUI.
         * @param isPlaceShipsFromFile: if ships should be placed randomly or from text files.
         *        playerOneFileName, playerTwoFileName: names of the files as defined in the settings gui
         * @return shipPlacementFileErrors: integers corresponding to types of errors found when placing the ships from
         *                                  the text files, or {0,0} if no errors were found in both files.
         */

        int[] shipPlacementFileErrors = new int[] {0,0}; // {errors in player 1 file, errors in player 2 file}
        if (isPlaceShipsFromFile){               // Place ships from specified files
            shipPlacementFileErrors[0] = boardPlayerOne.readShipPlacementFile(playerOneFileName);
            shipPlacementFileErrors[1] = boardPlayerTwo.readShipPlacementFile(playerTwoFileName);
        }else{                                           // Place ships randomly
            boardPlayerOne.placeShipsRandomly();
            boardPlayerTwo.placeShipsRandomly();
        }
        return shipPlacementFileErrors;
    }

    public static double getPlayerOneScore(){
        return playerOne.getScore();
    }

    public static double getPlayerTwoScore(){
        return playerTwo.getScore();
    }

    public static boolean getIsPlayerOnesTurn(){
        return isPlayerOnesTurn;
    }

    public static void setIsPlayerOnesTurn(boolean isPlayerOneTurn){
        /*
         * Returns who's turn it is in the game. isPlayerOnesTurn == false if it is player 2's turn.
         */
        isPlayerOnesTurn = isPlayerOneTurn;
    }

    public static boolean getIsGameOver(){
        /*
         * Returns the game's status corresponding to whether or not all the ships have been sunk on a board.
         */
        return isGameOver;
    }

    public static boolean attackCoordinates(int[] attackCoordinates) throws InterruptedException {
        /*
         * Process the action of attacking a set of coordinates on the board.
         * Checks if the attack was valid, gets the points for the attack, increases player scores,
         *      and checks if the game is over.
         * @param attackCoordinates: The coordinates of the attacked tile on the board corresponding to the GUI
         *                           click location
         * @return Boolean value corresponding to if the attack was valid. If false the player gets to attack again.
         */
        boolean isValidAttack = true;
        if (isPlayerOnesTurn == false) {  // Player 2 turn
            // See if player 2 gets points for shot taken at the attack coordinates
            int pointsForAttack = boardPlayerOne.pointsForAttack(attackCoordinates);
            if ( pointsForAttack > 0){
                if (playWithGUI == false) {
                    System.out.println("Hit!");
                    Thread.sleep(700);
                }
                // Assign points for hit
                playerTwo.increaseScore(pointsForAttack);
                // Check if game is over
                if (boardPlayerOne.areAllShipsSunk()){
                    isGameOver = true;
                }
            }else if (pointsForAttack == -1){
                isValidAttack = false;
            }else{
                if (playWithGUI == false) {
                    System.out.println("Miss!");
                    Thread.sleep(700);
                }
            }
        }else{  // Player 1 turn
            // See if player 1 gets points for shot taken at the attack coordinates
            int pointsForAttack = boardPlayerTwo.pointsForAttack(attackCoordinates);
            if ( pointsForAttack > 0){
                if (playWithGUI == false) {
                    System.out.println("Hit!");
                    Thread.sleep(700);
                }
                // Assign points for hit
                playerOne.increaseScore(pointsForAttack);
                // Check if game is over
                if (boardPlayerTwo.areAllShipsSunk()){
                    isGameOver = true;
                }
            }else if (pointsForAttack == -1){
                isValidAttack = false;
            }else{
                if (playWithGUI == false) {
                    System.out.println("Miss!");
                    Thread.sleep(700);
                }
            }
        }
        return isValidAttack;
    }

    public static char[][] getCurrentPlayerBoardChars(){
        /*
         * Provides the current board representation as tensor of characters based on who's turn it is.
         * @param None
         * @return Character representation of board being attacked
         */
        char[][] boardCharacters;
        if (isPlayerOnesTurn){// return player 2's board color characters of the tiles
            boardCharacters = boardPlayerTwo.getCharBasedBoard();
        }else{// return player 1's board color characters of the tiles
            boardCharacters = boardPlayerOne.getCharBasedBoard();
        }
        return boardCharacters;
    }

    public static int checkWhoWon(){
        /*
         * Checks who won the game by comparing the player scores.
         * @param None
         * @return Integer representing game result: 1  = player 1 won the game
         *                                           0  = player 2 won the game
         *                                           -1 = game was a draw
         */

        int didPlayerOneWin;
        if (playerOne.getScore()>playerTwo.getScore()){
            // Player one won the game
            didPlayerOneWin = 1;
        }else if (playerOne.getScore()<playerTwo.getScore()){
            // Player two won the game
            didPlayerOneWin = 0;
        }else{
            // Game is a draw
            didPlayerOneWin = -1;
        }
        return didPlayerOneWin;
    }

    public static int[] readBoardSize(String filename) {
        /*
         * Reads the board size from the first line of the provided text files.
         * @param filename of the textfile to be read from
         * @return boardSize as (xDimension, yDimension)
         * @throws FileNotFoundException, IOException, Invalid boardSize specification
         */

        // boardDimension defaults to 8 if not successfully read from file so that user menu experience is improved
        int boardDimension = 8;
        try(
                FileReader file = new FileReader(filename);
                Scanner scan = new Scanner(file))
        {
            String line = scan.nextLine();
            line = line.strip();
            if (line != null){
                try {
                    boardDimension = Integer.parseInt(line);
                }catch(NumberFormatException numberFormatException){
                    System.out.println("Problem with board size definition in gameSettings textfile");
                    System.out.println("Setting default board size of (Xsize,Ysize) = (8,8)");
                }
            }else { // invalid board size
                System.out.println("Problem with board size definition in gameSettings textfile");
                System.out.println("Setting default board size of (Xsize,Ysize) = (8,8)");
            }
        } catch(FileNotFoundException fileNotFoundException){
            System.out.println("Problem with board size definition. We cannot find the file, check spelling please.");
            System.out.println("Setting default board size of (Xsize,Ysize) = (8,8)");
            fileNotFoundException.printStackTrace();
        } catch(IOException ioException) {
            System.out.println("Problem with board size definition.");
            System.out.println("Setting default board size of (Xsize,Ysize) = (8,8)");
            ioException.printStackTrace();
        }

        return new int[] {boardDimension, boardDimension};
    }

    private static void loadLeaderboardFromFile(){
        /*
         * Reads the leaderboard text file into the locally defined leaderboard class variable.
         * @throws FileNotFoundException, IOException
         */

        try(
                FileReader file = new FileReader("src/datafiles/leaderboard.txt");
                Scanner scan = new Scanner(file))
        {
            while (scan.hasNext()) {
                String line = scan.nextLine();
                String[] lineElements = line.split("\\,");
                playersOnLeaderboard.put(Integer.valueOf(lineElements[1].strip()), lineElements[0].strip());
            }
        } catch(FileNotFoundException fileNotFoundException){
            fileNotFoundException.printStackTrace();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static String getLeaderboard(){
        /*
         * Returns a formatted string containing players on the games leaderboard.
         * @param None
         * @return Sorted leaderboard in string format to be displayed on a GUI message to the user
         */

        loadLeaderboardFromFile();
        String leaderBoard = "";
        Set set = playersOnLeaderboard.entrySet();
        Iterator i = set.iterator();

        // Loop players on leaderboard map and write's keys and values to a leaderboard string with proper formatting
        int positionCounter = 1;
        while (i.hasNext()) {
            Map.Entry tempMap = (Map.Entry)i.next();
            leaderBoard = leaderBoard + positionCounter + ".  "
                    + tempMap.getValue()+ " ("+ tempMap.getKey() +" wins)\n";
            positionCounter ++;
        }
        return leaderBoard;
    }

    public static void addWinnerToLeaderboard(String playerName){
        /*
         * Rewrites the leaderboard text file to include the new winner of the game just played.
         * If the winner's name already exists on the leaderboard his score is increased.
         * @param Player's name that just wont the game.
         * @return None
         */
        loadLeaderboardFromFile();

        Set set = playersOnLeaderboard.entrySet();
        Iterator i = set.iterator();

        String playerNameOriginal = playerName;
        int newPlayerNumWins = 1;
        boolean existsOnScoreboard = false;
        playerName = playerName.toLowerCase();

        // Loop players on leaderboard map and check if the player already exists on the leaderboard
        while (i.hasNext()) {
            Map.Entry tempMap = (Map.Entry)i.next();
            String name = String.valueOf(tempMap.getValue()).toLowerCase().strip();
            int oldPlayerNumberWins = (int) tempMap.getKey();
            if (name.equals(playerName)){
                existsOnScoreboard = true;
                // increase the players number of wins if he/she exists on the leaderboard
                newPlayerNumWins = oldPlayerNumberWins +1;
            }
        }
        if (existsOnScoreboard){
            playersOnLeaderboard.remove(newPlayerNumWins-1);
        }
        playersOnLeaderboard.put(newPlayerNumWins, playerNameOriginal);

        // Write new leaderboard to the leaderboard text file
        try(FileWriter fileWriter = new FileWriter("src/datafiles/leaderboard.txt", false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter output = new PrintWriter(bufferedWriter))
        {
            Set writeSet = playersOnLeaderboard.entrySet();
            // Write player names and their numbers of wins to each line of the leaderboard textfile
            for (Object o : writeSet) {
                Map.Entry tempMap = (Map.Entry) o;
                String name = String.valueOf(tempMap.getValue()).toLowerCase().strip();
                int wins = (int) tempMap.getKey();
                output.println(name + ", " + wins);
            }
        } catch(FileNotFoundException fileNotFoundException){
            fileNotFoundException.printStackTrace();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        /*
         * Main method of application. Runs the game either in GUI or console-based format.
         */


        // Play game with GUI
        if (playWithGUI) {
            SwingUtilities.invokeLater(SettingsGui::new);


        /*
        !NOTE!
        The rest of the code below here is for playing the game without a GUI !!!
        (This should be ignored when marking this project as the GUI is fully implemented and without errors).
        Playing the game without the GUI does not have as much functionality and error checking as playing the game
        with the GUI.
        */
        }else{
            // Initialise player boards:
            String gameFilePlayer1 = "src/datafiles/gameSettingsPlayer1.txt";
            String gameFilePlayer2 = "src/datafiles/gameSettingsPlayer2.txt";

            boolean boardSizeFromFile = false;
            int[] boardSizePlayer1;
            int[] boardSizePlayer2;
            if (boardSizeFromFile == true){
                boardSizePlayer1 = readBoardSize(gameFilePlayer1);
                boardSizePlayer2 = readBoardSize(gameFilePlayer2);
            }else{
                // Board size in the form: [numberRows x numberColumns]
                int boardNumRows = 8;
                int boardNumColumns = 8;
                boardSizePlayer1 = new int[] {boardNumRows,boardNumColumns};
                boardSizePlayer2 = new int[] {boardNumRows,boardNumColumns};
            }

            initBoards(boardSizePlayer1, boardSizePlayer2);

            boolean placeShipsRandomly = !boardSizeFromFile;
            if (placeShipsRandomly){
                boardPlayerOne.placeShipsRandomly();
                boardPlayerTwo.placeShipsRandomly();
            }else{
                boardPlayerOne.readShipPlacementFile(gameFilePlayer1);
                boardPlayerTwo.readShipPlacementFile(gameFilePlayer2);
            }

            // Initialise players and scoreboard:
            String playerOneName = "Lecturer";
            String playerTwoName = "Student";
            initPlayers(playerOneName, playerTwoName, false, false);

            // Play the game:
            isPlayerOnesTurn = true;
            Scanner userInput = new Scanner(System.in);
            System.out.println("\nGame has begun ... \n");
            while (isGameOver == false) {
                System.out.println("Current Score:");
                System.out.println(playerOne.getName()+" = "+ playerOne.getScore());
                System.out.println(playerTwo.getName()+" = "+ playerTwo.getScore()+"\n");
                if (isPlayerOnesTurn == false) {  // Player 2 turn
                    System.out.println(playerTwo.getName()+"'s turn! Take your shot.\n");
                    System.out.println(boardPlayerOne.getTextBasedBoard());
                }else {  // Player 1 turn
                    System.out.println(playerOne.getName()+"'s Turn! Take your shot...\n");
                    System.out.println(boardPlayerTwo.getTextBasedBoard());
                }

                // Take shot at board:
                // Get user input for attack coordinates
                System.out.println("Enter attack coordinates (x=1..8, y=1..8) {without brackets and separated with comma}");
                String input = userInput.nextLine();
                int x = Integer.parseInt(input.split(",")[0]);
                int y = Integer.parseInt(input.split(",")[1]);
                int[] coordinates = new int[] {x-1,y-1};

                attackCoordinates(coordinates);

                isPlayerOnesTurn = !isPlayerOnesTurn;
            } // end of game loop

            // Check if player one won the game 1=yes, 0=no, -1=draw
            if (checkWhoWon() == 1){
                System.out.println(playerOne.getName()+" has won the game!");
            }else if (checkWhoWon() == 0){
                System.out.println(playerTwo.getName()+" has won the game!");
            }else{
                System.out.println("The game is a tie!");
            }
        }

    }

}
