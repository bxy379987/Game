package comp1110.ass2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    void initTest() {
        // CASE: length error
        try {
            Board board = new Board("");
            Assertions.fail("should accept IllegalArgumentException()");
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), IllegalArgumentException.class);
        }

        try {
            Board board = new Board("n00n00n00n00n00n00n00");
            Assertions.fail("should accept IllegalArgumentException()");
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), IllegalArgumentException.class);
        }
        // CASE: argument error
        try {
            //                           Error of "x" ↓
            Board board = new Board("y00x00x00x00n00n00n00" +
                    "y00n00r00n00n00n00n00" +
                    "n00n00r00n00n00n00n00" +
                    "y00c00p00n00n00n00n00" +
                    "y00c00p00y00n00n00n00" +
                    "n00r00r00y00n00n00y00" +
                    "n00n00n00n00n00n00y00");
            Assertions.fail("should accept IllegalArgumentException()");
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), IllegalArgumentException.class);
        }
    }
    @Test
    void getColorsNearby() {
        String[] colorsNearby;
        String[] expect;
        Board board = new Board("y00n00n00n00n00n00n00" +
                        "y00n00r00n00n00n00n00" +
                        "n00n00r00n00n00n00n00" +
                        "y00c00p00n00n00n00n00" +
                        "y00c00p00y00n00n00n00" +
                        "n00r00r00y00n00n00y00" +
                        "n00n00n00n00n00n00y00");
        // CASE: middle
        //  ↓x\y→
        //     "y00 n00 n00 n00 n00 n00 n00"
        //     "y00 n00 r00 n00 n00 n00 n00"
        //     "n00 n00 r00 n00 n00 n00 n00"
        //     "y00 c00 p00[n00]n00 n00 n00"
        //     "y00 c00 p00 y00 n00 n00 n00"
        //     "n00 r00 r00 y00 n00 n00 y00"
        //     "n00 n00 n00 n00 n00 n00 y00"
        //          ↑ left
        //     ← up * down →
        //          ↓ right
        colorsNearby = board.getColorsNearby(3, 3);
        expect = new String[]{"p", "y", "n", "n"};
        for (int idx = 0; idx < colorsNearby.length; idx++) {
            Assertions.assertEquals(colorsNearby[idx], expect[idx]);
        }
        // CASE: corner
        //  ↓x\y→
        //    "[y00]n00 n00 n00 n00 n00 n00"
        //     "y00 n00 r00 n00 n00 n00 n00"
        //     "n00 n00 r00 n00 n00 n00 n00"
        //     "y00 c00 p00 n00 n00 n00 n00"
        //     "y00 c00 p00 y00 n00 n00 n00"
        //     "n00 r00 r00 y00 n00 n00 y00"
        //     "n00 n00 n00 n00 n00 n00 y00"
        //          ↑ left
        //     ← up * down →
        //          ↓ right
        colorsNearby = board.getColorsNearby(0, 0);
        expect = new String[]{"", "y", "n", ""};
        for (int idx = 0; idx < colorsNearby.length; idx++) {
            Assertions.assertEquals(colorsNearby[idx], expect[idx]);
        }
        // CASE: edge
        //  ↓x\y→
        //     "y00 n00 n00 n00 n00 n00 n00"
        //     "y00 n00 r00 n00 n00 n00 n00"
        //     "n00 n00 r00 n00 n00 n00 n00"
        //     "y00 c00 p00 n00 n00 n00[n00]"
        //     "y00 c00 p00 y00 n00 n00 n00"
        //     "n00 r00 r00 y00 n00 n00 y00"
        //     "n00 n00 n00 n00 n00 n00 y00"
        //          ↑ left
        //     ← up * down →
        //          ↓ right
        colorsNearby = board.getColorsNearby(3, 6);
        expect = new String[]{"n", "n", "", "n"};
        for (int idx = 0; idx < colorsNearby.length; idx++) {
            Assertions.assertEquals(colorsNearby[idx], expect[idx]);
        }
    }

    @Test
    void getBoardColor() {
        Board board = new Board(
                "y29r25n00y07p11c07r06" +
                        "y29r24r18y12c17c26r06" +
                        "c20y05y24y16p22c26n00" +
                        "p18c10y24y16y06y17n00" +
                        "y00c00y08y04r23r21n00" +
                        "y21r28r03p21r23r20y03" +
                        "n00r28n00p21n00r20p17");
        String[][] expect = new String[][]{
                {"y", "r", "n", "y", "p", "c", "r"},
                {"y", "r", "r", "y", "c", "c", "r"},
                {"c", "y", "y", "y", "p", "c", "n"},
                {"p", "c", "y", "y", "y", "y", "n"},
                {"y", "c", "y", "y", "r", "r", "n"},
                {"y", "r", "r", "p", "r", "r", "y"},
                {"n", "r", "n", "p", "n", "r", "p"},
        };
        String[][] boardColor = board.getBoardColor();
        for (int x = 0; x < boardColor.length; x++) {
            for (int y = 0; y < boardColor[0].length; y++) {
                Assertions.assertEquals(boardColor[x][y], expect[x][y]);
            }
        }
    }

    @Test
    void setColorByCoordinate() {
        Board board = new Board(
                "y29r25n00y07p11c07r06" +
                        "y29r24r18y12c17c26r06" +
                        "c20y05y24y16p22c26n00" +
                        "p18c10y24y16y06y17n00" +
                        "y00c00y08y04r23r21n00" +
                        "y21r28r03p21r23r20y03" +
                        "n00r28n00p21n00r20p17");
        // CASE: corner
        Board boardExpect = new Board(
                "n00r25n00y07p11c07r06" +
                        "y29r24r18y12c17c26r06" +
                        "c20y05y24y16p22c26n00" +
                        "p18c10y24y16y06y17n00" +
                        "y00c00y08y04r23r21n00" +
                        "y21r28r03p21r23r20y03" +
                        "n00r28n00p21n00r20p17");
        board.setColorByCoordinate(0, 0, "n", 0);
        Assertions.assertEquals(board.toString(), boardExpect.toString());
        // CASE: edge
        boardExpect = new Board(
                "n00r25n00y07p11c07r06" +
                        "y29r24r18y12c17c26r06" +
                        "c20y05y24y16p22c26n00" +
                        "p18c10y24y16y06y17n00" +
                        "y00c00y08y04p02r21n00" +
                        "y21r28r03p21r23r20y03" +
                        "n00r28n00p21n00r20p17");
        board.setColorByCoordinate(4, 4, "p", 2);
        Assertions.assertEquals(board.toString(), boardExpect.toString());
        // CASE: error
        try {
            board.setColorByCoordinate(1, 2, "x", 2);
            Assertions.fail("should accept IllegalArgumentException()");
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), IllegalArgumentException.class);
        }

        try {
            board.setColorByCoordinate(3, 5, "x", -2);
            Assertions.fail("should accept IllegalArgumentException()");
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), IllegalArgumentException.class);
        }
    }

    @Test
    void getColorByCoordinate() {
        Board board = new Board(
                "y29r25n00y07p11c07r06" +
                        "y29r24r18y12c17c26r06" +
                        "c20y05y24y16p22c26n00" +
                        "p18c10y24y16y06y17n00" +
                        "y00c00y08y04r23r21n00" +
                        "y21r28r03p21r23r20y03" +
                        "n00r28n00p21n00r20p17");
        Assertions.assertEquals(board.getColorByCoordinate(0, 0), "y");
        Assertions.assertEquals(board.getColorByCoordinate(1, 1), "r");
        Assertions.assertEquals(board.getColorByCoordinate(4, 5), "r");
    }

    @Test
    void getAssamViaTunnel() {
        Board board = new Board();
        Assam assam;
        Assam assamExpect;
        // CASE: corner
        assam = new Assam(0, 0, Direction.NORTH);
        assamExpect = new Assam(1, 0, Direction.SOUTH);
        assam = board.getAssamViaTunnel(assam);
        Assertions.assertEquals(assam.toString(), assamExpect.toString());
        // CASE: middle
        assam = new Assam(3, 5, Direction.EAST);
        assamExpect = new Assam(3, 5, Direction.EAST);
        assam = board.getAssamViaTunnel(assam);
        Assertions.assertEquals(assam.toString(), assamExpect.toString());
        // CASE: edge
        assam = new Assam(6, 4, Direction.EAST);
        assamExpect = new Assam(6, 3, Direction.WEST);
        assam = board.getAssamViaTunnel(assam);
        Assertions.assertEquals(assam.toString(), assamExpect.toString());
    }

//    @Test
//    void placeRug() {
//    }
//
//    @Test
//    void disusePlayer() {
//    }
}