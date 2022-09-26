
import src.BackEnd.Interlocking;
import src.BackEnd.Section;
import src.BackEnd.Train;
import src.utils.Pair;

import java.util.*;

public class InterlockingImpl implements Interlocking {
    List<Section> sections;
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

    public InterlockingImpl() {
        sections = new ArrayList<>();
        for (int i = 1; i < 12; i++) {
            sections.add(new Section(i));
        }
        trains = new HashMap<>();
    }

    /**
     * Adds a train to the rail corridor.
     *
     * @param   trainName A String that identifies a given train. Cannot be the same as any other train present.
     * @param   entryTrackSection The id number of the track section that the train is entering into.
     * @param   destinationTrackSection The id number of the track section that the train should exit from.
     * @throws  IllegalArgumentException
     *              if the train name is already in use, or there is no valid path from the entry to the destination
     * @throws  IllegalStateException
     *              if the entry track is already occupied
     */
    @Override
    public void addTrain(String trainName, int entryTrackSection, int destinationTrackSection)
            throws IllegalArgumentException, IllegalStateException {
        Train newTrain = new Train(trainName, entryTrackSection, destinationTrackSection);
        sections.get(entryTrackSection).addTrain(newTrain);
        trains.put(trainName, newTrain);
    }

    /**
     * The listed trains proceed to the next track section.
     * Trains only move if they are able to do so, otherwise they remain in their current section.
     * When a train reaches its destination track section, it exits the rail corridor next time it moves.
     *
     * @param   trainNames The names of the trains to move.
     * @return  The number of trains that have moved.
     * @throws  IllegalArgumentException
     *              if the train name does not exist or is no longer in the rail corridor
     */
    @Override
    public int moveTrains(String[] trainNames) throws IllegalArgumentException {
        return 0;
    }

    /**
     * Returns the name of the Train currently occupying a given track section
     *
     * @param   trackSection The id number of the section of track.
     * @return  The name of the train currently in that section, or null if the section is empty/unoccupied.
     * @throws  IllegalArgumentException
     *              if the track section does not exist
     */
    @Override
    public String getSection(int trackSection) throws IllegalArgumentException {
        if (!sections.contains(trackSection)){
            throw new IllegalArgumentException("Track section does not exist");
        }
        return sections.get(trackSection).getTrain();
    }


    /**
     * Returns the track section that a given train is occupying
     *
     * @param   trainName The name of the train.
     * @return  The id number of section of track the train is occupying, or -1 if the train is no longer in the rail corridor
     * @throws  IllegalArgumentException
     *              if the train name does not exist
     */
    @Override
    public int getTrain(String trainName) throws IllegalArgumentException {
        if (!Train.allTrains.contains(trainName)){
            throw new IllegalArgumentException("Train name does not exist");
        }
        return trains.get(trainName).getSection();
    }
}
