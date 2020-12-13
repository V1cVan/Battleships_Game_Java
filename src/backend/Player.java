package backend;

/**
 * The Player class contains the information of each player and their score during the game.
 * Player class is instantiated twice in the game.
 * A board with 6 rows and 6 columns with have 36 tile objects instantiated.
 * Players have the following properties:
 *      Name                - The name of the player
 *      Score               - The score of the player
 *      scoreDisadvantage   - Whether the player receives less points because they went first
 */

public class Player {
    // Variables of a player object
    private final String NAME;
    private double score;
    private final boolean SCORE_DISADVANTAGE;  // True if player should receive less points per ship hit

    // Player class constructor, set when game is started
    public Player(String name, boolean isDisadvantaged){
        this.NAME = name;
        this.score = 0;     // Initial player score is 0 when game begins
        this.SCORE_DISADVANTAGE = isDisadvantaged;
    }

    public String getName() {
        // Returns the players name.
        return this.NAME;
    }

    public double getScore() {
        // Returns the players current score.
        return this.score;
    }

    public void increaseScore(int points) {
        /*
         * Increases the players score when they hit a ship.
         *      Score increase is reduced if they have a score disadvantage.
         * @param int points corresponding to the type of ship hit
         * @return None
         */
        if (this.SCORE_DISADVANTAGE == true){
            // Score is reduced by 0.18 per hit if a player has as score disadvantage. 0.18 has been tested to be fair.
            this.score = this.score + points - 0.18;
        }else{  // if the player has no score disadvantage
            this.score = this.score + points;
        }
    }

}
