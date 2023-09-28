
# Test plan

## List of classes

* List below all classes in your implementation that should have unit tests.
* For each class, list methods that can be tested in isolation.
* For each class, if there are conditions on the class' behaviour that cannot
  be tested by calling one method in isolation, give at least one example of
  a test for such a condition.

Do **not** include in your test plan the `Marrakech` class or the predefined
static methods for which we have already provided unit tests.

---



Assam

-   [ ] ​    fromString()
-  test if string input fit the class Assam init method
-  length error "A25WW"
-  argument error: "B25W"
-
-   [ ] ​    fromGameString()
   test if string input fit the class Assam init method
-  length error "XXXXA25WWXXXX"
-  argument error: "XXXXB25WXXXX"
-   [ ] ​    getxCoordinate()
-  test if the assam is still in the board
-  outboundException:x=7

-   [ ] ​    getyCoordinate()
- - test if the assam is still in the board
-   outboundException: y=7

-   [ ] ​    testToString()
-   test when we use return method, assam return its Coordinate,direction by using the outputstring
    error: return assam hashcode
-   [ ] ​    rotateClockwise()
-   test if the assam can rotate correctly
-   



    Note: After calling rotateClockwise() and rotateCounterClockwise(), Assam object's direction has been changed. Within the tests, we validate whether the direction of Assam object after rotation aligns with expectation. For example, if the initial direction of Assam is 'North', and we call rotateClockWise(2) to represent a clockwise rotation of two steps, we expect the direction to become 'South'.

BiMap [DISCARD]

Board

-   [x] init()
    -   Test valid/ invalid initial
    -   length error: `n00n00n00n00n00n00n00`
    -   argument error: `y00x00x00`
-   [x] getColorsNearby()
    -   Testing whether the returned colors correct
    -   middle case
    -   edge case
    -   corner case
-   [x] getBoardColor()
    -   Testing whether the returned color matrix correct
    -   random case
-   [x] setColorByCoordinate()
    -   Testing whether the modified matrix meets the expectations
    -   random case
    -   middle case
    -   corner case
    -   color type error
    -   id error
-   [x] getColorByCoordinate()
    -   Testing whether the obtained colors match the expectations.
    -   random case
    -   index out of bound error
-   [x] getAssamViaTunnel()
    -   Checking if the Assam parameters passing through the channel match the expected parameters, including coordinates and orientation.
    -   middle case
    -   corner case
    -   edge case
    -   invalid Assam \<communicate with Assam class\>
-   [ ] placeRug()
    -   Checking if the Rug can be placed.
    -   invalid case
    -   two parts has same colors except `n`: False
    -   different colors: True
    -   invalid Rug \<communicate with Rug class\>
-   [ ] disusePlayer()
    -   Testing if the user exists, if they can be deleted, and whether the color matrix matches the expected result after deletion.
-   [ ] ~~testToString()~~
    -   No need to test
-   [ ] ~~showBoardColorInMatrix()~~
    -   No need to test

Direction

-   [ ]   getSymbol()

-   [ ]   testToString()

-   [ ]   getDescription()

-   [ ]   fromSymbol()


Marrakech [DONE]

Player

-   [ ]   fromString()
  -  test if string input fit the class Player init method
  -  length error ""Pr001803i"
  -  argument error: "Ar01803i"
-   [ ]   fromGameString()
  - Testing split of game string
  - identity error: "A25W"
-   [ ]   isIsplaying()

-   [ ]   setIsplaying()

-   [ ]   getColor()

-   [ ]   setColor()

-   [ ]   getDirhams()

-   [ ]   setDirhams()

-   [ ]   getRemainingRugs()

-   [ ]   setRemainingRugs()

-   [ ]   testToString()


Rug

-   [ ]   isCovered()

-   [ ]   setCovered()

-   [ ]   getColor()

-   [ ]   setColor()

-   [ ]   getID()

-   [ ]   setID()

-   [ ]   getFirstCoordinate()

-   [ ]   setFirstCoordinate()

-   [ ]   getSecondCoordinate()

-   [ ]   setSecondCoordinate()

-   [ ]   testToString()