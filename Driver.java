public class Driver {
    public static void main(String[] args) {
        Interlocking network = new InterlockingImpl();
        network.addTrain("t18",1,8);
        network.addTrain("t102", 10, 2);
        network.addTrain("t34", 3, 4);
        System.out.println(network.getTrain("t18"));
        System.out.println(network.getSection(1));
        System.out.println(network);
        network.moveTrains(new String[]{"t18","t34","t102"});
        System.out.println(network);
        network.moveTrains(new String[]{"t18","t34","t102"});
        System.out.println(network);
        network.moveTrains(new String[]{"t18","t34","t102"});
        System.out.println(network);
    }

}
