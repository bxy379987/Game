package comp1110.ass2;


import java.util.HashMap;
import java.util.Map;

public class Board {
    int BOARD_WIDTH = 7;
    int BOARD_HEIGHT = 7;
    String[][] boardColor;
    Map<String, String> tunnels = new HashMap<>();
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

    Board() {
        boardColor = new String[BOARD_HEIGHT][BOARD_WIDTH];
        for (int col = 0; col < BOARD_WIDTH; col++) {
            for (int row = 0; row < BOARD_HEIGHT; row++) {
                boardColor[col][row] = "n";
            }
//            System.out.println();
        }
        initTunnels();
    }

    public Board(String boardState) {
        boardColor = new String[BOARD_HEIGHT][BOARD_WIDTH];
        for (int col = 0; col < BOARD_WIDTH; col++) {
            for (int row = 0; row < BOARD_HEIGHT; row++) {
                char color = boardState.toCharArray()[(col * BOARD_WIDTH + row) * 3];
//                System.out.print(color + " ");
                boardColor[col][row] = String.valueOf(color);
            }
//            System.out.println();
        }
        initTunnels();
    }
    public String[] getColorsNearby(int x, int y) {
        assert x >= 0 && x < BOARD_WIDTH;
        assert y >= 0 && y < BOARD_HEIGHT;
        // get colors from      [up, right, down, left]
        // int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
        String[] colorsNearby = new String[4];
        colorsNearby[0] = y - 1 >= 0 ? boardColor[x][y-1] : "";
        colorsNearby[1] = x + 1 < BOARD_WIDTH ? boardColor[x+1][y] : "";
        colorsNearby[2] = y + 1 < BOARD_HEIGHT ? boardColor[x][y+1] : "";
        colorsNearby[3] = x - 1 >= 0 ? boardColor[x-1][y] : "";
        return colorsNearby;
    }

    public String[][] getBoardColor() {
        return boardColor.clone();
    }

    public void setColorByCoordinate(int x, int y, String color) {
        assert "cyrpn".contains(color);
        boardColor[x][y] = color;
    }

    public String getColorByCoordinate(int x, int y) {
        return boardColor[x][y];
    }

    public Assam getAssamViaTunnel(Assam assam) {
        if (!tunnels.containsKey(assam.toString())) return assam;
        return Assam.fromString(tunnels.get(assam.toString()));
    }
}
