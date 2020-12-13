package frontend;

import main.BattleshipsMain;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * The Board class manages each player's board during the game.
 * It is responsible for placing ships either randomly or from the user provided text files,
 *      checking whether or not all the ships on the board have been sunk (game end),
 *      the number of points received for attacking a specific tile,
 *      and providing the current status of the board at the start of each turn.
 * It hosts a number of tile objects relative to the board size, and all the ship objects placed on the board.
 * The Board class is instantiated twice in the game, once for each player.
 * Board has a single property BOARD_SIZE.
 */

public class GameBoardGui {
    // GUI related visual variables:
    private JFrame mainGuiFrame;
    private JPanel informationPanel = new JPanel();
    private JPanel player1Panel = new JPanel();
    private JPanel player2Panel = new JPanel();
    private JPanel currentTurnPanel = new JPanel();
    private JLabel player1Label = new JLabel();
    private JLabel player1ScoreLabel = new JLabel();
    private JLabel player2Label = new JLabel();
    private JLabel player2ScoreLabel = new JLabel();
    private JLabel turnLabel = new JLabel("Turn:");
    private JLabel currentTurnLabel = new JLabel();
    private JButton exitButton = new JButton("Quit Game");
    private JButton scoreBoardButton = new JButton("Leaderboard");
    private JPanel boardPanel = new JPanel();
    private JButton[][] boardButton;

    // Game functionality variables:
    private BattleshipsMain gameMain = new BattleshipsMain();
    private final boolean IS_BOARD_SIZE_FROM_FILE;
    private final String PLAYER_ONE_NAME;
    private final String PLAYER_TWO_NAME;
    private final boolean IS_PLAYER_ONE_FIRST;
    private final boolean IS_PLAYER_ONE_DISADVANTAGED;
    private final boolean IS_PLAYER_TWO_DISADVANTAGED;
    private final int[] BOARD_SIZE_PLAYER_ONE;
    private final int[] BOARD_SIZE_PLAYER_TWO;

