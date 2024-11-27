package cs3500.threetrios.gui;

/**
 * Represents the interface for the features or actions available in the Three Trios game.
 * This includes handling player and AI interactions, as well as card selection.
 */
public interface Features {

  /**
   * Handles the action when a cell on the game grid is clicked by a player.
   *
   * @param row the row of the clicked cell.
   * @param col the column of the clicked cell.
   */
  void handleCellClick(int row, int col);

  /**
   * Handles the action when an AI move is triggered.
   */
  void handleAIMove();

  /**
   * Handles the action when a card is selected by the player.
   *
   * @param cardIndex the index of the selected card in the player's hand.
   */
  void handleCardSelection(int cardIndex);
}
