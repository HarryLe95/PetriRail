
import src.BackEnd.Section;
import src.BackEnd.Train;
import src.utils.Pair;

import java.util.*;

public class InterlockingImpl implements Interlocking {
    HashMap<Integer, Section> sections;

    HashMap<String, Train> trains;

    HashMap<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> constraints = new HashMap<>(Map.ofEntries(
            Map.entry(Pair.of(4, 3), Set.of(Pair.of(3, 4))),
            Map.entry(Pair.of(3, 4), Set.of(Pair.of(4, 3))),
            Map.entry(Pair.of(3, 11), Set.of(Pair.of(11, 3), Pair.of(7, 3))),
            Map.entry(Pair.of(11, 3), Set.of(Pair.of(3, 11), Pair.of(7, 11)))
    ));

    HashMap<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> priority = new HashMap<>(Map.ofEntries(
            Map.entry(Pair.of(3, 4), Set.of(
                    Pair.of(1, 8),
                    Pair.of(1, 9),
                    Pair.of(6, 2)
            )),
            Map.entry(Pair.of(4, 3), Set.of(
                    Pair.of(1, 8),
                    Pair.of(1, 9),
                    Pair.of(6, 2)
            )),
            Map.entry(Pair.of(9, 6), Set.of(
                    Pair.of(5, 8),
                    Pair.of(10, 6)
            ))
    ));

    Set<Pair<Integer, Integer>> prioritySet;

    public InterlockingImpl() {
        sections = new HashMap<>();
        for (int i = 1; i < 12; i++) {
            sections.put(i, new Section(i));
        }
        trains = new HashMap<>();
        prioritySet = new HashSet<>();
        for (Set<Pair<Integer, Integer>> item : priority.values()) {
            prioritySet.addAll(item);
        }
    }

    private boolean hasPriorityTarget(Pair<Integer, Integer> key) {
        Set<Pair<Integer, Integer>> priorityTarget = priority.get(key);
        if (priorityTarget == null) {
            return false;
        }
        for (Pair<Integer, Integer> target : priorityTarget) {
            Section section = sections.get(target.first);
            int targetDestination = target.second;
            if (!section.isOccupied()) {
                continue;
            }
            if (section.train.getDestination() == targetDestination) {
                return true;
            }
        }
        return false;
    }

    private boolean isMovable(Train train) {
        Section nextSection = sections.get(train.getNextSection());
        Pair<Integer, Integer> key = Pair.of(train.getSection(), train.getDestination());
        //If nextSection is exit point -> Movable
        if (nextSection == null) {
            return true;
        }
        //If either nextSection is occupied or if a priority target is present -> Not movable
        if (nextSection.isOccupied() || hasPriorityTarget(key)) {
            return false;
        }
        return true;
    }

    private boolean moveTrain(Train train) {
        if (isMovable(train)) {
            Section currentSection = sections.get(train.getSection());
            Section nextSection = sections.get(train.getNextSection());
            currentSection.moveTrain();
            if (nextSection != null) {
                nextSection.addTrain(train);
            }
            return true;
        }
        return false;
    }

    /**
     * Adds a train to the rail corridor.
     *
     * @param trainName               A String that identifies a given train. Cannot be the same as any other train present.
     * @param entryTrackSection       The id number of the track section that the train is entering into.
     * @param destinationTrackSection The id number of the track section that the train should exit from.
     * @throws IllegalArgumentException if the train name is already in use, or there is no valid path from the entry to the destination
     * @throws IllegalStateException    if the entry track is already occupied
     */
    @Override
    public void addTrain(String trainName, int entryTrackSection, int destinationTrackSection)
            throws IllegalArgumentException, IllegalStateException {
        //Check if any constraint is violated
        Pair<Integer, Integer> key = Pair.of(entryTrackSection, destinationTrackSection);
        Set<Pair<Integer, Integer>> value = constraints.get(key);
        if (value != null) {
            for (Pair<Integer, Integer> c : value) {
                Section constraintSection = sections.get(c.first);
                int targetSection = c.second;
                if (constraintSection.isOccupied()) {
                    if (constraintSection.train.getDestination() == targetSection) {
                        throw new IllegalStateException("Constraint not met: trying to add a train heading for " +
                                destinationTrackSection + " from " + entryTrackSection + ", but a train heading for "
                                + targetSection + " is on " + c.first);
                    }
                }
            }
        }

        Train newTrain = new Train(trainName, entryTrackSection, destinationTrackSection);
        sections.get(entryTrackSection).addTrain(newTrain);
        trains.put(trainName, newTrain);
    }

