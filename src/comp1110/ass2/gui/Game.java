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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static comp1110.ass2.Marrakech.*;

public class Game extends Application {
    public static final boolean DEBUG = false;
    //show circles to debug
    public Stage stage;
    private static Group root = new Group();
    private static Group selectRoot = new Group();
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;
    /**
     * Game Back-end class
     */
    private Board board;
    PlayerEntity[] playerEntities;
    private AssamEntity assamEntity;
    private DraggableRugEntity CurrentDraggableRug;
    public String gameState;
    /**
     * Game Front-end entities
     */
    public Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);;
    public Scene selectScene = new Scene(selectRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
    public DiceEntity diceEntity;
    private static final int RUG_AMOUNT = 15;
    private static final int DIRHAM_AMOUNT = 30;
    // set minimal distance for draggable rug stick to nearest block ↓
    private static final double MAX_ANCHOR_DISTANCE = 40;
    private Circle[][] boardAnchors;
    public Circle[] nearest;
    public int[][] nearestIdx;
    public double[] nearestDistance;
    public double[] nearestTrans;

    /**
     * Game GUI Parameters
     * +---------------------+----------+
     * |                     |          |
     * |      BOARD          |  PLAYER  |
     * |                     |          |
     * +---------------------+----------+
     */
    // GAME STAGE
    /**
     * 0 : SELECT STAGE
     * 1 :
     */
    public int GAME_STAGE = 0;
    public int CURRENT_PLAYER_IDX = 0;
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
//         System.out.println("==============[ findNearest ]==============");
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
//                     System.out.println(x + "-" + y);
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
         nearestIdx = rugAnchorsIdx;
         nearestDistance = minDistance;
//         System.out.println("Find Nearest");
         for (Circle n: nearest) {
//             System.out.println(n.getCenterX() + " " + n.getCenterY());
             n.setFill(Color.GREEN);
         }

    }

    /**
     * ==================== GAME ENTITIES ====================
     */

    // ==================== RUG ENTITIES ====================
    static class RugEntity {
        Rug rug;
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
//             abstract rug
            this.rug = new Rug(color, ID);

            rugGroup = new Group();
            // init Rug patterns
            Image rugImage = new Image("comp1110/ass2/assets/Rug" +
                    color.getSymbol().toUpperCase() + ".png",
                    NODE_SIZE * 2 + 10, NODE_SIZE, false, false);
            ImageView rug = new ImageView(rugImage);

            rugGroup.getChildren().add(rug);
            // init Rug anchors for mapping Board anchors
            firstPart = new Circle(NODE_SIZE * 0.5, NODE_SIZE * 0.5,
                    NODE_SIZE * 0.5);
            secondPart = new Circle(NODE_SIZE * 1.5 + 10, NODE_SIZE * 0.5,
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
        private boolean draggable;
        public double originRotate;

        /**
         * Init entity with coordinates nad tag
         * @param color
         * @param x
         * @param y
         */
        public DraggableRugEntity(int ID, pieceColor color, double x, double y, boolean draggable) {
            super(ID, color, x, y);
            this.draggable = draggable;
            // mouse pressed: set init states & check keyboard rotate
            rugGroup.setOnMousePressed(event -> {
                if (this.draggable) {
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
                    CurrentDraggableRug = this;
                }
            });
            // when entity was dragged: check nearest mapping anchors and log
            rugGroup.setOnMouseDragged(event -> {
                if (this.draggable) {
                    isMouseDragged = true;
                    double deltaX = event.getSceneX() - mouseX;
                    double deltaY = event.getSceneY() - mouseY;
                    rugGroup.setLayoutX(rugGroup.getLayoutX() + deltaX);
                    rugGroup.setLayoutY(rugGroup.getLayoutY() + deltaY);
                    mouseX = event.getSceneX();
                    mouseY = event.getSceneY();
                    findNearest(rugGroup);
                }
            });
            // when anchors valid, stick entity to anchors
            rugGroup.setOnMouseReleased(event -> {
                if (this.draggable) {
                    board.showBoardColorInMatrix();
                    System.out.println();
                    isMousePressed = false;
                    String rugString = getColor().getSymbol() + getID() + nearestIdx[0][0] + nearestIdx[0][1] + nearestIdx[1][0] + nearestIdx[1][1];
                    System.out.println("[RugEntity] " + rugString);
//                    System.out.println(getCurrentGame());
                    // if anchors valid
                    if (Math.abs(nearestDistance[1] - nearestDistance[0]) < 1e-7
                            && nearestDistance[0] < MAX_ANCHOR_DISTANCE
                            && isPlacementValid(getCurrentGame(), rugString)) {
                        // set rug entity
                        rugGroup.setLayoutX(rugGroup.getLayoutX() + nearestTrans[0]);
                        rugGroup.setLayoutY(rugGroup.getLayoutY() + nearestTrans[1]);
                        // set board
                        this.rug.setFirstCoordinate(nearestIdx[0]);
                        this.rug.setSecondCoordinate(nearestIdx[1]);
                        board.placeRug(this.rug, assamEntity.assam);
                        System.out.println("[RugEntity] " + getCurrentGame());
                        board.showBoardColorInMatrix();
                        System.out.println();
                        // set player rug amount

                        playerEntities[CURRENT_PLAYER_IDX].player.deducedRemainingRugs();
                        // set un-draggable
                        this.draggable = false;
                        // reset current draggable rug
                        CurrentDraggableRug = null;
                        // [Phase] next player
                        System.out.println("[RugEntity] set assam rotatable");
                        CURRENT_PLAYER_IDX = (CURRENT_PLAYER_IDX + 1) % 4;
                        // find next valid player
                        if (!playerEntities[CURRENT_PLAYER_IDX].player.isIsplaying()) {
                            while (!playerEntities[CURRENT_PLAYER_IDX].player.isIsplaying()) {
                                CURRENT_PLAYER_IDX = (CURRENT_PLAYER_IDX + 1) % 4;
                            }
                        }
                        System.out.println("[RugEntity] find valid player: " + CURRENT_PLAYER_IDX);
                        assamEntity.setRotatable(true);
                        // set Rug draggable

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
                }
            });

        }

        public void setDraggable(boolean draggable) {
            this.draggable = draggable;
        }
        public boolean getDraggable() {
            return this.draggable;
        }

    }

    static class BlockEntity {
        double x, y;
        ImageView imageView;
        public BlockEntity(double x, double y) {
            this.x = x;
            this.y = y;
            imageView = new ImageView(new Image("comp1110/ass2/assets/Block.png",
                    NODE_SIZE * 2 + 10, NODE_SIZE, false, false));
            imageView.setX(x);
            imageView.setY(y);
            root.getChildren().add(imageView);
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
//                        System.out.println(number);
//                        System.out.println("Anime: change to [" + number + "]");
                        diceImage.set(new Image("comp1110/ass2/assets/dice/dice_" + number + ".png",
                                190, 200, false, false));
                        diceEntity.setImage(diceImage.get());
                    });
                    Timeline timeline = new Timeline(keyFrame);
                    timeline.setCycleCount(DICE_ANIME_FRAMES);

                    timeline.setOnFinished(eventTimelineFin -> {
                        assamEntity.moveXStep(number);
                        System.out.println("[DiceEntity] assam " + assamEntity.assam);
                        // assam to top
                        assamEntity.imageView.toFront();
                        // set Rug Draggable
                        System.out.println("[DiceEntity] Current player " + CURRENT_PLAYER_IDX);
                        playerEntities[CURRENT_PLAYER_IDX].rugEntities[playerEntities[CURRENT_PLAYER_IDX].player.getRemainingRugs() - 1].setDraggable(true);

                        gameState = getCurrentGame();
                        System.out.println("[DiceEntity] " + gameState);
                        if (!isGameOver(gameState)) {
                            pieceColor boardColor = board.getColorByCoordinate(assamEntity.x, assamEntity.y);
                            if (boardColor != playerEntities[CURRENT_PLAYER_IDX].color) {
                                int payment = getPaymentAmount(gameState);
                                if (payment != 0) {
                                    int playerToPayIdx = getIndexByColor(boardColor);
                                    System.out.println("[DiceEntity] " + CURRENT_PLAYER_IDX + " pay " + playerToPayIdx + ": " + payment);
                                    playerEntities[playerToPayIdx].setDirhams(playerEntities[playerToPayIdx].player.getDirhams() + payment);
                                    playerEntities[CURRENT_PLAYER_IDX].setDirhams(playerEntities[CURRENT_PLAYER_IDX].player.getDirhams() - payment);
                                    //
                                    // TODO: Check after pay & do something // dirhams under 0
                                }
                            }

                        } else {
                            // TODO: Game Over
                        }
                    });
                    timeline.play();
                    setClickable(false);
                }
            });
        }

        public void setClickable(boolean clickable) {
            this.clickable = clickable;
        }
    }

    // ==================== PLAYER ENTITIES ====================

    class PlayerEntity {
        boolean isSelect;
        //judge select
        public Player player;
        DraggableRugEntity[] rugEntities;
        Text dirhamsText;
        pieceColor color;
        int playerIdx;
        // Mode 0: Human
        // Mode 1: Random
        // Mode 2: AI
        // TODO: implement AI / need to add a selective button to change mode
        int characterMode = 0;
        public PlayerEntity(double x, double y, pieceColor color, boolean isSelect) {
            this.color = color;
            this.isSelect = isSelect;
            this.player = new Player(this.isSelect, color, DIRHAM_AMOUNT, RUG_AMOUNT);
            this.rugEntities = new DraggableRugEntity[RUG_AMOUNT];
            // init PlayerIdx
            playerIdx = getIndexByColor(color);
            // init DraggableRugs
            int rugX = PLAYER_START_X + PLAYER_RUG_START_X;
            int rugY = PLAYER_START_Y + PLAYER_RUG_START_Y + playerIdx * (PLAYER_RUG_SPACE + NODE_SIZE);
            for (int rugIdx = 0; rugIdx < RUG_AMOUNT; rugIdx++) {
                this.rugEntities[rugIdx] = new DraggableRugEntity(rugIdx, color, rugX, rugY, false);
            }

            // init Dirhams
            Font pixelFont = Font.loadFont(Game.class.getResourceAsStream("PixelFonts.ttf"), NODE_SIZE);
            dirhamsText = new Text(String.valueOf(player.getDirhams()));
            dirhamsText.setFont(pixelFont);
            dirhamsText.setFill(Color.WHITE);
            dirhamsText.setLayoutX(rugX + 3 * NODE_SIZE + 10);
            dirhamsText.setLayoutY(rugY + NODE_SIZE);
            root.getChildren().add(dirhamsText);


            // init Entity
            AtomicReference<Image> playerImage = new AtomicReference<>(new Image("comp1110/ass2/assets/selection/player" + this.color.getSymbol().toUpperCase() + ".png",
                    500, 500, false, false));
            ImageView playerEntity = new ImageView(playerImage.get());
            playerEntity.setX(x);
            playerEntity.setY(y);
            selectRoot.getChildren().add(playerEntity);

            // init Actions
            AtomicBoolean currentSelect = new AtomicBoolean(isSelect);
            playerEntity.setOnMouseClicked(event -> {
                currentSelect.getAndSet(!currentSelect.get());
                this.isSelect = currentSelect.get();
                this.player.setIsplaying(this.isSelect);

//                System.out.println(currentSelect.get());
                if (currentSelect.get()) {
                    playerImage.set(new Image("comp1110/ass2/assets/selection/player" + color.getSymbol().toUpperCase() + "_select.png",
                            500, 500, false, false));
                } else {
                    playerImage.set(new Image("comp1110/ass2/assets/selection/player" + color.getSymbol().toUpperCase() + ".png",
                            500, 500, false, false));
                }
                playerEntity.setImage(playerImage.get());
            });
        }

        public void setCharacterMode(int characterMode) {
            this.characterMode = characterMode;
        }

        // ADD group set method
        public void setRugsDraggableValue(boolean value) {
            for (int rugIdx = 0; rugIdx < RUG_AMOUNT; rugIdx++) {
                // if not on board
                rugEntities[rugIdx].setDraggable(value);
            }
        }

        public void setEntitiesToFront() {
            for (int rugIdx = 0; rugIdx < RUG_AMOUNT; rugIdx++) {
                rugEntities[rugIdx].rugGroup.toFront();
            }
            dirhamsText.toFront();
        }

        public void setDirhams(int value) {
            player.setDirhams(value);
            dirhamsText.setText(String.valueOf(value));
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
            selectRoot.getChildren().add(startEntity);

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
                System.out.println("=".repeat(40));
                System.out.println("GAME START");
                int count = 0;
                for (int idx = 0; idx < playerEntities.length; idx++) {
                    System.out.println("[startEntity] " + playerEntities[idx].player);
                    if (playerEntities[idx].player.isIsplaying()) {
                        count += 1;
                    } else {
                        // add block if player is not playing
                        int playerX = PLAYER_START_X + PLAYER_RUG_START_X;
                        int playerY = PLAYER_START_Y + PLAYER_RUG_START_Y + idx * (PLAYER_RUG_SPACE + NODE_SIZE);
                        BlockEntity blockEntity = new BlockEntity(playerX, playerY);
                    }

                }
                // Player count must larger than 1
                if (count > 1) {
                    // find first valid player

                    if (!playerEntities[CURRENT_PLAYER_IDX].player.isIsplaying()) {
                        while (!playerEntities[CURRENT_PLAYER_IDX].player.isIsplaying()) {
                            CURRENT_PLAYER_IDX = (CURRENT_PLAYER_IDX + 1) % 4;
                        }
                    }
                    System.out.println("[StartEntity] find valid player: " + CURRENT_PLAYER_IDX);
                    stage.setScene(scene);
                }
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
        System.out.println("==============[ start ]==============");
        // FIXME Task 7 and 15
        this.stage = stage;
        stage.setTitle("◀ Assam Game ▶");

        gameSelectStage();
        gamePrepareStage();

        stage.setResizable(false);
        stage.show();
    }
    /**
     * ==================== GAME SELECT STAGE ====================
     */
    public void gameSelectStage() {
        System.out.println("==============[ gameSelectStage ]==============");
        // get options from gamer
        initBackground("SELECT");
        PlayerEntity playerC = new PlayerEntity(140, -100, pieceColor.CYAN, false);
        StartEntity startEntity = new StartEntity(180, 190);
        PlayerEntity playerY = new PlayerEntity(610, -100, pieceColor.YELLOW, false);
        PlayerEntity playerP = new PlayerEntity(640, 290, pieceColor.PURPLE, false);
        PlayerEntity playerR = new PlayerEntity(-20, 300, pieceColor.RED, false);
        playerEntities = new PlayerEntity[] {playerC, playerY, playerR, playerP};
        stage.setScene(selectScene);
    }


    /**
     * =================== GAME PREPARE STAGE ===================
     */

    private void gamePrepareStage() {
        System.out.println("=============[ gamePrepareStage ]=============");
        // INIT BACKGROUND ENTITY
        initBackground("PREPARE");
        // INIT BOARD ENTITY
        initBoard();
        // ASSAM
        initAssam();
        // RUGS
        for (int playerIdx = 0; playerIdx < 4; playerIdx++) {
            playerEntities[playerIdx].setEntitiesToFront();
        }
        // DIRHAMS
        Image dirhamsImage = new Image("comp1110/ass2/assets/Dirhams.png",
                100, 120, false, false);
        ImageView dirhams = new ImageView(dirhamsImage);
        dirhams.setX(PLAYER_START_X + PLAYER_DIRHAMS_START_X);
        dirhams.setY(PLAYER_START_Y + PLAYER_DIRHAMS_START_Y);
        root.getChildren().add(dirhams);
        // DICE
        diceEntity = new DiceEntity(DICE_START_X, DICE_START_Y);

        // combine keyboard event
        scene.setOnKeyPressed(event -> {
//            System.out.println("[rootEvent] " + event.getCode() + " Pressed");
            // AssamEntity case
            if (assamEntity.rotatable) {
                //first we get the current direction
                // then we can set direction excepts opposite of the current direction
                if(assamEntity.getDirection()==Direction.NORTH){
                switch (event.getCode()) {
                    case UP: assamEntity.setDirection(Direction.NORTH); break;
                    case RIGHT: assamEntity.setDirection(Direction.EAST); break;
                    //case DOWN: assamEntity.setDirection(Direction.SOUTH); break;
                    case LEFT: assamEntity.setDirection(Direction.WEST); break;
                    case ENTER:
                        diceEntity.setClickable(true);
                        assamEntity.setRotatable(false);
                        System.out.println("[rootEvent] Keyboard Entered");
                        break;}
                }
                if(assamEntity.getDirection()==Direction.EAST){
                    switch (event.getCode()) {
                        case UP: assamEntity.setDirection(Direction.NORTH); break;
                        case RIGHT: assamEntity.setDirection(Direction.EAST); break;
                        case DOWN: assamEntity.setDirection(Direction.SOUTH); break;
                        //case LEFT: assamEntity.setDirection(Direction.WEST); break;
                        case ENTER:
                            diceEntity.setClickable(true);
                            assamEntity.setRotatable(false);
                            System.out.println("[rootEvent] Keyboard Entered");
                            break;}
                }
                if(assamEntity.getDirection()==Direction.WEST){
                    switch (event.getCode()) {
                        case UP: assamEntity.setDirection(Direction.NORTH); break;
                        //case RIGHT: assamEntity.setDirection(Direction.EAST); break;
                        case DOWN: assamEntity.setDirection(Direction.SOUTH); break;
                        case LEFT: assamEntity.setDirection(Direction.WEST); break;
                        case ENTER:
                            diceEntity.setClickable(true);
                            assamEntity.setRotatable(false);
                            System.out.println("[rootEvent] Keyboard Entered");
                            break;}
                }
                if(assamEntity.getDirection()==Direction.SOUTH){
                    switch (event.getCode()) {
                        //case UP: assamEntity.setDirection(Direction.NORTH); break;
                        case RIGHT: assamEntity.setDirection(Direction.EAST); break;
                        case DOWN: assamEntity.setDirection(Direction.SOUTH); break;
                        case LEFT: assamEntity.setDirection(Direction.WEST); break;
                        case ENTER:
                            diceEntity.setClickable(true);
                            assamEntity.setRotatable(false);
                            System.out.println("[rootEvent] Keyboard Entered");
                            break;}
                }
            }

            // DraggableEntity rotate case
            if (CurrentDraggableRug != null) {
                if (CurrentDraggableRug.isMousePressed && event.getCode() == KeyCode.E) {
                    CurrentDraggableRug.rugGroup.setRotate(CurrentDraggableRug.rugGroup.getRotate() + 90);
                    findNearest(CurrentDraggableRug.rugGroup);
                }
            }

        });
    }

    public int getIndexByColor(pieceColor color) {
        switch (color) {
            case CYAN -> { return 0;}
            case YELLOW -> { return 1;}
            case RED -> { return 2;}
            case PURPLE -> { return 3;}
        }
        return -1;
    }

    private String getCurrentGame() {
        return String.valueOf(playerEntities[0].player) + playerEntities[1].player + playerEntities[2].player + playerEntities[3].player + assamEntity + "B" + board;
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

            selectRoot.getChildren().add(background);
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

     public static class AssamEntity{
        int x;
        int y;
        boolean rotatable;
        Direction direction;
        public ImageView imageView;
        public Assam assam;
        public AssamEntity(int x,int y, Direction direction, boolean rotatable){
            this.x = x;
            this.y = y;
            this.rotatable = rotatable;
            this.direction=direction;
            this.imageView=new ImageView(new Image("comp1110/ass2/assets/pointer.png",
                    NODE_SIZE, NODE_SIZE, false, false));
            imageView.setX(BOARD_START_X + x * NODE_OUTER_BOUND_SIZE);
            imageView.setY(BOARD_START_Y + y * NODE_OUTER_BOUND_SIZE);
            assam = new Assam(x, y, direction);

        }

         public void setRotatable(boolean rotatable) {
             this.rotatable = rotatable;
         }

         public int getX() {
            return x;
         }

         public void setX(int x) {
            imageView.setX(BOARD_START_X + x * NODE_OUTER_BOUND_SIZE);
            assam.setxCoordinate(x);
             this.x = x;
         }

         public int getY() {
             return y;
         }

         public void setY(int y) {
            imageView.setY(BOARD_START_Y + y * NODE_OUTER_BOUND_SIZE);
            assam.setyCoordinate(y);
            this.y = y;
         }

         public Direction getDirection() {
             return direction;
         }

         public void setDirection(Direction direction) {
          //according to the direction to adjust the rotation of the picture
            this.direction = direction;
            assam.setDirection(direction);
            if (direction == Direction.NORTH)
                imageView.setRotate(0);
            if (direction == Direction.EAST)
                imageView.setRotate(90);
            if (direction == Direction.SOUTH)
                imageView.setRotate(180);
            if (direction == Direction.WEST)
                imageView.setRotate(270);
         }
         public void moveXStep(int step) {
             this.assam.moveXSteps(step);
             //we call assam.moveXsteps in AssamEntity to move the AssamEntity
             this.setDirection(this.assam.getDirection());
             this.setX(assam.getxCoordinate());
             this.setY(assam.getyCoordinate());
         }

         @Override
         public String toString() {
             return assam.toString();
         }
     }

    private void initAssam() {
        assamEntity = new AssamEntity(3, 3, Direction.NORTH, true);
        root.getChildren().add(assamEntity.imageView);
    }


}
