## Code Review

Reviewed by: <Xiangyu Bao>, <u7751023>

Reviewing code written by: <Yusi Zhong> <u7755061>

Component: <void displayState(String state) throws IOException {
String boardState = state.split("B")[1]; // Extract the board string

        // Display the player information, board and Assam
        displayPlayers(Player.fromGameString(state));
        displayBoard(new Board(boardState));
        displayAssam(new Assam(state));
        // [DEBUG] Test get next assam via tunnel
        System.out.println(new Board(boardState).getAssamViaTunnel(new Assam(state)).toString());
    }

    // Display the board based on the provided board object
    public void displayBoard(Board board){
        pieceColor[][] boardcolors = board.getBoardColor();
        for (int col = 0; col < BOARD_INDEX_WIDTH; col++) {
            for (int row = 0; row < BOARD_INDEX_HEIGHT; row++) {
                Rectangle node = new Rectangle(NODE_SIZE * 0.9, NODE_SIZE * 0.9);
                // set node position
                // [DEBUG] swap the col & row coordinates to correct board
                node.setLayoutX(BOARD_START_X + col * NODE_SIZE);
                node.setLayoutY(BOARD_START_Y + row * NODE_SIZE);
                // set node color
                node.setFill(Color.WHITE);
                switch (boardcolors[col][row]) {
                    case CYAN -> node.setFill(Color.CYAN);
                    case YELLOW -> node.setFill(Color.YELLOW);
                    case PURPLE -> node.setFill(Color.PURPLE);
                    case RED -> node.setFill(Color.RED);
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
    public void displayPlayers(Player[] players){
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
        for (Player player : players) {
            String playerInfo = player.getColor() +
                    "\t\t\t\t\t\t" + player.getDirhams() +
                    "\t\t\t\t\t\t" + player.getRemainingRugs() +
                    "\t\t\t\t\t\t" + player.isIsplaying();
            Label playerLabel = new Label(playerInfo);

            // Set the color of the player information label based on the player's color
            switch (player.getColor()){
                case CYAN -> playerLabel.setTextFill(Color.CYAN);
                case YELLOW -> playerLabel.setTextFill(Color.YELLOW);
                case PURPLE -> playerLabel.setTextFill(Color.PURPLE);
                case RED -> playerLabel.setTextFill(Color.RED);
            }

            // Set the position of the player information label
            playerLabel.setLayoutX(PLAYER_INFORMATION_X);
            playerLabel.setLayoutY(startY + 20);
            textGroup.getChildren().add(playerLabel);
            startY += 60 ;
        }

    }
>

### Comments 

1 Best Features of the Code

Modularity: The code is modular, 

with separate methods (displayBoard, displayAssam, displayPlayers) 

handling distinct functionalities.

Clarity: The code is relatively clear, 

with function and variable names 

that are descriptive enough to understand their roles.

great encapsulation

there is no need to know the underlying of the three display methods

2 Documentation:

Partially Documented: The code provides some comments that describe blocks of code or important lines, 

which is helpful. 

it lacks JavaDoc comments (I don't like that as well)

3 Program Decomposition:

Appropriate Decomposition: The methods appear to be logically 

separated based on functionality. 

great encapsulation 

4 Potential Errors:

If the state doesn't contain the "B" character,

the split method in displayState will 

cause an ArrayIndexOutOfBoundsException.





