package general;

import general.gameBoard.Participant;
import general.regularGame.RegularGame;
import resources.generated.GameDescriptor;

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

    public synchronized List<RegularGame.GameDetails> getGames(){
        List<RegularGame.GameDetails> loadedGames = new ArrayList<>();

        for(RegularGame game : gamesList){
            loadedGames.add(game.getGameForAjax());
        }

        return Collections.unmodifiableList(loadedGames);
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

    public void addParticipantToGame(int gameNumber, Participant newParticipant) {
        RegularGame game = gamesList.get(gameNumber);

        game.addParticipant(newParticipant);
    }

    public synchronized int getGameType(int gameNum) {
        return gamesList.get(gameNum).getGameType();
    }

    public synchronized int getGameGoal(int gameNum) {
        return  gamesList.get(gameNum).getN();
    }

    //public synchronized int addParticipant
}
