package general;

import general.regularGame.RegularGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameManager {
    private final List<RegularGame> gamesList;

    public GameManager(){ gamesList = new ArrayList<>();
    }

    public synchronized void addGame(RegularGame newGame){
        System.out.println("Adding Game: " + newGame);
        gamesList.add(newGame);
    }

    public synchronized List<RegularGame> getGames(){
        return Collections.unmodifiableList(gamesList);
    }

    public synchronized boolean isGameActive(int gameNumber){
        return gamesList.get(gameNumber).getIsActive();
    }

    public synchronized int getNumOfParticipants(int gameNumber){
        return gamesList.get(gameNumber).getNumOfParticipants();
    }

    public synchronized int getRequiredNumOfParticipants(int gameNumber){
        return gamesList.get(gameNumber).getRequiredNumOfParticipants();
    }
}
