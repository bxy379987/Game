package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicReference;

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
     * Game Front-end entities
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
    // GAME STAGE
    public int GAME_STAGE = 0;
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
    // DICE position
    private static final int DICE_START_X = 660;
    private static final int DICE_START_Y = 500;
    private static final double DICE_ANIME_TIME = 0.8;
    private static final int DICE_ANIME_FRAMES = 30;



     public void findNearest(Group group) {
         // clear flags
         if (nearest != null) {
             for (Circle n: nearest) {
                 n.setFill(Color.BLACK);
             }
         }
         Circle[] rugAnchors = {(Circle) group.getChildren().get(1), (Circle) group.getChildren().get(2)};
         // find nearest anchors/coordinates/distance
         int[][] rugAnchorsIdx = {{-1, -1}, {-1, -1}};
         double[] minDistance = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
         for (int col = 0; col < boardAnchors.length; col++) {
             for (int row = 0; row < boardAnchors[0].length; row++) {
                 double boardAnchorX = boardAnchors[col][row].getCenterX();
                 double boardAnchorY = boardAnchors[col][row].getCenterY();
                 for (int idx = 0; idx < rugAnchors.length; idx++) {
                     double x, y;
                     // get local to scene coordinates
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

    // ==================== RUG ENTITIES ====================
    static class RugEntity{
        private final int ID;
        private final pieceColor color;
         Group rugGroup;
//        ImageView rug;
        private final Circle firstPart;
        private final Circle secondPart;
//        private Rug rug;
        public RugEntity(int ID, pieceColor color, double x, double y) {
            this.ID = ID;
            this.color = color;
            // abstract rug
//            this.rug = new Rug(color, ID);
            rugGroup = new Group();
            // init Rug patterns
            Image rugImage = new Image("comp1110/ass2/assets/rug" +
                    color.getSymbol() + ".png",
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

        public pieceColor getColor() {
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
        public DraggableRugEntity(int ID ,pieceColor color, double x, double y) {
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

                // reset anchors
                isMouseDragged = false;
                for (Circle circle : nearest) {
                    circle.setFill(Color.BLACK);
                }
                nearest = null;
            });
        }
    }

    // ==================== DICE ENTITIES ====================
    class DiceEntity {
        private boolean clickable = false;
        private int number;
        private final Dice dice;
        DiceEntity(double x, double y) {
            dice = new Dice();
            number = dice.rollDice();
            AtomicReference<Image> diceImage = new AtomicReference<>(
                    new Image("comp1110/ass2/assets/dice/dice_" + number + ".png",
                            190, 200, false, false));
            ImageView diceEntity = new ImageView(diceImage.get());

            diceEntity.setX(x);
            diceEntity.setY(y);
            root.getChildren().add(diceEntity);
            // active event
            diceEntity.setOnMouseClicked(event -> {
                if (clickable) {
//                    System.out.println("Click");
                    KeyFrame keyFrame = new KeyFrame(Duration.seconds(DICE_ANIME_TIME / DICE_ANIME_FRAMES), eventAnime -> {
                        number = dice.rollDice();
//                        System.out.println("Anime: change to [" + number + "]");
                        diceImage.set(new Image("comp1110/ass2/assets/dice/dice_" + number + ".png",
                                190, 200, false, false));
                        diceEntity.setImage(diceImage.get());
                    });
                    Timeline timeline = new Timeline(keyFrame);
                    timeline.setCycleCount(DICE_ANIME_FRAMES);
                    timeline.play();
                }
            });
        }

        public void setClickable(boolean clickable) {
            this.clickable = clickable;
        }
    }

    // ==================== PLAYER ENTITIES ====================

    class PlayerEntity {
        private boolean isSelect = false;
        Player player;
        pieceColor color;
        public PlayerEntity(double x, double y, pieceColor color) {
            this.color = color;
            AtomicReference<Image> playerImage = new AtomicReference<>(new Image("comp1110/ass2/assets/selection/player" + color.getSymbol().toUpperCase() + ".png",
                    500, 500, false, false));
            ImageView playerEntity = new ImageView(playerImage.get());
            playerEntity.setX(x);
            playerEntity.setY(y);
            root.getChildren().add(playerEntity);

            playerEntity.setOnMouseClicked(event -> {
                if (isSelect) {
                    playerImage.set(new Image("comp1110/ass2/assets/selection/player" + color.getSymbol().toUpperCase() + "_select.png",
                            500, 500, false, false));
                } else {
                    playerImage.set(new Image("comp1110/ass2/assets/selection/player" + color.getSymbol().toUpperCase() + ".png",
                            500, 500, false, false));
                }
                playerEntity.setImage(playerImage.get());
                isSelect = !isSelect;
            });
        }
    }

    // ==================== START ENTITIES ====================
    class StartEntity {
        public StartEntity(double x, double y) {
            AtomicReference<Image> startImage = new AtomicReference<>(new Image("comp1110/ass2/assets/selection/START.png",
                    850, 420, false, false));
            ImageView startEntity = new ImageView(startImage.get());
            startEntity.setX(x);
            startEntity.setY(y);
            root.getChildren().add(startEntity);

            startEntity.setOnMouseEntered(event -> {
                startImage.set(new Image("comp1110/ass2/assets/selection/START_select.png",
                        850, 420, false, false));
                startEntity.setImage(startImage.get());
            });
            startEntity.setOnMouseExited(event -> {
                startImage.set(new Image("comp1110/ass2/assets/selection/START.png",
                        850, 420, false, false));
                startEntity.setImage(startImage.get());
            });
            startEntity.setOnMousePressed(event -> {

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

        if (GAME_STAGE == 0) gameSelectStage();
        if (GAME_STAGE == 1) gamePrepareStage();

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
        initBackground("SELECT");

        initPlayer(isPlayerPlaying);

    }

    private void initPlayer(boolean[] isPlayerPlaying) {
        PlayerEntity playerC = new PlayerEntity(140, -100, pieceColor.CYAN);
        StartEntity startEntity = new StartEntity(180, 190);
        PlayerEntity playerY = new PlayerEntity(610, -100, pieceColor.YELLOW);
        PlayerEntity playerP = new PlayerEntity(640, 290, pieceColor.PURPLE);
        PlayerEntity playerR = new PlayerEntity(-20, 300, pieceColor.RED);
        // init players
        players = new Player[]{
                new Player(isPlayerPlaying[0], pieceColor.CYAN, 30, RUG_AMOUNT),
                new Player(isPlayerPlaying[1], pieceColor.YELLOW, 30, RUG_AMOUNT),
                new Player(isPlayerPlaying[2], pieceColor.RED, 30, RUG_AMOUNT),
                new Player(isPlayerPlaying[3], pieceColor.PURPLE, 30, RUG_AMOUNT)
        };
    }

    /**
     * ==================== GAME PREPARE STAGE ====================
     */

    private void gamePrepareStage() {
        // INIT BACKGROUND ENTITY
        initBackground("PREPARE");
        // INIT BOARD ENTITY
        initBoard();
        // ASSAM
        initAssam();
        // PLAYER
        initRugs();
        // DICE
        DiceEntity diceEntity = new DiceEntity(DICE_START_X, DICE_START_Y);
        diceEntity.setClickable(true);
        // DIRHAMS
        Image dirhamsImage = new Image("comp1110/ass2/assets/Dirhams.png",
                100, 120, false, false);
        ImageView dirhams = new ImageView(dirhamsImage);
        dirhams.setX(PLAYER_START_X + PLAYER_DIRHAMS_START_X);
        dirhams.setY(PLAYER_START_Y + PLAYER_DIRHAMS_START_Y);
        root.getChildren().add(dirhams);
    }

    private void initBackground(String stage) {
        if (stage.equals("PREPARE")) {
            Image backgroundImage = new Image("comp1110/ass2/assets/Background.png",
                    WINDOW_WIDTH, WINDOW_HEIGHT, false, false);
            ImageView background = new ImageView(backgroundImage);

            root.getChildren().add(background);
        }

        if (stage.equals("SELECT")) {
            Image backgroundImage = new Image("comp1110/ass2/assets/selection/background_select.png",
                    WINDOW_WIDTH, WINDOW_HEIGHT, false, false);
            ImageView background = new ImageView(backgroundImage);

            root.getChildren().add(background);
        }
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
