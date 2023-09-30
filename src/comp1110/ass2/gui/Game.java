package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.HashMap;

public class Game extends Application {
    public static final boolean DEBUG = false;

    private static Group root = new Group();
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;
    /**
     * Game Back-end class
     */
    private Board board;
    private Player[] players;
    private Assam assam;
    /**
     * Game entities
     */
    public Scene scene;
    private static final int RUG_AMOUNT = 15;
    private DraggableRugEntity[][] playerDraggableRugEntities;
    // set minimal distance for draggable rug stick to nearest block ↓
    private static final double MAX_ANCHOR_DISTANCE = 40;
    private Circle[][] boardAnchors;
    public Circle[] nearest;
    public double[] nearestDistance;
    public double[] nearestTrans;

    /**
     * Game GUI Parameters
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
     public void findNearest(Group group) {
         if (nearest != null) {
             for (Circle n: nearest) {
                 n.setFill(Color.BLACK);
             }
         }
         Circle[] rugAnchors = {(Circle) group.getChildren().get(1), (Circle) group.getChildren().get(2)};

         int[][] rugAnchorsIdx = {{-1, -1}, {-1, -1}};
         double[] minDistance = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
         for (int col = 0; col < boardAnchors.length; col++) {
             for (int row = 0; row < boardAnchors[0].length; row++) {
                 double boardAnchorX = boardAnchors[col][row].getCenterX();
                 double boardAnchorY = boardAnchors[col][row].getCenterY();
                 for (int idx = 0; idx < rugAnchors.length; idx++) {
                     double x, y;
                     Bounds bounds = rugAnchors[idx].localToScene(rugAnchors[idx].getBoundsInLocal());
                     x = bounds.getCenterX();
                     y = bounds.getCenterY();
                     System.out.println(x + "-" + y);
                     double distance = Math.sqrt((boardAnchorX - x) * (boardAnchorX - x) + (boardAnchorY - y) * (boardAnchorY - y));
//                 System.out.println(distance);
                     if (minDistance[idx] > distance) {
                         minDistance[idx] = distance;
                         rugAnchorsIdx[idx][0] = col;
                         rugAnchorsIdx[idx][1] = row;
                         nearestTrans = new double[]{boardAnchorX - x, boardAnchorY - y};
                     }
                 }
             }
         }
         nearest = new Circle[]{boardAnchors[rugAnchorsIdx[0][0]][rugAnchorsIdx[0][1]],
                 boardAnchors[rugAnchorsIdx[1][0]][rugAnchorsIdx[1][1]]};
         nearestDistance = minDistance;
         System.out.println("Find Nearest");
         for (Circle n: nearest) {
             System.out.println(n.getCenterX() + " " + n.getCenterY());
             n.setFill(Color.GREEN);
         }

    }

    /**
     * ==================== GAME ENTITIES ====================
     */

    static class RugEntity{
        private final int ID;
        private final char color;
         Group rugGroup;
//        ImageView rug;
        private final Circle firstPart;
        private final Circle secondPart;
        public RugEntity(int ID, char color, double x, double y) {
            this.ID = ID;
            this.color = color;
            rugGroup = new Group();
            // init Rug patterns
            Image rugImage = new Image("comp1110/ass2/assets/rug" +
                    String.valueOf(color).toUpperCase() + ".png",
                    NODE_SIZE * 2 + 10, NODE_SIZE, false, false);
            ImageView rug = new ImageView(rugImage);

            rugGroup.getChildren().add(rug);
            // init Rug anchors for mapping Board anchors
            firstPart = new Circle(NODE_SIZE * 0.5, NODE_SIZE * 0.5,
                    NODE_SIZE * 0.5);
            secondPart = new Circle( NODE_SIZE * 1.5 + 10, NODE_SIZE * 0.5,
                    NODE_SIZE * 0.5);
            if (!DEBUG) {
                firstPart.setFill(Color.TRANSPARENT);
                secondPart.setFill(Color.TRANSPARENT);
            }
            rugGroup.getChildren().add(firstPart);
            rugGroup.getChildren().add(secondPart);
            // set Group coordinates
            rugGroup.setLayoutX(x);
            rugGroup.setLayoutY(y);
            root.getChildren().add(rugGroup);
        }

        public Circle getFirstPart() {
            return firstPart;
        }

        public Circle getSecondPart() {
            return secondPart;
        }

        public int getID() {
            return ID;
        }

        public char getColor() {
            return color;
        }
    }

    class DraggableRugEntity extends RugEntity {
        private double mouseX, mouseY;
        private double originX, originY;
        public boolean isMousePressed = false;
        public boolean isMouseDragged = false;
        public double originRotate;

