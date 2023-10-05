## Code Review

Reviewed by: Hongjun Xu, u7733037

Reviewing code written by: Xiangyu Bao u7751023

​					collaboration: Yusi Zhong u7755061

Component: [src/comp1110/ass2/Marrakech.java · 7273a8f7bc557a9bd9f284b517f7b73e33caa317 · Xiangyu Bao / comp1110-ass2 · GitLab (anu.edu.au)](https://gitlab.cecs.anu.edu.au/u7751023/comp1110-ass2/-/blob/7273a8f7bc557a9bd9f284b517f7b73e33caa317/src/comp1110/ass2/Marrakech.java#L448-533)

```java
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
		
        // ...
        
    }
```

### Comments 

-   What are the best features of this code?
    -   Obtain the post-placement game state and present it in String format using the current game state in String format and the pre-placed Rug.
-   Is the code well-documented?
    -   In the function header, the expected functionality, implementation process, and important considerations have already been pre-documented. Additionally, between the lines of code, it is possible to provide brief descriptions of the primary purposes of the code within that interval based on its functionality and steps.
-   Is the program decomposition (class and method structure) appropriate?
    -   This method effectively allocates different functional requirements to various classes and functions for implementation. It includes checking if placement is legal `isPlacementValid`, verifying the validity of the object itself `isRugValid`, and directly applying the placement functionality to the placed class `boardEntity.setColorByCoordinate`. This makes the method more modular and reusable, enhancing the overall readability of the code.
-   Does it follow Java code conventions (for example, are methods and variables properly named), and is the style consistent throughout?
    -   The variable and method names follow the camelCase naming convention and are in compliance with the standards. The overall style is consistent, and in version control, the purpose of the push and the implemented functionality are well-documented. Besides, non-repetitive variable names do not require numerical suffixes.
-   If you suspect an error in the code, suggest a particular situation in which the program will not function correctly.
    -   This method serves as an intermediate layer, and therefore, any error situations that should be raised are typically handled when calling the lower-level classes. As a result, there are no obvious errors in this method.
