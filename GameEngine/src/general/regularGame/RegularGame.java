package general.regularGame;

import general.Exceptions.CantPopoutException;
import general.Exceptions.ColumnFullException;
import general.gameBoard.NinaBoard;
import general.gameBoard.Participant;
import general.gameBoard.Turn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class RegularGame{
    public static final int popoutGame = 0;
    public static final int regularGame = 1;
    public static final int circularGame = 2;

    protected String gameName;
    protected int N;
    protected NinaBoard gameBoard;
    protected boolean winnerFound = false;
    protected boolean isActive = false;
    protected List<Participant> allParticipants;
    protected List<Participant> originalParticipants;
    protected Participant currentParticipant = null;
    private int currentParticipantNumber = 0;
    protected static int noMove = -1;
    protected List<Participant> winners;
    protected int requiredParticipants;
    protected int currentParticipants;
    protected String uploader;

    public RegularGame(int n, int rows, int cols, int requiredParticipants, String name, String uploader) {
        N = n;
        this.allParticipants = new ArrayList<>();
        this.gameBoard = new NinaBoard(rows, cols);
        winners = new LinkedList<>();
        this.requiredParticipants = requiredParticipants;
        gameName = name;
        currentParticipants = 0;
        this.uploader = uploader;
    }

    public boolean isCurrentParticipantBot() {
        return !currentParticipant.getIsHuman();
    }

    public boolean isWinnerFound() {
        return winners.size() != 0;
    }

    public int getN() {
        return N;
    }

    public boolean moveIsValid(int col) {
        return !((getFirstOpenRow(col) < 0) || (gameBoard.getRows() < getFirstOpenRow(col)));
    }

    protected List<Integer> getPossibleMoves() {
        LinkedList<Integer> possibleMoves = new LinkedList<>();
        int numOfCols = gameBoard.getCols();

        for(int i = 0; i < numOfCols; i++){
            if(moveIsValid(i)){
                possibleMoves.addLast(i);
            }
        }

        return possibleMoves;
    }

    protected int getPossibleColumn() {
        List<Integer> possibleMoves = getPossibleMoves();

        if (possibleMoves.isEmpty()) {
            return noMove;
        } else {
            Random random = new Random();
            return possibleMoves.get(random.nextInt(possibleMoves.size()));
        }
    }

    protected int getFirstOpenRow(int column) {
        return gameBoard.getFirstOpenRow(column);
    }

    protected Turn implementTurn(int col) throws ColumnFullException {
        Turn currTurn;
        if(!gameBoard.isColFull(col)){
            int row = getFirstOpenRow(col);
            currTurn = new Turn(row, col, currentParticipant.getParticipantSymbol(), Turn.addDisk);

            if(currTurn != null) {
                int currParticipantSymbol = currentParticipant.getParticipantSymbol();
                gameBoard.applyTurn(currTurn, currParticipantSymbol);
                currentParticipant.addTurnPlayed();
            }
        } else {
            // TODO - Take care of full column
            throw new ColumnFullException();
        }
        return currTurn;
    }

    public void changeCurrentParticipant() {
        if(currentParticipantNumber == allParticipants.size()-1){
            currentParticipantNumber = 0;
        } else {
            currentParticipantNumber++;
        }

        currentParticipant = allParticipants.get(currentParticipantNumber);
    }

    protected void addWinner(int currentTile) {
        for(Participant participant : allParticipants){
            if(currentTile == participant.getParticipantSymbol()){
                if(!winners.contains(participant)) {
                    winners.add(participant);
                }
            }
        }
    }

    protected void checkForWinner(int row, int col, int currParticipantSymbol){
        checkForWinningRow(row, col, currParticipantSymbol);
        checkForWinningCol(row, col, currParticipantSymbol);
        checkForWinningAcrossLeft(row,col,currParticipantSymbol);
        checkForWinningAcrossRight(row,col,currParticipantSymbol);

        if(winnerFound){
            addWinner(currParticipantSymbol);
        }
    }

    // direction: /
    protected void checkForWinningAcrossRight(int row, int col, int currParticipantSymbol) {
        int currStreak = 1;
        boolean keepLooking = true;
        int lastRow = gameBoard.getRows()-1;
        int lastCol = gameBoard.getCols()-1;

        // Check the first way starting from the starting cell
        int currRow = row;
        int currCol = col;
        while((currRow < lastRow) && (currCol > 0) && (!winnerFound) && keepLooking){
            currRow++;
            currCol--;
            if(gameBoard.getTileSymbol(currRow,currCol) == currParticipantSymbol){
                currStreak++;
            } else {
                keepLooking = false;
            }

            if(currStreak == N){
                winnerFound = true;
            }
        }

        // Get back to the starting cell, check the other way
        keepLooking = true;
        currRow = row;
        currCol = col;
        while((currRow > 0) && (currCol < lastCol) && !winnerFound && keepLooking){
            currRow--;
            currCol++;
            if(gameBoard.getTileSymbol(currRow,currCol) == currParticipantSymbol){
                currStreak++;
            } else {
                keepLooking = false;
            }

            if(currStreak == N){
                winnerFound = true;
            }
        }
    }

    // direction: \
    protected void checkForWinningAcrossLeft(int row, int col, int currParticipantSymbol) {
        int currStreak = 1;
        boolean keepLooking = true;
        int lastRow = gameBoard.getRows()-1;
        int lastCol = gameBoard.getCols()-1;

        // Check the first way starting from the starting cell
        int currRow = row;
        int currCol = col;
        while((currRow < lastRow) && (currCol < lastCol) && (!winnerFound) && keepLooking){
            currRow++;
            currCol++;
            if(gameBoard.getTileSymbol(currRow,currCol) == currParticipantSymbol){
                currStreak++;
            } else {
                keepLooking = false;
            }

            if(currStreak == N){
                winnerFound = true;
                keepLooking = false;
            }
        }

        // Get back to the starting cell, check the other way
        keepLooking = true;
        currRow = row;
        currCol = col;
        while((currRow > 0) && (currCol > 0) && !winnerFound && keepLooking){
            currRow--;
            currCol--;
            if(gameBoard.getTileSymbol(currRow,currCol) == currParticipantSymbol){
                currStreak++;
            } else {
                keepLooking = false;
            }

            if(currStreak == N){
                winnerFound = true;
                keepLooking = false;
            }
        }
    }

    private void checkForWinningCol(int row, int col, int currParticipantSymbol) {
        int currStreak = 1;
        boolean keepLooking = true;
        int lastCol = gameBoard.getCols()-1;

        // Check the first way starting from the starting cell
        int currRow = row;
        int currCol = col;
        while((currCol < lastCol) && (!winnerFound) && keepLooking){
            currCol++;
            if(gameBoard.getTileSymbol(currRow,currCol) == currParticipantSymbol){
                currStreak++;
            } else {
                keepLooking = false;
            }

            if(currStreak == N){
                winnerFound = true;
                keepLooking = false;
            }
        }

        // Get back to the starting cell, check the other way
        keepLooking = true;
        currRow = row;
        currCol = col;
        while((currCol > 0) && !winnerFound && keepLooking){
            currCol--;
            if(gameBoard.getTileSymbol(currRow,currCol) == currParticipantSymbol){
                currStreak++;
            } else {
                keepLooking = false;
            }

            if(currStreak == N){
                winnerFound = true;
                keepLooking = false;
            }
        }
    }

    private void checkForWinningRow(int row, int col, int currParticipantSymbol) {
        int currStreak = 1;
        int lastRow = gameBoard.getRows()-1;
        boolean keepLooking = true;

        // Check the first way starting from the starting cell
        int currRow = row;
        int currCol = col;
        while((currRow < lastRow) && (!winnerFound) && keepLooking){
            currRow++;
            if(gameBoard.getTileSymbol(currRow,currCol) == currParticipantSymbol){
                currStreak++;
            } else {
                keepLooking = false;
            }

            if(currStreak == N){
                winnerFound = true;
                keepLooking = false;
            }
        }

        // Get back to the starting cell, check the other way
        keepLooking = true;
        currRow = row;
        currCol = col;
        while((currRow > 0) && !winnerFound && keepLooking){
            currRow--;
            if(gameBoard.getTileSymbol(currRow,currCol) == currParticipantSymbol){
                currStreak++;
            } else {
                keepLooking = false;
            }

            if(currStreak == N){
                winnerFound = true;
                keepLooking = false;
            }
        }
    }

    public int getRows() {
        return gameBoard.getRows();
    }

    public int getCols() {
        return gameBoard.getCols();
    }

    public int getGameType() {
        return regularGame;
    }

    public List<Participant> getParticipants() {
        return  allParticipants;
    }

    public Turn getParticipantTurn(int col, int turnType) throws ColumnFullException, CantPopoutException {
        Turn turnMade = implementTurn(col);

        checkForWinner(turnMade.getRow(), col, currentParticipant.getParticipantSymbol());

        return turnMade;
    }

    public String getCurrentPlayerName() {
        return currentParticipant.getName();
    }

    public void clearGame() {
        winnerFound = false;
        gameBoard.clear();
        if(winners != null) {
            winners.clear();
        }
        for(Participant participant : allParticipants){
            participant.clearTurns();
        }
    }

    public boolean drawReached() {
        return getPossibleColumn() == noMove;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public Turn getBotTurn() {
        Turn turnMade = null;

        int col = getPossibleColumn();

        try {
            turnMade = implementTurn(col);
        } catch (ColumnFullException e) { }

        checkForWinner(turnMade.getRow(), col, currentParticipant.getParticipantSymbol());

        if (!winnerFound) {
            changeCurrentParticipant();
        }

        return turnMade;
    }

    public void setActive() {
        isActive = true;
    }

    public void deactivateGame() {
        isActive = false;
    }

    public void resetTurns() {
        allParticipants = FXCollections.observableArrayList(originalParticipants);
        currentParticipantNumber = 0;
        currentParticipant = allParticipants.get(currentParticipantNumber);
    }

    public int getCurrentPlayerSymbol() {
        return currentParticipant.getParticipantSymbol();
    }

    public int getTileSymbol(int row, int col) {
        return gameBoard.getTileSymbol(row,col);
    }

    public void removeTile(int row, int col) {
        gameBoard.popOutTile(row, col);
    }

    public void cascadeTiles(int row, int col) {
        boolean winnersFound = false;

        while((row > 0) && gameBoard.getTileSymbol(row-1, col) != NinaBoard.getEmptyTile()){
            int currentTile = gameBoard.getTileSymbol(row-1, col);
            gameBoard.dropTile(row-1, col);

            checkForWinner(row, col, currentTile);

            if(winnerFound){
                for(Participant participant : allParticipants){
                    if(currentTile == participant.getParticipantSymbol()){
                        if(!winners.contains(participant)) {
                            winners.add(participant);
                        }
                    }
                }
                winnersFound = true;
                winnerFound = false;
            }

            row--;
        }

        if(winnersFound){
            winnerFound = true;
        }
    }

    public void removeCurrentPlayer() {
        allParticipants.remove(currentParticipant);

        if(!allParticipants.isEmpty()) {
            currentParticipant = allParticipants.get(currentParticipantNumber);
        }
    }

    public ObservableList<Participant> getWinners(){
        return FXCollections.observableArrayList(winners);
    }

    public int getNumOfParticipants() {
        return allParticipants.size();
    }

    public int getRequiredNumOfParticipants() {
        return requiredParticipants;
    }

    public GameDetails getGameForAjax(){
        return new GameDetails(gameName, uploader, getRows(), getCols(), getN(), currentParticipants, requiredParticipants, isActive, this.getGameType());
    }

    public String getUploader() {
        return uploader;
    }

    public void addParticipant(Participant newParticipant){
        newParticipant.setParticipantSymbol(allParticipants.size()+1);

        if(!isActive) {
            System.out.println("Adding player " + newParticipant);
            allParticipants.add(newParticipant);
            currentParticipants++;

            if (currentParticipants == requiredParticipants) {
                originalParticipants = FXCollections.observableArrayList(allParticipants);
                isActive = true;
            }

            currentParticipant = allParticipants.get(currentParticipantNumber);
        } else {
            System.out.println("Adding spectator " + newParticipant);
        }
    }

    public boolean isCurrentParticiapnt(Participant myParticiapnt) {
        return currentParticipant.equals(myParticiapnt);
    }

    public GameRepresentation getRepresentation(){
        int rows = this.gameBoard.getRows();
        int cols = this.gameBoard.getCols();

        String variant;

        if(this.getGameType() == RegularGame.regularGame){
            variant = "Regular";
        } else if (this.getGameType() == RegularGame.popoutGame){
            variant = "Popout";
        } else {
            variant = "Circular";
        }

        int[][] board = gameBoard.getBoardTiles();

        return new GameRepresentation(rows, cols, variant, board);
    }

    public int[][] getGameBoard() {
        return gameBoard.getBoardTiles();
    }

    public List<String> getWinnerNames() {
        List<String> winnerNames = new ArrayList<>();

        for(Participant participant : winners){
            winnerNames.add(participant.getName());
        }

        return winnerNames;
    }

    public void makeRegularMove(int column) throws ColumnFullException, CantPopoutException {
        Turn turn = getParticipantTurn(column, Turn.addDisk);

        if(isWinnerFound()){
            deactivateGame();
        } else {
            if(drawReached()){
                deactivateGame();
            } else {
                changeCurrentParticipant();
            }
        }

        if(isCurrentParticipantBot() && getIsActive()){
            makeBotMove();
        }
    }

    public void makePopoutMove(int column) throws ColumnFullException, CantPopoutException {
        Turn turn = getParticipantTurn(column, Turn.removeDisk);

        if(isWinnerFound()){
            if(getWinners().size() == 1){
                // single winner
            } else {
                // multiple winners
            }
            deactivateGame();
        } else {
            if(drawReached()){
                deactivateGame();
            } else {
                changeCurrentParticipant();
            }
        }

        if(isCurrentParticipantBot() && getIsActive()){
            makeBotMove();
        }
    }

    public void makeBotMove(){
        while(isCurrentParticipantBot() && getIsActive()){
            Turn turn = getBotTurn();

            if(isWinnerFound() || drawReached()){
                deactivateGame();
            }
        }
    }

    public class GameDetails{
        private String name;
        private String uploader;
        private String variant;
        private String players;
        private String dimensions;
        private String goal;
        private boolean isActive;

        private GameDetails(String name, String uploader, int rows, int cols, int goal, int currentPlayers, int requiredPlayers, boolean isActive, int gameType){
            this.name = name;
            this.uploader = uploader;
            dimensions = Integer.toString(rows) + " x " + Integer.toString(cols);
            this.goal = Integer.toString(goal);
            players = Integer.toString(currentPlayers) + "\\" + Integer.toString(requiredPlayers);
            this.isActive = isActive;

            if(gameType == RegularGame.popoutGame){
                variant = "Popout";
            } else if (gameType == RegularGame.regularGame){
                variant = "Regular";
            } else {
                variant = "Circular";
            }
        }
    }

    public class GameRepresentation{
        private int rows;
        private int cols;
        private String variant;
        private int[][] board;

        private GameRepresentation(int rows, int cols, String variant, int[][] board){
            this.rows = rows;
            this.cols = cols;
            this.variant = variant;
            this.board = board;
        }
    }
}
