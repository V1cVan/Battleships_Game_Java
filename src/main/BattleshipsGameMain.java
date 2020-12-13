package main;// Import packages:
import backend.Board;
import backend.Player;
import frontend.SplashGuiManager;
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

public class BattleshipsGameMain {
    private static Board boardPlayerOne;
    private static Board boardPlayerTwo;
    private static Player playerOne;
    private static Player playerTwo;
    private static boolean isPlayerOnesTurn;
    private static boolean isGameOver = false;
    private static SortedMap<Integer, String> playersOnLeaderboard
            = new TreeMap<Integer, String>(Collections.reverseOrder());

    // You can adjust this boolean to disable the Gui and play from the console instead.
    // (game functionality is fully present without GUI interface)
    private static boolean playWithoutGUI = false;

    public static void initBoards(int[] boardSizePlayerOne, int[] boardSizePlayerTwo){
        boardPlayerOne = new Board(boardSizePlayerOne);
        boardPlayerTwo = new Board(boardSizePlayerTwo);
    }

    public static int[] placeShips(boolean isPlaceShipsFromFile, String playerOneFileName, String playerTwoFileName){
        int[] shipPlacementFileErrors = new int[] {0,0};
        if (isPlaceShipsFromFile == true){
            shipPlacementFileErrors[0] = boardPlayerOne.readShipPlacementFile(playerOneFileName);
            shipPlacementFileErrors[1] = boardPlayerTwo.readShipPlacementFile(playerTwoFileName);
        }else{ // Place ships randomly
            boardPlayerOne.placeShipsRandomly();
            boardPlayerTwo.placeShipsRandomly();
        }
        return shipPlacementFileErrors;
    }

    public static void initPlayers(String playerOneName, String playerTwoName,
                           boolean isPlayerOneDisadvantaged, boolean isPlayerTwoDisadvantaged){
        playerOne = new Player(playerOneName, isPlayerOneDisadvantaged);
        playerTwo = new Player(playerTwoName, isPlayerTwoDisadvantaged);
    }

    public double getPlayerOneScore(){
        return playerOne.getScore();
    }

    public double getPlayerTwoScore(){
        return playerTwo.getScore();
    }

    public boolean getIsPlayerOnesTurn(){
        return isPlayerOnesTurn;
    }

    public void setIsPlayerOnesTurn(boolean isPlayerOnesTurn){
        this.isPlayerOnesTurn = isPlayerOnesTurn;
    }

    public boolean getIsGameOver(){
        return isGameOver;
    }

    public static boolean attackCoordinates(int[] attackCoordinates) throws InterruptedException {
        boolean isValidAttack = true;
        if (isPlayerOnesTurn == false) {  // Player 2 turn
            // See if player gets points for shot
            int pointsForHit = boardPlayerOne.pointsForHit(attackCoordinates);
            if ( pointsForHit > 0){
                if (playWithoutGUI == true) {
                    System.out.println("Hit!");
                    Thread.sleep(700);
                }
                // Assign points for hit
                playerTwo.increaseScore(pointsForHit);
                // Check if game is over
                if (boardPlayerOne.areAllShipsSunk()){
                    isGameOver = true;
                }
            }else if (pointsForHit == -1){
                isValidAttack = false;
            }else{
                if (playWithoutGUI == true) {
                    System.out.println("Miss!");
                    Thread.sleep(700);
                }
            }
        }else{  // Player 1 turn
            int pointsForHit = boardPlayerTwo.pointsForHit(attackCoordinates);
            if ( pointsForHit > 0){
                if (playWithoutGUI == true) {
                    System.out.println("Hit!");
                    Thread.sleep(700);
                }
                // Assign points for hit
                playerOne.increaseScore(pointsForHit);
                // Check if game is over
                if (boardPlayerTwo.areAllShipsSunk()){
                    isGameOver = true;
                }
            }else if (pointsForHit == -1){
                isValidAttack = false;
            }else{
                if (playWithoutGUI == true) {
                    System.out.println("Miss!");
                    Thread.sleep(700);
                }
            }
        }
        return isValidAttack;
    }

