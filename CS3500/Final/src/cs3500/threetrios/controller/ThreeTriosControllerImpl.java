package cs3500.threetrios.controller;

import java.util.HashSet;
import java.util.List;

import cs3500.threetrios.ai.PosnStrategy;
import cs3500.threetrios.card.COLOR;
import cs3500.threetrios.card.Card;
import cs3500.threetrios.game.Cell;
import cs3500.threetrios.game.Game;
import cs3500.threetrios.gui.Features;
import cs3500.threetrios.gui.ThreeTriosGameView;
import cs3500.threetrios.player.Player;

/**
 * Implementation of the Three Trios game controller,
 * responsible for managing user interactions,
 * processing game logic, and updating the view accordingly.
 */
public class ThreeTriosControllerImpl implements ThreeTriosGameController {
  private final Game model;
  private final ThreeTriosGameView view;
  private Card selectedCard = null;
  private final PosnStrategy strategy;

  /**
   * Constructs a controller with a strategy for AI moves.
   *
   * @param model the game model
   * @param view the game view
   * @param strategy the strategy controller for AI moves
   */
  public ThreeTriosControllerImpl(Game model, ThreeTriosGameView view, PosnStrategy strategy) {
    this.model = model;
    this.view = view;
    this.strategy = strategy;

    if (this.view != null) {
      this.view.setFeatures(new FeaturesImpl());
      this.view.refresh();
      this.view.displayCurrentPlayer(model.getCurrentPlayer().getName());
    }
  }

  /**
   * Constructs a controller for player-only moves.
   *
   * @param model the game model
   * @param view the game view
   */
  public ThreeTriosControllerImpl(Game model, ThreeTriosGameView view) {
    this(model, view, null);
  }

  /**
   * Handles the action when a cell on the game grid is clicked.
   *
   * @param row the row of the clicked cell.
   * @param col the column of the clicked cell.
   */
  @Override
  public void handleCellClick(int row, int col) {
    Player currentPlayer = model.getCurrentPlayerModel();
    try {
      Card cardToPlace = selectedCard != null ? selectedCard
              : (currentPlayer.getHand().isEmpty() ? null : currentPlayer.getHand().get(0));

      if (cardToPlace == null) {
        view.displayErrorMessage("No cards available for the current player.");
        return;
      }

      // Place the card and refresh the view
      model.placeCard(row, col, cardToPlace);
      model.battleCards(row, col, new HashSet<>());
      selectedCard = null;
      view.refresh();
      System.out.println(model.getGrid().getCard(row,col));
      System.out.println(row + " " + col);

      if (model.isGameOver()) {
        view.displayGameOverMessage();
        return;
      }

      model.switchTurns();
      view.displayCurrentPlayer(model.getCurrentPlayer().getName());

      // Check if the AI should play next
      if (strategy != null && model.getCurrentPlayer().getColor() == COLOR.BLUE) {
        handleAIMove();
      }
    } catch (IllegalStateException exception) {
      view.displayErrorMessage("Invalid move. Try again.");
    }
  }

  /**
   * Testing purposes.
   * @param cardIndx for testing.
   */
  public void setSelectedCard(int cardIndx) {
    selectedCard = model.getCurrentPlayer().getHand().get(cardIndx);
  }

  /**
   * Handles the action when a card is selected by the player.
   *
   * @param cardIndex the index of the selected card in the player's hand.
   */
  @Override
  public void handleCardSelection(int cardIndex) {
    Player currentPlayer = model.getCurrentPlayerModel();
    if (cardIndex < 0 || cardIndex >= currentPlayer.getHand().size()) {
      view.displayErrorMessage("Invalid card selection.");
      return;
    }

    selectedCard = currentPlayer.getHand().get(cardIndex);
    view.refresh();
  }

  /**
   * Handles the action when the players plays
   * it looks through all the strategies the AI has and
   * it picks the best one.
   *
   */
  @Override
  public void handleAIMove() {
    try {
      AIController aiController =
              new AIController(model, strategy, model.getCurrentPlayer().getColor());
      int[] move = aiController.makeMove();

      if (move == null) {
        int[] topRight = findTopRightAvailableMove(model);
        Card card = model.getCurrentPlayer().getCard(topRight[2]);
        model.placeCard(topRight[0], topRight[1], card);
      } else {
        Card card = model.getCurrentPlayer().getCard(move[2]);
        model.placeCard(move[0], move[1], card);
      }

      view.refresh();
      if (model.isGameOver()) {
        view.displayGameOverMessage();
      } else {
        model.switchTurns();
        view.displayCurrentPlayer(model.getCurrentPlayer().getName());
      }
    } catch (Exception e) {
      view.displayErrorMessage("AI encountered an issue: " + e.getMessage());
    }
  }

  /**
   * Finds the top-right available move on the grid.
   * Serves as a fallback for the AI.
   *
   * @param model the game model
   * @return an array containing the row, column, and card index for the move
   */
  private int[] findTopRightAvailableMove(Game model) {
    int numCols = model.getGrid().getCols();
    int numRows = model.getGrid().getRows();
    List<Card> hand = model.getCurrentPlayerModel().getHand();

    for (int row = 0; row < numRows; row++) {
      for (int col = numCols - 1; col >= 0; col--) {
        if (model.getGrid().getCellType(row, col) == Cell.CellType.CARD_CELL &&
                model.getGrid().isEmpty(row, col)) {

          for (int cardIndex = 0; cardIndex < hand.size(); cardIndex++) {
            if (model.isMoveLegal(row, col)) {
              return new int[] { row, col, cardIndex };
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * Implementation of the Features interface to handle
   * actions for the controller.
   */
  private class FeaturesImpl implements Features {

    /**
     * Handles the action when a cell on the game grid is clicked.
     *
     * @param row the row of the clicked cell.
     * @param col the column of the clicked cell.
     */
    @Override
    public void handleCellClick(int row, int col) {
      ThreeTriosControllerImpl.this.handleCellClick(row, col);
    }

    /**
     * Handles the action when a card is selected by the player.
     *
     * @param cardIndex the index of the selected card in the player's hand.
     */
    @Override
    public void handleCardSelection(int cardIndex) {
      ThreeTriosControllerImpl.this.handleCardSelection(cardIndex);
    }

    /**
     * Handles the action when the players plays
     * it looks through all the strategies the AI has and
     * it picks the best one.
     *
     */
    @Override
    public void handleAIMove() {
      ThreeTriosControllerImpl.this.handleAIMove();
    }
  }
}
