package comp1110.ass2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Random;


public class PlayerTest {
    /**
     * Generate a random playerString for testing
     *
     * @return randomly generated playerSting. e.g. "Pr00803i"
     */
    private String playerStringRandom() {
        Random random = new Random();
        char color = "cypr".charAt(random.nextInt(4));
        int dirhams = random.nextInt(1000);
        int remaingRugs = random.nextInt(16);
        boolean isPlaying = random.nextBoolean();

        return "P" + color + String.format("%03d", dirhams) + String.format("%02d", remaingRugs) + (isPlaying ? "i" : "o");
    }

    /**
     * Test the fromString method of Player. It checks if the fromString method correctly constructs a PlayPattern object from a playerString
     */
    @Test
    public void testFromStringTest() {
        for (int i = 1; i < 10; i++) {
            String playerString = playerStringRandom();
            Player playerPattern = Player.fromString(playerString);

            // Check if properties are correctly set
            Assertions.assertEquals(playerString.charAt(1), playerPattern.getColor().getSymbol().charAt(0));
            Assertions.assertEquals(Integer.parseInt(playerString.substring(2, 5)), playerPattern.getDirhams());
            Assertions.assertEquals(Integer.parseInt(playerString.substring(5, 7)), playerPattern.getRemainingRugs());
            Assertions.assertEquals(playerString.charAt(7) == 'i', playerPattern.isIsplaying());
        }
    }

    /**
     *Test the toString method of Player. It checks if the toString method correctly converts a PlayPattern object to a playerString
     */
    @Test
    public void testToString() {
        for (int i = 1; i < 10; i++) {
            String expectedPlayerString = playerStringRandom();
            Player playerPattern = Player.fromString(expectedPlayerString);
            String actualPlayerString = playerPattern.toString();

            // Check if the generated playerString matches the expected playerString
            Assertions.assertEquals(expectedPlayerString, actualPlayerString);
        }

    }


    /**
     * Test the fromGameString method of Player. It checks if the fromGameString method correctly parses a playerString and creates an array of PlayPattern objects
     */
    @Test
    public void  testFromGameString(){
        //Set a testing case
        String gameString = "Pc03013iPy02613iPp03013iPr03413oA51WBy02n00n00n00n00n00n00y02n00r02n00n00n00n00n00n00r02n00n00n00n00y00c00p01n00n00n00n00y00c00p01y04n00n00n00n00r03r03y04n00n00y03n00n00n00n00n00n00y03";

        Player[] playerPatterns = Player.fromGameString(gameString);

        String expectedPlayerString = gameString.split("A")[0];
        StringBuilder actualPlayerString = new StringBuilder();
        for (Player playerPattern : playerPatterns) {
            actualPlayerString.append(playerPattern.toString());
        }

        // Check if the generated playerString matches the expected playerString
        Assertions.assertEquals(expectedPlayerString, actualPlayerString.toString());
    }
}
