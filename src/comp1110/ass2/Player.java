package comp1110.ass2;

public class Player {
    private boolean isPlaying = false;
    private char color;//"c" represents cyan ，"y" represents yellow，"r" represents red，"p" represents purple
    private int dirhams; // the number of dirhams
    private int remainingRugs; // the number of remain rugs

    /**
     *
     * @param isPlaying Represents if the player is currently playing
     * @param color Represents the color of the player
     * @param dirhams The number of dirhams the player has
     * @param remainingRugs The number of remaining rugs the player has
     */

    public Player(boolean isPlaying, char color, int dirhams, int remainingRugs){
        this.isPlaying = isPlaying;
        this.color = color;
        this.dirhams = dirhams;
        this.remainingRugs = remainingRugs;
    }

    /**
     * Parses a player string and creates a PlayerPattern object from it.
     *
     * @param playerString The player string to parse
     * @return A PlayerPattern object created from the given player string
     * @throws IllegalArgumentException If the player string is invalid
     */
    public static Player fromString(String playerString) {
        if (playerString.length() != 8){
            throw new IllegalArgumentException("Invalid Player String: " + playerString);
        }
        else {
            boolean isPlaying = playerString.charAt(7) == 'i';
            char color = playerString.charAt(1);
            String dirhamsStr = playerString.substring(2, 5);
            int dirhams = Integer.parseInt(dirhamsStr);
            String remainingRugsStr = playerString.substring(5, 7);
            int remainingRugs = Integer.parseInt(remainingRugsStr);
            return new Player(isPlaying, color, dirhams, remainingRugs);
        }
    }//

    /**
     * Parses the player information from a game state string and creates an array of PlayerPattern objects.
     *
     * @param gameString
     * @return An array of PlayerPattern objects representing the players in the game
     */
    public static Player[] fromGameString(String gameString) {
        String[] playersString = gameString.split("A")[0].split("P");

        Player[] players = new Player[playersString.length - 1];

        for (int i = 1; i < playersString.length; i++) {
            String playerStr = playersString[i];
            if (playerStr.equals("")) continue;

            Player player = Player.fromString('P' + playerStr);
            players[i-1] = player;
        }
        return players;
    }

    public boolean isIsplaying() {
        return isPlaying;
    }

    public void setIsplaying(boolean isplaying) {
        this.isPlaying = isplaying;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }

    public int getDirhams() {
        return dirhams;
    }

    public void setDirhams(int dirhams) {
        this.dirhams = dirhams;
    }

    public int getRemainingRugs() {
        return remainingRugs;
    }

    public void setRemainingRugs(int remainingRugs) {
        this.remainingRugs = remainingRugs;
    }

    @Override
    public String toString() {
        return 'P'+""+this.getColor()+""+'0'+""+this.getDirhams()+""+this.getRemainingRugs()+(isPlaying?'i':'o');
    }
}

