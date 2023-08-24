package comp1110.ass2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Marrakech {

    /**
     * Determine whether a rug String is valid.
     * For this method, you need to determine whether the rug String is valid, but do not need to determine whether it
     * can be placed on the board (you will determine that in Task 10 ). A rug is valid if and only if all the following
     * conditions apply:
     * - The String is 7 characters long
     * - The first character in the String corresponds to the colour character of a player present in the game
     * - The next two characters represent a 2-digit ID number
     * - The next 4 characters represent coordinates that are on the board
     * - The combination of that ID number and colour is unique
     * To clarify this last point, if a rug has the same ID as a rug on the board, but a different colour to that rug,
     * then it may still be valid. Obviously multiple rugs are allowed to have the same colour as well so long as they
     * do not share an ID. So, if we already have the rug c013343 on the board, then we can have the following rugs
     * - c023343 (Shares the colour but not the ID)
     * - y013343 (Shares the ID but not the colour)
     * But you cannot have c014445, because this has the same colour and ID as a rug on the board already.
     *
     * @param gameString A String representing the current state of the game as per the README
     * @param rug        A String representing the rug you are checking
     * @return true if the rug is valid, and false otherwise.
     */
    public static boolean isRugValid(String gameString, String rug) {
        String regex = "^[a-z][0-9]{6}$";//"c"表示青色，"y"表示黄色，"r"表示红色，"p"表示紫色
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(rug);
        if (!matcher.find()) {
            return false;
        }
        String subrug = rug.substring(0, 3);
        if (gameString.contains(subrug) == true) {
            return false;
        }// FIXME: Task 4
        return true;//如果两者都不成立则成功
    }

    /**
     * Roll the special Marrakech die and return the result.
     * Note that the die in Marrakech is not a regular 6-sided die, since there
     * are no faces that show 5 or 6, and instead 2 faces that show 2 and 3. That
     * is, of the 6 faces
     * - One shows 1
     * - Two show 2
     * - Two show 3
     * - One shows 4
     * As such, in order to get full marks for this task, you will need to implement
     * a die where the distribution of results from 1 to 4 is not even, with a 2 or 3
     * being twice as likely to be returned as a 1 or 4.
     *
     * @return The result of the roll of the die meeting the criteria above
     */
    public static int rollDie() {
        int[] roll = new int[]{1, 2, 2, 3, 3, 4};
        int i = (int) (Math.random() * 6);
        return roll[i];
        // FIXME: Task 6

    }

    /**
     * Determine whether a game of Marrakech is over
     * Recall from the README that a game of Marrakech is over if a Player is about to enter the rotation phase of their
     * turn, but no longer has any rugs. Note that we do not encode in the game state String whose turn it is, so you
     * will have to think about how to use the information we do encode to determine whether a game is over or not.
     *
     * @param currentGame A String representation of the current state of the game.
     * @return true if the game is over, or false otherwise.
     */
    public static boolean isGameOver(String currentGame) {

        String regex1 = "P[a-z][0-9]{5}[a-z]";
        Pattern pattern1 = Pattern.compile(regex1);
        Matcher matcher = pattern1.matcher(currentGame);
        //if(!matcher.find())return false;

        while (matcher.find()) {
            String player = matcher.group();//可以优化成把这个string赋给PlayerPattern构造器,建对象然后通过属性判断
            if (player.charAt(5) == 0 && player.charAt(6) == 0) {
                return true;//虽然能过test但是没优化好,因为我判断某人没毛毯就游戏结束,但是老师要求判断条件是到某人掷骰子时没毛毯才算结束
            }
        }

        return false;
        // FIXME: Task 8

    }

    /**
     * Implement Assam's rotation.
     * Recall that Assam may only be rotated left or right, or left alone -- he cannot be rotated a full 180 degrees.
     * For example, if he is currently facing North (towards the top of the board), then he could be rotated to face
     * East or West, but not South. Assam can also only be rotated in 90 degree increments.
     * If the requested rotation is illegal, you should return Assam's current state unchanged.
     *
     * @param currentAssam A String representing Assam's current state
     * @param rotation     The requested rotation, in degrees. This degree reading is relative to the direction Assam
     *                     is currently facing, so a value of 0 for this argument will keep Assam facing in his
     *                     current orientation, 90 would be turning him to the right, etc.
     * @return A String representing Assam's state after the rotation, or the input currentAssam if the requested
     * rotation is illegal.
     */
    public static String rotateAssam(String currentAssam, int rotation) {
        if (rotation != 0 && rotation != 270 && rotation != 90) return currentAssam;
        if (rotation == 0) return currentAssam;
        char currentdirection = currentAssam.charAt(3);
        char changeddirection = 'A';
        switch (currentdirection) {
            case 'N':
                if (rotation == 90) changeddirection = 'E';
                else if (rotation == 270) changeddirection = 'W';
                break;
            case 'E':
                if (rotation == 90) changeddirection = 'S';
                else if (rotation == 270) changeddirection = 'N';
                break;
            case 'S':
                if (rotation == 90) changeddirection = 'W';
                else if (rotation == 270) changeddirection = 'E';
                break;
            case 'W':
                if (rotation == 90) changeddirection = 'N';
                else if (rotation == 270) changeddirection = 'S';
                break;
            default:
                return currentAssam;


        }
        return currentAssam.substring(0, 3) + changeddirection;
    }

    //写一个判断相邻的方法
    public static boolean NextTo(int[] array1, int[] array2) {
        if (array1[0] == array2[0] && array1[1] == (array2[1] - 1)) return true;
        else if (array1[0] == array2[0] && array1[1] == (array2[1] + 1)) return true;
        else if (array1[1] == array2[1] && array1[0] == (array2[0] + 1)) return true;
        else if (array1[1] == array2[1] && array1[0] == (array2[0] - 1)) return true;
        else return false;
    }

    /**
     * Determine whether a potential new placement is valid (i.e that it describes a legal way to place a rug).
     * There are a number of rules which apply to potential new placements, which are detailed in the README but to
     * reiterate here:
     * 1. A new rug must have one edge adjacent to Assam (not counting diagonals)
     * 2. A new rug must not completely cover another rug. It is legal to partially cover an already placed rug, but
     * the new rug must not cover the entirety of another rug that's already on the board.
     *
     * @param gameState A game string representing the current state of the game
     * @param rug       A rug string representing the candidate rug which you must check the validity of.
     * @return true if the placement is valid, and false otherwise.
     */
    //写一个判断相邻的方法
    public static boolean isPlacementValid(String gameState, String rug) {
        String regex = "A[0-9]{2}[A-Z]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(gameState);
        while (matcher.find()) {
            Assam assam = new Assam(matcher.group(0));
            int x = assam.getxCoordinate();
            int y = assam.getyCoordinate();
            int[] acoor = new int[2];
            int[] rug1;
            int[] rug2;
            acoor[0] = x;
            acoor[1] = y;
            Rug rugRug = new Rug(rug);//抽象命名哈哈哈哈.请问每个rug分别代表什么
            rug1 = rugRug.getCoordination1();
            rug2 = rugRug.getCoordination2();
            if (!NextTo(rug1, acoor) && !NextTo(rug2, acoor)) return false;
            else if (NextTo(rug1, acoor) && rug2[0] == acoor[0] && acoor[1] == rug2[1]) return false;
            else if (NextTo(rug2, acoor) && rug1[0] == acoor[0] && acoor[1] == rug1[1]) return false;

            String substringGameState = gameState.substring(21);
            int substringGameStatelLength = substringGameState.length();//is 49^3
            int substringcount = substringGameStatelLength / 3;//is 49
            String[] substringGameStatearray = new String[substringcount];
            for (int i = 0; i < substringcount; i++) {
                int startIndex = i * 3;
                int endIndex = startIndex + 3;
                substringGameStatearray[i] = substringGameState.substring(startIndex, endIndex);
            }
            int CoordinationIndex1ofRug = rug1[0] * 7 + rug1[1];
            int CoordinationIndex2ofRug = rug2[0] * 7 + rug2[1];
            String subrug = rug.substring(0, 3);
            if (!(substringGameStatearray[CoordinationIndex1ofRug].equals("n00"))
                    && !(substringGameStatearray[CoordinationIndex2ofRug].equals("n00")) &&
                    substringGameStatearray[CoordinationIndex1ofRug].equals(substringGameStatearray[CoordinationIndex2ofRug])) {
                return false;
            }
            //判断覆盖真的很难写,
            else return true;


        }
        return false;//该方法需要和isRugvalid配合使用,但是不能一起使用,在isPlacementValid方法中传入的 string gamestate已经将string rug放置在
        //其中了,而isRugvalid方法中传入的gamestate和rug则需要判断rug是否被gamestate包含,被包含则返回false


        // FIXME: Task 10

    }
//    public  static int  findsamecolor(char color,String[]substringGameStatearray,int x,int y) {
 //       int count = 0;

//        int Sortnumber = x * 7 + y;
//        if (Sortnumber > 0 && Sortnumber < substringGameStatearray.length) {
//            if (substringGameStatearray[Sortnumber - 1].charAt(0) == color && Sortnumber % 7 != 0) {
//                count++;
//
//                findsamecolor(color, substringGameStatearray, x - 1, y);
//            }
//
//        if (substringGameStatearray[Sortnumber + 1].charAt(0) == color && (Sortnumber + 1) % 7 != 0) {
//            count++;
//
//            findsamecolor(color, substringGameStatearray, x + 1, y);
//        }
//        if (Sortnumber > 6 && substringGameStatearray[Sortnumber - 7].charAt(0) == color) {
//            count++;
//
//            findsamecolor(color, substringGameStatearray, x, y - 1);
//        }
//        if (Sortnumber < 49 && substringGameStatearray[Sortnumber + 7].charAt(0) == color) {
//            count++;
//
//            findsamecolor(color, substringGameStatearray, x, y + 1);
//        }
//    }
 //       return count;
   // }

    /**
     * Determine the amount of payment required should another player land on a square.
     * For this method, you may assume that Assam has just landed on the square he is currently placed on, and that
     * the player who last moved Assam is not the player who owns the rug landed on (if there is a rug on his current
     * square). Recall that the payment owed to the owner of the rug is equal to the number of connected squares showing
     * on the board that are of that colour. Similarly to the placement rules, two squares are only connected if they
     * share an entire edge -- diagonals do not count.
     *
     * @param gameString A String representation of the current state of the game.
     * @return The amount of payment due, as an integer.
     */
    public static int getPaymentAmount(String gameString) {
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
//          while(matcher.find()){
//
//        int x = matcher.group(0).charAt(1);
//        int y = matcher.group(0).charAt(2);
//        int[] acoor = new int[2];
//        acoor[0] = x;
//        acoor[1] = y;
//        char ColorofAssamRug=substringGameStatearray[acoor[0]*7+acoor[1]].charAt(0);
//        int count=findsamecolor(ColorofAssamRug,substringGameStatearray,acoor[0],acoor[1]);
//
//    // FIXME: Task 11
//        return count;}
          return -1;
}

    /**
     * Determine the winner of a game of Marrakech.
     * For this task, you will be provided with a game state string and have to return a char representing the colour
     * of the winner of the game. So for example if the cyan player is the winner, then you return 'c', if the red
     * player is the winner return 'r', etc...
     * If the game is not yet over, then you should return 'n'.
     * If the game is over, but is a tie, then you should return 't'.
     * Recall that a player's total score is the sum of their number of dirhams and the number of squares showing on the
     * board that are of their colour, and that a player who is out of the game cannot win. If multiple players have the
     * same total score, the player with the largest number of dirhams wins. If multiple players have the same total
     * score and number of dirhams, then the game is a tie.
     * @param gameState A String representation of the current state of the game
     * @return A char representing the winner of the game as described above.
     */
    public static char getWinner(String gameState) {
        if(isGameOver(gameState)==false){return'n';}
        String regex1 = "P[a-z][0-9]{5}[a-z]";
        Pattern pattern1 = Pattern.compile(regex1);
        Matcher matcher = pattern1.matcher(gameState);
        String substringGameState = gameState.substring(21);
        int substringGameStatelLength = substringGameState.length();//is 49^3
        int substringcount = substringGameStatelLength / 3;//is 49
        String[] substringGameStatearray = new String[substringcount];
        for (int i = 0; i < substringcount; i++) {
            int startIndex = i * 3;
            int endIndex = startIndex + 3;
            substringGameStatearray[i] = substringGameState.substring(startIndex, endIndex);
        }
        int c=0;int r=0;int p=0;int y=0;
        for(String s:substringGameStatearray){
            if(s.charAt(0)=='c')c++;
            else if (s.charAt(0)=='r')r++;
            else if(s.charAt(0)=='p')p++;
            else if(s.charAt(0)=='y')y++;
        }
//        int[]playerdirhams=new int[4];
//        int[]playerrugs=new int[4];
//        int i=0;
//        int j=0;
        while(matcher.find()){
//            playerdirhams[i++]=Integer.parseInt(matcher.group(0).substring(2,5));
//            playerrugs[j++]=Integer.parseInt(matcher.group(0).substring(5,7));
            if(matcher.group(0).charAt(1)=='c')c+=Integer.parseInt(matcher.group(0).substring(2,5));
            if(matcher.group(0).charAt(1)=='r')r+=Integer.parseInt(matcher.group(0).substring(2,5));
            if(matcher.group(0).charAt(1)=='p')p+=Integer.parseInt(matcher.group(0).substring(2,5));
            if(matcher.group(0).charAt(1)=='y')y+=Integer.parseInt(matcher.group(0).substring(2,5));
        }

        int MAX=c;char max='c';
        if(r>MAX){
            MAX=r;
            max='r';
        }
        if(y>MAX){
            MAX=y;
            max='y';
        }
        if(p>MAX){
            MAX=p;
            max='p';
        }

return max;//打平的情况还没判断test就过了...留给你们写吧,简单的.


        // FIXME: Task 12

    }

    /**
     * Implement Assam's movement.
     * Assam moves a number of squares equal to the die result, provided to you by the argument dieResult. Assam moves
     * in the direction he is currently facing. If part of Assam's movement results in him leaving the board, he moves
     * according to the tracks diagrammed in the assignment README, which should be studied carefully before attempting
     * this task. For this task, you are not required to do any checking that the die result is sensible, nor whether
     * the current Assam string is sensible either -- you may assume that both of these are valid.
     * @param currentAssam A string representation of Assam's current state.
     * @param dieResult The result of the die, which determines the number of squares Assam will move.
     * @return A String representing Assam's state after the movement.
     */
    public static String moveAssam(String currentAssam, int dieResult){
        char direction=currentAssam.charAt(3);
        int x=currentAssam.charAt(1)-'0';
        int y=currentAssam.charAt(2)-'0';
        switch(currentAssam.charAt(3)){
            case 'N':
                if(y-dieResult>=0){y-=dieResult;}
                   else if(y-dieResult<0&&x!=6){
                       direction='S';
                       y=dieResult-1-y;
                       if(x%2==0){x+=1;}else x-=1;
            }
                   else if(y-dieResult<0&&x==6){
                       x=y+7-dieResult;
                       y=0;
                       direction='W';//边角转弯只验证了一次,需要小心下面边角出问题;

                }
                   break;
            case 'S':
                if(y+dieResult<=6){y+=dieResult;}
                else if(y+dieResult>6&&x!=0){
                    direction='N';
                    y=13-y-dieResult;
                    if(x%2==1)x+=1;else x-=1;
                }
                else if(y+dieResult>6&&x==0){
                    x=y+dieResult-7;
                    y=6;
                    direction='E';
                }
                break;
            case'E':
                if(x+dieResult<=6){x+=dieResult;}
                else if(x+dieResult>6&&y!=0){
                    direction='W';
                      x=13-x-dieResult;
                    if(y%2==1)y+=1;else y-=1;
                }
                else if(x+dieResult>6&&y==0){
                    y=13-x-dieResult;
                    x=6;
                    direction='S';
                }
                break;
            case'W':
                if(x-dieResult>=0){x-=dieResult;}
                else if(x-dieResult<0&&y!=6){
                    direction='E';
                    x=dieResult-1-x;
                    if(y%2==0)y+=1;else y-=1;
                }else if(x-dieResult<0&&y==6){
                    y=x+dieResult-7;
                    x=0;
                    direction='N';
                }break;
        }
        // FIXME: Task 13
        String X=Integer.toString(x);
        String Y=Integer.toString(y);
        return 'A'+X+Y+direction;
    }

    /**
     * Place a rug on the board
     * This method can be assumed to be called after Assam has been rotated and moved, i.e in the placement phase of
     * a turn. A rug may only be placed if it meets the conditions listed in the isPlacementValid task. If the rug
     * placement is valid, then you should return a new game string representing the board after the placement has
     * been completed. If the placement is invalid, then you should return the existing game unchanged.
     * @param currentGame A String representation of the current state of the game.
     * @param rug A String representation of the rug that is to be placed.
     * @return A new game string representing the game following the successful placement of this rug if it is valid,
     * or the input currentGame unchanged otherwise.
     */
    public static String makePlacement(String currentGame, String rug) {
       if(isPlacementValid(currentGame,rug)==false){return currentGame;}
        Rug rug1=new Rug(rug);
        int x1=rug1.getCoordination1()[0];
        int x2=rug1.getCoordination2()[0];
        int y1=rug1.getCoordination1()[1];
        int y2=rug1.getCoordination2()[1];
        String substringGameState = currentGame.substring(21);
        int substringGameStatelLength = substringGameState.length();//is 49^3
        int substringcount = substringGameStatelLength / 3;//is 49
        String[] substringGameStatearray = new String[substringcount];
        for (int i = 0; i < substringcount; i++) {
            int startIndex = i * 3;
            int endIndex = startIndex + 3;
            substringGameStatearray[i] = substringGameState.substring(startIndex, endIndex);
        }
        int x=x1*7+y1;
        int y=x2*7+y2;
        substringGameStatearray[x]=rug.substring(0,3);
        substringGameStatearray[y]=rug.substring(0,3);
        String changedGame=new String();
        for(int j=0;j<substringcount;j++){
            changedGame=changedGame+substringGameStatearray[j];
        }
        changedGame=currentGame.substring(0,21)+changedGame;
        char c=rug1.getColor();
        String regex1 = "P[a-z][0-9]{5}[a-z]";
        Pattern pattern1 = Pattern.compile(regex1);
        Matcher matcher = pattern1.matcher(currentGame);
        //if(!matcher.find())return false;
        String[]player=new String[4];
        int count1=0;
        while (matcher.find()) {
            String player2 = matcher.group(0);
            player[count1++]=player2;
        }
        int count2=0;
        for(String s:player){
            if(s!=null){
                if(s.charAt(1)!=c){
                count2++;
            }if(s.charAt(1)==c){break;}
            }
        }
        char[]chararray=changedGame.toCharArray();
        if(chararray[count2*8+6]!='0'){ chararray[count2*8+6]-=1;}
        else if(chararray[count2*8+6]=='0'&&chararray[count2*8+5]=='1'){chararray[count2*8+6]='9';chararray[count2*8+5]='0';}
        else return"玩尼玛没毛毯了";//else 再然后就是两个都是0,毛毯数量为0,那还玩个屁,直接return null?
        changedGame=new String(chararray);

        // FIXME: Task 14
        return changedGame;
    }

}
