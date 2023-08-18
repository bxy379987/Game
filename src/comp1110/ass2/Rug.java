package comp1110.ass2;

public class Rug {



    private boolean isCovered;
    private char color;
    private int ID;//If it is covered, what is the ID of the rug which is occupying it. And if the STATU is empty, ID=00.
    private int[] Coordination1;//And if the STATU is empty, Coordination=[0,0]
    private int[] Coordination2;


    public Rug(boolean isCovered, char color, int ID, int[] coordination1, int[] coordination2) {
        this.isCovered = isCovered;
        this.color = color;
        this.ID = ID;
        Coordination1 = coordination1;
        Coordination2 = coordination2;
    }

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

    public int[] getCoordination1() {
        return Coordination1;
    }

    public void setCoordination1(int[] coordination1) {
        Coordination1 = coordination1;
    }

    public int[] getCoordination2() {
        return Coordination2;
    }

    public void setCoordination2(int[] coordination2) {
        Coordination2 = coordination2;
    }
}
