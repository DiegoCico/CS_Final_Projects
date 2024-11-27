package cs3500.threetrios.ai;


import cs3500.threetrios.game.ReadOnlyGameModel;
import cs3500.threetrios.player.Player;

/**
 * Represents an AI strategy where the player opts not to play if no valid moves
 * are available, or acts as a placeholder for no-move situations.
 */
public class NoPlay implements PosnStrategy {


  @Override
  public int[] choosePositions(ReadOnlyGameModel game) {
    Player player = game.getCurrentPlayer();

    for (int row = 0; row < game.getGrid().getRows(); row++) {
      for (int col = 0; col < game.getGrid().getCols(); col++) {
        if (game.isMoveLegal(row, col)) {
          return new int[]{row, col, 0};
        }
      }
    }

    return new int[]{-1, -1, -1};
  }

  // TODO: FOX THIS

  /**
   * Evaluates a position to determine the score based on potential flips of opponent cards.
   *
   * @param game the current game state
   * @param row the row position
   * @param col the column position
   * @param cardIndex the card to be placed
   * @return the score based on the number of opponent cards flipped
   */
  @Override
  public int evaluatePosition(ReadOnlyGameModel game, int row, int col, int cardIndex) {
    return 0;
  }
}
