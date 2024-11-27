package cs3500.threetrios.ai;

import cs3500.threetrios.game.ReadOnlyGameModel;

import java.util.List;

/**
 * A hybrid AI strategy that combines multiple strategies to determine the best move.
 * It uses a combination of strategies, such as Flip and MinMax, to select the highest
 * scoring move by evaluating each strategy’s best result and selecting the most advantageous.
 */
public class HybridStrategy implements PosnStrategy {

  private final List<PosnStrategy> strategies;

  /**
   * Constructs a HybridStrategy that combines multiple strategies.
   *
   * @param strategies a list of strategies to apply in sequence
   */
  public HybridStrategy(List<PosnStrategy> strategies) {
    this.strategies = strategies;
  }

  /**
   * Chooses a position based on the strategy that provides the best scoring move.
   *
   * @param game the current game state
   * @return an array containing the row, column, and card index for the chosen move
   */
  @Override
  public int[] choosePositions(ReadOnlyGameModel game) {
    int[] bestMove = new int[] {-1, -1, -1};
    int bestScore = Integer.MIN_VALUE;

    for (int row = 0; row < game.getGrid().getRows(); row++) {
      for (int col = 0; col < game.getGrid().getCols(); col++) {
        if (game.isMoveLegal(row, col)) {
          for (int cardIndex = 0;
               cardIndex < game.getCurrentPlayer().getHand().size(); cardIndex++) {
            int[] currentMove = new int[] {row, col, cardIndex};
            int currentScore = evaluateMove(game, currentMove);

            if (currentScore > bestScore) {
              bestScore = currentScore;
              bestMove = currentMove;
            }
          }
        }
      }
    }

    return bestMove;
  }

  /**
   * A helper method to evaluate a move's score by combining results from all strategies.
   * @param game the current game state
   * @param move the move represented as [row, column, card index]
   * @return the combined score of the move based on all strategies
   */
  public int evaluateMove(ReadOnlyGameModel game, int[] move) {
    if (move[0] == -1) {
      return Integer.MIN_VALUE;
    }

    int score = 0;
    for (PosnStrategy strategy : strategies) {
      score += strategy.evaluatePosition(game, move[0], move[1], move[2]);
    }
    return score;
  }

  /**
   * A placeholder method for evaluating a position within the hybrid strategy.
   * Each strategy should provide its own implementation.
   * @param game the current game state
   * @param row the row position
   * @param col the column position
   * @param cardIndex the index of the card in the player's hand
   * @return the score of the move, 0 by default
   */
  @Override
  public int evaluatePosition(ReadOnlyGameModel game, int row, int col, int cardIndex) {
    return 0;
  }
}
