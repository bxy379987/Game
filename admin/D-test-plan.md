
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

-   [ ] ​    fromGameString()

-   [ ] ​    getxCoordinate()

-   [ ] ​    setxCoordinate()

-   [ ] ​    getyCoordinate()

-   [ ] ​    setyCoordinate()

-   [ ] ​    getDirection()

-   [ ] ​    setDirection()

-   [ ] ​    testToString()

-   [ ] ​    rotateClockwise()

-   [ ] ​    rotateCounterClockwise()

    Note: After calling rotateClockwise() and rotateCounterClockwise(), Assam object's direction has been changed. Within the tests, we validate whether the direction of Assam object after rotation aligns with expectation.

BiMap [DISCARD]

Board

-   [ ]   getColorsNearby()

-   [ ]   getBoardColor()

-   [ ]   setColorByCoordinate()

-   [ ]   getColorByCoordinate()

-   [ ]   getAssamViaTunnel()

-   [ ]   placeRug()

-   [ ]   disusePlayer()

-   [ ]   testToString()

-   [ ]   showBoardColorInMatrix()

Direction

-   [ ]   getSymbol()

-   [ ]   testToString()

-   [ ]   getDescription()

-   [ ]   fromSymbol()


Marrakech [DONE]

Player

-   [ ]   fromString()

-   [ ]   fromGameString()

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