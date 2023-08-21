package comp1110.ass2;

/**
 * Direction for Assam
 */
public enum Direction {
    North(0, "N"),
    East(1,"E"),
    South(2, "S"),
    West(3, "W")
    ;

    int number;
    String name;

    Direction(int i, String n) {
        this.number = i;
        this.name = n;
    }

    @Override
    public String toString() {
        return name;
    }
}
