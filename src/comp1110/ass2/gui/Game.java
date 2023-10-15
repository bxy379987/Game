package comp1110.ass2.gui;

import comp1110.ass2.*;
import gittest.A;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static comp1110.ass2.Marrakech.*;
import static java.awt.PageAttributes.MediaType.D;

public class Game extends Application {
    public static final boolean DEBUG = false;
    public Stage stage;
    private static Group root = new Group();
    private static Group selectRoot = new Group();
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;
    /**
     * Game Back-end class
     */
    private Board board;
    PlayerEntity playerC;
    PlayerEntity playerY;
    PlayerEntity playerP;
    PlayerEntity playerR;
    Player[] players;

    private AssamEntity assamEntity;
    /**
     * Game Front-end entities
     */
    public Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);;
    public Scene selectScene = new Scene(selectRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
    private static final int RUG_AMOUNT = 15;
    private static final int DIRHAM_AMOUNT = 30;
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
     * |                     |          |
     * |      BOARD          |  PLAYER  |
     * |                     |          |
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
    static class RugEntity {
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
                int boardX = (int) ((rugGroup.getLayoutX() + nearestTrans[0] - BOARD_START_X) / NODE_OUTER_BOUND_SIZE);
                int boardY = (int) ((rugGroup.getLayoutY() + nearestTrans[1] - BOARD_START_Y) / NODE_OUTER_BOUND_SIZE);
                int secondBoardX = boardX + 1;
                int secondBoardY = boardY;
//                System.out.println(boardX);
//                System.out.println(boardY);
//                System.out.println(secondBoardX);
//                System.out.println(secondBoardY);
                double rotation = rugGroup.getRotate() % 360;
                if (Math.abs(rotation - 180) == 0) {
                    // 0° or 180° rotation
                    secondBoardX = boardX - 1;
                } else if (Math.abs(rotation - 90) == 0) {
                    // 90° or 270° rotation
                    boardX = boardX + 1;
                    secondBoardX = boardX;
                    secondBoardY = boardY + 1;
                } else if (Math.abs(rotation - 270) == 0) {
                    boardX ++;
                    boardY ++;
                    secondBoardX = boardX;
                    secondBoardY = boardY - 1;
                } else {
                    secondBoardX = boardX + 1;
                    secondBoardY = boardY;
                }
                System.out.println(boardX);
                System.out.println(boardY);
                System.out.println(secondBoardX);
                System.out.println(secondBoardY);
                String rugString = getColor().getSymbol() + getID() + boardX + boardY + secondBoardX + secondBoardY;
                System.out.println(rugString);
                System.out.println(getCurrentGame());
                // if anchors valid
                if (Math.abs(nearestDistance[1] - nearestDistance[0]) < 1e-7
                        && nearestDistance[0] < MAX_ANCHOR_DISTANCE) {
                    if (isPlacementValid(getCurrentGame(), rugString)) {
                        rugGroup.setLayoutX(rugGroup.getLayoutX() + nearestTrans[0]);
                        rugGroup.setLayoutY(rugGroup.getLayoutY() + nearestTrans[1]);
                        board.setColorByCoordinate(boardX, boardY, getColor(), getID());
                        board.setColorByCoordinate(secondBoardX,secondBoardY,getColor(),getID());
                        System.out.println(getCurrentGame());
                    } else {
                        rugGroup.setLayoutX(originX);
                        rugGroup.setLayoutY(originY);
                        rugGroup.setRotate(originRotate);
                    }
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
        DiceEntity(double x1, double y1) {
            dice = new Dice();
            number = dice.rollDice();
            AtomicReference<Image> diceImage = new AtomicReference<>(
                    new Image("comp1110/ass2/assets/dice/dice_" + number + ".png",
                            190, 200, false, false));
            ImageView diceEntity = new ImageView(diceImage.get());

            diceEntity.setX(x1);
            diceEntity.setY(y1);
            root.getChildren().add(diceEntity);
            // active event
            diceEntity.setOnMouseClicked(event -> {
                if (clickable) {
//                    System.out.println("Click");
                    KeyFrame keyFrame = new KeyFrame(Duration.seconds(DICE_ANIME_TIME / DICE_ANIME_FRAMES), eventAnime -> {
                        number = dice.rollDice();
                        System.out.println(number);
//                        System.out.println("Anime: change to [" + number + "]");
                        diceImage.set(new Image("comp1110/ass2/assets/dice/dice_" + number + ".png",
                                190, 200, false, false));
                        diceEntity.setImage(diceImage.get());
                    });
                    Timeline timeline = new Timeline(keyFrame);
                    timeline.setCycleCount(DICE_ANIME_FRAMES);

                    timeline.setOnFinished(event1 -> {
                        Direction direction1 = assamEntity.getDirection();
                        int x2 = assamEntity.getX();
                        int y2 = assamEntity.getY();

                        Assam assam1 = new Assam(x2, y2, direction1);
                        System.out.println("number" + number);
                        assam1.moveXSteps(number);
                        System.out.println(assam1);
                        x2 = assam1.getxCoordinate();
                        y2 = assam1.getyCoordinate();
                        // CHANGE the function to AssamEntity Class
                        assamEntity.setDirection(assam1.getDirection());
//                        assamEntity.direction = assam1.getDirection();
//                        if (assamEntity.direction == Direction.NORTH) assamEntity.setRotate(0);
//                        if (assamEntity.direction == Direction.SOUTH) assamEntity.setRotate(180);
//                        if (assamEntity.direction == Direction.EAST) assamEntity.setRotate(90);
//                        if (assamEntity.direction == Direction.WEST) assamEntity.setRotate(270);
                        assamEntity.setX(x2);
                        assamEntity.setY(y2);
                        System.out.println(assamEntity);
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
        public Player player;
        pieceColor color;
        public PlayerEntity(double x, double y, pieceColor color, boolean isSelect) {
            this.color = color;
            this.isSelect = isSelect;
            this.player = new Player(this.isSelect, color, DIRHAM_AMOUNT, RUG_AMOUNT);

            AtomicReference<Image> playerImage = new AtomicReference<>(new Image("comp1110/ass2/assets/selection/player" + this.color.getSymbol().toUpperCase() + ".png",
                    500, 500, false, false));
            ImageView playerEntity = new ImageView(playerImage.get());
            playerEntity.setX(x);
            playerEntity.setY(y);
            selectRoot.getChildren().add(playerEntity);

            AtomicBoolean currentSelect = new AtomicBoolean(isSelect);
            playerEntity.setOnMouseClicked(event -> {
                currentSelect.getAndSet(!currentSelect.get());
                this.isSelect = currentSelect.get();
                this.player.setIsplaying(this.isSelect);

                System.out.println(currentSelect.get());
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
                for (Player player : players) {
                    System.out.println(player);
                    if (player.isIsplaying()) count += 1;
                }
                // Player count must larger than 1
                if (count > 1) stage.setScene(scene);
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
        this.stage = stage;
        stage.setTitle("◀ Assam Game ▶");

        gameSelectStage();
        gamePrepareStage();
        gamePlayingStage(players);

        stage.setResizable(false);
        stage.show();
    }
    /**
     * ==================== GAME SELECT STAGE ====================
     */
    public void gameSelectStage() {
        // get options from gamer
        // TODO: add GUI to select players
        initBackground("SELECT");
        playerC = new PlayerEntity(140, -100, pieceColor.CYAN, false);
        StartEntity startEntity = new StartEntity(180, 190);
        playerY = new PlayerEntity(610, -100, pieceColor.YELLOW, false);
        playerP = new PlayerEntity(640, 290, pieceColor.PURPLE, false);
        playerR = new PlayerEntity(-20, 300, pieceColor.RED, false);
        players = new Player[]{playerC.player, playerY.player, playerP.player, playerR.player};

        stage.setScene(selectScene);
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
        // DIRHAMS
        Image dirhamsImage = new Image("comp1110/ass2/assets/Dirhams.png",
                100, 120, false, false);
        ImageView dirhams = new ImageView(dirhamsImage);
        dirhams.setX(PLAYER_START_X + PLAYER_DIRHAMS_START_X);
        dirhams.setY(PLAYER_START_Y + PLAYER_DIRHAMS_START_Y);
        root.getChildren().add(dirhams);
    }
    public void gamePlayingStage(Player[] currentPlayers){
        // [DEBUG] md 我说为什么如果不点player不管怎么初始化都固定为true，坑到我了
//        playerP.player.setIsplaying(true);
//        playerC.player.setIsplaying(true);
//        playerY.player.setIsplaying(true);
//        playerR.player.setIsplaying(true);
        String currentGame = getCurrentGame();
        for (Player currentPlayer : currentPlayers){
            System.out.println("++++++++++");
            rotatePhase(currentPlayer);
            movePhase(currentPlayer);
            placementPhase(currentPlayer);
            System.out.println("xinde"+ getCurrentGame());
            if (isGameOver(currentGame)){
                System.out.println("GameOver");
                break;
            }
        }
        System.out.println(getWinner(currentGame));

        //循环player,因为player还没写好所以先注释

//        TextField textField = new TextField();
//        textField.setLayoutX(50);
//        textField.setLayoutY(50);
//        textField.setText("player number 2-4");
//     textField.toFront();
//        root.getChildren().add(textField);
//        String S=textField.getText();
//        if(S.charAt(0)==2)players=new Player[]{playerC.player,playerY.player};
//        if(S.charAt(0)==3)players=new Player[]{playerC.player,playerY.player,playerP.player};
//        if(S.charAt(0)==4)players=new Player[]{playerC.player,playerY.player,playerP.player,playerR.player};
//        else players=new Player[]{playerC.player};
//        System.out.println("player number "+S.charAt(0));
//        String S=new String();
//        Player currentPlayer;
//        int u=0;
//        if(players!=null&&players.length!=0) {
//            while (!isGameOver(S)) {
//                currentPlayer = players[u % 3];
//                System.out.println(currentPlayer);
//                //玩游戏
//                u++;
//
//            }//isGameOver()
//       }

    }
    private String getCurrentGame() {
        return players[0].toString() + players[1]+ players[2]+players[3]+assamEntity+"B"+board.toString();
    }

    private boolean rotateComfirmed = false;
    private void rotatePhase(Player currentPlayer){
        rotateComfirmed = false;
        assamEntity.imageView.setFocusTraversable(true);
        assamEntity.imageView.requestFocus();
        assamEntity.imageView.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP: assamEntity.setDirection(Direction.NORTH);
                case RIGHT: assamEntity.setDirection(Direction.EAST);
                case DOWN: assamEntity.setDirection(Direction.SOUTH);
                case LEFT: assamEntity.setDirection(Direction.WEST);

                case ENTER:
                    rotateComfirmed = true;
                    break;
            }
            if (rotateComfirmed){
                assamEntity.imageView.setOnKeyPressed(null);
                movePhase(currentPlayer);
            }
        });}

    DiceEntity diceEntity;
    private void movePhase(Player currentPlayer){
        // DICE
        if (diceEntity == null){
            diceEntity = new DiceEntity(DICE_START_X, DICE_START_Y);
        }
        diceEntity.setClickable(true);
        if (board.getColorByCoordinate(assamEntity.x, assamEntity.y) != pieceColor.NONE) {
            int payment = getPaymentAmount(getCurrentGame());
            currentPlayer.setDirhams(currentPlayer.getDirhams() - payment);
            for (Player player : players){
                if (board.getColorByCoordinate(assamEntity.x, assamEntity.y) == player.getColor()){
                    player.setDirhams(player.getDirhams() + payment);
                    break;
                }
            }
        }
        System.out.println("pay"+getCurrentGame());
        placementPhase(currentPlayer);
    }
    private void placementPhase(Player currentPlayer) {


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

     public class AssamEntity{
        int x;
        int y;
        Direction direction;
        public ImageView imageView;
        public Assam assam;
        public AssamEntity(int x,int y, Direction direction){
            this.x = x;
            this.y = y;
            this.direction=direction;
            this.imageView=new ImageView(new Image("comp1110/ass2/assets/pointer.png",
                    NODE_SIZE, NODE_SIZE, false, false));
            imageView.setX(BOARD_START_X + x * NODE_OUTER_BOUND_SIZE);
            imageView.setY(BOARD_START_Y + y * NODE_OUTER_BOUND_SIZE);
            assam = new Assam(x, y, direction);
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
             this.direction = direction;
             assam.setDirection(direction);
             if (direction == Direction.NORTH) imageView.setRotate(0);
             if (direction == Direction.EAST) imageView.setRotate(90);
             if (direction == Direction.SOUTH) imageView.setRotate(180);
             if (direction == Direction.WEST) imageView.setRotate(270);
         }

         @Override
         public String toString() {
             return assam.toString();
         }
     }

    private void initAssam() {
        assamEntity = new AssamEntity(3, 3, Direction.NORTH);
        root.getChildren().add(assamEntity.imageView);
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
