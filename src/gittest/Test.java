package gittest;

import comp1110.ass2.Assam;
import comp1110.ass2.Rug;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
//    public static boolean isPlacementValid() {
//    String gameState=new String("A50ESJDFBSEJKJKDCA20W");
//    String regex="A[0-9]{2}[A-Z]";
//    Pattern pattern=Pattern.compile(regex);
//    Matcher matcher = pattern.matcher(gameState);
//   while(matcher.find()) {
//       System.out.println(matcher.group(0));
//
//   }
//   return matcher.find();
//}
//
//    public static void main(String[] args) {
//        isPlacementValid();
//        Rug rug=new Rug("p014445");
//        System.out.println(rug.getColor());
//    }
public static boolean NextTo(int[]array1,int[]array2){
    if(array1[0]==array2[0]&&array1[1]==(array2[1]-1))return true;
    else if(array1[0]==array2[0]&&array1[1]==(array2[1]+1))return true;
    else if(array1[1]==array2[1]&&array1[0]==(array2[0]+1))return true;
    else if(array1[1]==array2[1]&&array1[0]==(array2[1]-1))return true;
    else return false;
}

    //写一个判断相邻的方法

    public static boolean isPlacementValid() {
        String gameState="Pc03014iPy03015iA60WBn00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00c01c01n00n00n00n00";
        String rug="y016261";
        String regex="A[0-9]{2}[A-Z]";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher = pattern.matcher(gameState);
        while(matcher.find()){
            Assam assam=new Assam(matcher.group(0));
            int x=assam.getxCoordinate();
            int y=assam.getyCoordinate();
            int[] acoor=new int[2];
            int[] rug1;
            int[] rug2;
            acoor[0]=x;
            acoor[1]=y;
            Rug rugRug=new Rug(rug);//抽象命名哈哈哈哈.请问每个rug分别代表什么
            rug1=rugRug.getCoordination1();
            rug2=rugRug.getCoordination2();
            if(!NextTo(rug1,acoor)&&!NextTo(rug2,acoor))return false;
            else if (NextTo(rug1,acoor)&&rug2[0]==acoor[0]&&acoor[1]==rug2[1]) return false;
            else if (NextTo(rug2,acoor)&&rug1[0]==acoor[0]&&acoor[1]==rug1[1]) return false;

            String substringGameState=gameState.substring(21) ;
            int substringGameStatelLength=substringGameState.length();//is 49^3
            int substringcount=substringGameStatelLength/3;//is 49
            String[] substringGameStatearray=new String[substringcount];
            for (int i = 0; i < substringcount; i++) {
                int startIndex = i * 3;
                int endIndex = startIndex + 3;
                substringGameStatearray[i] = substringGameState.substring(startIndex, endIndex);
            }
            int CoordinationIndex1ofRug=rug1[0]*7+rug1[1];
            int CoordinationIndex2ofRug=rug2[0]*7+rug2[1];
            String subrug=rug.substring(0,3);
            if(!(substringGameStatearray[CoordinationIndex1ofRug].equals("n00"))
                    &&!(substringGameStatearray[CoordinationIndex2ofRug].equals("n00"))&&
                    substringGameStatearray[CoordinationIndex1ofRug]==substringGameStatearray[CoordinationIndex2ofRug]){
                return false;
            }
            //判断覆盖真的很难写
            else return true;


        }
        return false;//该方法需要和isRugvalid配合使用,但是不能一起使用,在isPlacementValid方法中传入的 string gamestate已经将string rug放置在
        //其中了,而isRugvalid方法中传入的gamestate和rug则需要判断rug是否被gamestate包含,被包含则返回false


        // FIXME: Task 10

    }

    public static void main(String[] args) {
        isPlacementValid();
    }

}
