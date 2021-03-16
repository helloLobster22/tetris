package tetris;

public class Tetris {
    private static GameForm gameForm;
    private static LeaderboardForm leaderboardForm;
    private static StartForm startForm;

    public static void start() {
        gameForm.setVisible(true);
        gameForm.startGame();
    }

    public static void main(String[] args) {
        gameForm = new GameForm();
        startForm = new StartForm();

        startForm.setVisible(true);
    }
}
