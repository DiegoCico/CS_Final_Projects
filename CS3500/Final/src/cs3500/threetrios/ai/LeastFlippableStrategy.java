package cs3500.threetrios.ai;

import cs3500.threetrios.card.Card;
import cs3500.threetrios.game.ReadOnlyGameModel;
import cs3500.threetrios.player.Player;

/**
 * An AI strategy that prioritizes finding the position where the least
 * amount of cards would be flipped over
 * if a card was placed in a specific spot.
 */
public class LeastFlippableStrategy implements PosnStrategy {

  /**
   * Chooses optimal position to place a card, reduce risk of card flips.
   * Evaluates each legal position on the grid to find the one with the
   * lowest potential flip risk based on the opponent's surrounding cards.
   *
   * @param game the current game state
   * @return an array of integers representing the row, column, and card
    *         index for the best move, or [-1, -1, -1] if no valid move is found.
   */
  @Override
  public int[] choosePositions(ReadOnlyGameModel game) {
    int[] bestMove = new int[3];
    int minFlipRisk = Integer.MAX_VALUE;

    Player player = game.getCurrentPlayer();

    for (int row = 0; row < game.getGrid().getRows(); row++) {
      for (int col = 0; col < game.getGrid().getCols(); col++) {
        if (game.isMoveLegal(row, col)) {
          for (int flip = 0; flip < player.getHand().size(); flip++) {
            Card card = player.getHand().get(flip);
            int flipRisk = calculateFlipRisk(game, row, col, card);
            if (flipRisk < minFlipRisk) {
              minFlipRisk = flipRisk;
              bestMove[0] = row;
              bestMove[1] = col;
              bestMove[2] = flip;
            }
          }
        }
      }
    }
    return minFlipRisk < Integer.MAX_VALUE ? bestMove : new int[] {-1, -1, -1};
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
   * Calculates the potential flip risk for placing a card at a specific
   * position on the grid. The flip risk is based on the opponent's adjacent cards
   * and their attack values compared to the card being placed.
   *
   * @param game the current game state
   * @param row the row position of the grid cell
   * @param col the column position of the grid cell
   * @param card the card to be placed
   * @return the calculated flip risk value
   */
  private int calculateFlipRisk(ReadOnlyGameModel game, int row, int col, Card card) {
    int risk = 0;

    int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    for (int dir = 0; dir < directions.length; dir++) {
      int adjRow = row + directions[dir][0];
      int adjCol = col + directions[dir][1];

      if (game.getGrid().validPosition(adjRow, adjCol)) {
        Card adjacentCard = game.getGrid().getCard(adjRow, adjCol);
        if (adjacentCard != null && adjacentCard.getColor() != card.getColor()) {
          int cardAttack = getAttackValue(card, dir);
          int adjAttack = getAttackValue(adjacentCard, (dir + 2) % 4); // Opposite direction

          if (adjAttack > cardAttack) {
            risk += adjAttack - cardAttack;
          }
        }
      }
    }

    return risk;
  }

  /**
   * Gets a certain attack value associated with a
   * Card and direction (ENUM).
   * @param card a game card
   * @param direction a direction
   * @return the value
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
