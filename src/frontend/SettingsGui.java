package frontend;

import main.BattleshipsMain;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.SpinnerModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

/**
 * The SettingsGUI class is responsible for controlling the splash screen of the game.
 * Here settings are defined before the game begins. It is the first thing the user is presented with when running the
 * game.
 * The settings splash screen allows users to input:
 *      - Player 1's name
 *      - Player 2's name
 *      - Which player should go first
 *      - Whether or not the first player should get a score disadvantage for going first
 *      - The board dimensions
 *      - The names of the text files for manually defining the placement of the ships on the board
 * Buttons are provided to display the start the game, display the leaderboard, view the game rules, and exit.
 */

public class SettingsGui {
    // Splash screen elements on the window
    private final JFrame splashScreenFrame;
    private final JCheckBox scoreCompensationButton = new JCheckBox("Score compensation for player going second?");
    private final JRadioButton firstPlayerRButton = new JRadioButton("Player 1");
    private final JRadioButton secondPlayerRButton = new JRadioButton("Player 2");
    private final JTextField playerOneNameBox = new JTextField();
    private final JTextField playerTwoNameBox = new JTextField();
    // Spinner values limited to ensure minimum space to place the largest ship
    private final SpinnerModel spinRowModel = new SpinnerNumberModel(8, 5, 15, 1);
    private final SpinnerModel spinColumnModel = new SpinnerNumberModel(8, 5, 15, 1);

    // Variables related to loading custom user defined ship placements
    private boolean isBoardSizeFromFile = false; // Set as true if user loads user defined ship placement files
    private String shipLayoutFileLayout1;        // Received from text fields for name of text files to load
    private String shipLayoutFileLayout2;        // Received from text fields for name of text files to load

