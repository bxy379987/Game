package comp1110.ass2;

public enum pieceColor {
    // Enum constants representing colors: CYAN, YELLOW, PURPLE, RED
    CYAN("c", "Cyan" ),
    YELLOW("y","Yellow"),
    PURPLE("p","Purple"),
    RED("r","Red"),
    NONE("n","None"),
    OUT("","OutOfBound");

    private String symbol;
    private String colorName;

    // Constructor to initialize enum constants with their respective symbol and name
    pieceColor(String symbol, String colorName) {
        this.symbol = symbol;
        this.colorName = colorName;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return colorName;
    }


    // Static method to obtain a Color enum from its symbolic characters
    public static pieceColor fromSymbol (String symbol){
        for (pieceColor color : pieceColor.values()){
            if (color.symbol.equals(symbol) ){
                return color;
            }
        }
        // If an invalid symbol is provided, throw an IllegalArgumentException
        throw new IllegalArgumentException("Invalid symbol: " + symbol);
    }

}
