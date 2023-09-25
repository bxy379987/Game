package comp1110.ass2;

public class Rug {

    private boolean isCovered = false;
    private char color;//"c"表示青色，"y"表示黄色，"r"表示红色，"p"表示紫色 if empty then 'w'(white);
    private int ID;//If it is covered, what is the ID of the rug which is occupying it. And if the STATU is empty, ID=00.
    private int[] firstCoordinate = new int[2];//And if the STATU is empty, Coordination=[0,0]
    private int[] secondCoordinate = new int[2];


    public Rug(String input) {
        this.color = input.charAt(0);
        this.ID = Integer.parseInt(input.substring(1,3));
        char[] firstCoordinates = input.substring(3,5).toCharArray();
        char[] secondCoordinates = input.substring(5).toCharArray();
        this.firstCoordinate[0] = firstCoordinates[0] - '0';
        this.firstCoordinate[1] = firstCoordinates[1] - '0';
        this.secondCoordinate[0] = secondCoordinates[0] - '0';
        this.secondCoordinate[1] = secondCoordinates[1] - '0';
    }//将一个Rug string转化为一个Rug对象

    public boolean isCovered() {
        return isCovered;
    }

    public void setCovered(boolean covered) {
        isCovered = covered;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
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

    @Override
    public String toString() {
        return color + String.format("%02d", ID) + firstCoordinate[0] + firstCoordinate[1] + secondCoordinate[0] + secondCoordinate[1];
    }
}
