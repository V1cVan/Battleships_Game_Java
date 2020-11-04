
public class PlayerManager {

    private final String NAME;
    private int score;

    // Constructor for Player class
    public PlayerManager (String nme){
        NAME = nme;
        score = 0;

    }

    public String getName() {
        return NAME;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = this.score + score;
    }

}
