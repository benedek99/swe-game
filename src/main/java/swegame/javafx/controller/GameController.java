package swegame.javafx.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
    private int player=0;
    private boolean isFirst=true;


    @FXML
    private Label messageLabel;

    @FXML
    private GridPane gameGrid;


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
        gameOver.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                //log.info("Game is over");
                //log.debug("Saving result to database...");
                //gameResultDao.persist(createGameResult());
                }
        });
        resetGame();
    }


    private void resetGame() {
        gameState = new SweGameState(SweGameState.INITIAL);
        displayGameState();
    }

    private void displayGameState() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                ImageView view = (ImageView) gameGrid.getChildren().get(i * 4 + j);
                if (view.getImage() != null) {
                    //log.trace("Image({}, {}) = {}", i, j, view.getImage().getUrl());
                }
                view.setImage(cellImages.get(gameState.getBoard()[i][j].getValue()));
            }
        }
        System.out.println("move");
        System.out.println(gameState);
    }

    public  void handleClickOnDisk(MouseEvent mouseEvent){
        if (isFirst){
            fromRow = GridPane.getRowIndex((Node) mouseEvent.getSource());
            fromCol = GridPane.getColumnIndex((Node) mouseEvent.getSource());
            System.out.println(fromRow +" " + fromCol + " " + player);
            isFirst=false;
            if (player == 0){
                if (gameState.getBoard()[fromRow][fromCol].getValue() != 0){
                    player = gameState.getBoard()[fromRow][fromCol].getValue() == 1 ? 1 : 2;
                }

                System.out.println(player);
            }
        }
        else{
            int toRow = GridPane.getRowIndex((Node) mouseEvent.getSource());
            int toCol = GridPane.getColumnIndex((Node) mouseEvent.getSource());
            //log.debug("Cube ({}, {}) is pressed", row, col);

            if (! gameState.isGoal() && gameState.canMoveTo(fromRow,fromCol,toRow,toCol,player)) {
                gameState.move(fromRow,fromCol,toRow,toCol,player);
                System.out.println("move");

                if (gameState.isGoal()) {
                    gameOver.setValue(true);
                    //log.info("Player {} has solved the game in {} steps", playerName, steps.get());
                    if (player == 1){
                        messageLabel.setText(redPlayerName + " is the WINNER!");
                    }
                    else{
                        messageLabel.setText(bluePlayerName + " is the WINNER!");
                    }

                    //giveUpButton.setText("Finish");
                }
                if (player == 1){
                    player = 2;
                }
                else{
                    player = 1;
                }
            }
            displayGameState();
            System.out.println(toRow +" " + toCol + " " + player);
            isFirst=true;
        }


    }
/*
    public void handleReleaseOnCell(MouseEvent mouseEvent) {
        int toRow = GridPane.getRowIndex((Node) mouseEvent.getSource());
        int toCol = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        //log.debug("Cube ({}, {}) is pressed", row, col);
        if (player == 0){
            player = gameState.getBoard()[fromRow][fromCol].getValue();
            System.out.println(player);
        }
        else if (player == 1){
            player = 2;
        }
        else{
            player = 1;
        }

        if (! gameState.isGoal() && gameState.canMoveTo(fromRow,fromCol,toRow,toCol,player)) {
            gameState.move(fromRow,fromCol,toRow,toCol,player);
            System.out.println("move");
            if (gameState.isGoal()) {
                gameOver.setValue(true);
                //log.info("Player {} has solved the game in {} steps", playerName, steps.get());
                if (player == 1){
                    messageLabel.setText(redPlayerName + " is the WINNER!");
                }
                else{
                    messageLabel.setText(bluePlayerName + " is the WINNER!");
                }

                //giveUpButton.setText("Finish");
            }
        }
        displayGameState();
        System.out.println(toRow +" " + toCol + " " + player);
    }

 */

/*
    public void handleGiveUpButton(ActionEvent actionEvent) throws IOException {
        String buttonText = ((Button) actionEvent.getSource()).getText();
        log.debug("{} is pressed", buttonText);
        if (buttonText.equals("Give Up")) {
            log.info("The game has been given up");
        }
        gameOver.setValue(true);
        log.info("Loading high scores scene...");
        fxmlLoader.setLocation(getClass().getResource("/fxml/highscores.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
*/
/*
    private GameResult createGameResult() {
        GameResult result = GameResult.builder()
                .player(playerName)
                .solved(gameState.isSolved())
                .duration(Duration.between(startTime, Instant.now()))
                .steps(steps.get())
                .build();
        return result;
    }
*/

}

