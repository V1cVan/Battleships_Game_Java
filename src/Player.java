public class Player {
    private final String NAME;
    private int score;
    private Board board;
    // Constructor for Player class
    public Player (String nme, int[] brdSze){
        NAME = nme;
        score = 0;
        board = new Board(brdSze);
    }

    public String getBoard(){
        return board.showBoard();
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
