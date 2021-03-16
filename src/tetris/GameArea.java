package tetris;

import tetrisblocks.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ColorConvertOp;
import java.util.Random;

public class GameArea extends JPanel {

    final private int gridRows;
    final private int gridColumns;
    final private int gridCellSize;
    private Color[][] background;

    private Block block;
    private Block[] blocks;

    public GameArea(int columns) {
        this.setBackground(new Color(255,249,230));
        this.setBounds(10,70,360,480);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.gridColumns = columns;
        this.gridCellSize = this.getBounds().width / this.gridColumns;
        this.gridRows = this.getBounds().height / this.gridCellSize;

        this.background = new Color[this.gridRows][this.gridColumns];

        this.blocks = new Block[]{new IShape(), new JShape(), new LShape(), new OShape(), new SShape(), new TShape(), new ZShape()};
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.drawBackground(g);
        this.drawBlock(g);
    }

    private void drawBlock(Graphics g) {
        for(int i = 0; i < block.getHeight(); i++) {
            for(int j = 0; j < block.getWidth(); j++) {
                if(block.getShape()[i][j] == 1) {
                    int x = (block.getX() + j) * this.gridCellSize;
                    int y = (block.getY() + i) * this.gridCellSize;

                    this.drawGridSquare(g, block.getColor(), x, y);
                }
            }
        }
    }

    public void createBlock() {
        Random random = new Random();
        block = blocks[random.nextInt(blocks.length)];
        block.create(this.gridColumns);
    }

    public boolean moveBlockDown() {

        if(!checkBottom()) {
            return false;
        }

        block.moveDown();
        super.repaint();
        return true;
    }

    public void moveBlockRight() {
        if(block == null) return;
        if(!checkRight()) return;

        this.block.moveRight();
        repaint();
    }

    public void moveBlockLeft() {
        if(block == null) return;
        if(!checkLeft()) return;

        this.block.moveLeft();
        repaint();
    }

    public void dropBlock() {
        if(block == null) return;
        while (checkBottom()) {
            block.moveDown();
        }

        repaint();
    }

    public void rotateBlock() {
        if(block == null) return;
        this.block.rotate();

        if(block.getLeftEdge() < 0 ) block.setX(0);
        if(block.getRightEdge() >= gridColumns) block.setX(gridColumns-block.getWidth());
        if(block.getBottomEdge() >= gridRows) block.setY(gridRows-block.getHeight());

        repaint();
    }


    private boolean checkBottom() {
        if(block.getBottomEdge() == this.gridRows) return false;

        int[][]shape = block.getShape();
        int w  = block.getWidth();
        int h = block.getHeight();

        for(int col = 0; col < w; col++) {
            for(int row = h - 1; row >= 0;  row-- ) {
                if(shape[row][col] != 0) {
                    int x = col + block.getX();
                    int y = row + block.getY() + 1;
                    if(y < 0) break;
                    if(background[y][x] != null) return false;
                    break;
                }
            }
        }

        return true;
    }

    private boolean checkLeft() {
        if(block.getLeftEdge() == 0) return false;

        int[][]shape = block.getShape();
        int w  = block.getWidth();
        int h = block.getHeight();

        for(int row = 0; row < h; row++) {
            for(int col = 0; col < w;  col++ ) {
                if(shape[row][col] != 0) {
                    int x = col + block.getX() - 1;
                    int y = row + block.getY();
                    if(y < 0) break;
                    if(background[y][x] != null) return false;
                    break;
                }
            }
        }

        return true;
    }

    private  boolean checkRight() {
        if(block.getRightEdge() == this.gridColumns) return false;

        int[][]shape = block.getShape();
        int w  = block.getWidth();
        int h = block.getHeight();

        for(int row = 0; row < h; row++) {
            for(int col = w - 1; col >= 0;  col-- ) {
                if(shape[row][col] != 0) {
                    int x = col + block.getX() + 1;
                    int y = row + block.getY();
                    if(y < 0) break;
                    if(background[y][x] != null) return false;
                    break;
                }
            }
        }

        return true;
    }

    private void drawBackground(Graphics g) {
        Color color;

        for(int r = 0; r < this.gridRows; r++) {
            for(int c = 0; c < this.gridColumns; c++) {
                color = background[r][c];

                if(color != null) {
                    int x = c * gridCellSize;
                    int y = r * gridCellSize;

                    this.drawGridSquare(g, color, x, y);
                }
            }
        }
    }

    private void drawGridSquare(Graphics g, Color color, int x, int y) {
        g.setColor(color);
        g.fillRect(x, y, this.gridCellSize, this.gridCellSize);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, this.gridCellSize, this.gridCellSize);
    }

    public void moveBlockToBackground() {
        int[][] shape = block.getShape();
        int h = block.getHeight();
        int w = block.getWidth();

        int xPos = block.getX();
        int yPos = block.getY();

        Color color = block.getColor();

        for(int r = 0; r < h; r++) {
            for(int c = 0; c < w; w++) {
                if(shape[r][c] == 1) {
                    this.background[r + yPos][r + xPos] = color;
                }
            }
        }
    }

    public int clearLines() {
        boolean lineFilled;
        int linesCleared = 0;

        for(int r=gridRows-1; r>=0; r-- ) {
            lineFilled = true;
            for(int c=0; c<gridColumns;c++) {
                if(background[r][c]==null) {
                    lineFilled = false;
                    break;
                }

                if(lineFilled) {
                    linesCleared++;
                    this.clearLine(r);
                    this.shiftDown(r);
                    this.clearLine(0);

                    r++;

                    repaint();
                }

            }
        }

        return linesCleared;
    }

    public void clearLine(int r) {
        for(int i =0; i<gridColumns;i++) {
            background[r][i] = null;
        }
    }

    public void shiftDown(int r) {
        for(int row=0;  row >0; row--) {
            for(int col= 0; col <gridColumns; col++) {
                background[row][col] = background[row-1][col];
            }
        }
    }

    public boolean isBlockOutOfBounds() {
        if(block.getY() < 0) {
            block = null;
            return true;
        }

        return false;
    }
}
