package comp1110.ass2;

public class Assam {
    // Coordinates of Assam on the board
    private int xCoordinate;
    private int yCoordinate;

    // Direction Assam is facing
    // When Assam is facing N, he is facing towards the top of the board
    // When Assam is facing E, he is facing towards the right of the board
    // When Assam is facing S, he is facing towards the bottom of the board
    // When Assam is facing W, he is facing towards the left of the board
    private Direction direction;

    public Assam () {

    }
    // Constructor to initialize Assam with coordinates and direction
    public Assam (int xCoordinate, int yCoordinate, Direction direction){
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.direction = direction;
    }

    public Assam (String gameString){
        Assam assam = fromGameString(gameString);
        this.xCoordinate = assam.xCoordinate;
        this.yCoordinate = assam.yCoordinate;
        this.direction = assam.direction;
    }

    /**
     *  Parse Assam information from an Assam string representation
     *
     * @param assamString Assam string contains 4 characters. e.g."A51W"
     * @return An Assam object created from the given assam string
     */
    public static Assam fromString(String assamString) {
        // Validate the length of the string
        if (assamString.length() != 4){
            throw new IllegalArgumentException("Invalid Assam String: " + assamString);
        }
        else {
            // Extract x and y coordinates from the string
            // [DEBUG] swap the col & row coordinates to correct board in Viewer
            int xCoordinate = (assamString.charAt(1) - '0');//char int transform automatically
            int yCoordinate = (assamString.charAt(2) - '0');

            // Parse direction using the Direction enum
            Direction direction = Direction.fromSymbol(assamString.charAt(3));

            // Create a new Assam object with the parsed values
            return new Assam(xCoordinate, yCoordinate, direction);
        }
    }

    /**
     *  Parse Assam information from a game state string representation
     *
     * @param gameString gameString contains different parts of objects
     * @return An Assam object created from the given gameString string
     */
    public static Assam fromGameString(String gameString){
        // Extract Assam string from the game string
        String assamString = gameString.substring(32,36);

        // Parse Assam information using fromString method
        return fromString(assamString);
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "A"+xCoordinate+yCoordinate+direction.getSymbol();
    }
}
