package cs3500.threetrios.ai;

import cs3500.threetrios.card.Card;
import cs3500.threetrios.game.ReadOnlyGameModel;
import cs3500.threetrios.player.Player;

/**
 * An AI strategy based on the Minimax algorithm, aims to minimize the maximum
 * potential flips that the opponent can achieve on their next turn.
 */
public class MinMaxStrategy implements PosnStrategy {

  /**
   * Chooses the position that minimizes the potential flips for the opponent.
   *
   * @param game the current game state
   * @return an array containing the row, column, and card index for the chosen move
   */
  @Override
  public int[] choosePositions(ReadOnlyGameModel game) {
    int[] bestMove = new int[3];
    int minOpponentScore = Integer.MAX_VALUE;

    Player player = game.getCurrentPlayer();
    for (int row = 0; row < game.getGrid().getRows(); row++) {
      for (int col = 0; col < game.getGrid().getCols(); col++) {
        if (game.isMoveLegal(row, col)) {
          for (int cardIndex = 0; cardIndex < player.getHand().size(); cardIndex++) {
            int opponentBestScore = evaluateOpponentBestMove(game);

            if (opponentBestScore < minOpponentScore) {
              minOpponentScore = opponentBestScore;
              bestMove[0] = row;
              bestMove[1] = col;
              bestMove[2] = cardIndex;
            }
          }
        }
      }
    }
    return minOpponentScore < Integer.MAX_VALUE ? bestMove : new int[] {-1, -1, -1};
  }

  /**
   * Evaluates a position to determine the score based on potential flips of opponent cards.
   * This is a placeholder implementation that can be customized further.
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
    int[] opposingSides = {2, 3, 0, 1};

    for (int i = 0; i < directions.length; i++) {
      int adjRow = row + directions[i][0];
      int adjCol = col + directions[i][1];

      if (game.getGrid().validPosition(adjRow, adjCol)) {
        Card adjacentCard = game.getGrid().getCard(adjRow, adjCol);
        if (adjacentCard != null && adjacentCard.getColor()
                != game.getCurrentPlayer().getHand().get(cardIndex).getColor()) {
          int cardAttack = getAttackValue(game.getCurrentPlayer().getHand().get(cardIndex), adjRow);
          int adjAttack = getAttackValue(adjacentCard, opposingSides[i]);
          if (cardAttack > adjAttack) {
            score += cardAttack - adjAttack;
          }
        }
      }
    }
    return score;
  }

  /**
   * Evaluate the opponent's best move score after a simulated move.
   * @param game a simulated game state after the player has placed a card
   * @return the best score the opponent can achieve
   */
  private int evaluateOpponentBestMove(ReadOnlyGameModel game) {
    Player opponent = game.getCurrentPlayer();
    int maxScore = Integer.MIN_VALUE;

    for (int row = 0; row < game.getGrid().getRows(); row++) {
      for (int col = 0; col < game.getGrid().getCols(); col++) {
        if (game.isMoveLegal(row, col)) {
          for (int i = 0; i < opponent.getHand().size(); i++) {
            int score = evaluatePosition(game, row, col, i);
            if (score > maxScore) {
              maxScore = score;
            }
          }
        }
      }
    }
    return maxScore;
  }

  /**
   * Gets a certain attack value associated with a Card and direction.
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
      default: throw new IllegalArgumentException("Invalid direction " + direction);
    }
  }
}
