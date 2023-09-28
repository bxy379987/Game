package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


class AssamTest {
    String assam ="A41W";

    @Test
    public void TestFirstCharacter(){
        assertTrue(assam.charAt(0)=='A');
    }
    @Test
    public void TestCoordinations(){
        assertTrue(assam.charAt(1)>='0'&&assam.charAt(1)<='6');
        assertTrue(assam.charAt(2)>='0'&&assam.charAt(2)<='6');
    }
    @Test
    public void TestDirections(){
        assertTrue(IsDirectionValid(assam.charAt(3)));
    }
    public static boolean IsDirectionValid(char ch){
        return ch == 'W' || ch == 'E' || ch == 'N' || ch == 'S';
    }


}