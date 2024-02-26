import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int DOT_SIZE = 20;
    private final int ALL_DOTS = (WIDTH * HEIGHT) / (DOT_SIZE * DOT_SIZE);
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];

    private int dots;
    private int appleX;
    private int appleY;
    private boolean inGame = true;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    private Timer timer;

    private int score = 0; // Variable to store the score

    public SnakeGame() {
        initGame();
    }

    private void initGame() {
        setBackground(Color.black);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());

        initSnake();
        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void initSnake() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 100 - i * DOT_SIZE;
            y[i] = 100;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
        drawScore(g); // Call method to draw the score
    }

    private void doDrawing(Graphics g) {
        if (inGame) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, DOT_SIZE, DOT_SIZE);

            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(new Color(0, 128, 0));
                }
                g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            dots++;
            score++; // Increment the score when apple is eaten
            locateApple();
        }
    }

    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }
        if (rightDirection) {
            x[0] += DOT_SIZE;
        }
        if (upDirection) {
            y[0] -= DOT_SIZE;
        }
        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
        }

        if (y[0] >= HEIGHT || y[0] < 0 || x[0] >= WIDTH || x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {
        Random rand = new Random();
        appleX = rand.nextInt(RAND_POS) * DOT_SIZE;
        appleY = rand.nextInt(RAND_POS) * DOT_SIZE;
    }

    private void gameOver(Graphics g) {
        String gameOverMsg = "Game Over";
        String restartMsg = "Press R to Restart";

        Font gameOverFont = new Font("Helvetica", Font.BOLD, 30);
        g.setColor(Color.white);
        g.setFont(gameOverFont);

        FontMetrics gameOverMetrics = getFontMetrics(gameOverFont);
        int gameOverX = (WIDTH - gameOverMetrics.stringWidth(gameOverMsg)) / 2;
        int gameOverY = HEIGHT / 2;

        g.drawString(gameOverMsg, gameOverX, gameOverY);

        Font restartFont = new Font("Helvetica", Font.BOLD, 25);
        g.setFont(restartFont);

        FontMetrics restartMetrics = getFontMetrics(restartFont);
        int restartX = (WIDTH - restartMetrics.stringWidth(restartMsg)) / 2;
        int restartY = HEIGHT - restartMetrics.getHeight();

        g.drawString(restartMsg, restartX, restartY);
    }

    private void drawScore(Graphics g) {
        Font scoreFont = new Font("Helvetica", Font.BOLD, 20);
        g.setColor(Color.white);
        g.setFont(scoreFont);

        String scoreText = "Score: " + score;
        g.drawString(scoreText, 20, 20);
    }

    private class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            if (!inGame && key == KeyEvent.VK_R) {
                restartGame();
            }
        }
    }

    private void restartGame() {
        inGame = true;
        score = 0; // Reset score when game is restarted
        initSnake();
        locateApple();
        timer.restart();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new SnakeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
