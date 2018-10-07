package general;

import general.Exceptions.CantPopoutException;
import general.Exceptions.ColumnFullException;
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

    public String getCurrentPlayerName(int gameNum) {
        return gamesList.get(gameNum).getCurrentPlayerName();
    }

    public boolean isItMyTurn(int gameNum, Participant myParticiapnt) {
        return gamesList.get(gameNum).isCurrentParticiapnt(myParticiapnt);
    }

    public List<Participant> getListOfParticipants(int gameNum) {
        return gamesList.get(gameNum).getParticipants();
    }

    public int[][] getGameBoard(int gameNum) {
        return gamesList.get(gameNum).getGameBoard();
    }

    public int getColumns(int gameNum) {
        return gamesList.get(gameNum).getCols();
    }

    public int getRows(int gameNum) {
        return gamesList.get(gameNum).getRows();
    }

    public boolean isWinnerFound(int gameNum) {
        return gamesList.get(gameNum).isWinnerFound();
    }

    public List<String> getWinnerNames(int gameNum) {
        return gamesList.get(gameNum).getWinnerNames();
    }

    public String makeRegularMove(int gameNum, int column) {
        String result;
        try {
            gamesList.get(gameNum).makeRegularMove(column);
            result = "";
        } catch (ColumnFullException e) {
            System.out.println(column + " is full");
            result = "Column " + column + " is full. Please select a valid move.";
        } catch (CantPopoutException e) {
            result = "Error";
        }


        return result;
    }

    public String makePopoutMove(int gameNum, int column) {
        String result;
        try {
            gamesList.get(gameNum).makePopoutMove(column);
            result = "";
        } catch (ColumnFullException e) {
            result = "Column " + column + " is full. Please select a valid move.";
            System.out.println(column + " is full");
        } catch (CantPopoutException e) {
            result = "Column " + column + " can't be popped up by player.";
            System.out.println(column + " can't be popped up by player.");
        }

        return result;
    }

    public void removeParticipantFromGame(int gameNum, Participant participant){
        gamesList.get(gameNum).removePlayerFromGame(participant);
    }

    public boolean getIfMyTurn(int gameNum, String name) {
        return gamesList.get(gameNum).getIsMyTurn(name);
    }

    public String getVariant(int gameNum) {
        return gamesList.get(gameNum).getVariantName();
    }
}
