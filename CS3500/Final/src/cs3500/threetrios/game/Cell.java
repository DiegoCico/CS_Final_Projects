package cs3500.threetrios.game;

import cs3500.threetrios.card.Card;

/**
 * Represents a cell on the game grid, either playable or non-playable.
 * Holds the card currently placed on it, if any.
 */
public class Cell {

  /**
   * ENUM class for define cell status.
   */
  public enum CellType {
    CARD_CELL,
    HOLE
  }

  private Card card;
  private final CellType type;

  /**
   * Constructor for Cell.
   * @param type the type of cell
   */
  public Cell(CellType type) {
    if (type == null) {
      throw new IllegalStateException("Card and cell type cannot be null");
    }
    this.type = type;
    this.card = null;
  }


  /**
   * Constructor for Cell.
   * @param card ca card
   * @param type type of card
   */
  public Cell(Card card, CellType type) {
    if (card == null || type == null) {
      throw new IllegalStateException("Card and cell type cannot be null");
    }
    this.card = card;
    this.type = type;
  }


  /**
   * Checks if a cell is a CARD_CELL or not.
   * @return the type of cell it is
   */
  public boolean isCardCell() {
    return this.type == CellType.CARD_CELL;
  }

  /**
   * Method to place a card in the cell only if it is
   * a CARD_CELL.
   * @param card using.
   */
  public void placeCard(Card card) {
    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null");
    }
    if (!isEmpty()) {
      throw new IllegalStateException("This cell already contains a card");
    }
    if (isCardCell()) {
      this.card = card;
    } else {
      throw new IllegalStateException("Cannot place a card in a non-card cell");
    }
  }

  /**
   * Gets the card in the cell.
   * @return the card
   */
  public Card getCard() {
    return this.card;
  }

  /**
   * Checks if a cell is empty.
   * @return true or false if it is empty or not
   */
  public boolean isEmpty() {
    return this.card == null;
  }

  /**
   * Gets the cell type.
   * @return type.
   */
  public CellType getType() {
    return this.type;
  }

  /**
   * to string method.
   * @return string.
   */
  public String toString() {
    return this.card.toString();
  }
}