    public char[][] getCurrentBoardChars(){
        char[][] boardCharacters;
        if (isPlayerOnesTurn){// return player 2's board color characters of the tiles
            boardCharacters = boardPlayerTwo.getCharBasedBoard();
        }else{// return player 1's board color characters of the tiles
            boardCharacters = boardPlayerOne.getCharBasedBoard();
        }
        return boardCharacters;
    }

    public static int checkWhoWon(){
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
        /**
         Reads the board size from the provided files.
         @param filename = file read from
         @return boardSize
         @throws FileNotFoundException, IOException, Invalid boardSize
         */

        // boardDimension defaults to 8 if not successfully read from file
        int boardDimension = 8;
        try(
                FileReader file = new FileReader(filename);
                Scanner scan = new Scanner(file);)
        {
            String line = scan.nextLine();
            line = line.strip();
            if (line != null){
                try {
                    boardDimension = Integer.parseInt(line);
                }catch(NumberFormatException numberFormatException){
                    System.out.println("Problem with board size definition in gameSettings*.txt");
                    System.out.println("Setting default board size of (Xsize,Ysize) = (8,8)");
                }
            }else { // invalid board size
                System.out.println("Problem with board size definition in gameSettings*.txt");
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
        int[] boardSize = new int[] {boardDimension, boardDimension};
        return boardSize;
    }

    public void loadLeaderboardFromFile(){
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

    public String getLeaderboard(){
        loadLeaderboardFromFile();
        String leaderBoard = "";
        Set set = playersOnLeaderboard.entrySet();
        Iterator i = set.iterator();

        // Loop through map and add to leaderboard string
        int positionCounter = 1;
        while (i.hasNext()) {
            Map.Entry tempMap = (Map.Entry)i.next();
            leaderBoard = leaderBoard + String.valueOf(positionCounter) + ".  "
                    + tempMap.getValue()+ " ("+String.valueOf(tempMap.getKey()) +" wins)\n";
            positionCounter ++;
        }
        return leaderBoard;
    }

    public void addWinnerToLeaderboard(String playerName){
        Set set = playersOnLeaderboard.entrySet();
        Iterator i = set.iterator();

        // Loop through leaderboard
        String playerNameOriginal = playerName;
        int newPlayerNumWins = 1;
        boolean existsOnScoreboard = false;
        playerName = playerName.toLowerCase();

        while (i.hasNext()) {
            Map.Entry tempMap = (Map.Entry)i.next();
            String name = String.valueOf(tempMap.getValue()).toLowerCase().strip();
            int oldPlayerNumberWins = (int) tempMap.getKey();
            if (name.equals(playerName)){
                existsOnScoreboard = true;
                newPlayerNumWins = oldPlayerNumberWins +1;
            }
        }
        if (existsOnScoreboard){
            playersOnLeaderboard.remove(newPlayerNumWins-1);
            playersOnLeaderboard.put(newPlayerNumWins, playerNameOriginal);
        }else{
            playersOnLeaderboard.put(newPlayerNumWins, playerNameOriginal);
        }

        try(FileWriter fileWriter = new FileWriter("src/datafiles/leaderboard.txt", false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter output = new PrintWriter(bufferedWriter))
        {
            Set writeSet = playersOnLeaderboard.entrySet();
            Iterator writeIterator = writeSet.iterator();

            while (writeIterator.hasNext()) {
                Map.Entry tempMap = (Map.Entry)writeIterator.next();
                String name = String.valueOf(tempMap.getValue()).toLowerCase().strip();
                int wins = (int) tempMap.getKey();
                output.println(name +", "+String.valueOf(wins));
            }
        } catch(FileNotFoundException fileNotFoundException){
            fileNotFoundException.printStackTrace();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        // play game with GUI
        if (playWithoutGUI == false) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    SplashGuiManager splashGui = new SplashGuiManager();
                }
            });
        // play without GUI (can ignore when marking this project as GUI is fully working).
        }else{
            // Initialise player boards:
            String gameFilePlayer1 = new String("src/datafiles/gameSettingsPlayer1.txt");
            String gameFilePlayer2 = new String("src/datafiles/gameSettingsPlayer2.txt");
            String playerScoreboardFile = new String("src/datafiles/playerScoreboard.txt");

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
                System.out.println(playerOne.getName()+" = "+ String.valueOf(playerOne.getScore()));
                System.out.println(playerTwo.getName()+" = "+ String.valueOf(playerTwo.getScore())+"\n");
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
