import org.junit.Test;
import src.BackEnd.Train;

import static org.junit.Assert.*;

public class InterlockingImpl_Test {
    //Test exception raised when getting section that does not exist
    @Test(expected = IllegalArgumentException.class)
    public void testGetSectionIllegal1(){
        Interlocking network = new InterlockingImpl();
        network.getSection(12);
    }

    //Test all sections initially were unoccupied
    @Test
    public void testGetSection1(){
        Interlocking network = new InterlockingImpl();
        for (int track_id = 1; track_id <= 11; track_id++) {
            assertNull(network.getSection(track_id));
        }
    }

    //Test exception raised when getting train not in service
    @Test(expected = IllegalArgumentException.class)
    public void testGetTrainIllegal1(){
        Interlocking network = new InterlockingImpl();
        network.getTrain("zxc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTrainIllegal2(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("a",3,4);
        network.moveTrains(new String[]{"a"});
        network.moveTrains(new String[]{"a"});
        network.getTrain("a");
    }

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

    //Test train cannot be added to an occupied section
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
        Train.removeTrain("t19");
        network.addTrain("t19", 1, 9);
        network.moveTrains(new String[]{"t19"});
        network.moveTrains(new String[]{"t19"});
        assertEquals(network.getTrain("t19"),9);
        network.addTrain("t92", 9, 2);

    }

    //Test add train already in network
    @Test(expected = IllegalArgumentException.class)
    public void testAddTrainInNetwork1(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("abc",1,8);
        network.addTrain("abc",3,4);
    }

    //Test add train without valid path
    @Test(expected = IllegalArgumentException.class)
    public void testAddTrainInvalidPath1(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("ac",1,3);
    }

    //Test add trains that does not satisfy constraint
    //If train 3 4 is present, train 4 3 cannot enter
    @Test(expected = IllegalStateException.class)
    public void testAddConstraint1(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("a34", 3, 4);
        network.addTrain("a43", 4, 3);
    }

    //If train 4 3 is present train 3 4 cannot enter
    @Test(expected = IllegalStateException.class)
    public void testAddConstraint2(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("b34", 3, 4);
        network.addTrain("b43", 4, 3);
        Train.removeTrain("b34");
    }

    //If train 3->11 present, train 11->3 cannot enter
    @Test(expected = IllegalStateException.class)
    public void testAddConstraint3(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("a311", 3, 11);
        network.addTrain("a113", 11, 3);
        Train.removeTrain("a311");
        Train.removeTrain("a113");
    }

    //If train 3->11 present, train 11->3 cannot move
    //Second scenario when train 3 11 is at section 7
    @Test(expected = IllegalStateException.class)
    public void testAddConstraint4(){
        Interlocking network = new InterlockingImpl();
        Train.removeTrain("a311");
        network.addTrain("a311", 3, 11);
        network.moveTrains(new String[]{"a311"});
        network.addTrain("a113", 11, 3);
        Train.removeTrain("a311");
    }

    //If train 11->3 is present, train 3->11 cannot enter
    @Test(expected = IllegalStateException.class)
    public void testAddConstraint5(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("a113", 11, 3);
        network.addTrain("a311",  3,11);
        Train.removeTrain("a113");
    }

    //If train 11->3 is present, train 3->11 cannot enter
    @Test(expected = IllegalStateException.class)
    public void testAddConstraint6(){
        Interlocking network = new InterlockingImpl();
        Train.removeTrain("a113");
        network.addTrain("a113", 11, 3);
        network.moveTrains(new String[]{"a113"});
        network.addTrain("a311",  3,11);
        Train.removeTrain("a113");
        Train.removeTrain("a311");
    }

    //If train 1->9 is present, train 9->2 cannot enter
    @Test(expected = IllegalStateException.class)
    public void testAddConstraint7(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("a19", 1,9);
        network.addTrain("a92", 9,2);
    }

    //If train 1->9 is present, train 9->2 cannot enter
    @Test(expected = IllegalStateException.class)
    public void testAddConstraint8(){
        Interlocking network = new InterlockingImpl();
        Train.removeTrain("a19");
        network.addTrain("a19", 1,9);
        network.moveTrains(new String[]{"a19"});
        network.addTrain("a92", 9,2);
    }

    //Test basic train movements
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

    //Check that only trains with priority are allowed to move
    //Train 1 8 has priority over 3 4 so 3 4 cannot move - i.e. calling moveTrain 3 4 would not change its position
    //Train 34 can only move if moveTrain is called with both 34 and 18 or train 18 must be moved first
    @Test
    public void testMovePriority1(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("c34", 3, 4);
        network.addTrain("c18", 1, 8);
        network.moveTrains(new String[]{"c34"});
        assertEquals(network.getTrain("c34"),3);
        network.moveTrains(new String[]{"c34","c18"});
        assertEquals(network.getTrain("c34"),4);
        assertEquals(network.getTrain("c18"),5);
        network.moveTrains(new String[]{"c34","c18"});
        network.moveTrains(new String[]{"c18"});
    }

    //Same as test case 1, but now the priority train is from 1 to 9
    @Test
    public void testMovePriority2(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("c34", 3, 4);
        network.addTrain("c19", 1, 8);
        network.moveTrains(new String[]{"c34"});
        assertEquals(network.getTrain("c34"),3);
        network.moveTrains(new String[]{"c34","c19"});
        assertEquals(network.getTrain("c34"),4);
        assertEquals(network.getTrain("c19"),5);
        network.moveTrains(new String[]{"c34","c19"});
        network.moveTrains(new String[]{"c19"});
    }

