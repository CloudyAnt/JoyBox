package cn.itscloudy.joybox.joys.game.snake;

import cn.itscloudy.joybox.joys.VBoxJoy;
import cn.itscloudy.joybox.util.JoyButton;
import cn.itscloudy.joybox.util.JoyDimension;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

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
        setupControls();
        setupGameBoard();
        setupKeyboardControls();
    }
    
    private void initializeGame() {
        gameBoard = new GameBoard(GAME_WIDTH, GAME_HEIGHT);
        
        // 游戏循环
        gameLoop = new Timeline(new KeyFrame(Duration.millis(200), e -> gameUpdate()));
        gameLoop.setCycleCount(Animation.INDEFINITE);
    }
    
    private void setupControls() {
        HBox controls = getControls();
        
        scoreLabel = new Label("Score: 0");
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setFont(Font.font(16));
        
        startButton = new JoyButton("Start");
        startButton.setOnAction(e -> startNewGame());
        
        pauseButton = new JoyButton("Pause");
        pauseButton.setOnAction(e -> togglePause());
        pauseButton.setDisable(true);
        
        controls.getChildren().addAll(scoreLabel, startButton, pauseButton);
    }
    
    private void setupGameBoard() {
        gameOverLabel = new Label("");
        gameOverLabel.setTextFill(Color.RED);
        gameOverLabel.setFont(Font.font(24));
        
        addAll(getControls(), gameBoard, gameOverLabel);
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
            pauseButton.setText("暂停");
            isPaused = false;
        } else {
            gameLoop.pause();
            pauseButton.setText("继续");
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
                    // 每吃5个食物加速一次
                    if (score % 50 == 0) {
                        double currentRate = gameLoop.getRate();
                        gameLoop.setRate(currentRate + 0.2);
                    }
                    break;
                case GAME_OVER:
                    gameOver();
                    break;
                case RUNNING:
                    // 继续游戏
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
        gameLoop.setRate(1.0); // 重置速度
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