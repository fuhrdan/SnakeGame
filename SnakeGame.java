import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame implements KeyListener, ActionListener {
    private final int TILE_SIZE = 25;
    private final int GRID_WIDTH = 20;
    private final int GRID_HEIGHT = 20;
    private final int DELAY = 100;

    private ArrayList<Point> snake;
    private Point food;
    private String direction = "RIGHT";
    private boolean gameRunning = true;
    private Timer timer;

    public SnakeGame() {
        // Set up the game window
        setTitle("Snake Game");
        setSize(TILE_SIZE * GRID_WIDTH, TILE_SIZE * GRID_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Initialize game
        initGame();
        addKeyListener(this);
        setVisible(true);
    }

    private void initGame() {
        snake = new ArrayList<>();
        snake.add(new Point(5, 5)); // Initial position of the snake's head
        snake.add(new Point(4, 5)); // Snake's body
        snake.add(new Point(3, 5)); // Snake's body

        spawnFood();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void spawnFood() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(GRID_WIDTH);
            y = random.nextInt(GRID_HEIGHT);
        } while (snake.contains(new Point(x, y)));
        food = new Point(x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {
            moveSnake();
            checkCollision();
            repaint();
        } else {
            timer.stop();
        }
    }

    private void moveSnake() {
        Point head = snake.get(0);
        Point newHead = switch (direction) {
            case "UP" -> new Point(head.x, head.y - 1);
            case "DOWN" -> new Point(head.x, head.y + 1);
            case "LEFT" -> new Point(head.x - 1, head.y);
            case "RIGHT" -> new Point(head.x + 1, head.y);
            default -> head;
        };

        snake.add(0, newHead);
        if (newHead.equals(food)) {
            spawnFood(); // Generate new food
        } else {
            snake.remove(snake.size() - 1); // Remove the tail
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);

        // Check wall collision
        if (head.x < 0 || head.x >= GRID_WIDTH || head.y < 0 || head.y >= GRID_HEIGHT) {
            gameRunning = false;
        }

        // Check self-collision
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameRunning = false;
                break;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (gameRunning) {
            // Draw the grid
            for (int i = 0; i < GRID_WIDTH; i++) {
                for (int j = 0; j < GRID_HEIGHT; j++) {
                    g.drawRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }

            // Draw the snake
            g.setColor(Color.GREEN);
            for (Point p : snake) {
                g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }

            // Draw the food
            g.setColor(Color.RED);
            g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        } else {
            // Game over
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", getWidth() / 3, getHeight() / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP && !direction.equals("DOWN")) {
            direction = "UP";
        } else if (key == KeyEvent.VK_DOWN && !direction.equals("UP")) {
            direction = "DOWN";
        } else if (key == KeyEvent.VK_LEFT && !direction.equals("RIGHT")) {
            direction = "LEFT";
        } else if (key == KeyEvent.VK_RIGHT && !direction.equals("LEFT")) {
            direction = "RIGHT";
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new SnakeGame();
    }
}
