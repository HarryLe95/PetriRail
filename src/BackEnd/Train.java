package src.BackEnd;

import src.utils.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Train {
    public static HashMap<Pair<Integer, Integer>, List<Integer>> allPaths = new HashMap<>(Map.ofEntries(
            Map.entry(Pair.of(1, 8), List.of(1, 5, 8)),
            Map.entry(Pair.of(1, 9), List.of(1, 5, 9)),
            Map.entry(Pair.of(3, 4), List.of(3, 4)),
            Map.entry(Pair.of(4, 3), List.of(4, 3)),
            Map.entry(Pair.of(9, 2), List.of(9, 6, 2)),
            Map.entry(Pair.of(10, 2), List.of(10, 6, 2)),
            Map.entry(Pair.of(3, 11), List.of(3, 7, 11)),
            Map.entry(Pair.of(11, 3), List.of(11, 7, 3))
    ));

    public static HashSet<String> allTrains = new HashSet<>();

    public final String trainName;
    private final int start;
    private final int end;
    private int journeyIndex;

    /**
     * Initialise a new train
     *
     * @param trainName trainName
     * @param start     start section
     * @param end       destination section
     * @throws IllegalArgumentException if trying to initialise a train in service or
     *                                  if there is no path from start to end.
     */
    public Train(String trainName, int start, int end) throws IllegalArgumentException {
        if (!allPaths.containsKey(Pair.of(start, end))) {
            throw new IllegalArgumentException("No path exists between " + start + " and " + end);
        }
        if (allTrains.contains(trainName)) {
            throw new IllegalArgumentException("Train " + trainName + " is in service.");
        }
        this.trainName = trainName;
        this.start = start;
        this.end = end;
        this.journeyIndex = 0;
        allTrains.add(trainName);
    }

    /**
     * Get a list of section ID connecting start to end
     *
     * @return List<Integer> section path
     */
    public List<Integer> getPath() {
        return allPaths.get(Pair.of(this.start, this.end));
    }

    /**
     * Check if the current train is in service given a path index - i.e. on a section in the train network
     *
     * @param index a path index
     * @return true if train is in service
     */
    public boolean isInService(int index) {
        return index < this.getPath().size();
    }

    /**
     * Check if the current train is in service - i.e. on a section in the train network
     *
     * @return true if train is in service else false
     */
    public boolean isInService() {
        return isInService(journeyIndex);
    }

    /**
     * Get track section the train is occupying
     *
     * @return section ID if train is in service, else -1
     */
    public int getSection() {
        if (isInService()) {
            return getPath().get(journeyIndex);
        }
        return -1;
    }

    /**
     * Get the next track section the train is moving to
     *
     * @return section ID if train will be in service, else -1
     */
    public int getNextSection() {
        if (isInService(journeyIndex + 1)) {
            return getPath().get(journeyIndex + 1);
        }
        return -1;
    }

    /**
     * Get the destination of current train
     * @return destination section
     */
    public int getDestination(){
        return end;
    }

    /**
     * Move train to the next track section
     *
     * @throws IllegalArgumentException if trying to move a train not in the network
     */
    public void move() throws IllegalArgumentException {
        if (isInService()) {
            journeyIndex++;
            if (journeyIndex >= getPath().size()) {
                allTrains.remove(trainName);
            }
            return;
        }
        throw new IllegalArgumentException("Trying to move a train not in service.");
    }

    /**
     * Remove train from all trains
     */
    public static void removeTrain(String trainName){
        if (allTrains.contains(trainName)){
            allTrains.remove(trainName);
        }
    }
}