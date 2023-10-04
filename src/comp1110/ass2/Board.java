package comp1110.ass2;


import java.util.*;

import static comp1110.ass2.Marrakech.NextTo;

public class Board {
    public int BOARD_WIDTH = 7;
    public int BOARD_HEIGHT = 7;
    private pieceColor[][] boardColor;
    private int[][] subID;
    Map<String, String> tunnels = new HashMap<>();

//    ArrayList<ArrayList<Stack<Rug>>> boardHistoryRug = new ArrayList<>();
    // 我不确定是否需要添加记录每一个方格曾经放置的Rug内容，目前来看并不会需要调用历史记录，所以注释了
    // I'm not sure if it's necessary to record the Rug contents placed on each square.
    // At the moment, it doesn't seem like we will need to access the history records, so I have commented it out.

    /**
     * Init board Tunnels
     */
    private void initTunnels() {
        // upper line
        tunnels.put("A00N", "A10S");
        tunnels.put("A10N", "A00S");
        tunnels.put("A20N", "A30S");
        tunnels.put("A30N", "A20S");
        tunnels.put("A40N", "A50S");
        tunnels.put("A50N", "A40S");
        // upper right corner
        tunnels.put("A60N", "A60W");
        tunnels.put("A60E", "A60S");
        // right line
        tunnels.put("A61E", "A62W");
        tunnels.put("A62E", "A61W");
        tunnels.put("A63E", "A64W");
        tunnels.put("A64E", "A63W");
        tunnels.put("A65E", "A66W");
        tunnels.put("A66E", "A65W");
        // bottom line
        tunnels.put("A66S", "A56N");
        tunnels.put("A56S", "A66N");
        tunnels.put("A46S", "A36N");
        tunnels.put("A36S", "A46N");
        tunnels.put("A26S", "A16N");
        tunnels.put("A16S", "A26N");
        // bottom left corner
        tunnels.put("A06S", "A06E");
        // left line
        tunnels.put("A05W", "A04E");
        tunnels.put("A04W", "A05E");
        tunnels.put("A03W", "A02E");
        tunnels.put("A02W", "A03E");
        tunnels.put("A01W", "A00E");
        tunnels.put("A00W", "A01E");
    }

    /**
     * Empty Constructor
     */
    public Board() {
        boardColor = new pieceColor[BOARD_HEIGHT][BOARD_WIDTH];
        subID = new int[BOARD_HEIGHT][BOARD_WIDTH];
        for (int col = 0; col < BOARD_WIDTH; col++) {
            for (int row = 0; row < BOARD_HEIGHT; row++) {
                boardColor[col][row] = pieceColor.NONE;
                subID[col][row] = 0;
            }
//            System.out.println();
        }
        initTunnels();
    }

    /**
     * Constructor with board state string
     */
    public Board(String boardState) {
        String[] boardStateArray = boardState.split("");
        if (boardStateArray.length != BOARD_WIDTH * BOARD_HEIGHT * 3)
            throw new IllegalArgumentException("Incomplete board state string is not acceptable");
        boardColor = new pieceColor[BOARD_HEIGHT][BOARD_WIDTH];
        subID = new int[BOARD_HEIGHT][BOARD_WIDTH];
        for (int col = 0; col < BOARD_WIDTH; col++) {
            for (int row = 0; row < BOARD_HEIGHT; row++) {
                int flattenIdx = (col * BOARD_WIDTH + row) * 3;
                String colorSymbol = boardStateArray[flattenIdx];
                int id = Integer.parseInt(boardStateArray[flattenIdx + 1] + boardStateArray[flattenIdx + 2]);
                if (!"cyrpn".contains(colorSymbol)) throw new IllegalArgumentException("Only accept \"cyrpn\" as color");
//                System.out.print(color + " ");
                boardColor[col][row] = pieceColor.fromSymbol(colorSymbol);
                subID[col][row] = id;
            }
//            System.out.println();
        }
        initTunnels();
    }

    /**
     * Provide the XY coordinates of a certain point, and return the colors of the chessboard in the order of
     * 'up, right, down, left' for adjacent positions. If the adjacent positions are beyond the board's
     * boundaries, return null values.
     * @param x coordinate
     * @param y coordinate
     * @return the colors of neighbours in the order of "up, right, down, left"
     */
    public pieceColor[] getColorsNearby(int x, int y) {
        if (!(x >= 0 && x < BOARD_WIDTH)) throw new IllegalArgumentException("Coordinate out of bounds only accept [0-" + BOARD_WIDTH + "]");
        if (!(y >= 0 && y < BOARD_HEIGHT)) throw new IllegalArgumentException("Coordinate out of bounds only accept [0-" + BOARD_HEIGHT + "]");
        // get colors from      [up, right, down, left]
        // int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
        pieceColor[] colorsNearby = new pieceColor[4];
        colorsNearby[0] = y - 1 >= 0 ? boardColor[x][y-1] : pieceColor.OUT;
        colorsNearby[1] = x + 1 < BOARD_WIDTH ? boardColor[x+1][y] : pieceColor.OUT;
        colorsNearby[2] = y + 1 < BOARD_HEIGHT ? boardColor[x][y+1] : pieceColor.OUT;
        colorsNearby[3] = x - 1 >= 0 ? boardColor[x-1][y] : pieceColor.OUT;
        return colorsNearby;
    }

