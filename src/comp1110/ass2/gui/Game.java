package comp1110.ass2.gui;

import comp1110.ass2.Board;
import comp1110.ass2.Rug;
import gittest.C;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Game extends Application {

    private static Group root = new Group();
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;

    /**
     * Game entities
     */
    private static final int RUG_AMOUNT = 15;
    private final Board board = new Board();
    private final DraggableRugEntity[][] playerDraggableRugEntities = new DraggableRugEntity[4][RUG_AMOUNT];
    // set minimal distance for draggable rug stick to nearest block ↓
    private static final double MIN_STICK_DISTANCE = 40;
    private final Rectangle[][] boardSticks = new Rectangle[board.BOARD_WIDTH][board.BOARD_HEIGHT];
    Rectangle nearest;
    /**
     * Game Parameters
     * +---------------------+----------+
     * +                     |          |
     * +      BOARD          |  PLAYER  |
     * +                     |          |
     * +---------------------+----------+
     */
    // BOARD panel
    private static final int NODE_OUTER_BOUND_SIZE = 70;
    private static final int NODE_SIZE = 60;
    private static final int BOARD_START_X = 110;
    private static final int BOARD_START_Y = 110;

    // PLAYER information panel
    private static final int PLAYER_START_X = 860;
    private static final int PLAYER_START_Y = 0;
    // RUG relative to PLAYER information panel
    private static final int PLAYER_RUG_START_X = 0;
    private static final int PLAYER_RUG_START_Y = 150;
    private static final int PLAYER_RUG_SPACE = 90;
    // DIRHAMS relative to PLAYER information panel
    private static final int PLAYER_DIRHAMS_START_X = 180;
    private static final int PLAYER_DIRHAMS_START_Y = 0;
     public void findNearest(double x, double y) {
//        System.out.println(triangles.get(0));
        int minDistanceX = -1;
        int minDistanceY = -1;
        double minDistance = Double.POSITIVE_INFINITY;
         for (int col = 0; col < boardSticks.length; col++) {
             for (int row = 0; row < boardSticks[0].length; row++) {
                 double stickerX = boardSticks[col][row].getX();
                 double stickerY = boardSticks[col][row].getY();
                 double distance = Math.sqrt((stickerX - x) * (stickerX - x) + (stickerY - y) * (stickerY - y));
                 if (minDistance > distance) {
                     minDistance = distance;
                     minDistanceX = col;
                     minDistanceY = row;
                 }
             }
         }
       nearest = boardSticks[minDistanceX][minDistanceY];
    }

    static class RugEntity extends Rug {
        ImageView rug;
        int colorIdx;
        public RugEntity(char color) {
            switch (color) {
                case 'c' -> colorIdx = 0;
                case 'y' -> colorIdx = 1;
                case 'r' -> colorIdx = 2;
                case 'p' -> colorIdx = 3;
            }
            Image rugImage = new Image("comp1110/ass2/assets/rug" +
                    String.valueOf(color).toUpperCase() + ".png",
                    NODE_SIZE * 2 + 10, NODE_SIZE, false, false);
            rug = new ImageView(rugImage);
            rug.setX(PLAYER_START_X + PLAYER_RUG_START_X);
            rug.setY(PLAYER_START_Y + PLAYER_RUG_START_Y + colorIdx * (PLAYER_RUG_SPACE + NODE_SIZE));
            root.getChildren().add(rug);
        }
        public RugEntity(String input) {
            super(input);

        }

        public void setCoordinate(int x, int y) {
            rug.setX(x);
            rug.setY(y);
        }
    }

    class DraggableRugEntity extends RugEntity {
        private double mouseX;
        private double mouseY;

        public DraggableRugEntity(char color) {
            super(color);
            this.rug.setOnMousePressed(event -> {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
//                System.out.println("mouseX/mouseY " + mouseX + " " +mouseY);

                this.rug.toFront();
            });

            this.rug.setOnMouseDragged(event -> {
                double deltaX = event.getSceneX() - mouseX;
                double deltaY = event.getSceneY() - mouseY;
//                System.out.println(this.x + " " + this.y + "\n" + deltaX + " " + deltaY + "\n");
                this.rug.setLayoutX(this.rug.getLayoutX() + deltaX);
                this.rug.setLayoutY(this.rug.getLayoutY() + deltaY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                findNearest(event.getSceneX(), event.getSceneY());
//                highlightedNearestTriangle(event.getSceneX(), event.getSceneY());
            });

            this.rug.setOnMouseReleased(event -> {
                this.rug.setLayoutX(nearest.getLayoutX());
                this.rug.setLayoutY(nearest.getLayoutY());
            });
        }
    }

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
        // BACKGROUND
        Image backgroundImage = new Image("comp1110/ass2/assets/Background.png",
                WINDOW_WIDTH, WINDOW_HEIGHT, false, false);
        ImageView background = new ImageView(backgroundImage);

        root.getChildren().add(background);
        // BOARD

        for (int x = 0; x < board.BOARD_WIDTH; x++) {
            for (int y = 0; y < board.BOARD_HEIGHT; y++) {
                Rectangle sticker = new Rectangle(NODE_SIZE, NODE_SIZE);
                sticker.setLayoutX(BOARD_START_X + x * NODE_OUTER_BOUND_SIZE);
                sticker.setLayoutY(BOARD_START_Y + y * NODE_OUTER_BOUND_SIZE);
//                sticker.setFill(Color.BLACK);
//                root.getChildren().add(sticker);
                boardSticks[x][y] = sticker;
            }
        }
        // ASSAM
        Image assamImage = new Image("comp1110/ass2/assets/pointer.png",
                NODE_SIZE, NODE_SIZE, false, false);
        ImageView assam = new ImageView(assamImage);
        assam.setX(BOARD_START_X + 3 * NODE_OUTER_BOUND_SIZE);
        assam.setY(BOARD_START_Y + 3 * NODE_OUTER_BOUND_SIZE);
        root.getChildren().add(assam);

        // RUG
        char[] rugColors = {'c', 'y', 'r', 'p'};
        for (int colorIdx = 0; colorIdx < rugColors.length; colorIdx++) {
            for (int rugIdx = 0; rugIdx < RUG_AMOUNT; rugIdx++) {
                playerDraggableRugEntities[colorIdx][rugIdx] = new DraggableRugEntity(rugColors[colorIdx]);
            }
        }

        // DIRHAMS
        Image dirhamsImage = new Image("comp1110/ass2/assets/Dirhams.png",
                100, 120, false, false);
        ImageView dirhams = new ImageView(dirhamsImage);
        dirhams.setX(PLAYER_START_X + PLAYER_DIRHAMS_START_X);
        dirhams.setY(PLAYER_START_Y + PLAYER_DIRHAMS_START_Y);
        root.getChildren().add(dirhams);
    }

    private void startGame() {
    }


}
