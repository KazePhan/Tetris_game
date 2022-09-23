import javax.swing.*;
import java.awt.*;

public class Window {
    private JFrame window;
    private Board board;
    private JLabel p1;
    public static final int WIDTH=450, HEIGHT=629;


    public Window(){
        window = new JFrame("Tetris");
        window.setSize(WIDTH,HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        board = new Board();
        window.addKeyListener(board);
        window.add(board);


    }
    public static void main(String[] args) {
        new Window();
    }
}
