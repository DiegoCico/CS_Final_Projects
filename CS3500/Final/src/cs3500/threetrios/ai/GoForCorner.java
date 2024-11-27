package cs3500.threetrios.ai;

import cs3500.threetrios.card.Card;
import cs3500.threetrios.game.Game;
import cs3500.threetrios.game.ReadOnlyGameModel;
import cs3500.threetrios.player.Player;

/**
 * An AI strategy that prioritizes placing cards in corner positions on the grid,
 * because they are harder for the opponent to flip.
 */
public class GoForCorner implements PosnStrategy {

  /**
   * Chooses a position in one of the corners to maximize resistance to flipping.
   *
   * @param game the current game state
   * @return an array containing the row, column, and card index for the best move
   */
  @Override
  public int[] choosePositions(ReadOnlyGameModel game) {
    int[] bestMove = new int[3];
    int bestResistance = -1;

    Player player = game.getCurrentPlayer();
    int[][] corners = {
            {0, 0},
            {0, game.getGrid().getCols() - 1},
            {game.getGrid().getRows() - 1, 0},
            {game.getGrid().getRows() - 1, game.getGrid().getCols() - 1}
    };

    for (int[] corner : corners) {
      int row = corner[0];
      int col = corner[1];
      if (game.isMoveLegal(row, col)) {
        for (int i = 0; i < player.getHand().size(); i++) {
          Card card = player.getHand().get(i);
          int resistance = calculateResistance(card, row, col, (Game) game);
          if (resistance > bestResistance) {
            bestResistance = resistance;
            bestMove[0] = row;
            bestMove[1] = col;
            bestMove[2] = i;
          }
        }
      }
    }

    return bestResistance >= 0 ? bestMove : new int[] {-1, -1, -1};
  }

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

  /**
   * Calculates the resistance of a corner
   * position based on the card's direction values.
   *
   * @param card the card to be placed
   * @param row the row position
   * @param col the column position
   * @param game the current game state
   * @return the calculated resistance value for the position
   */
  private int calculateResistance(Card card, int row, int col, Game game) {
    int maxRow = game.getGrid().getRows() - 1;
    int maxCol = game.getGrid().getCols() - 1;

    if (row == 0 && col == 0) {
      return card.getEast() + card.getSouth();
    }
    if (row == 0 && col == maxCol) {
      return card.getWest() + card.getSouth();
    }
    if (row == maxRow && col == 0) {
      return card.getNorth() + card.getEast();
    }
    if (row == maxRow && col == maxCol) {
      return card.getNorth() + card.getWest();
    }

    return 0;
  }
}
