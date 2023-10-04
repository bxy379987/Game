package comp1110.ass2;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.OptionalInt;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

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
        // FIXME: Task 6
        Dice dice = new Dice();
//        System.out.println(dice.rollDice());
        return dice.rollDice();
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
        System.out.println("=============[rotateAssam]=============");
        System.out.println(currentAssam);
        System.out.println(rotation);
        if (rotation % 90 != 0) return currentAssam;
        if (rotation == 180) return currentAssam;
        Assam assam = new Assam(currentAssam);
        assam.rotateClockwise(rotation / 90);
        return assam.toString();
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
//        System.out.println("=============[isPlacementValid]=============");
//        System.out.println(gameState);
        String boardString = gameState.split("B")[1];
        Board boardEntity = new Board(boardString);
//        System.out.println(boardEntity);
//        boardEntity.showBoardColorInMatrix();
        Rug rugEntity = new Rug(rug);
//        System.out.println(rugEntity);
        //[DEBUG] Added the rug cover assam situation
        Assam assam = new Assam(gameState);

        return boardEntity.placeRug(rugEntity, assam);
    }
    /**
     * Testdata Bug: 385
     */

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
        boolean DEBUG = false;
        // FIXME: Task 11
        if (DEBUG) System.out.println("=============[getPaymentAmount]=============");
