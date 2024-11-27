package cs3500.threetrios.controller;

import cs3500.threetrios.ai.PosnStrategy;
import cs3500.threetrios.card.COLOR;
import cs3500.threetrios.game.Game;
import cs3500.threetrios.game.ReadOnlyGameModel;

/**
 * Manages the behavior of an AI player in the
 * Three Trios game. Interacts with the game model using strategies to
 * decide and execute moves for the AI-controlled player.
 */
public class AIController {
  private final Game model;
  private final PosnStrategy strategy;
  private final COLOR playerColor;

  /**
   * Constructs an AIController.
   *
   * @param model       the game model to interact with
   * @param strategy    the strategy the AI will use to determine its moves
   * @param playerColor the color representing the AI player
   */
  public AIController(Game model, PosnStrategy strategy, COLOR playerColor) {
    this.model = model;
    this.strategy = strategy;
    this.playerColor = playerColor;
  }

  /**
   * Makes a move for the AI player using its defined strategy.
   * The AI will attempt to choose a valid position on the grid to place its card.
   *
   * @return an array of ints representing a move:
   *         - [0]: row index of move
   *         - [1]: column index of  move
   *         - [2]: the card index from the player's hand
   *         Returns null if the move is invalid or cannot be made.
   */
  public int[] makeMove() {
    if (model.getCurrentPlayer().getColor() == playerColor) {
      int[] move = strategy.choosePositions((ReadOnlyGameModel) model);
      if (move != null && move.length == 3) {
        try {
          return move;
        } catch (IllegalArgumentException e) {
          System.err.println(e.getMessage());
        }
      } else {
        System.err.println("AI failed to determine a valid move.");
      }
    }
    return null;
  }
}
