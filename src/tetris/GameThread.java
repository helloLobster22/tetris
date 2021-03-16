package tetris;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameThread extends Thread {

    private GameArea gameArea;
    private GameForm gameForm;
    private int score;
    private int level = 1;
    private int scorePerLevel = 3;

    private int pause = 1000;
    private int speedPerLevel = 100;

    public GameThread(GameArea gameArea, GameForm gameForm) {
        this.gameArea = gameArea;
        this.gameForm = gameForm;
    }

    @Override
    public void run() {
        while (true) {

            gameArea.createBlock();

            while (gameArea.moveBlockDown()) {
                try {
                    Thread.sleep(this.pause);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            if (gameArea.isBlockOutOfBounds()) {
                System.out.println("Game over.");
                break;
            }

            gameArea.moveBlockToBackground();
            this.score += gameArea.clearLines();
            this.gameForm.updateScore(this.score);

            int lvl = score /scorePerLevel + 1;
            if(lvl > level) {
                level = lvl;
                gameForm.updateLevel(this.level);
                this.pause -= this.speedPerLevel;
            }
        }
    }
}
