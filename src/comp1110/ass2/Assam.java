package comp1110.ass2;

public class Assam {

//    When Assam is facing N, he is facing towards the top of the board
//    When Assam is facing E, he is facing towards the right of the board
//    When Assam is facing S, he is facing towards the bottom of the board
//    When Assam is facing W, he is facing towards the left of the board
  private int xCoordinate;
    private int yCoordinate;
    private char direction;
    // 不用字符储存的方式: enum
//    private Direction direction;

    public Assam(String input) {
        this.xCoordinate = (input.charAt(1));//char int transform automatically
        this.yCoordinate = (input.charAt(2));
        this.direction = (input.charAt(3));//老师说不要用字符存储方向,但是我不知道怎么弄
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

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }
}
