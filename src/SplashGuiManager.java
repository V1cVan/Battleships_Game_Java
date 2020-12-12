import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton; // Buttons
import javax.swing.JLabel; // Labels
import javax.swing.JTextField; // Entering data for ship placement
import javax.swing.JSpinner; // Spinner for board size
import javax.swing.SpinnerNumberModel;
import javax.swing.JCheckBox; // Disadvantage for fist player
import javax.swing.JRadioButton; // Start player selection
import javax.swing.ButtonGroup; // For radio button selection
import javax.swing.SpinnerModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane; // Message dialogs
import javax.swing.JPanel;

public class SplashGuiManager {
    // Splash screen:
    private JFrame splashScreenFrame;

    private JPanel informationPanel = new JPanel();
    private JPanel optionsPanel = new JPanel();
    private JPanel customScoringPanel = new JPanel();
    private JPanel boardSizePanel = new JPanel();
    private JPanel buttonsPanel = new JPanel();
    private JPanel placeShipsPanel = new JPanel();
    private JPanel startPanel = new JPanel();
    private JPanel extrasPanel = new JPanel();

    // Labels on splash screen:
    private JLabel battleShipTitleLabel = new JLabel("Welcome to Battleship");
    private JLabel splashInformationLabel = new JLabel("Please select your options and get started");
    private JLabel playerOneNameLabel = new JLabel("Player 1 name:");
    private JLabel playerTwoNameLabel = new JLabel("Player 2 name:");
    private JLabel firstPlayerLabel = new JLabel("Player to go first:");
    private JLabel boardInformationLabel = new JLabel("Choose the board size:");
    private JLabel boardRowNumberLabel = new JLabel("  Rows",JLabel.LEFT);
    private JLabel boardColumnNumberLabel = new JLabel("  Columns",JLabel.LEFT);

    // Elements on splash screen:
    private JButton shipPlacementButton = new JButton("Choose Ship Placement");
    private JCheckBox scoreCompensationButton = new JCheckBox("Score compensation for player going second?");
    private JRadioButton firstPlayerRButton = new JRadioButton("Player 1");
    private JRadioButton secondPlayerRButton = new JRadioButton("Player 2");
    private JTextField playerOneNameBox = new JTextField();
    private JTextField playerTwoNameBox = new JTextField();
    private JButton startGameButton = new JButton("Start Game");
    private JButton rulesButton = new JButton("Rules");
    private JButton scoreboardButton = new JButton("High Scores");
    private JButton exitButton = new JButton("Exit");
    private SpinnerModel spinRowModel = new SpinnerNumberModel(8, 5, 15, 1);
    private JSpinner numberRowsSpin = new JSpinner(spinRowModel);
    private SpinnerModel spinColumnModel = new SpinnerNumberModel(8, 5, 15, 1);
    private JSpinner numberColumnsSpin = new JSpinner(spinColumnModel);

    // Game functionality variables:
    private BattleshipsGameMain gameMain = new BattleshipsGameMain();
    private boolean isBoardSizeFromFile = false;
    private String playerOneName;
    private String playerTwoName;
    private boolean isPlayerOneFirst;
    private boolean playerOneDisadvantage;
    private boolean playerTwoDisadvantage;
    private int[] boardSizePlayerOne;
    private int[] boardSizePlayerTwo;
    private String shipLayoutFileLayout1;
    private String shipLayoutFileLayout2;

