package comp1110.ass2.gui;

import comp1110.ass2.Assam;
import comp1110.ass2.Board;
import comp1110.ass2.Direction;
import comp1110.ass2.PlayerPattern;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;


public class Viewer extends Application {
//    @FXML
//    public Label label1;
//    public Label label2;
//    public Label label3;
//    public Label label4;
//    public Label label5;
//    public Label label6;
//    public Label label7;
//    public Label label8;
//    public Label label9;
//    public Label label10;
//    public Label label11;
//    public Label label12;
//    public Label label13;
//    public Label label14;

    private static final int VIEWER_WIDTH = 1200;
    private static final int VIEWER_HEIGHT = 700;
    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group textGroup = new Group();
    private TextField boardTextField;
    //    // set initial x y of board
    private static final int BOARD_START_X = 60;
    private static final int BOARD_START_Y = 70;
    private static final int BOARD_INDEX_WIDTH = 7;
    private static final int BOARD_INDEX_HEIGHT = 7;
    private static final int NODE_SIZE = 70;

    private static final int PLAYER_INFORMATION_X = 630;
    private static final int PLAYER_INFORMATION_SPACE = 100;

    private static final int ASSAM_START_X = 630;
    private static final int ASSAM_START_Y = 500;


    /**
     * Draw a placement in the window, removing any previously drawn placements
     *
     * @param state an array of two strings, representing the current game state
     */
    void displayState(String state) throws IOException {


//        System.out.println(state);
//
//
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("viewer.fxml"));
//        root.getChildren().addAll((Node) loader.load());
//        Viewer controller=loader.getController();
//        PlayerPattern player1=new PlayerPattern(state.substring(0,8));
//        PlayerPattern player2=new PlayerPattern(state.substring(8,16));
//        PlayerPattern player3=new PlayerPattern(state.substring(16,24));
//        PlayerPattern player4=new PlayerPattern(state.substring(24,32));
//        Assam aassam = Assam.fromGameString(state);
//
//        if(controller!=null){controller.label1.setText(player1.isIsplaying()+"");
//        controller.label2.setText(player2.isIsplaying()+"");
//        controller.label3.setText(player3.isIsplaying()+"");
//        controller.label4.setText(player4.isIsplaying()+"");
//        controller.label5.setText(player1.getDirhams()+"");
//        controller.label6.setText(player2.getDirhams()+"");
//        controller.label7.setText(player3.getDirhams()+"");
//        controller.label8.setText(player4.getDirhams()+"");
//        controller.label9.setText(player1.getRemainingRugs()+"");
//        controller.label10.setText(player2.getRemainingRugs()+"");
//        controller.label11.setText(player3.getRemainingRugs()+"");
//        controller.label12.setText(player4.getRemainingRugs()+"");
//        controller.label13.setText(aassam.getxCoordinate()+"  "+aassam.getyCoordinate());
//        controller.label14.setText((aassam.getDirection())+"");
//        }
//
//        else
//            System.out.println(1);
//        try {
//            root.getChildren().addAll((Node) loader.load());
//        } catch (IOException ignored) {
//        }
//

//        System.out.println("=============[displayState]=============");
//        System.out.println(state);
//        String boardState = state.split("B")[1];
//        System.out.println(boardState);
//        String[] playersState = state.split("B")[0].split("A")[0].split("P");
//        System.out.println(playersState[0]);


        String boardState = state.split("B")[1]; // Extract the board string

