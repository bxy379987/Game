package comp1110.ass2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board {
    int BOARD_WIDTH = 7;
    int BOARD_HEIGHT = 7;
    String[][] boardColor;
    Board() {
        boardColor = new String[BOARD_WIDTH][BOARD_HEIGHT];
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                boardColor[row][col] = "n";
            }
        }
    }

    Board(String boardState) {
        boardColor = new String[BOARD_WIDTH][BOARD_HEIGHT];
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                char color = boardState.toCharArray()[(row * BOARD_WIDTH + col) * 3];
//                System.out.print(color + " ");
                boardColor[row][col] = String.valueOf(color);
            }
//            System.out.println();
        }
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
}