    public SplashGuiManager(){
        splashScreenFrame = new JFrame("Battleship Game: selection screen");

        // Set splash screen fonts
        battleShipTitleLabel.setFont(new Font("", Font.PLAIN, 30));
        splashInformationLabel.setFont(new Font("", Font.PLAIN, 18));
        boardInformationLabel.setFont(new Font("", Font.PLAIN, 16));
        boardRowNumberLabel.setFont(new Font("", Font.PLAIN, 16));
        boardColumnNumberLabel.setFont(new Font("", Font.PLAIN, 16));
        playerOneNameLabel.setFont(new Font("", Font.PLAIN, 16));
        playerOneNameBox.setFont(new Font("", Font.PLAIN, 16));
        playerTwoNameLabel.setFont(new Font("", Font.PLAIN, 16));
        playerTwoNameBox.setFont(new Font("", Font.PLAIN, 16));
        firstPlayerLabel.setFont(new Font("", Font.PLAIN, 16));
        firstPlayerRButton.setFont(new Font("", Font.PLAIN, 14));
        secondPlayerRButton.setFont(new Font("", Font.PLAIN, 14));
        scoreCompensationButton.setFont(new Font("", Font.PLAIN, 14));
        numberRowsSpin.setFont(new Font("", Font.PLAIN, 14));
        numberColumnsSpin.setFont(new Font("", Font.PLAIN, 14));
        scoreCompensationButton.setFont(new Font("", Font.PLAIN, 14));

        // Splash screen information panel
        informationPanel.add(battleShipTitleLabel);
        battleShipTitleLabel.setHorizontalAlignment(0);
        informationPanel.add(splashInformationLabel);
        splashInformationLabel.setHorizontalAlignment(0);
        informationPanel.setBorder(BorderFactory.createEmptyBorder(10,30,10,30));
        informationPanel.setLayout(new GridLayout(2,1));

        // Splash screen game options panel
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10,30,10,30));
        optionsPanel.setLayout(new GridLayout(1,2));
        // Scoring setting panel:
        customScoringPanel.setBorder(BorderFactory.createEtchedBorder());
        customScoringPanel.setLayout(new GridLayout(8,1));
        customScoringPanel.add(playerOneNameLabel);
        customScoringPanel.add(playerOneNameBox);
        playerOneNameBox.setText("Lecturer");
        customScoringPanel.add(playerTwoNameLabel);
        customScoringPanel.add(playerTwoNameBox);
        playerTwoNameBox.setText("Student");
        customScoringPanel.add(firstPlayerLabel);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(firstPlayerRButton);
        firstPlayerRButton.setSelected(true);
        buttonGroup.add(secondPlayerRButton);
        customScoringPanel.add(firstPlayerRButton);
        customScoringPanel.add(secondPlayerRButton);
        customScoringPanel.add(scoreCompensationButton);
        optionsPanel.add(customScoringPanel);

        // Splash screen board options panel
        boardSizePanel.setBorder(BorderFactory.createEtchedBorder());
        boardSizePanel.setLayout(new GridLayout(5,1));
        boardSizePanel.add(boardInformationLabel);
        boardSizePanel.add(boardRowNumberLabel);
        boardSizePanel.add(numberRowsSpin);
        boardSizePanel.add(boardColumnNumberLabel);
        boardSizePanel.add(numberColumnsSpin);
        optionsPanel.add(boardSizePanel);

        // Splash screen buttons panel
        buttonsPanel.setLayout(new GridLayout(3,1));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0,0,30,0));
        shipPlacementButton.setPreferredSize(new Dimension(300,50));
        placeShipsPanel.add(shipPlacementButton);
        buttonsPanel.add(placeShipsPanel);
        shipPlacementButton.addActionListener(new ShipPlacementActionListener());

        startPanel.add(startGameButton);
        startGameButton.setPreferredSize(new Dimension(300,50));
        buttonsPanel.add(startPanel);
        startGameButton.addActionListener(new StartButtonActionListener());

        extrasPanel.setLayout(new GridLayout(1,3));
        extrasPanel.setBorder(BorderFactory.createEmptyBorder(15,60,30,60));
        extrasPanel.add(rulesButton);
        extrasPanel.add(scoreboardButton);
        extrasPanel.add(exitButton);
        rulesButton.setPreferredSize(new Dimension(50,20));
        rulesButton.addActionListener(new GameRulesActionListener());
        scoreboardButton.setPreferredSize(new Dimension(50,20));
        scoreboardButton.addActionListener(new ScoreboardActionListener());
        exitButton.setPreferredSize(new Dimension(50,20));
        exitButton.addActionListener(new ExitActionListener());
        buttonsPanel.add(extrasPanel);

        // Final splash screen layout
        splashScreenFrame.setLayout(new GridLayout(3,1));
        splashScreenFrame.setLocation(1000,100);
        splashScreenFrame.add(informationPanel);
        splashScreenFrame.add(optionsPanel);
        splashScreenFrame.add(buttonsPanel);
        splashScreenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        splashScreenFrame.pack();
        splashScreenFrame.setVisible(true);
    }

    private class GameRulesActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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
        @Override
        public void actionPerformed(ActionEvent e) {

            String leaderboard = gameMain.getLeaderboard();
            JOptionPane.showMessageDialog(splashScreenFrame,
                    "Local Battleships Scoreboard:\n\n" +
                            leaderboard,
                    "Local Leaderboard",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class ExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            splashScreenFrame.dispose();
            System.exit(0);
        }
    }

    private class StartButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get all the game settings from splash screen:
            playerOneName = playerOneNameBox.getText();
            playerTwoName = playerTwoNameBox.getText();
            playerOneDisadvantage = false;
            playerTwoDisadvantage = false;
            if (firstPlayerRButton.isSelected() == true){
                isPlayerOneFirst = true;
                if (scoreCompensationButton.isSelected()){
                    playerOneDisadvantage = true;
                    playerTwoDisadvantage = false;
                }
            }else if (secondPlayerRButton.isSelected() == true) {
                isPlayerOneFirst = false;
                if (scoreCompensationButton.isSelected()){
                    playerOneDisadvantage = false;
                    playerTwoDisadvantage = true;
                }
            }
            if (isBoardSizeFromFile == false){
                int numRows = (Integer) spinRowModel.getValue();
                int numColumns = (Integer) spinColumnModel.getValue();
                boardSizePlayerOne = new int[] {numRows,numColumns};
                boardSizePlayerTwo = new int[] {numRows,numColumns};
            }else{
                boardSizePlayerOne = null;
                boardSizePlayerTwo = null;
            }
            splashScreenFrame.dispose();
            MainGuiManager mainGuiManager = new MainGuiManager(
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
        private JFrame boatLayoutFrame;
        private JPanel player1Panel = new JPanel();
        private JPanel player2Panel = new JPanel();
        private JLabel player1Label = new JLabel("Player 1 boat layout file name :\n" +
                "(place in <gamedirectory>/src/");
        private JLabel player2Label = new JLabel("Player 2 boat layout file name :\n" +
                "(place in <gamedirectory>/src/");
        private JTextField player1PlacementField = new JTextField();
        private JTextField player2PlacementField = new JTextField();
        private JButton confirmPlacementButton = new JButton("Load ship placement file");
        private JButton closeButton = new JButton("Close");
        private JButton informationButton = new JButton("Info");


        @Override
        public void actionPerformed(ActionEvent e) {
            splashScreenFrame.setEnabled(false);
            boatLayoutFrame = new JFrame("Set file names for boat placements:");
            boatLayoutFrame.setLayout(new GridLayout(1,2));
            boatLayoutFrame.setLayout(new GridLayout(1,2));

            player1Panel.setLayout(new GridLayout(3,1));
            player1Panel.setBorder(BorderFactory.createEmptyBorder(15,30,15,15));
            player1Panel.add(player1Label);
            player1Panel.add(player1PlacementField);
            player1PlacementField.setPreferredSize(new Dimension(200,50));
            player1PlacementField.setText("src/gameSettingsPlayer1.txt");
            player1PlacementField.setFont(new Font("",Font.PLAIN,15));
            player1Panel.add(confirmPlacementButton);
            confirmPlacementButton.addActionListener( new SaveActionListener());
            confirmPlacementButton.setPreferredSize(new Dimension(150,20));

            player2Panel.setLayout(new GridLayout(3,1));
            player2Panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,30));
            player2Panel.add(player2Label);
            player2Panel.add(player2PlacementField);
            player2PlacementField.setPreferredSize(new Dimension(200,50));
            player2PlacementField.setText("src/gameSettingsPlayer2.txt");
            player2PlacementField.setFont(new Font("",Font.PLAIN,15));
            player2Panel.add(closeButton);
            closeButton.addActionListener(new ActionListener() {
                    public void actionPerformed (ActionEvent event){
                        boatLayoutFrame.dispose();
                        splashScreenFrame.setEnabled(true);
                        splashScreenFrame.setVisible(true);
                    }
                }
            ); // end of addActionListener method
            closeButton.setPreferredSize(new Dimension(150,20));
            informationButton.setPreferredSize(new Dimension(50,20));
            informationButton.addActionListener(new InfoActionListener());

            boatLayoutFrame.add(player1Panel);
            boatLayoutFrame.add(player2Panel);
            boatLayoutFrame.setLocation(1000,100);
            boatLayoutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            boatLayoutFrame.pack();
            boatLayoutFrame.setVisible(true);
        }

        private class SaveActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                shipLayoutFileLayout1 = player1PlacementField.getText();
                shipLayoutFileLayout2 = player2PlacementField.getText();
                isBoardSizeFromFile = true;
            }
        }

        private class InfoActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(boatLayoutFrame,
                        "Please note that the text file location has to be in the correct directory.\n" +
                                "Boat entries should be in the following form:\n" +
                                "8\n" +
                                "Carrier;3*2;3*3;3*4;3*5;3*6 \n" +
                                "Battleship;5*6;6*6;7*6;8*6 \n" +
                                "Submarine;5*2;6*2;7*2;\n" +
                                "Destroyer;1*7;1*8\n" +
                                "\nWhere:\n8 = square board size\n" +
                                "Elements of a line are separated by a semicolon ;\n" +
                                "And x and y coordinates are separated by an asterisk *",
                        "Text file location",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

    }

}