    public GameBoardGui(boolean isBoardSizeFrmFile,
                        String playerOneNme,
                        String playerTwoNme,
                        boolean playerOneFirst,
                        boolean playerOneDisadvantaged,
                        boolean playerTwoDisadvantaged,
                        int[] boardSzePlayerOne,
                        int[] boardSzePlayerTwo,
                        String playerOneLayoutFileNme,
                        String playerTwoLayoutFileNme) {

        IS_BOARD_SIZE_FROM_FILE = isBoardSizeFrmFile;
        PLAYER_ONE_NAME = playerOneNme;
        PLAYER_TWO_NAME = playerTwoNme;
        IS_PLAYER_ONE_FIRST = playerOneFirst;
        IS_PLAYER_ONE_DISADVANTAGED = playerOneDisadvantaged;
        IS_PLAYER_TWO_DISADVANTAGED = playerTwoDisadvantaged;

        if (IS_BOARD_SIZE_FROM_FILE == true){
            // Read board size from files if custom ship placement was selected
            BOARD_SIZE_PLAYER_ONE = gameMain.readBoardSize(playerOneLayoutFileNme);
            BOARD_SIZE_PLAYER_TWO = gameMain.readBoardSize(playerTwoLayoutFileNme);
        }else{
            // Get board size from spinners on splash screen
            BOARD_SIZE_PLAYER_ONE = boardSzePlayerOne;
            BOARD_SIZE_PLAYER_TWO = boardSzePlayerTwo;
        }

        // Current turn is set depending on whether player one was selected to go first
        gameMain.setIsPlayerOnesTurn(IS_PLAYER_ONE_FIRST);

        // Initialise the two boards in the backend
        gameMain.initBoards(BOARD_SIZE_PLAYER_ONE, BOARD_SIZE_PLAYER_TWO);
        // Initialise the two players in the backend
        gameMain.initPlayers(PLAYER_ONE_NAME, PLAYER_TWO_NAME,
                IS_PLAYER_ONE_DISADVANTAGED, IS_PLAYER_TWO_DISADVANTAGED);

        // Place ships on the boards, either randomly or from the text file definitions
        int[] shipPlacementFileErrors = gameMain.placeShips(
                IS_BOARD_SIZE_FROM_FILE,
                playerOneLayoutFileNme,
                playerTwoLayoutFileNme);

        displayBoatPlacementErrors(shipPlacementFileErrors);

        // Initialise main gui frame
        mainGuiFrame = new JFrame("Battleship");

        // Initialise tiles on board for the player going first
        this.createBoardGui();
        boardPanel.setMinimumSize(new Dimension(800,800));

        // Set game gui fonts:
        player1Label.setFont(new Font("", Font.BOLD, 16));
        player1ScoreLabel.setFont(new Font("", Font.BOLD, 16));
        turnLabel.setFont(new Font("", Font.BOLD, 16));
        currentTurnLabel.setFont(new Font("", Font.BOLD, 24));
        player2Label.setFont(new Font("", Font.BOLD, 16));
        player2ScoreLabel.setFont(new Font("", Font.BOLD, 16));
        exitButton.setFont(new Font("", Font.PLAIN, 16));
        scoreBoardButton.setFont(new Font("", Font.PLAIN, 16));

        // Set panel containing game information above the board
        informationPanel.setLayout(new GridLayout(1, 5));
        informationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        informationPanel.add(scoreBoardButton);
        scoreBoardButton.addActionListener(new ScoreboardActionListener());

        // Player 1 information:
        player1Panel.setLayout(new GridLayout(2,1));
        player1Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        player1Label.setText(PLAYER_ONE_NAME+"'s score:");
        player1Panel.add(player1Label);
        player1ScoreLabel.setHorizontalAlignment(JLabel.CENTER);
        player1Panel.add(player1ScoreLabel);
        informationPanel.add(player1Panel);

        // Current turn panel:
        currentTurnPanel.setLayout(new GridLayout(2,1));
        currentTurnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        currentTurnPanel.add(turnLabel);
        turnLabel.setHorizontalAlignment(JLabel.CENTER);
        currentTurnLabel.setHorizontalAlignment(JLabel.CENTER);
        currentTurnPanel.add(currentTurnLabel);
        informationPanel.add(currentTurnPanel);

        // Player 2 information:
        player2Panel.setLayout(new GridLayout(2,1));
        player2Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        player2Label.setText(PLAYER_TWO_NAME+"'s score:");
        player2Panel.add(player2Label);
        player2ScoreLabel.setHorizontalAlignment(JLabel.CENTER);
        player2Panel.add(player2ScoreLabel);
        informationPanel.add(player2Panel);

        // Place exit button
        exitButton.addActionListener(new ExitActionListener());
        informationPanel.add(exitButton);

        // Add items to main gui for battleships game:
        mainGuiFrame.setLayout(new BorderLayout());
        mainGuiFrame.setLocation(1000, 100);
        mainGuiFrame.setMinimumSize(new Dimension(800,800));
        mainGuiFrame.add(informationPanel, BorderLayout.NORTH);
        mainGuiFrame.add(boardPanel, BorderLayout.CENTER);
        mainGuiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGuiFrame.pack();
        mainGuiFrame.setVisible(true);
    }

