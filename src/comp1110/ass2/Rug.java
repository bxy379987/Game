package comp1110.ass2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rug {



    private boolean isCovered=false;
    private char color;//"c"表示青色，"y"表示黄色，"r"表示红色，"p"表示紫色 if empty then 'w'(white);
    private int ID;//If it is covered, what is the ID of the rug which is occupying it. And if the STATU is empty, ID=00.
    private int[] Coordination1;//And if the STATU is empty, Coordination=[0,0]
    private int[] Coordination2;


    public Rug(String input) {
        String regex="^[a-z][0-9]{6}$";
        java.util.regex.Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if(matcher.matches()==true){this.isCovered=true;}
        this.color=input.charAt(0);
        String IDnumberPart = input.substring(1,2);
        this.ID=Integer.parseInt(IDnumberPart);
        String C1numberpart=input.substring(3,2);
        String C2numberpart=input.substring(5,2);
        this.Coordination1[0]=C1numberpart.charAt(0);
        this.Coordination1[1]=C1numberpart.charAt(1);
        this.Coordination2[0]=C2numberpart.charAt(0);
        this.Coordination2[1]=C2numberpart.charAt(1);
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
