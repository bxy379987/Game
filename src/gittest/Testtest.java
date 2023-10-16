package gittest;

import comp1110.ass2.Assam;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//
public class Testtest {
//    public  static int  findsamecolor(char color,String[]substringGameStatearray,int[]Assamcoordination){
//        int count=0;
//        int Sortnumber=Assamcoordination[0]*7+Assamcoordination[1];
//        if(substringGameStatearray[Sortnumber-1].charAt(0)==color&&Sortnumber%7!=0){
//            count++;
//            int[] AssamcoordinationLeft=new int[2];
//            AssamcoordinationLeft[0]=Assamcoordination[0]-1;
//            AssamcoordinationLeft[1]=Assamcoordination[1];
//            findsamecolor(color,substringGameStatearray,AssamcoordinationLeft);
//        }
//        if(substringGameStatearray[Sortnumber+1].charAt(0)==color&&(Sortnumber+1)%7!=0){
//            count++;
//            int[] AssamcoordinationRight=new int[2];
//            AssamcoordinationRight[0]=Assamcoordination[0]+1;
//            AssamcoordinationRight[1]=Assamcoordination[1];
//            findsamecolor(color,substringGameStatearray,AssamcoordinationRight);
//        }
//        if(Sortnumber>6&&substringGameStatearray[Sortnumber-7].charAt(0)==color){
//            count++;
//            int[] AssamcoordinationTop=new int[2];
//            AssamcoordinationTop[0]=Assamcoordination[0];
//            AssamcoordinationTop[1]=Assamcoordination[1]-1;
//            findsamecolor(color,substringGameStatearray,AssamcoordinationTop);
//        }
//        if(Sortnumber<49&&substringGameStatearray[Sortnumber+7].charAt(0)==color){
//            count++;
//            int[] AssamcoordinationBottom=new int[2];
//            AssamcoordinationBottom[0]=Assamcoordination[0];
//            AssamcoordinationBottom[1]=Assamcoordination[1]+1;
//            findsamecolor(color,substringGameStatearray,AssamcoordinationBottom);
//        }
//        return count;
//    }
//    public static int getPaymentAmount() {
//        String gameString="Pc02813iPy03214iA55NBn00n00n00n00n00n00c01n00n00n00n00n00n00y01y01n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00y02n00n00n00n00n00n00y02n00";
//        String substringGameState = gameString.substring(21);
//        int substringGameStatelLength = substringGameState.length();//is 49^3
//        int substringcount = substringGameStatelLength / 3;//is 49
//        String[] substringGameStatearray = new String[substringcount];
//        for (int i = 0; i < substringcount; i++) {
//            int startIndex = i * 3;
//            int endIndex = startIndex + 3;
//            substringGameStatearray[i] = substringGameState.substring(startIndex, endIndex);
//        }
//        String regex = "A[0-9]{2}[A-Z]";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(gameString);
//        matcher.find();
//        Assam assam = new Assam(matcher.group(0));
//        int x = assam.getxCoordinate();
//        int y = assam.getyCoordinate();
//        int[] acoor = new int[2];
//        acoor[0] = x;
//        acoor[1] = y;
//        char ColorofAssamRug=substringGameStatearray[acoor[0]*7+acoor[1]].charAt(0);
//        int count=findsamecolor(ColorofAssamRug,substringGameStatearray,acoor);
//
//        // FIXME: Task 11
//        return count;
    }
//    public static void main(String[] args) {
//
//        System.out.println(getPaymentAmount());
//    }
//}
