package tetris;

import java.awt.Color;
import java.util.Random;

public class Block {
    private int[][] shape;
    private Color color;
    private int x, y;
    private int[][][] shapes;
    private int currentRotation;

    private Color[] availableColors = {Color.red, Color.green, Color.blue};

    public Block(int[][] shape) {
        this.shape = shape;

        this.initShapes();
    }

    private void initShapes() {
        this.shapes = new int[4][][];

        for(int i = 0; i < 4; i++) {
            int r = shape[0].length;
            int c = shape.length;

            shapes[i] = new int[r][c];

            for(int j = 0; j < r; j++) {
                for(int k = 0; k < c; k++) {
                    shapes[i][y][x] = shape[c-x-1][y];
                }
            }

            shape = shapes[i];
        }
    }

    public void create(int gridWidth) {
        Random random = new Random();

        this.currentRotation = random.nextInt(4);
        shape = shapes[currentRotation];

        this.x = random.nextInt(gridWidth - getWidth());
        this.y = -this.getHeight();

        color = availableColors[random.nextInt(availableColors.length)];
    }

    public int[][] getShape() {
        return this.shape;
    }

    public Color getColor() {
        return this.color;
    }

    public int getX() { return this.x; }

    public void setX(int x) { this.x = x; }

    public  int getY() { return this.y; }

    public void setY(int y) { this.y = y; }

    public int getWidth() {
        return this.shape[0].length;
    }

    public int getHeight() {
        return this.shape.length;
    }

    public void moveDown() {
        this.y++;
    }

    public void moveRight() {
        this.x++;
    }

    public void moveLeft() {
        this.x--;
    }

    public void rotate() {
        this.currentRotation++;
        if(this.currentRotation > 3) this.currentRotation = 0;
        this.shape = this.shapes[this.currentRotation];
    }

    public int getBottomEdge() { return this.y + this.getHeight(); }

    public int getLeftEdge() { return x; }

    public int getRightEdge() { return x + this.getWidth(); }
}
