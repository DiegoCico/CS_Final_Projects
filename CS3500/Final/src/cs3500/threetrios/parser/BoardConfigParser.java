package cs3500.threetrios.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cs3500.threetrios.card.Card;
import cs3500.threetrios.card.CardModel;
import cs3500.threetrios.game.Cell;
import cs3500.threetrios.game.GameGrid;
import cs3500.threetrios.game.GameModel;
import cs3500.threetrios.game.Grid;

/**
 * Reads and processes configuration files to set up the game grid and cards,
 * parsing data into the appropriate game components.
 */
public class BoardConfigParser {

  /**
   * Parses a combined configuration file to create a Grid and a list of Cards.
   *
   * @return a GameModel initialized with parsed Grid and Cards
   * @throws FileNotFoundException if the file cannot be found
   * @throws IllegalStateException if the grid or card configuration is invalid
   */
  public static GameModel parseBoardConfig(String path) throws FileNotFoundException {
    File file = new File(path);
    Scanner scanner = new Scanner(file);
    Grid grid = null;
    List<Card> cards = new ArrayList<>();
    int rows = 0;
    int cardCellCount = 0;
    boolean parsingGridLayout = false;
    boolean parsingCardList = false;
    int currentRow = 0;
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine().trim();
      if (line.startsWith("#") || line.isEmpty()) {
        continue;
      }
      if (line.equals("GRID_LAYOUT")) {
        parsingGridLayout = true;
        continue;
      } else if (line.equals("CARD_LIST")) {
        parsingGridLayout = false;
        parsingCardList = true;
        continue;
      }
      if (line.startsWith("GRID_SIZE")) {
        String[] size = line.split(" ");
        rows = Integer.parseInt(size[1]);
        grid = new GameGrid(rows, Integer.parseInt(size[2]));
        continue;
      }
      if (parsingGridLayout && currentRow < rows) {
        cardCellCount = getCurrentRow(parsingGridLayout,
                currentRow, rows, line, grid, cardCellCount);
        currentRow++;
      }
      parseCardList(parsingCardList, line, cards);
    }
    scanner.close();
    validateConfig(grid, cardCellCount, cards);
    return new GameModel(grid, cards);
  }

  /**
   * Validates grid and card configuration.
   */
  private static void validateConfig(Grid grid, int cardCellCount, List<Card> cards) {
    if (grid == null) {
      throw new IllegalStateException("Invalid grid configuration in board config file.");
    }
    if (cardCellCount % 2 == 0) {
      throw new IllegalStateException("Grid must have an odd number of card cells.");
    }
    if (cards.size() < cardCellCount + 1) {
      throw new IllegalStateException("Insufficient cards. Required: " + (cardCellCount + 1 ));
    }
  }

  /**
   * Will get current row of game and parse through it, counting card cells.
   * @param parsingGridLayout boolean to determine action
   * @param currentRow current row
   * @param rows a row
   * @param line a string
   * @param grid a game grid
   * @param cardCellCount current count of card cells
   * @return updated card cell count
   */
  private static int getCurrentRow(boolean parsingGridLayout, int currentRow,
                                   int rows, String line, Grid grid, int cardCellCount) {
    if (parsingGridLayout && currentRow < rows) {
      for (int col = 0; col < line.length(); col++) {
        char cellType = line.charAt(col);
        if (cellType == 'C') {
          grid.setCellType(currentRow, col, Cell.CellType.CARD_CELL);
          cardCellCount++;
        } else if (cellType == 'X') {
          grid.setCellType(currentRow, col, Cell.CellType.HOLE);
        }
      }
    }
    return cardCellCount;
  }

  /**
   * Helper method to parse through card list.
   * @param parsingCardList boolean to determine what action to take.
   * @param line a string
   * @param cards list of cards
   * @throws IllegalArgumentException if the card data format is incorrect
   */
  private static void parseCardList(boolean parsingCardList,
                                    String line, List<Card> cards) {
    if (parsingCardList) {
      String[] cardData = line.split(" ");
      if (cardData.length != 5) {
        throw new IllegalArgumentException("Invalid card data format: " + line);
      }
      try {
        String name = cardData[0];
        int north = parseValue(cardData[1]);
        int south = parseValue(cardData[2]);
        int east = parseValue(cardData[3]);
        int west = parseValue(cardData[4]);

        Card card = new CardModel(name, north, south, east, west, null);
        cards.add(card);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Error parsing card: " + line + " - " +
                e.getMessage(), e);
      }
    }
  }

  /**
   * Converts a string value to an integer, treating "A" as 10.
   *
   * @param value the string to parse
   * @return the integer value
   */
  private static int parseValue(String value) {
    return value.equals("A") ? 10 : Integer.parseInt(value);
  }
}
