import org.junit.Test;
import src.BackEnd.Train;

import static org.junit.Assert.*;

public class InterlockingImpl_Test {
    @Test
    public void testInit(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("t18", 1, 8);
        assertEquals(network.getSection(1), "t18");
        assertEquals(network.getTrain("t18"), 1);
        network.moveTrains(new String[]{"t18"});
        assertNull(network.getSection(1));
        assertEquals(network.getSection(5),"t18");
        assertEquals(network.getTrain("t18"), 5);
        network.moveTrains(new String[]{"t18"});
        assertNull(network.getSection(1));
        assertNull(network.getSection(5));
        assertEquals(network.getSection(8),"t18");
        assertEquals(network.getTrain("t18"), 8);
        network.moveTrains(new String[]{"t18"});
    }

    @Test(expected = IllegalStateException.class)
    public void testAddTrainFull1(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("t18", 1, 8);
        network.addTrain("t19", 1, 9);
        network.moveTrains(new String[]{"t18"});
        network.moveTrains(new String[]{"t18"});
        network.moveTrains(new String[]{"t18"});
    }

    @Test(expected = IllegalStateException.class)
    public void testAddTrainFull2(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("a19", 1, 9);
        network.moveTrains(new String[]{"a19"});
        network.moveTrains(new String[]{"a19"});
        network.addTrain("t92", 9, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddTrainFull3(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("a34", 3, 4);
        network.addTrain("a43", 4, 3);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddTrainFull4(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("b34", 3, 4);
        network.addTrain("b43", 4, 3);
        Train.removeTrain("b34");
    }

    @Test
    public void testMove1(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("b34", 3, 4);
        network.moveTrains(new String[]{"b34"});
        assertEquals(network.getSection(4), "b34");
        assertEquals(network.getTrain("b34"),4);
        network.moveTrains(new String[]{"b34"});
        assertNull(network.getSection(3));
    }

    @Test
    public void testMove2(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("b43", 4, 3);
        assertNull(network.getSection(3));
        network.moveTrains(new String[]{"b43"});
        assertNull(network.getSection(4));
        assertEquals(network.getTrain("b43"),3);
        network.moveTrains(new String[]{"b43"});
    }

    @Test
    public void testMove3(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("b18", 1, 8);
        network.moveTrains(new String[]{"b18"});
        network.addTrain("b92", 9, 2);
        network.moveTrains(new String[]{"b18", "b92"});
        network.moveTrains(new String[]{"b18","b92"});
        network.moveTrains(new String[]{"b92"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMoveTrainNotInService1(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("b34", 3, 4);
        network.moveTrains(new String[]{"b34"});
        network.moveTrains(new String[]{"b34"});
        network.moveTrains(new String[]{"b34"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMoveTrainNotInService2(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("b43", 3, 4);
        network.moveTrains(new String[]{"b43"});
        network.moveTrains(new String[]{"b43"});
        network.moveTrains(new String[]{"b43"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMoveTrainNotInService3(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("b18", 1, 8);
        network.moveTrains(new String[]{"b18"});
        network.moveTrains(new String[]{"b18"});
        network.moveTrains(new String[]{"b18"});
        network.moveTrains(new String[]{"b18"});
    }
}
