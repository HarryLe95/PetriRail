import org.junit.Test;

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
}
