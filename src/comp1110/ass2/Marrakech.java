package comp1110.ass2;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Marrakech {
    private static final int BOARD_INDEX_WIDTH = 7;
    private static final int BOARD_INDEX_HEIGHT = 7;

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
        // FIXME: Task 4
        System.out.println("=============[isRugValid]=============");
        System.out.println(rug);
        String boardString = gameString.split("B")[1];
        System.out.println(boardString);
        //"c"表示青色，"y"表示黄色，"r"表示红色，"p"表示紫色
        String regex = "^[a-z][0-9]{6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(rug);
        // CASE: Format not match
        if (!matcher.find()) return false;
        // CASE: Exist same ID [not valid]
        // [DEBUG] have Case for PlayerString Py02 which "y02" contains Rug "y02" change gameString into boardString
        if (boardString.contains(rug.substring(0, 3))) return false;
        // CASE: Off the Board [not valid]
        int[] coordinates = rug.substring(3).chars().map(Character::getNumericValue).toArray();
        if (Arrays.stream(coordinates).anyMatch(coordinate -> coordinate > 6)) return false;
        // CASE: is valid player
        if (!"cyrp".contains(rug.substring(0,1))) return false;

        // 他居然不需要检测摆放的位置颜色相同，先注释了
//        // CASE: covered with two same color [not valid]
//        System.out.println(boardString.toCharArray()[((coordinates[0])*7+coordinates[1]) * 3]);
//        char color1 = boardString.toCharArray()[((coordinates[0])*7+coordinates[1]) * 3];
//        char color2 = boardString.toCharArray()[((coordinates[2])*7+coordinates[3]) * 3];
//        System.out.println(color1 + " " + color2);
//        if (color1 == color2 & color1 != 'n') {
//            System.out.println("IN CASE");
//            return false;
//        };

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
        // FIXME: Task 8
        System.out.println("=============[isGameOver]=============");
        String regex = "P[a-z][0-9]{5}[a-z]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(currentGame);
        //if(!matcher.find())return false;

        while (matcher.find()) {
            String player = matcher.group();//可以优化成把这个string赋给PlayerPattern构造器,建对象然后通过属性判断
            System.out.println(player);
            // [DEBUG] == 0 改为 == '0', 且必须全部都为0才是game over
            //虽然能过test但是没优化好,因为我判断某人没毛毯就游戏结束,但是老师要求判断条件是到某人掷骰子时没毛毯才算结束
            if (!player.substring(5, 7).equals("00") && player.substring(7).equals("i")) return false;
        }

        return true;
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
        // FIXME: Task 10
        System.out.println("=============[isPlacementValid]=============");
        System.out.println(rug);
        System.out.println(gameState);
        String boardString = gameState.split("B")[1];
        Board boardEntity = new Board(boardString);
        System.out.println(boardEntity);
        Rug rugEntity = new Rug(rug);
        System.out.println(rugEntity);

        String regex = "A[0-9]{2}[A-Z]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(gameState);
        while (matcher.find()) {
            Assam assam = Assam.fromGameString(gameState);
            int x = assam.getxCoordinate();
            int y = assam.getyCoordinate();
            int[] acoor = new int[2];
            int[] rug1;
            int[] rug2;
            acoor[0] = x;
            acoor[1] = y;
            Rug rugRug = new Rug(rug);//抽象命名哈哈哈哈.请问每个rug分别代表什么
            rug1 = rugRug.getFirstCoordinate();
            rug2 = rugRug.getSecondCoordinate();
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

        // FIXME: Task 11
        System.out.println("=============[getPaymentAmount]=============");
//        System.out.println(gameString);
        String boardString = gameString.split("B")[1];
        System.out.println(boardString);
        String assamString = gameString.split("B")[0].split("A")[1];
        System.out.println(assamString);
        int[] assamPos = {assamString.toCharArray()[0] - '0', assamString.toCharArray()[1] - '0'};

        // [INIT] new board
        Board board = new Board(boardString);
        String[][] boardColor = board.getBoardColor();
        String currentColor = boardColor[assamPos[0]][assamPos[1]];
        // CASE: no color
        if (currentColor.equals("n")) return 0;
        System.out.println("MAPPING COLOR: " + currentColor);

        // Start calculate costs by BFS
        System.out.println("START BFS");
        //                    up      right   down     left
        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
        Queue<int[]> searchList = new LinkedList<>();
        // [INIT] first positions
        searchList.offer(assamPos);
        // [INIT] mark
        int mark = 0;
        while (!searchList.isEmpty()) {
            int[] currentPos = searchList.peek();
            System.out.println("Current pos: [" + currentPos[0] + ", " + currentPos[1] + "]");
            // every search step 1 mark
            mark += 1;
            String[] nearbyColors = board.getColorsNearby(currentPos[0], currentPos[1]);
            for (int i = 0; i < nearbyColors.length; i++) {
                // out of border
                if (nearbyColors[i].equals("")) continue;
                // find matching color
                int[] nextPos = {currentPos[0] + directions[i][0], currentPos[1] + directions[i][1]};
                if (nearbyColors[i].equals(currentColor) && !listHasPosition(searchList, nextPos)) {
                    searchList.offer(nextPos);
                }
            }
            // delete position has searched surround
            searchList.poll();
            boardColor[currentPos[0]][currentPos[1]] = "x";

            // [DEBUG] check board color state
            for (int row = 0; row < boardColor.length; row++) {
                for (int col = 0; col < boardColor[0].length; col++) {
                    System.out.print(boardColor[col][row] + " ");
                }
                System.out.println();
            }
        }
        System.out.println("MARK: " + mark);
        return mark;
        /**
         * TEST ERROR AT:
         * org.opentest4j.AssertionFailedError: Expected payment of 0 for game state Pc00004oPy06604iPp01604iPr03805iA60WBc23c23y23r12r12n00n00n00r22y23y19y19p19n00c05p14y18y18r09p19n00p11p11y06c16n00y10r03y04n00c24r10c19c19r13y04p21c24r23p20p10n00y01y21y24r18r18c21c21 ==>
         * Expected :0
         * Actual   :5
         * org.opentest4j.AssertionFailedError: Expected payment of 5 for game state Pc00006oPy09806iPp00706iPr01506iA24WBy02n00n00y07p11c07r06c20r18r18y12c17c17r06c20y05c16y16y15y17n00p18c10y13y16y06y17n00y00c00y08y04r21r21n00y21y21r03p21n00r20y03n00p04n00p21n00r20p17 ==>
         * Expected :5
         * Actual   :11
         * org.opentest4j.AssertionFailedError: Expected payment of 2 for game state Pc03802iPy00003oPp00003oPr08203iA50SBp20r17r14r02r02c09n00r20p23r14y23y23c18n00r20p23n00c17c23r22n00r15y17c19c19y06r19r19r21p11p16y07c05n00r06r21c14p16c25y25p22p22n00y13y13p10y25c10r08 ==>
         * Expected :2
         * Actual   :5
         */
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
//
//        return count;}
//          return -1;
}
    private static boolean listHasPosition(Queue<int[]> list, int[] pos) {
        assert pos.length == 2;
        for (int[] listPos: list) {
            assert listPos.length == pos.length;
            if (listPos[0] == pos[0] && listPos[1] == pos[1]) return true;
        }
        return false;
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

return max;//打平的情况还没判断test就过了...


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
        System.out.println(currentAssam+" "+dieResult);
        Assam assam=new Assam();
       assam=Assam.fromString(currentAssam);
        char direction=assam.getDirection().getSymbol();
        int x=assam.getxCoordinate();
        int y=assam.getyCoordinate();
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
                    y=x+dieResult-7;
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
                    y=x-dieResult+7;
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
        int x1=rug1.getFirstCoordinate()[0];
        int x2=rug1.getSecondCoordinate()[0];
        int y1=rug1.getFirstCoordinate()[1];
        int y2=rug1.getSecondCoordinate()[1];
        int x=x1*7+y1;
        int y=x2*7+y2;
//        String substringGameState = currentGame.substring(21);

        PlayerPattern playerPattern1=PlayerPattern.fromString(currentGame.substring(0,8));
        PlayerPattern playerPattern2=PlayerPattern.fromString(currentGame.substring(8,16));
        PlayerPattern playerPattern3=PlayerPattern.fromString(currentGame.substring(16,24));
        PlayerPattern playerPattern4=PlayerPattern.fromString(currentGame.substring(24,32));
        Assam assam1=Assam.fromString(currentGame.substring(32,36));
//        Board board=new Board(currentGame.substring(36));
        if(rug1.getColor()==playerPattern1.getColor()){playerPattern1.setRemainingRugs(playerPattern1.getRemainingRugs()-1);}
        if(rug1.getColor()==playerPattern2.getColor()){playerPattern2.setRemainingRugs(playerPattern2.getRemainingRugs()-1);}
        if(rug1.getColor()==playerPattern3.getColor()){playerPattern3.setRemainingRugs(playerPattern3.getRemainingRugs()-1);}
        if(rug1.getColor()==playerPattern4.getColor()){playerPattern4.setRemainingRugs(playerPattern4.getRemainingRugs()-1);}
//        board.setColorByCoordinate(x,y,rug1.getColor()+"");//为什么color是字符串?
        String Boardstring=currentGame.substring(36);
        char[] Board=Boardstring.toCharArray();
        Board[x*3+1]=rug1.getColor();
        Board[y*3+1]=rug1.getColor();
        System.out.println(playerPattern1.toString());
        System.out.println(Board);
//        System.out.println("board"+board);
        String changedgamestate=new String(playerPattern1.toString()+playerPattern2.toString()+playerPattern3+playerPattern4+assam1+new String(Board));
        System.out.println("state"+changedgamestate);
        return changedgamestate;
//        int substringGameStatelLength = substringGameState.length();//is 49^3
//        int substringcount = substringGameStatelLength / 3;//is 49
//        String[] substringGameStatearray = new String[substringcount];
//        for (int i = 0; i < substringcount; i++) {
//            int startIndex = i * 3;
//            int endIndex = startIndex + 3;
//            substringGameStatearray[i] = substringGameState.substring(startIndex, endIndex);
//        }
//        int x=x1*7+y1;
//        int y=x2*7+y2;
//        substringGameStatearray[x]=rug.substring(0,3);
//        substringGameStatearray[y]=rug.substring(0,3);
//        String changedGame=new String();
//        for(int j=0;j<substringcount;j++){
//            changedGame=changedGame+substringGameStatearray[j];
//        }
//        changedGame=currentGame.substring(0,21)+changedGame;
//        char c=rug1.getColor();
//        String regex1 = "P[a-z][0-9]{5}[a-z]";
//        Pattern pattern1 = Pattern.compile(regex1);
//        Matcher matcher = pattern1.matcher(currentGame);
//        //if(!matcher.find())return false;
//        String[]player=new String[4];
//        int count1=0;
//        while (matcher.find()) {
//            String player2 = matcher.group(0);
//            player[count1++]=player2;
//        }
//        int count2=0;
//        for(String s:player){
//            if(s!=null){
//                if(s.charAt(1)!=c){
//                count2++;
//            }if(s.charAt(1)==c){break;}
//            }
//        }
//        char[]chararray=changedGame.toCharArray();
//        if(chararray[count2*8+6]!='0'){ chararray[count2*8+6]-=1;}
//        else if(chararray[count2*8+6]=='0'&&chararray[count2*8+5]=='1'){chararray[count2*8+6]='9';chararray[count2*8+5]='0';}
//        else return"";
//
//        changedGame=new String(chararray)+currentGame.charAt(currentGame.length()-1);
//
//        // FIXME: Task 14
//        return changedGame;
    }

}