        /**
         * Init entity with coordinates nad tag
         * @param color
         * @param x
         * @param y
         */
        public DraggableRugEntity(int ID ,char color, double x, double y) {
            super(ID, color, x, y);
            // mouse pressed: set init states & check keyboard rotate
            rugGroup.setOnMousePressed(event -> {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                // set original states
                originX = rugGroup.getLayoutX();
                originY = rugGroup.getLayoutY();
                originRotate = rugGroup.getRotate();

                rugGroup.toFront();
                getFirstPart().toFront();
                getSecondPart().toFront();

                isMousePressed = true;
                // CASE: rotate
                scene.setOnKeyPressed(keyEvent -> {
                    if (isMousePressed && keyEvent.getCode() == KeyCode.E) {
                        rugGroup.setRotate(rugGroup.getRotate() + 90);
                        findNearest(rugGroup);
                    }
                });
            });
            // when entity was dragged: check nearest mapping anchors and log
            rugGroup.setOnMouseDragged(event -> {
                isMouseDragged = true;
                double deltaX = event.getSceneX() - mouseX;
                double deltaY = event.getSceneY() - mouseY;
                rugGroup.setLayoutX(rugGroup.getLayoutX() + deltaX);
                rugGroup.setLayoutY(rugGroup.getLayoutY() + deltaY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                findNearest(rugGroup);
            });
            // when anchors valid, stick entity to anchors
            rugGroup.setOnMouseReleased(event -> {
                isMousePressed = false;
                if (!isMouseDragged) findNearest(rugGroup);
                // if anchors valid
                if (Math.abs(nearestDistance[1] - nearestDistance[0]) < 1e-7
                        && nearestDistance[0] < MAX_ANCHOR_DISTANCE) {
                    rugGroup.setLayoutX(rugGroup.getLayoutX() + nearestTrans[0]);
                    rugGroup.setLayoutY(rugGroup.getLayoutY() + nearestTrans[1]);
                } else {
                    rugGroup.setLayoutX(originX);
                    rugGroup.setLayoutY(originY);
                    rugGroup.setRotate(originRotate);
                }
                isMouseDragged = false;
                for (Circle circle : nearest) {
                    circle.setFill(Color.BLACK);
                }
                nearest = null;
            });
        }
    }

    /**
     * MAIN SCENE
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        // FIXME Task 7 and 15
        scene = new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("◀ Assam Game ▶");

        gameSelectStage();
        gamePrepareStage();

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * ==================== GAME SELECT STAGE ====================
     */
    public void gameSelectStage() {
        boolean[] isPlayerPlaying = new boolean[4];
        // get options from gamer
        // TODO: add GUI to select players


        // init players
        players = new Player[]{new Player(isPlayerPlaying[0], 'c', 30, RUG_AMOUNT),
                new Player(isPlayerPlaying[1], 'y', 30, RUG_AMOUNT),
                new Player(isPlayerPlaying[2], 'r', 30, RUG_AMOUNT),
                new Player(isPlayerPlaying[3], 'p', 30, RUG_AMOUNT)
        };


    }

    /**
     * ==================== GAME PREPARE STAGE ====================
     */

    private void gamePrepareStage() {
        // INIT BACKGROUND ENTITY
        initBackground();
        // INIT BOARD ENTITY
        initBoard();
        // ASSAM
        initAssam();
        // PLAYER
        initRugs();


        // DIRHAMS
        Image dirhamsImage = new Image("comp1110/ass2/assets/Dirhams.png",
                100, 120, false, false);
        ImageView dirhams = new ImageView(dirhamsImage);
        dirhams.setX(PLAYER_START_X + PLAYER_DIRHAMS_START_X);
        dirhams.setY(PLAYER_START_Y + PLAYER_DIRHAMS_START_Y);
        root.getChildren().add(dirhams);
    }

    private void initBackground() {
        Image backgroundImage = new Image("comp1110/ass2/assets/Background.png",
                WINDOW_WIDTH, WINDOW_HEIGHT, false, false);
        ImageView background = new ImageView(backgroundImage);

        root.getChildren().add(background);
    }

    private void initBoard() {
        // INIT BOARD
        board = new Board();

        boardAnchors = new Circle[board.BOARD_WIDTH][board.BOARD_HEIGHT];
        for (int x = 0; x < board.BOARD_WIDTH; x++) {
            for (int y = 0; y < board.BOARD_HEIGHT; y++) {
                boardAnchors[x][y] = new Circle(BOARD_START_X + x * NODE_OUTER_BOUND_SIZE + NODE_SIZE * 0.5,
                        BOARD_START_Y + y * NODE_OUTER_BOUND_SIZE + NODE_SIZE * 0.5,10);
//                boardAnchors[x][y].setFill(Color.TRANSPARENT);
                boardAnchors[x][y].setFill(Color.BLACK);
                if (DEBUG) root.getChildren().add(boardAnchors[x][y]);
            }
        }

    }

    private void initAssam() {
        Image assamImage = new Image("comp1110/ass2/assets/pointer.png",
                NODE_SIZE, NODE_SIZE, false, false);
        ImageView assamEntity = new ImageView(assamImage);
        assamEntity.setX(BOARD_START_X + 3 * NODE_OUTER_BOUND_SIZE);
        assamEntity.setY(BOARD_START_Y + 3 * NODE_OUTER_BOUND_SIZE);
        root.getChildren().add(assamEntity);

        assam = new Assam(3, 3, Direction.NORTH);
    }

    private void initRugs() {
        // init draggable rug
        playerDraggableRugEntities = new DraggableRugEntity[players.length][RUG_AMOUNT];
        for (int playerIdx = 0; playerIdx < players.length; playerIdx++) {
            int x = PLAYER_START_X + PLAYER_RUG_START_X;
            int y = PLAYER_START_Y + PLAYER_RUG_START_Y + playerIdx * (PLAYER_RUG_SPACE + NODE_SIZE);
            System.out.println(x + " " + y);
            for (int rugIdx = 0; rugIdx < RUG_AMOUNT; rugIdx++) {
                playerDraggableRugEntities[playerIdx][rugIdx] = new DraggableRugEntity(rugIdx, players[playerIdx].getColor(), x, y);

            }
        }

    }




}
