package comp1110.ass2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DirectionTest {
    @Test
    public void testGetSymbol(){
        Assertions.assertEquals('N', Direction.NORTH.getSymbol());
        Assertions.assertEquals('E', Direction.EAST.getSymbol());
        Assertions.assertEquals('S', Direction.SOUTH.getSymbol());
        Assertions.assertEquals('W', Direction.WEST.getSymbol());
    }

    @Test
    public void testToString(){
        Assertions.assertEquals("North", Direction.NORTH.toString());
        Assertions.assertEquals("East", Direction.EAST.toString());
        Assertions.assertEquals("South", Direction.SOUTH.toString());
        Assertions.assertEquals("West", Direction.WEST.toString());
    }

    @Test
    public void  testFromSymbol(){
        Assertions.assertEquals(Direction.NORTH, Direction.fromSymbol('N'));
        Assertions.assertEquals(Direction.EAST, Direction.fromSymbol('E'));
        Assertions.assertEquals(Direction.SOUTH, Direction.fromSymbol('S'));
        Assertions.assertEquals(Direction.WEST, Direction.fromSymbol('W'));

    }

}
