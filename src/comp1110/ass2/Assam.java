package comp1110.ass2;

public class Assam {
    public enum DIRECTION{
        N,S,E,W
    }
//    When Assam is facing N, he is facing towards the top of the board
//    When Assam is facing E, he is facing towards the right of the board
//    When Assam is facing S, he is facing towards the bottom of the board
//    When Assam is facing W, he is facing towards the left of the board
  private int xCoordinate;
    private int yCoordinate;
    private DIRECTION direction;

    public Assam(int xCoordinate, int yCoordinate, DIRECTION direction) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.direction = direction;
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

    public DIRECTION getDirection() {
        return direction;
    }

    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }
}
