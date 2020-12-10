


public class Player {

    private String NAME;
    private int score;
    private boolean scoreDisadvantage;
    public Player(String name, boolean disadvantage){
        this.NAME = name;
        this.score = 0;
        scoreDisadvantage = disadvantage;
    }

    public String getName() {
        return this.NAME;
    }

    public int getScore() {
        return this.score;
    }

    public void increaseScore(int points) {
        if (this.scoreDisadvantage == true){
            this.score = this.score + points - 1;
        }else{
            this.score = this.score + points;
        }
    }

}
