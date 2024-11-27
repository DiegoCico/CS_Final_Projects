package cs3500.threetrios.gui;


import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import cs3500.threetrios.card.COLOR;
import cs3500.threetrios.card.Card;
import cs3500.threetrios.game.Cell;
import cs3500.threetrios.game.ReadOnlyGameModel;

/**
 * A panel that visually represents the grid of the Three Trios game.
 * Responsible for rendering the cells, cards,
 * and other game elements on the grid.
 */
public class GridPanel extends JPanel {
  private final ReadOnlyGameModel model;

  /**
   * Constructs a GridPanel with the specified game model.
   *
   * @param model the game model
   */
  public GridPanel(ReadOnlyGameModel model) {
    this.model = model;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawGrid(g);
    drawCards(g);
    drawGameStatus(g);
  }

  /**
   * Draws the game grid on the panel, coloring each cell based on its type.
   * The grid is rendered as a square matrix, with each cell drawn as a square.
   * - Card cells are colored yellow to indicate they are playable positions.
   * - Hole cells are colored gray to indicate they are non-playable positions.
   * Grid lines are drawn to visually separate each cell, creating a board effect.
   * The cell size calculated based on the smaller dimension of the panel to ensure a square grid.
   *
   * @param g the Graphics object used to draw the grid
   */
  private void drawGrid(Graphics g) {
    int gridSize = model.getGridSize();
    int cellWidth = getWidth() / gridSize;
    int cellHeight = getHeight() / gridSize;

    for (int row = 0; row < gridSize; row++) {
      for (int col = 0; col < gridSize; col++) {
        Cell.CellType cellType = model.getGrid().getCellType(row, col);
        g.setColor(cellType == Cell.CellType.HOLE ? Color.GRAY : Color.YELLOW);
        g.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
      }
    }

    g.setColor(Color.BLACK);
    for (int i = 0; i <= gridSize; i++) {
      g.drawLine(0, i * cellHeight, getWidth(), i * cellHeight);
      g.drawLine(i * cellWidth, 0, i * cellWidth, getHeight());
    }
  }


  /**
   * Draws the cards on the grid.
   *
   * @param g the Graphics object
   */
  private void drawCards(Graphics g) {
    int gridSize = model.getGridSize();
    int cellWidth = getWidth() / gridSize;
    int cellHeight = getHeight() / gridSize;

    for (int row = 0; row < model.getGridSize(); row++) {
      for (int col = 0; col < model.getGridSize(); col++) {
        Card card = model.getCardAt(row, col);

        if (card != null) {
          g.setColor(card.getColor() == COLOR.RED ? Color.PINK : Color.CYAN);
          g.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);

          g.setColor(Color.BLACK);
          g.drawRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);

          g.drawString(getDisplayValue(card.getNorth()), col * cellWidth + cellWidth
                  / 2 - 5, row * cellHeight + 15);
          g.drawString(getDisplayValue(card.getSouth()), col * cellWidth + cellWidth
                  / 2 - 5, row * cellHeight + cellHeight - 5);
          g.drawString(getDisplayValue(card.getEast()), col * cellWidth + cellWidth - 15,
                  row * cellHeight + cellHeight / 2);
          g.drawString(getDisplayValue(card.getWest()), col * cellWidth + 5,
                  row * cellHeight + cellHeight / 2);
          g.drawString(card.getColor().toString(), col * cellWidth + cellWidth
                  / 2 - 10, row * cellHeight + cellHeight / 2 + 5);
        }
      }
    }
  }

  /**
   * Returns the display value of a card side, converting 10 to "A".
   *
   * @param value the side value of the card
   * @return "A" if the value is 10, otherwise the value as a string
   */
  private String getDisplayValue(int value) {
    return value == 10 ? "A" : String.valueOf(value);
  }

  /**
   * Displays the game status.
   *
   * @param g the Graphics object
   */
  private void drawGameStatus(Graphics g) {
    g.setColor(Color.BLACK);
    if (model.isGameOver()) {
      String status =
              model.getWinner().equals("Tie") ? "It's a tie!" : model.getWinner() + " wins!";
      g.drawString(status, getWidth() / 2, getHeight() / 2);
    }
  }
}