    // SettingsGui class constructor, called when program is run.
    public SettingsGui(){
        /*
        splashScreenFrame = Main window for the splash screen GUI on game settings.
        Contains 3 main elements(panels):
            (1) informationPanel - Game title and gif
            (2) optionsPanel     - Player options (Player names, who goes first, etc.), and the board sizes
            (3) buttonsPanel     - All buttons (start button, rules button, leaderboard button, exit button)
        */
        splashScreenFrame = new JFrame("Battleship Game: selection screen");

        // Set splash screen labels and their fonts, along with the fonts of other splash screen elements.
        JLabel battleShipTitleLabel = new JLabel("Welcome to Battleships");
        battleShipTitleLabel.setFont(new Font("", Font.BOLD, 30));
        JLabel splashInformationLabel = new JLabel("Please select your options and get started");
        splashInformationLabel.setFont(new Font("", Font.PLAIN, 18));
        JLabel boardInformationLabel = new JLabel("Choose the board size:");
        boardInformationLabel.setFont(new Font("", Font.BOLD, 16));
        JLabel boardRowNumberLabel = new JLabel("  Rows", JLabel.LEFT);
        boardRowNumberLabel.setFont(new Font("", Font.PLAIN, 16));
        JLabel boardColumnNumberLabel = new JLabel("  Columns", JLabel.LEFT);
        boardColumnNumberLabel.setFont(new Font("", Font.PLAIN, 16));
        JLabel playerOneNameLabel = new JLabel("Player 1 name:");
        playerOneNameLabel.setFont(new Font("", Font.BOLD, 16));
        playerOneNameBox.setFont(new Font("", Font.PLAIN, 16));
        JLabel playerTwoNameLabel = new JLabel("Player 2 name:");
        playerTwoNameLabel.setFont(new Font("", Font.BOLD, 16));
        playerTwoNameBox.setFont(new Font("", Font.PLAIN, 16));
        JLabel firstPlayerLabel = new JLabel("Player to go first:");
        firstPlayerLabel.setFont(new Font("", Font.PLAIN, 16));
        firstPlayerRButton.setFont(new Font("", Font.PLAIN, 14));
        secondPlayerRButton.setFont(new Font("", Font.PLAIN, 14));
        scoreCompensationButton.setFont(new Font("", Font.PLAIN, 14));
        JSpinner numberRowsSpin = new JSpinner(spinRowModel);
        numberRowsSpin.setFont(new Font("", Font.PLAIN, 14));
        JSpinner numberColumnsSpin = new JSpinner(spinColumnModel);
        numberColumnsSpin.setFont(new Font("", Font.PLAIN, 14));
        scoreCompensationButton.setFont(new Font("", Font.PLAIN, 14));
        JButton startGameButton = new JButton("Start Game");
        startGameButton.setFont(new Font("", Font.BOLD, 16));

        // (1) Set splash screen information panel items including Battleships heading and gif.
        JPanel informationPanel = new JPanel();
        informationPanel.setBorder(BorderFactory.createEmptyBorder(10,30,10,30));
        informationPanel.setLayout(new BorderLayout());
        informationPanel.add(battleShipTitleLabel,BorderLayout.NORTH);
        battleShipTitleLabel.setHorizontalAlignment(0);
        informationPanel.add(splashInformationLabel,BorderLayout.CENTER);
        splashInformationLabel.setHorizontalAlignment(0);
        ImageIcon battleshipImage = new ImageIcon(this.getClass().getResource("splash.gif"));
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(battleshipImage);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        informationPanel.add(imageLabel,BorderLayout.SOUTH);

        // (2) Splash screen game options panel for setting player names etc. and board sizes
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10,30,10,30));
        optionsPanel.setLayout(new GridLayout(1,2));

        // Player settings panel contains name fields, the player going first, and the score compensation checkbox
        JPanel playerSettingsPanel = new JPanel();
        playerSettingsPanel.setBorder(BorderFactory.createEtchedBorder());
        playerSettingsPanel.setLayout(new GridLayout(8,1));
        playerSettingsPanel.add(playerOneNameLabel);
        playerSettingsPanel.add(playerOneNameBox);
        playerOneNameBox.setText("Lecturer");           // default text
        playerSettingsPanel.add(playerTwoNameLabel);
        playerSettingsPanel.add(playerTwoNameBox);
        playerTwoNameBox.setText("Student");            // default text
        playerSettingsPanel.add(firstPlayerLabel);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(firstPlayerRButton);
        firstPlayerRButton.setSelected(true);
        buttonGroup.add(secondPlayerRButton);
        playerSettingsPanel.add(firstPlayerRButton);
        playerSettingsPanel.add(secondPlayerRButton);
        playerSettingsPanel.add(scoreCompensationButton);
        optionsPanel.add(playerSettingsPanel);  // add player settings to the game options panel

        // Splash screen board options panel (for setting board size)
        JPanel boardSizePanel = new JPanel();
        boardSizePanel.setBorder(BorderFactory.createEtchedBorder());
        boardSizePanel.setLayout(new GridLayout(5,1));
        boardSizePanel.add(boardInformationLabel);
        boardSizePanel.add(boardRowNumberLabel);
        boardSizePanel.add(numberRowsSpin);
        boardSizePanel.add(boardColumnNumberLabel);
        boardSizePanel.add(numberColumnsSpin);
        optionsPanel.add(boardSizePanel);

        // (3) Splash screen buttons panel (game start button, game rules button, exit button etc.)
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BorderLayout());
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0,30,30,30));
        // Interactive elements on the splash screen
        JButton shipPlacementButton = new JButton("Choose Ship Placement");
        shipPlacementButton.setPreferredSize(new Dimension(615,40));
        JPanel placeShipsPanel = new JPanel();
        placeShipsPanel.add(shipPlacementButton);
        buttonsPanel.add(placeShipsPanel, BorderLayout.NORTH);
        shipPlacementButton.addActionListener(new ShipPlacementActionListener());

        JPanel startPanel = new JPanel();
        startPanel.add(startGameButton);
        startGameButton.setPreferredSize(new Dimension(615,100));
        buttonsPanel.add(startPanel,BorderLayout.CENTER);
        startGameButton.addActionListener(new StartButtonActionListener());

        JPanel extrasPanel = new JPanel();
        JButton rulesButton = new JButton("Rules");
        extrasPanel.add(rulesButton, BorderLayout.LINE_START);
        rulesButton.setPreferredSize(new Dimension(200,50));
        JButton scoreboardButton = new JButton("High Scores");
        extrasPanel.add(scoreboardButton, BorderLayout.CENTER);
        scoreboardButton.setPreferredSize(new Dimension(200,50));
        JButton exitButton = new JButton("Exit");
        extrasPanel.add(exitButton, BorderLayout.LINE_END);
        exitButton.setPreferredSize(new Dimension(200,50));
        rulesButton.addActionListener(new GameRulesActionListener());
        scoreboardButton.addActionListener(new ScoreboardActionListener());
        exitButton.addActionListener(new ExitActionListener());
        buttonsPanel.add(extrasPanel, BorderLayout.SOUTH);

        // Adding main elements (1), (2), (3), the the splash screen.
        splashScreenFrame.setLayout(new BorderLayout());
        splashScreenFrame.setLocation(1000,100);
        splashScreenFrame.add(informationPanel, BorderLayout.NORTH);
        splashScreenFrame.add(optionsPanel, BorderLayout.CENTER);
        splashScreenFrame.add(buttonsPanel, BorderLayout.SOUTH);
        splashScreenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        splashScreenFrame.pack();
        splashScreenFrame.setVisible(true);
    }

    private class GameRulesActionListener implements ActionListener {
        /*
         * Action listener triggered when the game rules button is pressed.
         * Displays the rules of the Battleships game in a pop-up window.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // Display game rules in message dialogue
            JOptionPane.showMessageDialog(splashScreenFrame,
                    "Basics of the game Battleships:\n" +
                            "- At the start of the game each player places 5 ships on his/her board.\n" +
                            "- Each player then takes a turn at attacking the other players fleet.\n" +
                            "- At the beginning of the game the locations of all the ships are hidden by the 'fog of war'.\n" +
                            "- When attacking the player clicks on the tile where they think an opponents ship may be located.\n" +
                            "- After a tile is selected, the attack may either be a hit or a miss.\n" +
                            "- If you hit a ship you will be assigned points in accordance with the length of the ship hit.\n" +
                            "- When an attack misses a ship you will be assigned no points but the 'fog of war' will be cleared.\n" +
                            "- If a ship is sunk additional points are awarded according to the length of the ship.\n" +
                            "- The game ends when a players fleet of ships is completely destroyed.\n" +
                            "- At this time the player with the highest score wins the game.\n" +
                            "\nNote: Various options can be explored on the games splash screen including:\n" +
                            "Different board sizes, scoring penalties on the player who goes first, and the local leaderboards.",
                    "Rules of Battleships",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class ScoreboardActionListener implements ActionListener {
        /*
         * Action listener triggered when the scoreboard/leaderboard button is pressed.
         * Displays players, ordered by their number of wins in a pop-up window.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // gameMain instantiation for displaying the leaderboard

            // Get leaderboard in String format for displaying
            String leaderboard = BattleshipsMain.getLeaderboard();
            JOptionPane.showMessageDialog(splashScreenFrame,
                    "Local Battleships Scoreboard:\n\n" +
                            leaderboard,
                    "Local Leaderboard",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class ExitActionListener implements ActionListener {
        /*
         * Action listener triggered when the exit button is pressed. Closes the program.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            splashScreenFrame.dispose();
            System.exit(0);
        }
    }

    private class StartButtonActionListener implements ActionListener {
        /*
         * Action listener triggered when the start game button button is pressed.
         * Instantiates the main game gui where user play.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get all the game settings from splash screen:
            String playerOneName = playerOneNameBox.getText();
            String playerTwoName = playerTwoNameBox.getText();
            boolean playerOneDisadvantage = false;
            boolean playerTwoDisadvantage = false;
            boolean isPlayerOneFirst = true;
            if (firstPlayerRButton.isSelected()){           // Receives value from radiobutton
                if (scoreCompensationButton.isSelected()){          // Receives value from checkbox
                    playerOneDisadvantage = true;
                    playerTwoDisadvantage = false;
                }
            }else if (secondPlayerRButton.isSelected()) {   // Receives value from radiobutton
                isPlayerOneFirst = false;
                if (scoreCompensationButton.isSelected()){          // Receives value from checkbox
                    playerTwoDisadvantage = true;
                }
            }
            int[] boardSizePlayerTwo;
            int[] boardSizePlayerOne;
            // If custom user defined ship placement files were not loaded, board sizes are received from spinners
            if (!isBoardSizeFromFile){
                int numRows = (Integer) spinRowModel.getValue();
                int numColumns = (Integer) spinColumnModel.getValue();
                boardSizePlayerOne = new int[] {numRows,numColumns};
                boardSizePlayerTwo = new int[] {numRows,numColumns};
            }else{
                boardSizePlayerOne = null;
                boardSizePlayerTwo = null;
            }
            splashScreenFrame.dispose();    // close splash screen when game is started
            // Load the main game gui when the game start button is pressed and load settings defined on the splash screen
            new GameBoardGui(
                    isBoardSizeFromFile,
                    playerOneName,
                    playerTwoName,
                    isPlayerOneFirst,
                    playerOneDisadvantage,
                    playerTwoDisadvantage,
                    boardSizePlayerOne,
                    boardSizePlayerTwo,
                    shipLayoutFileLayout1,
                    shipLayoutFileLayout2);
        }
    }

    private class ShipPlacementActionListener implements ActionListener {
        /*
         * Action listener triggered when the custom ship placement button is pressed.
         * Opens a new window where the user can enter the text file names containing the custom ship placements.
         */

        // Define elements on the ship placements window
        private JFrame boatLayoutFrame;
        private final JPanel player1Panel = new JPanel();
        private final JPanel player2Panel = new JPanel();
        private final JLabel player1Label = new JLabel("Player 1 boat layout file name:");
        private final JLabel player2Label = new JLabel("Player 2 boat layout file name:");
        private final JTextField player1PlacementField = new JTextField();
        private final JTextField player2PlacementField = new JTextField();
        private final JButton confirmPlacementButton = new JButton("Load ship placement file");
        private final JButton closeButton = new JButton("Close");

        @Override
        public void actionPerformed(ActionEvent e) {
            splashScreenFrame.setEnabled(false);    // Temporarily disable the splash screen when the button is pressed.
            boatLayoutFrame = new JFrame("Set file names for boat placements:");
            boatLayoutFrame.setLayout(new GridLayout(1,2));
            boatLayoutFrame.setLayout(new GridLayout(1,2));

            // Panel where player 1 can enter their text file name
            player1Panel.setLayout(new GridLayout(3,1));
            player1Panel.setBorder(BorderFactory.createEmptyBorder(15,30,15,15));
            player1Panel.add(player1Label);
            player1Panel.add(player1PlacementField);
            player1PlacementField.setPreferredSize(new Dimension(200,50));
            player1PlacementField.setText("gameSettingsPlayer1.txt"); // default text file name
            player1PlacementField.setFont(new Font("",Font.PLAIN,15));
            player1Panel.add(confirmPlacementButton);
            confirmPlacementButton.addActionListener( new SaveActionListener());
            confirmPlacementButton.setPreferredSize(new Dimension(150,20));

            // Panel where player 1 can enter their text file name
            player2Panel.setLayout(new GridLayout(3,1));
            player2Panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,30));
            player2Panel.add(player2Label);
            player2Panel.add(player2PlacementField);
            player2PlacementField.setPreferredSize(new Dimension(200,50));
            player2PlacementField.setText("gameSettingsPlayer2.txt");   // default text file name
            player2PlacementField.setFont(new Font("",Font.PLAIN,15));
            player2Panel.add(closeButton);
            // Basic action listener for closing the window
            closeButton.addActionListener(event -> {
                boatLayoutFrame.dispose();
                splashScreenFrame.setEnabled(true);             // re-enable the splash screen
                splashScreenFrame.setVisible(true);             // Bring splash screen to the foreground again
            }
            ); // end of addActionListener method for the exit button
            closeButton.setPreferredSize(new Dimension(150,20));
            boatLayoutFrame.add(player1Panel);
            boatLayoutFrame.add(player2Panel);
            boatLayoutFrame.setLocation(1000,100);
            boatLayoutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            boatLayoutFrame.pack();
            boatLayoutFrame.setVisible(true);
        }

        private class SaveActionListener implements ActionListener {
            /*
             * Action listener triggered when the custom ship placement save button is pressed.
             * Saves the text file names and sets isBoardSizeFromFile to true.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                shipLayoutFileLayout1 = "src/datafiles/"+player1PlacementField.getText();
                shipLayoutFileLayout2 = "src/datafiles/"+player2PlacementField.getText();
                isBoardSizeFromFile = true;
            }
        }
    }
}
