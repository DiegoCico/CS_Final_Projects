package cs3500.threetrios.controller;

/**
 * Represents the controller interface for the Three Trios game, responsible for handling
 * user interactions such as clicking on cells or selecting cards.
 */
public interface ThreeTriosGameController {

  /**
   * Handles the action when a cell on the game grid is clicked.
   *
   * @param row the row of the clicked cell.
   * @param col the column of the clicked cell.
   */
  void handleCellClick(int row, int col);

  /**
   * Handles the action when a card is selected by the player.
   *
   * @param cardIndex the index of the selected card in the player's hand.
   */
  void handleCardSelection(int cardIndex);

  /**
   * Handles the action when the players plays
   * it looks through all the strategies the AI has and
   * it picks the best one.
   *
   */
  void handleAIMove();

}
