package cs3500.threetrios.view;

import cs3500.threetrios.game.Cell;
import cs3500.threetrios.game.Grid;

/**
 * A text-based view of the game state,
 * providing a user-friendly console output
 * for debugging or non-GUI gameplay.
 */
public class TextView {
  private final Grid grid;

  /**
   * Constructor for a TextView.
   * @param grid game grid
   */
  public TextView(Grid grid) {
    this.grid = grid;
  }

  /**
   * This will render the view of the game.
   * @return game view
   */
  public String render() {
    StringBuilder sb = new StringBuilder();
    for (int row = 0; row < grid.getRows(); row++) {
      for (int col = 0; col < grid.getCols(); col++) {
        if (grid.getCellType(row, col) == Cell.CellType.HOLE) {
          sb.append("X ");
        } else if (grid.isEmpty(row, col)) {
          sb.append("_ ");
        } else {
          sb.append(grid.getCard(row, col).toString().charAt(0) + " ");
        }
      }
      sb.append("\n");
    }
    return sb.toString();
  }

}
