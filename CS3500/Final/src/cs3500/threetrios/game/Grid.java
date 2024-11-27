package cs3500.threetrios.game;

import cs3500.threetrios.card.Card;

/**
 * interface for grid.
 * Takes care of all the grids, and checks condition.
 * whether to place a card get a card or check if it is empty.
 */
public interface Grid {

  /**
   * This will place the card at a specific location on the grid.
   * @param row grid row
   * @param col grid column
   * @param card the card
   */
  void placeCard(int row, int col, Card card);

  /**
   * Gets the card at a specific location.
   * @param row grid row
   * @param col grid column
   * @return the card at that location
   */
  Card getCard(int row, int col);

  /**
   * Check if a cell at a specific location is empty.
   * @param row grid row
   * @param col grid column
   * @return if cell is empty or not
   */
  boolean isEmpty(int row, int col);

  /**
   * Get the type of cell at a specific location.
   * @param row grid row
   * @param col grid column
   * @return the CellType
   */
  Cell.CellType getCellType(int row, int col);

  /**
   * Get the number of rows in the grid.
   * @return number of rows
   */
  int getRows();

  /**
   * Get the number of cols in the grid.
   * @return number of cols
   */
  int getCols();


  /**
   * checks if it is a valid position in the grid.
   * @param row row of grid.
   * @param col col of grid.
   * @return if the coordinates is a valid position.
   */
  boolean validPosition(int row, int col);

  /**
   * gets the area of the grid.
   * @return area of grid
   */
  int getNumCardsCells();

  /**
   * This will get all the cells in a grid.
   * @return a grid (2d array) of cells
   */
  Cell[][] getCells();

  /**
   * Adds a card cell or hole to game grid.
   * @param row a row
   * @param col a col
   */
  void setCellType(int row, int col, Cell.CellType type);

}