    /**
     * The listed trains proceed to the next track section.
     * Trains only move if they are able to do so, otherwise they remain in their current section.
     * When a train reaches its destination track section, it exits the rail corridor next time it moves.
     *
     * @param trainNames The names of the trains to move.
     * @return The number of trains that have moved.
     * @throws IllegalArgumentException if the train name does not exist or is no longer in the rail corridor
     */
    @Override
    public int moveTrains(String[] trainNames) throws IllegalArgumentException {
        int count = 0;
        List<String> priorityNames = new ArrayList<>();
        List<String> nonPriorityNames = new ArrayList<>();
        //First pass - check illegal exception and prioritise priority sets.
        for (String name : trainNames) {
            //Check if train is in service
            if (!Train.allTrains.contains(name)) {
                throw new IllegalArgumentException("Train " + name + " is not in service.");
            }
            Train train = trains.get(name);
            Pair<Integer, Integer> key = Pair.of(train.getSection(), train.getDestination());
            if (prioritySet.contains(key)) {
                priorityNames.add(name);
                continue;
            }
            nonPriorityNames.add(name);
        }
        priorityNames.addAll(nonPriorityNames);
        //Second pass - move trains
        for (String name : priorityNames) {
            Train train = trains.get(name);
            boolean isMoved = moveTrain(train);
            if (isMoved) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the name of the Train currently occupying a given track section
     *
     * @param trackSection The id number of the section of track.
     * @return The name of the train currently in that section, or null if the section is empty/unoccupied.
     * @throws IllegalArgumentException if the track section does not exist
     */
    @Override
    public String getSection(int trackSection) throws IllegalArgumentException {
        if (!sections.containsKey(trackSection)) {
            throw new IllegalArgumentException("Track section does not exist");
        }
        return sections.get(trackSection).getTrain();
    }


    /**
     * Returns the track section that a given train is occupying
     *
     * @param trainName The name of the train.
     * @return The id number of section of track the train is occupying, or -1 if the train is no longer in the rail corridor
     * @throws IllegalArgumentException if the train name does not exist
     */
    @Override
    public int getTrain(String trainName) throws IllegalArgumentException {
        if (!Train.allTrains.contains(trainName)) {
            throw new IllegalArgumentException("Train name does not exist");
        }
        return trains.get(trainName).getSection();
    }

    public String printSection(int trackSection){
        if (getSection(trackSection)==null){
            return " ";
        }
        return getSection(trackSection);
    }
    /**
     * Print to console the network for debugging purposes
     */
    public String toString() {
        String repr = String.format("%-15s|%-15s|%-15s", " ", " ", " ");
        int underlineLength = repr.length();
        repr = repr + "\n";

        //A dotted line under header
        String underline = "";
        for (int i = 0; i < underlineLength; i++) {
            underline += "-";
        }
        underline += "\n";
        repr = underline;

        //Add Tracks
        repr = repr + String.format("%-15s|%-15s|%-15s",
                " ",
                "4: " + printSection(4),
                "8: " + printSection(8)) + "\n";
        repr = repr + String.format("%-15s|%-15s|%-15s",
                "1: " + printSection(1),
                "5: " + printSection(5),
                "9: " + printSection(9)) + "\n";
        repr = repr + String.format("%-15s|%-15s|%-15s",
                "2: " + printSection(2),
                "6: " + printSection(6),
                "10: " + printSection(10)) + "\n";
        repr = repr + String.format("%-15s|%-15s|%-15s",
                "3: " + printSection(3),
                "7: " + printSection(7),
                "11: " + printSection(11)) + "\n";
        repr = repr + underline;

        return repr;
    }

}
