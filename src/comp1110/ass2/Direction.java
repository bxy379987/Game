package comp1110.ass2;

/**
 * Direction for Assam
 */
public enum Direction {
    // Enum constants representing directions: NORTH, EAST, SOUTH, WEST
    NORTH('N', "North" , "he is facing towards the top of the board"),
    EAST('E',"East","he is facing towards the right of the board"),
    SOUTH('S',"South","he is facing towards the bottom of the board"),
    WEST('W',"West", "he is facing towards the left of the board");

    private char symbol;
    private String name;
    private String Description;

    // Constructor to initialize enum constants with their respective symbol, name, and description
    Direction(char symbol, String name, String Description) {
        this.symbol = symbol;
        this.name = name;
        this.Description = Description;
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getDescription(){
        return Description;
    }
    public Direction getOpposite() {
        if (symbol == 'N') return Direction.SOUTH;
        if (symbol == 'E') return Direction.WEST;
        if (symbol == 'S') return Direction.NORTH;
        if (symbol == 'W') return Direction.EAST;
        return null;
    }

    // Static method to obtain a Direction enum from its symbolic characters
    public static Direction fromSymbol (char symbol){
        for (Direction direction : Direction.values()){
            if (direction.symbol == symbol){
                return direction;
            }
        }
        // If an invalid symbol is provided, throw an IllegalArgumentException
        throw new IllegalArgumentException("Invalid symbol: " + symbol);
    }
}
