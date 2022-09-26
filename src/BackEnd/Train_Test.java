package src.BackEnd;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class Train_Test {
    //Test correct Initialisation of train path
    @Test
    public void testInit() {
        Train t1 = new Train("t1", 1, 8);
        Train t2 = new Train("t2", 9, 2);
        Train t3 = new Train("t3", 3, 4);
        Train t4 = new Train("t4", 4, 3);
        Train t5 = new Train("t5", 1, 9);
        Train t6 = new Train("t6", 10, 2);
        Train t7 = new Train("t7", 3, 11);
        Train t8 = new Train("t8", 11, 3);
    }

    //Test Exception thrown when intialising trains with same name:
    @Test(expected = IllegalArgumentException.class)
    public void testName1() {
        Train t1 = new Train("a1", 1, 9);
        Train t2 = new Train("a1", 1, 9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testName2() {
        Train t1 = new Train("b1", 1, 9);
        Train t2 = new Train("b2", 1, 9);
        Train t3 = new Train("b2", 1, 9);
    }

    //Test Exception thrown when intialising illegal path:
    @Test(expected = IllegalArgumentException.class)
    public void testPath1() {
        Train t1 = new Train("c1", 1, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPath2() {
        Train t1 = new Train("c1", 1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPath3() {
        Train t1 = new Train("c1", 1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPath4() {
        Train t1 = new Train("c1", 1, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPath5() {
        Train t1 = new Train("c1", 1, 7);
    }

    //Test Move - test train has moved from sections correctly
    @Test
    public void testMove() {
        Train t1 = new Train("z1", 1, 9);
        assertEquals(t1.getSection(), 1);
        t1.move();
        assertEquals(t1.getSection(), 5);
        t1.move();
        assertEquals(t1.getSection(), 9);
        t1.move();
        assertEquals(t1.getSection(), -1);
        assertFalse(Train.allTrains.contains(t1.trainName));
        Train t2 = new Train("z1", 9, 2);
        assertEquals(t2.getSection(), 9);
        t2.move();
        assertEquals(t2.getSection(), 6);
        t2.move();
        assertEquals(t2.getSection(), 2);
        t2.move();
        assertEquals(t2.getSection(), -1);
    }

    //Test Move - try to move a train out of system
    @Test(expected = IllegalArgumentException.class)
    public void testMoveIllegal() {
        Train t1 = new Train("z1", 1, 9);
        assertEquals(t1.getSection(), 1);
        t1.move();
        assertEquals(t1.getSection(), 5);
        t1.move();
        assertEquals(t1.getSection(), 9);
        t1.move();
        assertEquals(t1.getSection(), -1);
        t1.move();
    }
}