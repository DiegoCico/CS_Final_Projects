package cs3500.threetrios.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cs3500.threetrios.card.Card;
import cs3500.threetrios.player.Player;

/**
 * This creates a Mock of the game model implementing the interface Game
 * that can give simulated answers and that can record
 * a transcript of what methods were used.
 */
public class MockGameModel implements Game {

  private final List<String> methodCalls = new ArrayList<>();
  private boolean isMoveLegal = true;
  private boolean isGameOver = false;
  private String checkWinningCondition = "Tie";
  private final Player currentPlayer;
  private final Grid grid;
  private final Player[] players;
  private int turn = 0;

  private final List<String> checkedCoordinates = new ArrayList<>();

  /**
   * A mock constructor that sets all values for the game model.
   * @param currentPlayer the gameModel's current player
   * @param grid the gameModel grid
   * @param players the gameModel players
   */
  public MockGameModel(Player currentPlayer, Grid grid, Player[] players) {
    this.currentPlayer = currentPlayer;
    this.grid = grid;
    this.players = players;
  }

  /**
   * Sets the move a player wants to make as valid.
   * @param moveLegal a boolean to check if the move is legal.
   */
  public void setMoveLegal(boolean moveLegal) {
    this.isMoveLegal = moveLegal;
  }

  /**
   * Will set the game over if it is.
   * @param gameOver a boolean to check if game is over
   */
  public void setGameOver(boolean gameOver) {
    this.isGameOver = gameOver;
  }

  /**
   * This will set the winning condition of the mock gameModel.
   * @param winningCondition a winning condition
   */
  public void setWinningCondition(String winningCondition) {
    this.checkWinningCondition = winningCondition;
  }

  /**
   * Gets the game model method call.
   * @return an ArrayList of methodCalls
   */
  public List<String> getMethodCalls() {
    return new ArrayList<>(methodCalls);
  }

  /**
   * a helper method to manage logging, so no call is duplicated.
   * @param methodName the current game model method
   */
  private void logMethodCall(String methodName) {
    if (methodCalls.isEmpty() || !methodCalls.get(methodCalls.size() - 1).equals(methodName)) {
      methodCalls.add(methodName);
    }
  }


  /**
   * Checks the game model coordinates.
   * @return a list of checked coordinates for cards.
   */
  public List<String> getCheckedCoordinates() {
    return new ArrayList<>(checkedCoordinates);
  }

  /**
   * Chekcs of a game model move is legal.
   * @param row of the column.
   * @param col of the column.
   * @return if it is a valid move or not
   */
  @Override
  public boolean isMoveLegal(int row, int col) {
    checkedCoordinates.add("isMoveLegal(" + row + ", " + col + ")");
    methodCalls.add("isMoveLegal(" + row + ", " + col + ")");  // Make sure this line exists
    return isMoveLegal;
  }



  /**
   * will get the game model's current player.
   * @return the current player.s
   */
  @Override
  public Player getCurrentPlayerModel() {
    methodCalls.add("getCurrentPlayerModel()");
    return currentPlayer;
  }

  /**
   * gets who is the winner when the game ends.
   * @return the winner.
   */
  @Override
  public String getWinner() {
    methodCalls.add("getWinner()");
    return checkWinningCondition;
  }

  /**
   * switches the player turn.
   */
  @Override
  public void switchTurns() {
    methodCalls.add("switchTurns");
    turn = 1 - turn;
  }

  /**
   * batlles all the sorrounding cards and the connecting cards.
   * @param row the row where the card is located
   * @param col the column where the card is located
   * @param cards that been already checked.
   */
  @Override
  public void battleCards(int row, int col, Set<Card> cards) {
    methodCalls.add("battleCards(" + row + ", " + col + ")");
  }

  /**
   * places the card in the game.
   * @param row  the row to place the card
   * @param col  the column to place the card
   * @param card the card to place
   */
  @Override
  public void placeCard(int row, int col, Card card) {
    methodCalls.add("placeCard(" + row + ", " + col + ", " + card + ")");
  }

  /**
   * generates and creates all the cards.
   * @return the cards for the game.
   */
  @Override
  public List<Card> getCards() {
    methodCalls.add("getCards");
    return new ArrayList<>();
  }

  /**
   * gets the current player.
   * @return current player.
   */
  @Override
  public Player getCurrentPlayer() {
    methodCalls.add("getCurrentPlayer");
    return currentPlayer;
  }

  /**
   * gets the grid in the game.
   * @return the grid
   */
  @Override
  public Grid getGrid() {
    methodCalls.add("getGrid");
    return grid;
  }

  /**
   * checks for whose turn is in the game.
   * @return gets the player turn.
   */
  @Override
  public int getTurn() {
    methodCalls.add("getTurn");
    return turn;
  }

  /**
   * gets the players in the game.
   * @return the players in the game.
   */
  @Override
  public Player[] getPlayers() {
    methodCalls.add("getPlayers");
    return players;
  }

  /**
   * checks the win game condition.
   * @return checks the game condition.
   */
  @Override
  public String checkWinCondition() {
    methodCalls.add("checkWinCondition");
    return checkWinningCondition;
  }

  /**
   * shows whether it is game over or not.
   * @return if the game is over or not.
   */
  @Override
  public boolean isGameOver() {
    methodCalls.add("isGameOver");
    return isGameOver;
  }


}
