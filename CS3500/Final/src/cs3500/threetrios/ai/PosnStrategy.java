package cs3500.threetrios.ai;

import cs3500.threetrios.game.ReadOnlyGameModel;

/**
 * An interface for strategies that involve specific
 * position-based logic for AI decision-making.
 */
public interface PosnStrategy {

  /**
   * Chooses a position in one of the corners to maximize resistance to flipping.
   *
   * @param game the current game state
   * @return an array containing the row, column, and card index for the best move
   */
  int[] choosePositions(ReadOnlyGameModel game);


  /**
   * Evaluates a position to determine the score based on potential flips of opponent cards.
   *
   * @param game the current game state
   * @param row the row position
   * @param col the column position
   * @param cardIndex the card to be placed
   * @return the score based on the number of opponent cards flipped
   */
  int evaluatePosition(ReadOnlyGameModel game, int row, int col, int cardIndex);
}
