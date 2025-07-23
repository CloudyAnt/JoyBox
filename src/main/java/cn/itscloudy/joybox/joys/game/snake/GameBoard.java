package cn.itscloudy.joybox.joys.game.snake;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard extends Canvas {
    private static final int CELL_SIZE = 20;
    private final int rows;
    private final int cols;
    private final GraphicsContext gc;
    
    private List<Position> snake;
    private Position food;
    private Direction currentDirection;
    private Direction nextDirection;
    private final Random random;
    
    public GameBoard(int width, int height) {
        super(width, height);
        this.rows = height / CELL_SIZE;
        this.cols = width / CELL_SIZE;
        this.gc = getGraphicsContext2D();
        this.random = new Random();
        
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        
        resetGame();
    }
    
    public void resetGame() {
        snake = new ArrayList<>();
        int centerX = cols / 2;
        int centerY = rows / 2;
        
        // Init len 3 snake
        for (int i = 0; i < 3; i++) {
            snake.add(new Position(centerX - i, centerY));
        }
        
        currentDirection = Direction.RIGHT;
        nextDirection = Direction.RIGHT;
        
        generateFood();
        draw();
    }
    
    public void changeDirection(Direction newDirection) {
        if (currentDirection.isOpposite(newDirection)) {
            return;
        }
        nextDirection = newDirection;
    }
    
    public GameState update() {
        currentDirection = nextDirection;
        
        Position head = snake.get(0);
        Position newHead = new Position(head.x, head.y);
        
        switch (currentDirection) {
            case UP:
                newHead.y--;
                break;
            case DOWN:
                newHead.y++;
                break;
            case LEFT:
                newHead.x--;
                break;
            case RIGHT:
                newHead.x++;
                break;
        }
        
        // Check if out of bound
        if (newHead.x < 0 || newHead.x >= cols || newHead.y < 0 || newHead.y >= rows) {
            return GameState.GAME_OVER;
        }
        
        // Check if hit snake
        for (Position segment : snake) {
            if (newHead.equals(segment)) {
                return GameState.GAME_OVER;
            }
        }
        
        // Moving
        snake.add(0, newHead);
        
        // Catch food
        if (newHead.equals(food)) {
            generateFood();
            draw();
            return GameState.FOOD_EATEN;
        } else {
            // Remove tail
            snake.remove(snake.size() - 1);
            draw();
            return GameState.RUNNING;
        }
    }
    
    private void generateFood() {
        do {
            int x = random.nextInt(cols);
            int y = random.nextInt(rows);
            food = new Position(x, y);
        } while (isSnakePosition(food));
    }
    
    private boolean isSnakePosition(Position pos) {
        for (Position segment : snake) {
            if (segment.equals(pos)) {
                return true;
            }
        }
        return false;
    }
    
    private void draw() {
        // Draw background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setStroke(Color.GRAY.darker().darker());
        gc.setLineWidth(0.5);
        for (int i = 0; i <= cols; i++) {
            gc.strokeLine(i * CELL_SIZE, 0, i * CELL_SIZE, getHeight());
        }
        for (int i = 0; i <= rows; i++) {
            gc.strokeLine(0, i * CELL_SIZE, getWidth(), i * CELL_SIZE);
        }
        
        // Draw snake
        for (int i = 0; i < snake.size(); i++) {
            Position segment = snake.get(i);
            if (i == 0) {
                gc.setFill(Color.LIGHTGREEN);
            } else {
                gc.setFill(Color.GREEN);
            }
            gc.fillRect(segment.x * CELL_SIZE + 1, segment.y * CELL_SIZE + 1, 
                       CELL_SIZE - 2, CELL_SIZE - 2);
        }
        
        // Draw food
        gc.setFill(Color.RED);
        gc.fillOval(food.x * CELL_SIZE + 2, food.y * CELL_SIZE + 2, 
                   CELL_SIZE - 4, CELL_SIZE - 4);
    }
}