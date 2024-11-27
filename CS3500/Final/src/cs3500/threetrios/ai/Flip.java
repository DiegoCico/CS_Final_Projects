package cs3500.threetrios.ai;

import cs3500.threetrios.card.Card;
import cs3500.threetrios.game.ReadOnlyGameModel;
import cs3500.threetrios.player.Player;

/**
 * An AI strategy that focuses on maximizing
 * the number of opponent cards flipped
 * when placing a card on the grid.
 */
public class Flip implements PosnStrategy {

  /**
   * Chooses the best position for the AI player to maximize
   * the number of opponent cards flipped.
   *
   * @param game the current game state
   * @return an array containing the row, column, and card index for the best move
   */
  @Override
  public int[] choosePositions(ReadOnlyGameModel game) {
    int[] play = new int[3];
    int bestScore = -1;

    Player player = game.getCurrentPlayer();
    for (int row = 0; row < game.getGrid().getRows(); row++) {
      for (int col = 0; col < game.getGrid().getCols(); col++) {
        if (game.isMoveLegal(row, col)) {
          for (int i = 0; i < player.getHand().size(); i++) {
            int score = evaluatePosition(game, row, col, i);
            if (score > bestScore) {
              bestScore = score;
              play[0] = row;
              play[1] = col;
              play[2] = i;
            }
          }
        }
      }
    }
    return play;
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
    int score = 0;

    int[][] directions = {
            {-1, 0}, {0, 1}, {1, 0}, {0, -1}
    };

    for (int dir = 0; dir < directions.length; dir++) {
      int adjRow = row + directions[dir][0];
      int adjCol = col + directions[dir][1];

      if (game.getGrid().validPosition(adjRow, adjCol)) {
        Card adjacentCard = game.getGrid().getCard(adjRow, adjCol);
        if (adjacentCard != null && adjacentCard.getColor()
                != game.getCurrentPlayer().getHand().get(cardIndex).getColor()) {
          int cardAttack = getAttackValue(game.getCurrentPlayer().getHand().get(cardIndex), dir);
          int adjAttack = getAttackValue(adjacentCard, (dir + 2) % 4);
          if (cardAttack > adjAttack) {
            score += cardAttack - adjAttack;
          }
        }
      }
    }
    return score;
  }

  /**
   * Gets a certain attack value associated with a Card and direction.
   *
   * @param card a game card
   * @param direction a direction (0 = North, 1 = South, 2 = East, 3 = West)
   * @return the value of the attack in that direction
   */
  private int getAttackValue(Card card, int direction) {
    switch (direction) {
      case 0: return card.getNorth();
      case 1: return card.getEast();
      case 2: return card.getSouth();
      case 3: return card.getWest();
      default: throw new IllegalArgumentException("Invalid direction");
    }
  }
}
