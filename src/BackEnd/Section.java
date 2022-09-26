
package src.BackEnd;

public class Section {
    int sectionID;
    public Train train;
    boolean occupied;

    /**
     * Initialise a section
     *
     * @param sectionID name of the train section to add
     */
    public Section(int sectionID) {
        this.sectionID = sectionID;
        this.train = null;
        this.occupied = false;
    }

    /**
     * Check if any train is occupying the track
     *
     * @return
     */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * Add a train to the current track if permitted
     *
     * @param train Train object
     * @throws IllegalStateException if the train is occupied
     */
    public void addTrain(Train train) throws IllegalStateException {
        if (!isOccupied()) {
            this.train = train;
            this.occupied = true;
            return;
        }
        if (this.train == train) {
            return;
        }
        throw new IllegalStateException("Track " + sectionID + " is currently occupied.");
    }

    /**
     * Get name of the train currently occupying track
     *
     * @return trainName if there is a train on track, otherwise null
     */
    public String getTrain() {
        if (train != null) {
            return train.trainName;
        }
        return null;
    }

    /**
     * Move the train in the current section to the next scheduled section if permitted.
     * Reset track to empty state.
     */
    public void moveTrain() {
        if (this.train != null) {
            this.train.move();
            this.occupied = false;
            this.train = null;
        }
    }

}