package swegame.javafx.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DurationFormatUtils;
import swegame.results.GameResult;
import swegame.results.GameResultDao;
import swegame.state.SweGameState;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
public class GameController {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultDao gameResultDao;

    private String redPlayerName;
    private String bluePlayerName;
    private SweGameState gameState;
    private List<Image> cellImages;
    private int fromRow;
    private int fromCol;
    private boolean isFirst=true;
    private Instant startTime;
    private IntegerProperty steps = new SimpleIntegerProperty();
    private String winner;



    @FXML
    private Label messageLabel;

    @FXML
    private Label redLabel;

    @FXML
    private Label blueLabel;

    @FXML
    private GridPane gameGrid;

    @FXML
    private Label stepsLabel;

    @FXML
    private Label stopWatchLabel;

    private Timeline stopWatchTimeline;


    @FXML
    private Button giveUpButton;

    private BooleanProperty gameOver = new SimpleBooleanProperty();

    public void setPlayerNames(String redPlayerName, String bluePlayerName) {
        this.redPlayerName = redPlayerName;
        this.bluePlayerName = bluePlayerName;
    }

    @FXML
    public void initialize() {
        cellImages = List.of(
                new Image(getClass().getResource("/images/cell0.png").toExternalForm()),
                new Image(getClass().getResource("/images/cell1.png").toExternalForm()),
                new Image(getClass().getResource("/images/cell2.png").toExternalForm())
        );
        stepsLabel.textProperty().bind(steps.asString());
        redLabel.setText("Red player starts");
        blueLabel.setText("");
        gameOver.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                log.info("Game is over");
                log.debug("Saving result to database...");
                gameResultDao.persist(createGameResult());
                stopWatchTimeline.stop();
                messageLabel.setText(winner + " is the WINNER!");
                redLabel.setText("");
                blueLabel.setText("");
                }
        });
        resetGame();
    }


    private void resetGame() {
        gameState = new SweGameState(SweGameState.INITIAL);
        steps.set(0);
        startTime = Instant.now();
        gameOver.setValue(false);
        createStopWatch();
        displayGameState();
        messageLabel.setText("");
    }

    private void displayGameState() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                ImageView view = (ImageView) gameGrid.getChildren().get(i * 4 + j);
                if (view.getImage() != null) {
                    log.trace("Image({}, {}) = {}", i, j, view.getImage().getUrl());
                }
                view.setImage(cellImages.get(gameState.getBoard()[i][j].getValue()));
            }
        }
    }

    public  void handleClickOnDisk(MouseEvent mouseEvent){
        if (isFirst){
            fromRow = GridPane.getRowIndex((Node) mouseEvent.getSource());
            fromCol = GridPane.getColumnIndex((Node) mouseEvent.getSource());
            log.debug("Cell ({}, {}) is pressed", fromRow, fromCol);
            if(gameState.getBoard()[fromRow][fromCol].getValue() != 0)
            isFirst=false;
        }
        else{
            int toRow = GridPane.getRowIndex((Node) mouseEvent.getSource());
            int toCol = GridPane.getColumnIndex((Node) mouseEvent.getSource());
            log.debug("Cell at ({}, {}) is pressed", toRow, toCol);

            if (! gameState.isGoal() && gameState.canMoveTo(fromRow,fromCol,toRow,toCol) && ! gameOver.getValue()) {
                gameState.move(fromRow,fromCol,toRow,toCol);
                steps.set(steps.get() + 1);
                if (gameState.getPlayer()==1){
                    redLabel.setText(redPlayerName + " is moving");
                    blueLabel.setText("");
                }
                else{
                    blueLabel.setText((bluePlayerName + " is moving"));
                    redLabel.setText("");
                }

                if (gameState.isGoal()) {
                    winner = gameState.getPlayer() == 2 ? redPlayerName : bluePlayerName;
                    log.info("Player {} has won the game", winner);
                    giveUpButton.setText("Finish");
                    gameOver.setValue(true);
                }
            }
            displayGameState();
            isFirst=true;
        }


    }



    public void handleGiveUpButton(ActionEvent actionEvent) throws IOException {
        String buttonText = ((Button) actionEvent.getSource()).getText();
        log.debug("{} is pressed", buttonText);
        if (buttonText.equals("Give Up")) {
            log.info("The game has been given up");
            winner = gameState.getPlayer() == 2 ? redPlayerName : bluePlayerName;
            giveUpButton.setText("Finish");
            gameOver.setValue(true);
        }
        else {

            log.info("Loading previous results scene...");
            fxmlLoader.setLocation(getClass().getResource("/fxml/highscores.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<HighScoreController>getController().setPlayerNames(redPlayerName,bluePlayerName);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    private GameResult createGameResult() {
        GameResult result = GameResult.builder()
                .redplayer(redPlayerName)
                .blueplayer(bluePlayerName)
                .winner(winner)
                .steps(steps.get())
                .duration(Duration.between(startTime, Instant.now()))
                .build();
        return result;
    }

    private void createStopWatch() {
        stopWatchTimeline = new Timeline(new KeyFrame(javafx.util.Duration.ZERO, e -> {
            long millisElapsed = startTime.until(Instant.now(), ChronoUnit.MILLIS);
            stopWatchLabel.setText(DurationFormatUtils.formatDuration(millisElapsed, "HH:mm:ss"));
        }), new KeyFrame(javafx.util.Duration.seconds(1)));
        stopWatchTimeline.setCycleCount(Animation.INDEFINITE);
        stopWatchTimeline.play();
    }
}

