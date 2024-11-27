package cs3500.threetrios.game;

import cs3500.threetrios.card.Card;

/**
 * Represents the game grid in the Three Trios Game,
 * where each cell can hold a card
 * or is a non-playable spot.
 * Uses a 2D array of {@link Cell} objects for layout and storage.
 * Coordinate System:
 * Origin - Top-left corner of the grid at (0, 0).
 * Axes - Rows increase downwards; columns increase to the right.
 * Storage - `cells[row][col]` where `row` is the first (outer)
 * index and `col` is the second (inner) index.
 */
public class GameGrid implements Grid {
  private final Cell[][] cells;
  private final int rows;
  private final int cols;

  /**
   * Constructs a GameGrid with a predefined array of cells.
   * @param row number of rows
   * @param col number of columns
   * @param cells array of cells
   */
  public GameGrid(int row, int col, Cell[][] cells) {
    /* INVARIANT: Grid rows and cells must be non-zero and positive. */
    if (row <= 0 || col <= 0) {
      throw new IllegalStateException("Invalid row or column");
    }
    if (cells == null) {
      throw new IllegalArgumentException("cells cannot be null");
    }
    this.rows = row;
    this.cols = col;
    this.cells = cells;

    initializeGrid();
  }

  /**
   * Creates a GameGrid from an existing Grid.
   * @param grid an existing grid
   */
  public GameGrid(Grid grid) {
    // INVARIANT:Grid cannot be null
    if (grid == null) {
      throw new IllegalArgumentException("grid cannot be null");
    }
    this.rows = grid.getRows();
    this.cols = grid.getCols();
    this.cells = grid.getCells();
  }

  /**
   * Creates an empty GameGrid with specified dimensions,
   * initializing each cell as CARD_CELL.
   * @param row number of rows
   * @param col number of columns
   */
  public GameGrid(int row, int col) {
    // INVARIANT: Grid rows and cells must be non-zero and positive
    if (row < 0 || col < 0) {
      throw new IllegalStateException("Invalid row or column");
    }
    this.rows = row;
    this.cols = col;
    this.cells = new Cell[row][col];
    initializeGrid();
  }

  /**
   * Initializes all cells as CARD_CELL by default.
   */
  public void initializeGrid() {
    /* INVARIANT: All cells are initialized as CARD_CELL by default */
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        cells[row][col] = new Cell(Cell.CellType.CARD_CELL);
      }
    }
  }

  /**
   * Gets the grid columns.
   * @return cols
   */
  @Override
  public int getCols() {
    return cols;
  }

  /**
   * Places a card in the specified cell if it's playable and empty.
   * @param row row index
   * @param col column index
   * @param card the card to place
   */
  @Override
  public void placeCard(int row, int col, Card card) {
    if (validPosition(row, col) && cells[row][col].isCardCell() &&
            cells[row][col].isEmpty()) {
      cells[row][col].placeCard(card);
      System.out.println(cells[row][col].toString());
    } else {
      System.out.println("Valid? " + validPosition(row, col));
      System.out.println(cells[row][col].toString());
      System.out.println(card.toString());
      throw new IllegalStateException("Invalid position " + row + "," + col);
    }
  }

  /**
   * Gets the grid rows.
   * @return rows
   */
  @Override
  public int getRows() {
    return rows;
  }


  /**
   * Checks if the specified coordinates are within bounds.
   * @param row row index
   * @param col column index
   * @return true if valid, false otherwise
   */
  @Override
  public boolean validPosition(int row, int col) {
    return row >= 0 && row < cells.length && col >= 0 && col < cells[0].length;
  }


  /**
   * gets the area of the grid.
   * @return area of grid
   */
  @Override
  public int getNumCardsCells() {
    return rows * cols;
  }


  /**
   * Gets the card at the specified cell, if any.
   * @param row row index
   * @param col column index
   * @return the card in the cell, or null if empty
   */
  @Override
  public Card getCard(int row, int col) {
    if (validPosition(row, col)) {
      return cells[row][col].getCard();
    } else {
      throw new IllegalStateException("Invalid position");
    }
  }

  /**
   * Checks if the cell at specified position is empty.
   * @param row row index
   * @param col column index
   * @return true if empty, false otherwise
   */
  @Override
  public boolean isEmpty(int row, int col) {
    if (validPosition(row, col)) {
      return cells[row][col].isEmpty();
    }
    return false;
  }

  /**
   * Gets the type of cell at specified position.
   * @param row row index
   * @param col column index
   * @return the cell type (CARD_CELL or HOLE)
   */
  @Override
  public Cell.CellType getCellType(int row, int col) {
    if (validPosition(row, col)) {
      return cells[row][col].getType();
    } else {
      throw new IllegalStateException("Invalid CellType");
    }
  }

  /**
   * Gets all the cells in the grid.
   * @return a copy of the grid
   */
  public Cell[][] getCells() {
    Cell[][] copy = new Cell[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Cell original = cells[i][j];
        copy[i][j] = original.isEmpty() ? new Cell(original.getType())
                : new Cell(original.getCard(), original.getType());
      }
    }
    return copy;
  }

  /**
   * This will make a cell - CARD_CELL or HOLE.
   * @param row specific row
   * @param col specific column
   * @param type the cell type ENUM
   */
  @Override
  public void setCellType(int row, int col, Cell.CellType type) {
    if (validPosition(row, col)) {
      cells[row][col] = new Cell(type);
    }
  }

  /**
   * Creates a copy of the game grid.
   * @return the copied Grid
   */
  public Grid copyGrid() {
    return new GameGrid(this);
  }


}
