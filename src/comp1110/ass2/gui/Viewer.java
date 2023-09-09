package comp1110.ass2.gui;

import comp1110.ass2.Assam;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;


public class Viewer extends Application {
    @FXML
    public Label label1;
    public Label label2;
    public Label label3;
    public Label label4;
    public Label label5;
    public Label label6;
    public Label label7;
    public Label label8;
    public Label label9;
    public Label label10;
    public Label label11;
    public Label label12;
    public Label label13;








    private static final int VIEWER_WIDTH = 1200;
    private static final int VIEWER_HEIGHT = 700;
    private final Group root = new Group();
    private final Group controls = new Group();
    private TextField boardTextField;
    // set initial x y of board
    private static final int BOARD_START_X = 100;
    private static final int BOARD_START_Y = 100;
    private static final int BOARD_INDEX_WIDTH = 7;
    private static final int BOARD_INDEX_HEIGHT = 7;
    private static final int NODE_SIZE = 50;


    /**
     * Draw a placement in the window, removing any previously drawn placements
     *
     * @param state an array of two strings, representing the current game state
     */
    void displayState(String state)  {


        System.out.println(state);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("viewer.fxml"));
        Viewer controller=loader.getController();
        PlayerPattern player1=new PlayerPattern(state.substring(0,8));
        PlayerPattern player2=new PlayerPattern(state.substring(8,16));
        PlayerPattern player3=new PlayerPattern(state.substring(16,24));
        PlayerPattern player4=new PlayerPattern(state.substring(24,32));
        Assam aassam=new Assam(state.substring(32,36));

        if(controller!=null){controller.label1.setText(player1.isIsplaying()+"");
        controller.label2.setText(player2.isIsplaying()+"");
        controller.label3.setText(player3.isIsplaying()+"");
        controller.label4.setText(player4.isIsplaying()+"");
        controller.label5.setText(player1.getDirhams()+"");
        controller.label6.setText(player2.getDirhams()+"");
        controller.label7.setText(player3.getDirhams()+"");
        controller.label8.setText(player4.getDirhams()+"");
        controller.label9.setText(player1.getRemainingRugs()+"");
        controller.label10.setText(player2.getRemainingRugs()+"");
        controller.label11.setText(player3.getRemainingRugs()+"");
        controller.label12.setText(player4.getRemainingRugs()+"");
        controller.label13.setText("assam direction and location"+aassam.getDirection()+aassam.getxCoordinate()+aassam.getyCoordinate());}else
            System.out.println(1);
        try {
            root.getChildren().addAll((Node) loader.load());
        } catch (IOException ignored) {
        }


        System.out.println("=============[displayState]=============");
        System.out.println(state);
        String boardState = state.split("B")[1];
        System.out.println(boardState);
        String[] playersState = state.split("B")[0].split("A")[0].split("P");
//        System.out.println(playersState[0]);

        // PLAYERS
        for (String player : playersState) {
            if (player.equals("")) continue;
            System.out.println(player);
        }

        // BOARD
        Rectangle[] board = new Rectangle[BOARD_INDEX_WIDTH * BOARD_INDEX_HEIGHT];
        for (int row = 0; row < BOARD_INDEX_WIDTH; row++) {
            for (int col = 0; col < BOARD_INDEX_HEIGHT; col++) {
                char color = boardState.toCharArray()[(row * BOARD_INDEX_HEIGHT + col) * 3];
                System.out.print(color + " ");
                Rectangle node = new Rectangle(NODE_SIZE * 0.9, NODE_SIZE * 0.9);
                // set node position
                // [DEBUG] swap the col & row coordinates to correct board
                node.setLayoutX(BOARD_START_X + col * NODE_SIZE);
                node.setLayoutY(BOARD_START_Y + row * NODE_SIZE);
                // set node color
                node.setFill(Color.WHITE);
                switch (color) {
                    case 'c' -> node.setFill(Color.CYAN);
                    case 'y' -> node.setFill(Color.YELLOW);
                    case 'p' -> node.setFill(Color.PURPLE);
                    case 'r' -> node.setFill(Color.RED);
                }
                // add node
                board[row * BOARD_INDEX_HEIGHT + col] = node;
            }
            System.out.println();
        }

        root.getChildren().addAll(board);

        // ASSAM
        char[] coordinates = state.split("B")[0].split("A")[1].toCharArray();
        Image pointer = new Image("comp1110/ass2/assets/pointer.png", false);
        ImageView assam = new ImageView(pointer);
        assam.setFitWidth(NODE_SIZE*0.9);
        assam.setFitHeight(NODE_SIZE*0.9);
        assam.setX(BOARD_START_X + (coordinates[1] - '0') * NODE_SIZE);
        assam.setY(BOARD_START_Y + (coordinates[0] - '0') * NODE_SIZE);
        switch (coordinates[2]) {
            case 'E' -> assam.setRotate(90);
            case 'S' -> assam.setRotate(180);
            case 'W' -> assam.setRotate(270);
        }
        root.getChildren().add(assam);



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
                displayState(boardTextField.getText());
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

        makeControls();

        scene.getStylesheets().add("styles.css");

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
