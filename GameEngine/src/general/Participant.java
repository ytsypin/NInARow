package general;

public class Participant {
    String name;
    boolean isHuman;

    public Participant(String name, boolean isHuman){
        this.name = name;
        this.isHuman = isHuman;
    }

    @Override
    public String toString() {
        return name + isHuman;
    }
}