package cs3500.threetrios.game;

import cs3500.threetrios.card.Card;
import cs3500.threetrios.player.Player;

/**
 * Represents a read-only view of the GameModel, providing access to methods
 * that do not modify the game state. This interface allows viewing game details
 * like grid size, cards, players, and the game's win state without changing it.
 */
public interface ReadOnlyGameModel {

  /**
   * Gets the size of the game grid.
   *
   * @return the grid size as an integer.
   */
  int getGridSize();

  /**
   * Retrieves the card located at the specified row and column in the grid.
   *
   * @param row the row of the card's location.
   * @param col the column of the card's location.
   * @return the Card at the specified position, or null if none exists.
   */
  Card getCardAt(int row, int col);

  /**
   * Checks if the game has ended.
   *
   * @return true if the game is over; false otherwise.
   */
  boolean isGameOver();

  /**
   * Determines the winner of the game.
   *
   * @return a String representing the winner: "Red Wins", "Blue Wins", or "Tie".
   */
  String getWinner();

  /**
   * Retrieves the current player whose turn it is.
   *
   * @return the Player object of the current player.
   */
  Player getCurrentPlayer();

  /**
   * Checks if a move is legal at the specified position.
   *
   * @param row the row to check.
   * @param col the column to check.
   * @return true if the move is legal; false otherwise.
   */
  boolean isMoveLegal(int row, int col);

  /**
   * Gets an array of the players in the game.
   *
   * @return an array containing the Player objects in the game.
   */
  Player[] getPlayers();

  /**
   * Retrieves a copy of the current game grid.
   *
   * @return the Grid object representing the game grid.
   */
  Grid getGrid();
}