    //Same as test case 1, but now the priority train is from 10 to 2 and is at junction
    @Test
    public void testMovePriority3(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("c34", 3, 4);
        network.addTrain("c102", 10, 2);
        network.moveTrains(new String[]{"c102"});
        assertEquals(network.getTrain("c102"),6);
        network.moveTrains(new String[]{"c34"});
        assertEquals(network.getTrain("c34"),3);
        network.moveTrains(new String[]{"c34","c102"});
        assertEquals(network.getTrain("c34"),4);
        assertEquals(network.getTrain("c102"),2);
        network.moveTrains(new String[]{"c34","c102"});
    }

    //Same as test case 1, but now the priority train is from 10 to 2 and is at junction
    @Test
    public void testMovePriority4(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("c34", 3, 4);
        network.addTrain("c92", 9, 2);
        network.moveTrains(new String[]{"c92"});
        assertEquals(network.getTrain("c92"),6);
        network.moveTrains(new String[]{"c34"});
        assertEquals(network.getTrain("c34"),3);
        network.moveTrains(new String[]{"c34","c92"});
        assertEquals(network.getTrain("c34"),4);
        assertEquals(network.getTrain("c92"),2);
        network.moveTrains(new String[]{"c34","c92"});
    }

    //Same as test case 1, but now there are two priority trains
    //One is from 10 to 2 and is at junction
    //Another is from 1 to 9 and is at junction
    @Test
    public void testMovePriority5(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("c34", 3, 4);
        network.addTrain("c92", 9, 2);
        network.addTrain("c18", 1, 8);
        network.moveTrains(new String[]{"c92"});
        assertEquals(network.getTrain("c92"),6);
        network.moveTrains(new String[]{"c34"}); //c34 cannot move because c92 and c18 are both at junction
        assertEquals(network.getTrain("c34"),3);
        network.moveTrains(new String[]{"c34","c92"}); //c34 cannot move because c18 is at junction
        assertEquals(network.getTrain("c34"),3);
        assertEquals(network.getTrain("c92"),2);
        network.moveTrains(new String[]{"c34","c92"}); //c34 still cannot move
        assertEquals(network.getTrain("c34"),3);
        network.moveTrains(new String[]{"c18"});
        network.moveTrains(new String[]{"c34"}); //c34 can now move because c18 is no longer at junction
        assertEquals(network.getTrain("c34"),4);
        network.moveTrains(new String[]{"c18"});
        network.moveTrains(new String[]{"c18","c34"});
    }

    //Test the second junction - train moving 18 and 102 has priority over 92
    //Test priority train is 10 2
    @Test
    public void testMovePriority6(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("c102", 10, 2);
        network.addTrain("c92", 9, 2);
        network.moveTrains(new String[]{"c92"});
        assertEquals(network.getTrain("c92"),9); //c92 cannot move because c102 has priority
        network.moveTrains(new String[]{"c102"});
        network.moveTrains(new String[]{"c92"});
        assertEquals(network.getTrain("c92"),9); //c92 cannot move because junction is occupied
        network.moveTrains(new String[]{"c102"});
        network.moveTrains(new String[]{"c92"}); //c92 can now move
        assertEquals(network.getTrain("c92"),6);
        network.moveTrains(new String[]{"c102"});
        network.moveTrains(new String[]{"c92"});
        assertEquals(network.getTrain("c92"),2);
        network.moveTrains(new String[]{"c92"});
    }

    //Test the second junction - train moving 18 and 102 has priority over 92
    //Test priority train is 1 8
    @Test
    public void testMovePriority7(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("c18", 1, 8);
        network.addTrain("c92", 9, 2);
        network.moveTrains(new String[]{"c18"});
        network.moveTrains(new String[]{"c92"});
        assertEquals(network.getTrain("c92"),9); //c92 cannot move because c18 has priority
        network.moveTrains(new String[]{"c18"});
        network.moveTrains(new String[]{"c92"});
        assertEquals(network.getTrain("c92"),6);
        network.moveTrains(new String[]{"c18"});
        network.moveTrains(new String[]{"c92"});
        assertEquals(network.getTrain("c92"),2);
        network.moveTrains(new String[]{"c92"});
    }

    //Test that train cannot move to an occupied track
    @Test
    public void testMovePriority8(){
        Interlocking network = new InterlockingImpl();
        network.addTrain("c18", 1, 8);
        network.moveTrains(new String[]{"c18"});
        network.addTrain("c182",1,8);
        network.moveTrains(new String[]{"c182"}); //Cannot move because section 5 is occupied
        assertEquals(network.getTrain("c182"),1);
        network.moveTrains(new String[]{"c18"});
        network.moveTrains(new String[]{"c182"}); //Can now move to section 5
        assertEquals(network.getTrain("c182"),5);
        network.moveTrains(new String[]{"c182"}); //Cannot move because section 8 is occupied
        assertEquals(network.getTrain("c182"),5);
        network.moveTrains(new String[]{"c18"});
        network.moveTrains(new String[]{"c182"}); //Can now move to section 5
        assertEquals(network.getTrain("c182"),8);
        network.moveTrains(new String[]{"c182"});
    }

    //Check that calling train that has left the system returns an error
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
