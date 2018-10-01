package general.gameBoard;

import java.util.Objects;

public class Participant{

    private String name;
    private boolean isHuman;
    private int turnsTaken;
    private int participantSymbol;

    public Participant(String name, boolean isHuman){
        this.name = name;
        this.isHuman = isHuman;
        this.turnsTaken = 0;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsHuman() {
        return isHuman;
    }

    public int getTurnsTaken() {
        return turnsTaken;
    }

    public void addTurnPlayed() {
        turnsTaken++;
    }

    public int getParticipantSymbol() {
        return participantSymbol;
    }

    public void clearTurns() {
        turnsTaken = 0;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return isHuman == that.isHuman &&
                turnsTaken == that.turnsTaken &&
                participantSymbol == that.participantSymbol &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isHuman, turnsTaken, participantSymbol);
    }
}