        // Display the player information, board and Assam
        displayPlayers(PlayerPattern.fromGameString(state));
        displayBoard(new Board(boardState));
        displayAssam(new Assam(state));

    }

    // Display the board based on the provided board object
    public void displayBoard(Board board){
        String[][] boardcolors = board.getBoardColor();
        for (int row = 0; row < BOARD_INDEX_WIDTH; row++) {
            for (int col = 0; col < BOARD_INDEX_HEIGHT; col++) {
                Rectangle node = new Rectangle(NODE_SIZE * 0.9, NODE_SIZE * 0.9);
                // set node position
                // [DEBUG] swap the col & row coordinates to correct board
                node.setLayoutX(BOARD_START_X + col * NODE_SIZE);
                node.setLayoutY(BOARD_START_Y + row * NODE_SIZE);
                // set node color
                node.setFill(Color.WHITE);
                switch (boardcolors[row][col]) {
                    case "c" -> node.setFill(Color.CYAN);
                    case "y" -> node.setFill(Color.YELLOW);
                    case "p" -> node.setFill(Color.PURPLE);
                    case "r" -> node.setFill(Color.RED);
                    }
                // add node
                root.getChildren().addAll(node);
             }
        }
    }

    // Display Assam information based on the provided Assam object
    public void displayAssam(Assam myassam){
        // Extract Assam's coordinates and direction
        int assamCoordinateX = myassam.getxCoordinate();
        int assamCoordinateY = myassam.getyCoordinate();
        Direction assamDirection = myassam.getDirection();

        // Display Assam's image
        Image pointer = new Image("comp1110/ass2/assets/pointer.png", false);
        ImageView assam = new ImageView(pointer);
        // Set the position and rotation of the Assam image based on its coordinates and direction
        assam.setFitWidth(NODE_SIZE*0.9);
        assam.setFitHeight(NODE_SIZE*0.9);
        assam.setX(BOARD_START_X + assamCoordinateX * NODE_SIZE);
        assam.setY(BOARD_START_Y + assamCoordinateY * NODE_SIZE);
        switch (assamDirection) {
            case EAST -> assam.setRotate(90);
            case SOUTH -> assam.setRotate(180);
            case WEST -> assam.setRotate(270);
        }
        // Create a label to display Assam's information
        String assamInfo = "ASSAM INFORMATION \n" +
                "Coordinate:  ( " + assamCoordinateX + ", " + assamCoordinateY + " )\n" +
                "Direction:  " + assamDirection;
        // Set the position of the Assam information label
        Label assamLabel = new Label(assamInfo);
        assamLabel.setLayoutX(ASSAM_START_X);
        assamLabel.setLayoutY(ASSAM_START_Y);
        textGroup.getChildren().add(assamLabel); // Add Assam's information label to the text group
        root.getChildren().add(assam); // Add Assam's image to the scene
    }

    // Display player information based on the provided player object
    public void displayPlayers(PlayerPattern[] playerPatterns){
        textGroup.getChildren().clear(); // Clear previous text to display updated player information
        int startY = 70;

        // Display column titles for player information
        Label titleColor = new Label("COLOR");
        titleColor.setLayoutX(PLAYER_INFORMATION_X);
        titleColor.setLayoutY(startY);
        textGroup.getChildren().add(titleColor);

        Label titleDirhams = new Label("DIRHAMS");
        titleDirhams.setLayoutX(PLAYER_INFORMATION_X + PLAYER_INFORMATION_SPACE);
        titleDirhams.setLayoutY(startY);
        textGroup.getChildren().add(titleDirhams);

        Label titleRemainingRugs = new Label("REMAININGRUGS");
        titleRemainingRugs.setLayoutX(PLAYER_INFORMATION_X + PLAYER_INFORMATION_SPACE * 2);
        titleRemainingRugs.setLayoutY(startY);
        textGroup.getChildren().add(titleRemainingRugs);

        Label titleisIsplaying = new Label("ISPLAYING");
        titleisIsplaying.setLayoutX(PLAYER_INFORMATION_X + PLAYER_INFORMATION_SPACE * 3.5);
        titleisIsplaying.setLayoutY(startY);
        textGroup.getChildren().add(titleisIsplaying);

        // Display each player's information in a row
        for (PlayerPattern playerPattern : playerPatterns) {
            String playerInfo = playerPattern.getColor() +
                    "\t\t\t\t\t\t" + playerPattern.getDirhams() +
                    "\t\t\t\t\t\t" + playerPattern.getRemainingRugs() +
                    "\t\t\t\t\t\t" + playerPattern.isIsplaying();
            Label playerLabel = new Label(playerInfo);

            // Set the color of the player information label based on the player's color
            switch (playerPattern.getColor()){
                case 'c' -> playerLabel.setTextFill(Color.CYAN);
                case 'y' -> playerLabel.setTextFill(Color.YELLOW);
                case 'p' -> playerLabel.setTextFill(Color.PURPLE);
                case 'r' -> playerLabel.setTextFill(Color.RED);
            }

            // Set the position of the player information label
            playerLabel.setLayoutX(PLAYER_INFORMATION_X);
            playerLabel.setLayoutY(startY + 20);
            textGroup.getChildren().add(playerLabel);
            startY += 60 ;
        }

    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label boardLabel = new Label("Game State:");
        boardTextField = new TextField();
        boardTextField.setPrefWidth(800);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    displayState(boardTextField.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(boardLabel,
                boardTextField, button);
        hb.setSpacing(10);
        hb.setLayoutX(50);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Marrakech Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);
        // BACKGROUND
        Rectangle background = new Rectangle(VIEWER_WIDTH, VIEWER_HEIGHT);
        background.setFill(Color.web("F5F5F5"));
        root.getChildren().add(background);

        root.getChildren().add(controls);
        root.getChildren().add(textGroup);
        makeControls();

        scene.getStylesheets().add("styles.css");

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