//        if (DEBUG) System.out.println(gameString);
        String boardString = gameString.split("B")[1];
        if (DEBUG) System.out.println(boardString);
        String assamString = gameString.split("B")[0].split("A")[1];
        if (DEBUG) System.out.println(assamString);
        int[] assamPos = {assamString.toCharArray()[0] - '0', assamString.toCharArray()[1] - '0'};

        // [INIT] new board
        Board board = new Board(boardString);
        pieceColor[][] boardColor = board.getBoardColor();
        pieceColor currentColor = boardColor[assamPos[0]][assamPos[1]];
        // CASE: no color
        if (currentColor == pieceColor.NONE) return 0;
        if (DEBUG) System.out.println("MAPPING COLOR: " + currentColor);

        // Start calculate costs by BFS
        if (DEBUG) System.out.println("START BFS");
        //                    up      right   down     left
        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
        Queue<int[]> searchList = new LinkedList<>();
        // [INIT] first positions
        searchList.offer(assamPos);
        // [INIT] mark
        int mark = 0;
        while (!searchList.isEmpty()) {
            int[] currentPos = searchList.peek();
            if (DEBUG) System.out.println("Current pos: [" + currentPos[0] + ", " + currentPos[1] + "]");
            // every search step 1 mark
            mark += 1;
            pieceColor[] nearbyColors = board.getColorsNearby(currentPos[0], currentPos[1]);
            for (int i = 0; i < nearbyColors.length; i++) {
                // out of border
                if (nearbyColors[i] == pieceColor.OUT) continue;
                // find matching color
                int[] nextPos = {currentPos[0] + directions[i][0], currentPos[1] + directions[i][1]};
                if (nearbyColors[i].equals(currentColor) && !listHasPosition(searchList, nextPos)) {
                    searchList.offer(nextPos);
                }
            }
            // delete position has searched surround
            searchList.poll();
            boardColor[currentPos[0]][currentPos[1]] = pieceColor.OUT;

            // [DEBUG] check board color state
            if (DEBUG) {
                for (int row = 0; row < boardColor.length; row++) {
                    for (int col = 0; col < boardColor[0].length; col++) {
                        System.out.print(boardColor[col][row] + " ");
                    }
                    System.out.println();
                }
            }
        }
        if (DEBUG) System.out.println("MARK: " + mark);
        return mark;
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
        // FIXME: Task 12
        System.out.println("=============[getWinner]=============");
        // CASE: If the game is not yet over
        if(!isGameOver(gameState)) return 'n';
        // [INIT]
        String boardString = gameState.split("B")[1];
        Board board = new Board(boardString);
        int[] numSquaresOnBoard = board.countColors();
        System.out.println("C Y P R N on board: " + Arrays.toString(numSquaresOnBoard));
        // from 1 to 4 NOT 0 to 3
        // Sequence c y p r
        String[] playerStrings = gameState.split("A")[0].split("P");
        Player[] players = new Player[4];
        int[] totalScore = new int[4];
        for (int idx = 0; idx < players.length; idx++) {
            players[idx] = Player.fromString("P" + playerStrings[idx + 1]);
            if (players[idx].isIsplaying()) {
                // total score is the sum of their number of dirhams and the number of squares showing on the
                // board that are of their colour
                totalScore[idx] = players[idx].getDirhams() + numSquaresOnBoard[idx];
            } else {
                //  that a player who is out of the game cannot win
                totalScore[idx] = -1;
            }
//            System.out.println(players[idx].toString());
        }
        System.out.println("Total score: " + Arrays.toString(totalScore));
        // COUNT MAX
        int max = -1; // max num
        int countMax = 0; // amount of max
        int[] maxIdxList = {-1, -1, -1, -1}; // index of max num
        for (int idx = 0; idx < totalScore.length; idx++) {
            if (max == totalScore[idx]) {
                countMax += 1;
                maxIdxList[countMax - 1] = idx;
            }
            if (max < totalScore[idx]) {
                maxIdxList = new int[]{-1, -1, -1, -1};
                countMax = 1;
                max = totalScore[idx];
                maxIdxList[countMax - 1] = idx;
            }
        }
        if (countMax == 1) return players[maxIdxList[0]].getColor().getSymbol().charAt(0);

        // same total score, the player with the largest number of dirhams wins.
        max = -1;
        int maxIdx = -1;
        for (int idx = 0; idx < maxIdxList.length; idx++) {
            if (maxIdxList[idx] == -1) break;
            // If multiple players have the same total core and number of dirhams, then the game is a tie.
            if (max == players[maxIdxList[idx]].getDirhams()) return 't';
            // the largest number of dirhams wins.
            if (max < players[maxIdxList[idx]].getDirhams()) {
                max = players[maxIdxList[idx]].getDirhams();
                maxIdx = maxIdxList[idx];
            }
        }
        return players[maxIdx].getColor().getSymbol().charAt(0);
        /**
         * Test Error At
         * =============[getWinner]=============
         * =============[isGameOver]=============
         * Pc02000i
         * Py04000i
         * Pp04100i
         * Pr01900i
         * C Y P R N on board: [9, 9, 8, 17, 6]
         * Total score: [29, 49, 49, 36]
         * Expected :t
         * Actual   :p
         *
         * BUT p041 > y040 should be p wins
         * =============[getWinner]=============
         * =============[isGameOver]=============
         * Pc06000i
         * Py00000o
         * Pp00900i
         * Pr05100i
         * C Y P R N on board: [11, 5, 10, 20, 3]
         * Total score: [71, -1, 19, 71]
         * Expected :t
         * Actual   :c
         *
         * BUT c060 > r051 should be c wins
         *
         * if ignore dirhams count test will pass
         */
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
        // FIXME: Task 13
        System.out.println("=============[moveAssam]=============");
        System.out.println(currentAssam + " " + dieResult);
        // [INIT]
        Assam assam = Assam.fromString(currentAssam);
        assam.moveXSteps(dieResult);
        return assam.toString();
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
        if (!isRugValid(currentGame,rug)){return currentGame;}
        if (!isPlacementValid(currentGame, rug)){return currentGame;}
        Rug rug1=new Rug(rug);
        int x1=rug1.getFirstCoordinate()[0];
        int x2=rug1.getSecondCoordinate()[0];
        int y1=rug1.getFirstCoordinate()[1];
        int y2=rug1.getSecondCoordinate()[1];

        Player[] players = Player.fromGameString(currentGame);
        Assam assam1 = new Assam(currentGame);

        for (Player player: players){
            if (rug1.getColor()==player.getColor()){
                player.setRemainingRugs(player.getRemainingRugs()-1);
            }
        }

        String Boardstring=currentGame.split("B")[1];
        Board boardEntity =new Board(Boardstring);
        boardEntity.setColorByCoordinate(x1, y1, rug1.getColor(), rug1.getID());
        boardEntity.setColorByCoordinate(x2, y2, rug1.getColor(), rug1.getID());

        String changedGameState=new String(players[0].toString()+ players[1]+ players[2] + players[3] +assam1+ "B" +boardEntity.toString());
        System.out.println("state"+changedGameState);
        return changedGameState;

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
