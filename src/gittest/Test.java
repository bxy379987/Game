//import comp1110.ass2.gui.Game;
//import comp1110.ass2.pieceColor;
//import javafx.scene.Group;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.KeyCode;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//
//static class RugEntity{
//    private final int ID;
//    private final pieceColor color;
//    Group rugGroup;
//    //        ImageView rug;
//    private final Circle firstPart;
//    private final Circle secondPart;
//    //        private Rug rug;
//    public RugEntity(int ID, pieceColor color, double x, double y) {
//        this.ID = ID;
//        this.color = color;
//        // abstract rug
////            this.rug = new Rug(color, ID);
//        rugGroup = new Group();
//        // init Rug patterns
//        Image rugImage = new Image("comp1110/ass2/assets/Rug" +
//                color.getSymbol().toUpperCase() + ".png",
//                NODE_SIZE * 2 + 10, NODE_SIZE, false, false);
//        ImageView rug = new ImageView(rugImage);
//
//        rugGroup.getChildren().add(rug);
//        // init Rug anchors for mapping Board anchors
//        firstPart = new Circle(NODE_SIZE * 0.5, NODE_SIZE * 0.5,
//                NODE_SIZE * 0.5);
//        secondPart = new Circle( NODE_SIZE * 1.5 + 10, NODE_SIZE * 0.5,
//                NODE_SIZE * 0.5);
//        if (!DEBUG) {
//            firstPart.setFill(Color.TRANSPARENT);
//            secondPart.setFill(Color.TRANSPARENT);
//        }
//        rugGroup.getChildren().add(firstPart);
//        rugGroup.getChildren().add(secondPart);
//        // set Group coordinates
//        rugGroup.setLayoutX(x);
//        rugGroup.setLayoutY(y);
//        root.getChildren().add(rugGroup);
//    }
//
//    public Circle getFirstPart() {
//        return firstPart;
//    }
//
//    public Circle getSecondPart() {
//        return secondPart;
//    }
//
//    public int getID() {
//        return ID;
//    }
//
//    public pieceColor getColor() {
//        return color;
//    }
//
//
//    /**
//     * Init entity with coordinates nad tag
//     * @param color
//     * @param x
//     * @param y
//     */
//    public DraggableRugEntity(int ID , pieceColor color, double x, double y) {
//        super(ID, color, x, y);
//        // mouse pressed: set init states & check keyboard rotate
//        rugGroup.setOnMousePressed(event -> {
//            mouseX = event.getSceneX();
//            mouseY = event.getSceneY();
//            // set original states
//            originX = rugGroup.getLayoutX();
//            originY = rugGroup.getLayoutY();
//            originRotate = rugGroup.getRotate();
//
//            rugGroup.toFront();
//            getFirstPart().toFront();
//            getSecondPart().toFront();
//
//            isMousePressed = true;
//            // CASE: rotate
//            scene.setOnKeyPressed(keyEvent -> {
//                if (isMousePressed && keyEvent.getCode() == KeyCode.E) {
//                    rugGroup.setRotate(rugGroup.getRotate() + 90);
//                    findNearest(rugGroup);
//                }
//            });
//        });
//        // when entity was dragged: check nearest mapping anchors and log
//        rugGroup.setOnMouseDragged(event -> {
//            isMouseDragged = true;
//            double deltaX = event.getSceneX() - mouseX;
//            double deltaY = event.getSceneY() - mouseY;
//            rugGroup.setLayoutX(rugGroup.getLayoutX() + deltaX);
//            rugGroup.setLayoutY(rugGroup.getLayoutY() + deltaY);
//            mouseX = event.getSceneX();
//            mouseY = event.getSceneY();
//            findNearest(rugGroup);
//        });
//        // when anchors valid, stick entity to anchors
//        rugGroup.setOnMouseReleased(event -> {
//            isMousePressed = false;
//            if (!isMouseDragged) findNearest(rugGroup);
//            // if anchors valid
//            if (Math.abs(nearestDistance[1] - nearestDistance[0]) < 1e-7
//                    && nearestDistance[0] < MAX_ANCHOR_DISTANCE) {
//                rugGroup.setLayoutX(rugGroup.getLayoutX() + nearestTrans[0]);
//                rugGroup.setLayoutY(rugGroup.getLayoutY() + nearestTrans[1]);
//            } else {
//                rugGroup.setLayoutX(originX);
//                rugGroup.setLayoutY(originY);
//                rugGroup.setRotate(originRotate);
//            }
//
//            // reset anchors
//            isMouseDragged = false;
//            for (Circle circle : nearest) {
//                circle.setFill(Color.BLACK);
//            }
//            nearest = null;
//        });
//    }
//}
