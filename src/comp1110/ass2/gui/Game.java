package comp1110.ass2.gui;

import gittest.C;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

public class Game extends Application {

    private final Group root = new Group();
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;

    @Override
    public void start(Stage stage) throws Exception {
        // FIXME Task 7 and 15
        Scene scene = new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("◀ Assam Game ▶");

        startGame();
        gameStage();


        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void gameStage() {
        Image backgroundImage = new Image("comp1110/ass2/assets/Background.png",
                WINDOW_WIDTH, WINDOW_HEIGHT, false, false);
        ImageView background = new ImageView(backgroundImage);

        root.getChildren().add(background);
    }

    private void startGame() {
    }
}
