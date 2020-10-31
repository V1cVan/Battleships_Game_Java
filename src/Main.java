

public class Main {

    // TODO Implement random ship placement
    // TODO Implement different board sizes
    // TODO Implement scoring system
    // TODO Implement ship placement via input file
    // TODO Implement GUI

    public static void main(String[] args) {
        int[] boardSize = {8,8};  // Board size in the form: [boardSize x boardSize]
        Player player1 = new Player("Victor", boardSize);
        Player player2 = new Player("Ines", boardSize);
        System.out.println("Player 1 Board:");
        System.out.print(player1.getBoard());
        System.out.println("Player 2 Board:");
        System.out.print(player2.getBoard());
    }


}