    public void createBoardGui(){
        // Set current score

        player1ScoreLabel.setText(String.format("%.2f",gameMain.getPlayerOneScore()));
        player2ScoreLabel.setText(String.format("%.2f",gameMain.getPlayerTwoScore()));

        // Get current attacked players board in characters
        char[][] boardCharacters = gameMain.getCurrentPlayerBoardChars();

        // Create board tiles
        if (gameMain.getIsPlayerOnesTurn()){
            // Set turn label
            currentTurnLabel.setText(PLAYER_ONE_NAME);
            // Set board of player 2
            boardButton = new JButton[BOARD_SIZE_PLAYER_TWO[0]][BOARD_SIZE_PLAYER_TWO[1]];
            boardPanel.setLayout(new GridLayout(BOARD_SIZE_PLAYER_TWO[0],BOARD_SIZE_PLAYER_TWO[1]));
            for (int xTileIndex=0; xTileIndex<BOARD_SIZE_PLAYER_TWO[0]; xTileIndex++){
                for (int yTileIndex=0; yTileIndex<BOARD_SIZE_PLAYER_TWO[1]; yTileIndex++){
                    boardButton[xTileIndex][yTileIndex] = new JButton();
                    boardButton[xTileIndex][yTileIndex].setSize(50,50);
                    boardButton[xTileIndex][yTileIndex].addActionListener(
                            new AttackActionListener(xTileIndex, yTileIndex));
                    boardButton[xTileIndex][yTileIndex].setBackground(
                            getMatchingColor(boardCharacters[xTileIndex][yTileIndex]));
                    boardPanel.add(boardButton[xTileIndex][yTileIndex]);
                }
            }
        }else{ // if player two's turn
            // Set turn label
            currentTurnLabel.setText(PLAYER_TWO_NAME);
            // Set board of player 1
            boardButton = new JButton[BOARD_SIZE_PLAYER_ONE[0]][BOARD_SIZE_PLAYER_ONE[1]];
            boardPanel.setLayout(new GridLayout(BOARD_SIZE_PLAYER_ONE[0],BOARD_SIZE_PLAYER_ONE[1]));
            for (int xTileIndex=0; xTileIndex<BOARD_SIZE_PLAYER_ONE[0]; xTileIndex++){
                for (int yTileIndex=0; yTileIndex<BOARD_SIZE_PLAYER_ONE[1]; yTileIndex++){
                    boardButton[xTileIndex][yTileIndex] = new JButton();
                    boardButton[xTileIndex][yTileIndex].setSize(50,50);
                    boardButton[xTileIndex][yTileIndex].addActionListener(
                            new AttackActionListener(xTileIndex, yTileIndex));
                    boardButton[xTileIndex][yTileIndex].setBackground(
                            getMatchingColor(boardCharacters[xTileIndex][yTileIndex]));
                    boardPanel.add(boardButton[xTileIndex][yTileIndex]);
                }
            }
        }
    }

