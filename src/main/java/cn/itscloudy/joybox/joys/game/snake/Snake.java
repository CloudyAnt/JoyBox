package cn.itscloudy.joybox.joys.game.snake;

import cn.itscloudy.joybox.joys.VBoxJoy;
import cn.itscloudy.joybox.util.JoyButton;
import cn.itscloudy.joybox.util.JoyDimension;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Snake extends VBoxJoy {
    public static final String NAME = "Snake";
    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 400;
    
    private GameBoard gameBoard;
    private Timeline gameLoop;
    private Label scoreLabel;
    private Label gameOverLabel;
    private Button startButton;
    private Button pauseButton;
    private boolean isPaused = false;
    private int score = 0;
    
    public Snake() {
        initializeGame();
        setupGameBoard();
        setupKeyboardControls();
    }

    @Override
    protected List<Node> getRightControlNodes() {
        List<Node> controlNodes = new ArrayList<>();
        scoreLabel = new Label("Score: 0");
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setFont(Font.font(16));
        controlNodes.add(scoreLabel);

        startButton = new JoyButton("Start");
        startButton.setOnAction(e -> startNewGame());
        controlNodes.add(startButton);

        pauseButton = new JoyButton("Pause");
        pauseButton.setOnAction(e -> togglePause());
        pauseButton.setDisable(true);
        controlNodes.add(pauseButton);
        return controlNodes;
    }

    private void initializeGame() {
        gameBoard = new GameBoard(GAME_WIDTH, GAME_HEIGHT);
        
        gameLoop = new Timeline(new KeyFrame(Duration.millis(170), e -> gameUpdate()));
        gameLoop.setCycleCount(Animation.INDEFINITE);
    }
    
    private void setupGameBoard() {
        gameOverLabel = new Label("");
        gameOverLabel.setTextFill(Color.RED);
        gameOverLabel.setFont(Font.font(24));
        
        addAll(gameBoard, gameOverLabel);
    }
    
    private void setupKeyboardControls() {
        setOnKeyPressed(event -> {
            if (gameLoop.getStatus() == Animation.Status.RUNNING && !isPaused) {
                KeyCode code = event.getCode();
                switch (code) {
                    case UP:
                    case W:
                        gameBoard.changeDirection(Direction.UP);
                        break;
                    case DOWN:
                    case S:
                        gameBoard.changeDirection(Direction.DOWN);
                        break;
                    case LEFT:
                    case A:
                        gameBoard.changeDirection(Direction.LEFT);
                        break;
                    case RIGHT:
                    case D:
                        gameBoard.changeDirection(Direction.RIGHT);
                        break;
                    case SPACE:
                        togglePause();
                        break;
                }
            }
        });
    }
    
    private void startNewGame() {
        gameBoard.resetGame();
        score = 0;
        updateScore();
        gameOverLabel.setText("");
        gameLoop.play();
        startButton.setDisable(true);
        pauseButton.setDisable(false);
        isPaused = false;
        requestFocus();
    }
    
    private void togglePause() {
        if (isPaused) {
            gameLoop.play();
            pauseButton.setText("Pause");
            isPaused = false;
        } else {
            gameLoop.pause();
            pauseButton.setText("Resume");
            isPaused = true;
        }
        requestFocus();
    }
    
    private void gameUpdate() {
        if (!isPaused) {
            GameState state = gameBoard.update();
            
            switch (state) {
                case FOOD_EATEN:
                    score += 10;
                    updateScore();
                    // Accelerate every 50 points
                    if (score % 50 == 0) {
                        double currentRate = gameLoop.getRate();
                        gameLoop.setRate(currentRate + 0.2);
                    }
                    break;
                case GAME_OVER:
                    gameOver();
                    break;
                case RUNNING:
                    // Continue
                    break;
            }
        }
    }
    
    private void updateScore() {
        scoreLabel.setText("Score: " + score);
    }
    
    private void gameOver() {
        gameLoop.stop();
        gameOverLabel.setText("GAME OVER! Score: " + score);
        startButton.setDisable(false);
        pauseButton.setDisable(true);
        gameLoop.setRate(1.0); // Reset rate
    }
    
    @Override
    protected JoyDimension getJoyDimension() {
        return new JoyDimension(GAME_WIDTH + 40, GAME_HEIGHT + 100);
    }
    
    @Override
    public void afterTaken() {
        requestFocus();
    }
}