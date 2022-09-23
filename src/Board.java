import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Board extends JPanel implements KeyListener {
    public static int STATE_GAME_PLAY=0;
    public static int STATE_GAME_PAUSE=1;

    public static int STATE_GAME_OVER=2;
    public int state = STATE_GAME_PLAY;


    private static int FPS = 60;
    private static int delay = FPS / 1000;
    public static final int blockSize = 30;
    public static final int boardWidth = 10, boardHeight = 20;
    private Timer timer;
    private Color[][] board = new Color[boardHeight][boardWidth];
    private Shape[] shapes = new Shape[7];
    private Color[] colors = {Color.decode("#ed1c24"), Color.decode("#ff7f27"), Color.decode("#fff200"),
            Color.decode("#22b14c"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc")};
    private Shape currentShape, nextShape;
    private Random random;
    private int score = 0;


    public Board() {
        random = new Random();
        //Create shape
        shapes[0] = new Shape(new int[][]{
                {1, 1, 1, 1} // I shape;
        }, this, colors[0]);

        shapes[1] = new Shape(new int[][]{
                {1, 1, 1},
                {0, 1, 0}, // T shape;
        }, this, colors[1]);

        shapes[2] = new Shape(new int[][]{
                {1, 1, 1},
                {1, 0, 0}, // L shape;
        }, this, colors[2]);

        shapes[3] = new Shape(new int[][]{
                {1, 1, 1},
                {0, 0, 1}, // J shape;
        }, this, colors[3]);

        shapes[4] = new Shape(new int[][]{
                {0, 1, 1},
                {1, 1, 0}, // S shape;
        }, this, colors[4]);

        shapes[5] = new Shape(new int[][]{
                {1, 1, 0},
                {0, 1, 1}, // Z shape;
        }, this, colors[5]);

        shapes[6] = new Shape(new int[][]{
                {1, 1},
                {1, 1}, // O shape;
        }, this, colors[6]);
    currentShape =shapes[0];
    //Create timer
    timer =new Timer(delay, new ActionListener() {
        int n = 0;

        @Override

        public void actionPerformed (ActionEvent e){
            update();
            repaint();
        }
    });
        timer.start();
}
    // update hanh dong cua vien gach
    private void update() {
        if (state == STATE_GAME_PLAY) {
            currentShape.update();
        }
    }
    public void setCurrentShape() {
        currentShape = shapes[random.nextInt(shapes.length)];
        currentShape.reset();
        gameOver();
    }
    //Game Over
    private void gameOver(){
        int[][] coords = currentShape.getCoords();
        for(int row =0; row < coords.length;row ++){
            for(int col =0; col < coords[0].length; col++){
                if(coords[row][col] !=0){
                    if (board[currentShape.getY() + row][currentShape.getX() + col] != null){
                        state=STATE_GAME_OVER;
                    }
                }
            }
        }
    }
    //Draw the shape
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        currentShape.render(g);
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] != null) {
                    g.setColor(board[row][col]);
                    g.fillRect(col * blockSize, row * blockSize, blockSize, blockSize);
                }
            }
        }
        //Draw the board
        g.setColor(Color.white);
        for (int row = 0; row < boardHeight; row++) {
            g.drawLine(0, row * blockSize, boardWidth * blockSize, row * blockSize);
        }
        for (int col = 0; col < boardWidth + 1; col++) {
            g.drawLine(col * blockSize, 0, col * blockSize, boardHeight * blockSize);
        }
        if (state == STATE_GAME_OVER) {
            g.setColor(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 44));
            g.drawString("GAME OVER", 20, 300);
        }
        if(state == STATE_GAME_PAUSE){
            g.setColor(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 44));
            g.drawString("GAME PAUSED", 20, 300);
        }
        g.setColor(Color.WHITE);

        g.setFont(new Font("Georgia", Font.BOLD, 20));

        g.drawString("SCORE", Window.WIDTH - 125, Window.HEIGHT / 2);
        g.drawString(score + "", Window.WIDTH - 125, Window.HEIGHT / 2 + 30);

    }


    public Color[][] getBoard() {
        return board;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentShape.SpeedUp();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            currentShape.toTheRight();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            currentShape.toTheLeft();
        } else if(e.getKeyCode() == KeyEvent.VK_UP){
            currentShape.rotation();
        }
        //Clean man choi
        if(state == STATE_GAME_OVER){
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                for(int row =0; row < board.length; row++){
                    for(int col =0; col < board[row].length;col++){
                        board[row][col] = null;
                    }
                }
                setCurrentShape();
                state = STATE_GAME_PLAY;
            }
        }
        //Tam dung
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            if(state == STATE_GAME_PLAY){
                state = STATE_GAME_PAUSE;
            } else if (state == STATE_GAME_PAUSE) {
                state = STATE_GAME_PLAY;
            }
        }
    }
        @Override
        public void keyReleased (KeyEvent e){
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                currentShape.SpeedNormal();
            }
        }
        public void addScore(){
        score+=150;
        }
}