    private Color getMatchingColor(char tileCharacter){
        Color tileColor;
        switch(tileCharacter) {
            case 'f': // fog of war ; grey tile
                tileColor = Color.GRAY;
                break;
            case 'w': // water ; blue tile
                tileColor = Color.BLUE;
                break;
            case 'd': // destroyer ; green tile
                tileColor = Color.GREEN;
                break;
            case 's': // submarine ; pink tile
                tileColor = Color.MAGENTA;
                break;
            case 'b': // battleship ; red tile
                tileColor = Color.RED;
                break;
            case 'c': // carrier ; yellow tile
                tileColor = Color.YELLOW;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + tileCharacter);
        }
        return tileColor;
    }

    private class ExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainGuiFrame.dispose();
            System.exit(0);
        }
    }

    private class ScoreboardActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String leaderboard = gameMain.getLeaderboard();
            JOptionPane.showMessageDialog(mainGuiFrame,
                    "Local Battleships Scoreboard:\n\n" +
                            leaderboard,
                    "Local Leaderboard",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class AttackActionListener implements ActionListener  {
        // Attack coordinates
        private int xAttackCoordinate;
        private int yAttackCoordinate;
        public AttackActionListener(int xTileIndex, int yTileIndex){
            // Set attack coordinates corresponding to the button pressed on the GUI
            xAttackCoordinate = xTileIndex;
            yAttackCoordinate = yTileIndex;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try { // Try Catch block is implemented for thread sleep after an attack
                int[] attackCoordinates = new int[] {xAttackCoordinate, yAttackCoordinate};
                boolean isValidAttack = gameMain.attackCoordinates(attackCoordinates);
                if (isValidAttack == true) {
                    // Next players turn if attack was not on a tile already attacked
                    gameMain.setIsPlayerOnesTurn(!gameMain.getIsPlayerOnesTurn());
//                    Thread.sleep(500);
                    boardPanel.removeAll();
                    createBoardGui();
                }else{
                    gameMain.setIsPlayerOnesTurn(gameMain.getIsPlayerOnesTurn());
                }
                if (gameMain.getIsGameOver() == true){
                    if(gameMain.checkWhoWon() == 1) {
                        gameMain.addWinnerToLeaderboard(PLAYER_ONE_NAME);
                        JOptionPane.showMessageDialog(mainGuiFrame,
                                PLAYER_ONE_NAME.toUpperCase()+" HAS WON THE GAME!\n\n"+
                                        "Score Overview:\n" +
                                        PLAYER_ONE_NAME+" = "+String.valueOf(gameMain.getPlayerOneScore())+" points\n"+
                                        PLAYER_TWO_NAME+" = "+String.valueOf(gameMain.getPlayerTwoScore())+" points",
                                "The game is over!",
                                JOptionPane.INFORMATION_MESSAGE);
                    }else if(gameMain.checkWhoWon() == 0){
                        gameMain.addWinnerToLeaderboard(PLAYER_TWO_NAME);
                        JOptionPane.showMessageDialog(mainGuiFrame,
                                PLAYER_TWO_NAME.toUpperCase()+" HAS WON THE GAME!\n\n"+
                                        "Score Overview:\n" +
                                        PLAYER_ONE_NAME+" = "+String.valueOf(gameMain.getPlayerOneScore())+" points\n"+
                                        PLAYER_TWO_NAME+" = "+String.valueOf(gameMain.getPlayerTwoScore())+" points",
                                "The game is over!",
                                JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(mainGuiFrame,
                                "THE GAME IS A TIE!\n\n"+
                                        "Score Overview:\n" +
                                        PLAYER_ONE_NAME+" = "+String.valueOf(gameMain.getPlayerOneScore())+" points\n"+
                                        PLAYER_TWO_NAME+" = "+String.valueOf(gameMain.getPlayerTwoScore())+" points",
                                "The game is over!",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    mainGuiFrame.dispose();
                    String leaderboard = gameMain.getLeaderboard();
                    JOptionPane.showMessageDialog(mainGuiFrame,
                            "Local Battleships Scoreboard:\n\n" +
                                    leaderboard,
                            "Local Leaderboard",
                            JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }

            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }

    private void displayBoatPlacementErrors(int[] shipPlacementFileErrors) {
        // If the user selected to place the ships using the textfiles check if they were properly loaded
        if (IS_BOARD_SIZE_FROM_FILE == true) {
            String errorMessageFileOne;
            String errorMessageFileTwo;
            int fileOneErrors = shipPlacementFileErrors[0];
            switch (fileOneErrors) {
                case 0:
                    errorMessageFileOne = "Ship definition text file was loaded successfully.";
                    break;
                case 3:
                    errorMessageFileOne = "Invalid placement of ships: Too few ships defined!";
                    break;
                case 4:
                    errorMessageFileOne = "Invalid placement of ships. Too many ships defined!";
                    break;
                case 5:
                    errorMessageFileOne = "Invalid placement of ships. Too few coordinates specified one of the ships.";
                    break;
                case 6:
                    errorMessageFileOne = "Invalid placement of ships. Too many coordinates specified one of the ships.";
                    break;
                case 8:
                    errorMessageFileOne = "Invalid placement of ships. A ship was possibly spelled incorrectly.";
                    break;
                case 9:
                    errorMessageFileOne = "Invalid placement of ships. Coordinates have to be numbers.";
                    break;
                case 10:
                    errorMessageFileOne = "Error during placement of ships. " +
                            "Ship definition text file was not found.";
                    break;
                case 11:
                    errorMessageFileOne = "Error during placement of ships. " +
                            "Ship definition text file had an InputOutputException.";
                    break;
                default:
                    errorMessageFileOne = "Error during placement of ships. " +
                            "Possible causes:\n" +
                            "- A ship has been placed off of the board.\n" +
                            "- A ship has been placed on another ship.\n" +
                            "- A ship is not continuous.";
            }

            int fileTwoErrors = shipPlacementFileErrors[1];
            switch (fileTwoErrors) {
                case 0:
                    errorMessageFileTwo = "Ship definition text file was loaded successfully.";
                    break;
                case 3:
                    errorMessageFileTwo = "Invalid placement of ships: Too few ships defined!";
                    break;
                case 4:
                    errorMessageFileTwo = "Invalid placement of ships. Too many ships defined!";
                    break;
                case 5:
                    errorMessageFileTwo = "Invalid placement of ships. Too few coordinates specified one of the ships.";
                    break;
                case 6:
                    errorMessageFileTwo = "Invalid placement of ships. Too many coordinates specified one of the ships.";
                    break;
                case 8:
                    errorMessageFileTwo = "Invalid placement of ships. A ship was possibly spelled incorrectly.";
                    break;
                case 9:
                    errorMessageFileTwo = "Invalid placement of ships. Coordinates have to be numbers.";
                    break;
                case 10:
                    errorMessageFileTwo = "Error during placement of ships. " +
                            "Ship definition text file was not found.";
                    break;
                case 11:
                    errorMessageFileTwo = "Error during placement of ships. " +
                            "Ship definition text file had an InputOutputException.";
                    break;
                default:
                    errorMessageFileTwo = "Error during placement of ships. " +
                            "Possible causes:\n" +
                            "- A ship has been placed off of the board.\n" +
                            "- A ship has been placed on another ship.\n" +
                            "- A ship is not continuous.";
            }
            if ((fileOneErrors == 0) & (fileTwoErrors) == 0) {
                JOptionPane.showMessageDialog(mainGuiFrame,
                        PLAYER_ONE_NAME + "'s ship placement file was loaded successfully.\n\n" +
                                PLAYER_TWO_NAME + "'s ship placement files was loaded successfully.\n\n" +
                                "Enjoy the game!",
                        "Loading ship placement text files.",
                        JOptionPane.INFORMATION_MESSAGE);
            } else if ((fileOneErrors != 0) & (fileTwoErrors == 0)) {
                JOptionPane.showMessageDialog(mainGuiFrame,
                        PLAYER_ONE_NAME + "'s ship placement file was NOT loaded successfully.\n" +
                                errorMessageFileOne + "\n" + "The ships have been placed randomly instead.\n\n" +
                                PLAYER_TWO_NAME + "'s ship placement files was loaded successfully.\n\n" +
                                "Enjoy the game!",
                        "Loading ship placement text files.",
                        JOptionPane.WARNING_MESSAGE);
            } else if ((fileOneErrors == 0) & (fileTwoErrors != 0)) {
                JOptionPane.showMessageDialog(mainGuiFrame,
                        PLAYER_ONE_NAME + "'s ship placement files was loaded successfully.\n\n" +
                                PLAYER_TWO_NAME + "'s ship placement files was NOT loaded successfully.\n" +
                                errorMessageFileTwo + "\n" + "The ships have been placed randomly instead.\n\n" +
                                "Enjoy the game!",
                        "Loading ship placement text files.",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainGuiFrame,
                        PLAYER_ONE_NAME + "'s ship placement files was NOT loaded successfully.\n" +
                                errorMessageFileOne + "\n" + "The ships have been placed randomly instead.\n\n" +
                                PLAYER_TWO_NAME + "'s ship placement files was NOT loaded successfully.\n" +
                                errorMessageFileTwo + "\n" + "The ships have been placed randomly instead.\n\n" +
                                "Enjoy the game!",
                        "Loading ship placement text files.",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