    /**
     * Get board colors as matrix form
     * @return Colors Matrix
     */
    public pieceColor[][] getBoardColor() {
        return boardColor.clone();
    }

    /**
     * Based on the provided coordinates and colors, set the corresponding parameters
     * at the corresponding positions. Only accept four specific colors for the
     * player and empty spaces.
     * @param x coordinate
     * @param y coordinate
     * @param color of corresponding coordinate
     */
    // DISCARD?
//    public void setColorByCoordinate(int x, int y, String color) {
//        if (!"cyrpn".contains(color)) throw new IllegalArgumentException("Only accept \"cyrpn\" as color");
//        boardColor[x][y] = color;
//    }

    public void setColorByCoordinate(int x, int y, pieceColor color, int id) {
//        if (!"cyrpn".contains(color)) throw new IllegalArgumentException("Only accept \"cyrpn\" as color");
        if (id < 0) throw new IllegalArgumentException("Only accept rug ID");
        boardColor[x][y] = color;
        subID[x][y] = id;
    }

    public pieceColor getColorByCoordinate(int x, int y) {
        return boardColor[x][y];
    }

    /**
     * When Assam reaches the edge of the board and meets the required orientation,
     * he will enter a tunnel predefined on the board. The board will return
     * Assam's state after he exits the tunnel.
     * @param assam before entered the tunnel
     * @return assam after entered the tunnel
     */
    public Assam getAssamViaTunnel(Assam assam) {
        // CASE: do not enter tunnels
        if (!tunnels.containsKey(assam.toString())) return assam;
        // CASE: enter tunnels, find Mapping tunnel
        return Assam.fromString(tunnels.get(assam.toString()));
    }

    public int[][] getSubID() {
        return subID;
    }

    /**
     * Input the Rug instance that is about to be placed on the Board.
     * If it can be placed, update the Board's state and return `True`;
     * otherwise, return `False`
     * @param rug is about to be placed
     * @return is place valid
     */
    public boolean placeRug(Rug rug, Assam assam) {
        // TODO: place rug into board
        pieceColor firstPartColor = getColorByCoordinate(rug.getFirstCoordinate()[0], rug.getFirstCoordinate()[1]);
        pieceColor secondPartColor = getColorByCoordinate(rug.getSecondCoordinate()[0], rug.getSecondCoordinate()[1]);
        // CASE: invalid
        if (firstPartColor.equals(secondPartColor) && !firstPartColor.equals(pieceColor.NONE)  && getSubID()[rug.getFirstCoordinate()[0]][rug.getFirstCoordinate()[1]] == getSubID()[rug.getSecondCoordinate()[0]][rug.getSecondCoordinate()[1]]) {
            return false;}
        int[] assamCoordinate = {assam.getxCoordinate(),assam.getyCoordinate()};
        if (Arrays.equals(rug.getFirstCoordinate(),assamCoordinate) || Arrays.equals(rug.getSecondCoordinate(),assamCoordinate))
            return false;
        if (!NextTo(assamCoordinate, rug.getFirstCoordinate()) && !NextTo(assamCoordinate, rug.getSecondCoordinate()))
            return false;
        // CASE: valid
        setColorByCoordinate(rug.getFirstCoordinate()[0], rug.getFirstCoordinate()[1], rug.getColor(), rug.getID());
        setColorByCoordinate(rug.getSecondCoordinate()[0], rug.getSecondCoordinate()[1], rug.getColor(), rug.getID());
        return true;
    }

    /**
     * Eliminate the player when their score runs out. If the player
     * exists and has a score of 0, or if the player voluntarily exits
     * the game, set all squares owned by that player to empty and return
     * True. Otherwise, if the player's score is greater than 0, or they
     * did not participate in the game, return False.
     * @param player to disuse
     * @return is disuse valid
     */
    public boolean disusePlayer(Player player) {
        // TODO: disuse the player
        return false;
    }

    public int[] countColors() {
        // represent the amount of [c y p r n]
        int[] colorsAmount = new int[5];
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                pieceColor color = boardColor[col][row];
                colorsAmount[color.ordinal()]++;
                }
            }
        return colorsAmount;
    }
    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int col = 0; col < BOARD_WIDTH; col++) {
            for (int row = 0; row < BOARD_HEIGHT; row++) {
                boardString.append(boardColor[col][row].getSymbol()).append(String.format("%02d", subID[col][row]));
            }
        }
        return boardString.toString();
    }

    public void showBoardColorInMatrix() {
        for (int row = 0; row < boardColor.length; row++) {
            for (int col = 0; col < boardColor[0].length; col++) {
                System.out.print(boardColor[col][row].getSymbol() + " ");
            }
            System.out.println();
        }
    }
}
