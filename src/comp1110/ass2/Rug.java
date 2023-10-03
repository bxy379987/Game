package comp1110.ass2;

public class Rug {

    private boolean isCovered = false;
    private pieceColor color;//"c"表示青色，"y"表示黄色，"r"表示红色，"p"表示紫色 if empty then 'w'(white);
    private int ID;//If it is covered, what is the ID of the rug which is occupying it. And if the STATU is empty, ID=00.
    private int[] firstCoordinate;//And if the STATU is empty, Coordination=[0,0]
    private int[] secondCoordinate;

    public Rug() {}
    public Rug(String input) {
        this.color = pieceColor.fromSymbol(input.charAt(0)+"");
        this.ID = Integer.parseInt(input.substring(1,3));
        char[] firstCoordinates = input.substring(3,5).toCharArray();
        char[] secondCoordinates = input.substring(5).toCharArray();
        this.firstCoordinate = new int[2];
        this.secondCoordinate = new int[2];
        this.firstCoordinate[0] = firstCoordinates[0] - '0';
        this.firstCoordinate[1] = firstCoordinates[1] - '0';
        this.secondCoordinate[0] = secondCoordinates[0] - '0';
        this.secondCoordinate[1] = secondCoordinates[1] - '0';
    }//将一个Rug string转化为一个Rug对象

    public Rug(pieceColor color, int id) {
        this.color = color;
        this.ID = id;
    }

    public boolean isCovered() {
        return isCovered;
    }

    public void setCovered(boolean covered) {
        isCovered = covered;
    }

    public pieceColor getColor() {
        return color;
    }

    public void setColor(pieceColor color) {
        this.color = color;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int[] getFirstCoordinate() {
        return firstCoordinate;
    }

    public void setFirstCoordinate(int[] firstCoordinate) {
        this.firstCoordinate = firstCoordinate;
    }

    public int[] getSecondCoordinate() {
        return secondCoordinate;
    }

    public void setSecondCoordinate(int[] secondCoordinate) {
        this.secondCoordinate = secondCoordinate;
    }

    /**
     * Rotate rug with different center provided by [x, y]
     * @param x
     * @param y
     */
    public void rotateClockwise(int x, int y) {
        // TODO: Rotate rug with different center provided by [x, y]
    }

    @Override
    public String toString() {
        return color.getSymbol() + String.format("%02d", ID) + firstCoordinate[0] + firstCoordinate[1] + secondCoordinate[0] + secondCoordinate[1];
    }
}
