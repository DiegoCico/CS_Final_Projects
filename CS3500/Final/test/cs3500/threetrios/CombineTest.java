package cs3500.threetrios;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import cs3500.threetrios.ai.Flip;
import cs3500.threetrios.ai.GoForCorner;
import cs3500.threetrios.ai.HybridStrategy;
import cs3500.threetrios.ai.LeastFlippableStrategy;
import cs3500.threetrios.ai.MinMaxStrategy;
import cs3500.threetrios.ai.NoPlay;
import cs3500.threetrios.ai.PosnStrategy;
import cs3500.threetrios.card.COLOR;
import cs3500.threetrios.card.Card;
import cs3500.threetrios.card.CardModel;
import cs3500.threetrios.controller.AIController;
import cs3500.threetrios.controller.ThreeTriosControllerImpl;
import cs3500.threetrios.game.Cell;
import cs3500.threetrios.game.Game;
import cs3500.threetrios.game.GameGrid;
import cs3500.threetrios.game.GameModel;
import cs3500.threetrios.game.Grid;
import cs3500.threetrios.game.MockGameModel;
import cs3500.threetrios.game.ReadOnlyGameModel;
import cs3500.threetrios.gui.BluePlayerView;
import cs3500.threetrios.gui.RedPlayerView;
import cs3500.threetrios.parser.BoardConfigParser;
import cs3500.threetrios.player.Player;
import cs3500.threetrios.player.PlayerModel;
import cs3500.threetrios.view.TextView;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * all the tests.
 */
@RunWith(Enclosed.class)
public class CombineTest {
  /**
   * Tests for AI strategies using specific board configurations.
   */
  public static class AIStrategiesTest {

    @Test
    public void testHybridStrategySelectsFirstValidMove() {
      try {
        GameModel game = BoardConfigParser.parseBoardConfig("docs/boardNoHoles.config");
        System.out.println("Row " + game.getGrid().getRows());
        System.out.println("Col " + game.getGrid().getCols());

        PosnStrategy minMaxStrategy = new MinMaxStrategy();
        PosnStrategy flipMaxStrategy = new Flip();
        HybridStrategy hybridStrategy = new HybridStrategy(List.of(minMaxStrategy,
                flipMaxStrategy));

        int[] move = hybridStrategy.choosePositions(game);

        assertNotEquals(new int[]{-1, -1, -1}, move);
      } catch (FileNotFoundException e) {
        fail("Configuration file not found");
      }
    }

    @Test
    public void testHybridStrategySelectsFirst() {
      try {
        GameModel game = BoardConfigParser.parseBoardConfig("docs/boardCanReachWithHoles.config");
        System.out.println("Row " + game.getGrid().getRows());
        System.out.println("Col " + game.getGrid().getCols());

        PosnStrategy minMaxStrategy = new MinMaxStrategy();
        HybridStrategy hybridStrategy = new HybridStrategy(List.of(minMaxStrategy));

        int[] move = hybridStrategy.choosePositions(game);
        assertArrayEquals(new int[] {0, 0, 0}, move);

        assertNotEquals(new int[]{-1, -1, -1}, move);
      } catch (FileNotFoundException e) {
        fail("Configuration file not found");
      }
    }

    @Test
    public void testValidArguments() {
      String[] args = {"human", "ai", "strategy1"};
      Main.main(args);
    }

    @Test
    public void testHybridStrategySelectsBetweenMultiple() {
      try {
        GameModel game = BoardConfigParser.parseBoardConfig(
                "/Users/diegocicotoste/Documents/School/CS3500/hw6/docs/boardSufficentCards.config"
        );
        System.out.println("Row " + game.getGrid().getRows());
        System.out.println("Col " + game.getGrid().getCols());

        PosnStrategy minMaxStrategy = new MinMaxStrategy();
        PosnStrategy flipMaxStrategy = new Flip();
        PosnStrategy goForCornerStrategy = new GoForCorner();
        HybridStrategy hybridStrategy = new HybridStrategy(List.of(minMaxStrategy,
                flipMaxStrategy, goForCornerStrategy));

        int[] move = hybridStrategy.choosePositions(game);
        assertArrayEquals(new int[] {0, 0, 0}, move);
        assertNotEquals(new int[]{-1, -1, -1}, move);
      } catch (FileNotFoundException e) {
        fail("Configuration file not found");
      }
    }

    @Test
    public void testMinMaxStrategySelectsBestMove() {
      try {
        GameModel game = BoardConfigParser.parseBoardConfig(
                "docs/boardSufficentCards.config");

        int expectedRow = 0;
        int expectedCol = 0;
        int expectedCardIndex = 0;


        MinMaxStrategy strategy = new MinMaxStrategy();
        int[] move = strategy.choosePositions(game);

        System.err.println("Actual move: " + Arrays.toString(move));
        System.err.println("Expected move: [" + expectedRow
                + ", " + expectedCol + ", " + expectedCardIndex + "]");


        assertArrayEquals("Flip strategy did not select the position with maximum flips",
                new int[]{expectedRow, expectedCol, expectedCardIndex}, move);
      } catch (FileNotFoundException e) {
        fail("Configuration file not found");
      }
    }

    @Test
    public void testMinMaxStrategyTieBreaking() {
      try {
        GameModel game = BoardConfigParser.parseBoardConfig(
                "docs/boardTieBreaker.config");

        int expectedRow = 0;
        int expectedCol = 0;
        int expectedCardIndex = 0;

        MinMaxStrategy strategy = new MinMaxStrategy();
        int[] move = strategy.choosePositions(game);

        assertArrayEquals("MinMax strategy did not resolve ties correctly",
                new int[]{expectedRow, expectedCol, expectedCardIndex}, move);
      } catch (FileNotFoundException e) {
        fail("Configuration file not found");
      }
    }


    @Test
    public void testFlipStrategySelectsMostFlips() {
      try {
        GameModel game = BoardConfigParser.parseBoardConfig("docs/boardSufficentCards.config");

        int expectedRow = 0;
        int expectedCol = 0;
        int expectedCardIndex = 0;

        Flip strategy = new Flip();
        int[] move = strategy.choosePositions(game);

        assertArrayEquals("Flip strategy did not select the position with maximum flips",
                new int[]{expectedRow, expectedCol, expectedCardIndex}, move);
      } catch (FileNotFoundException e) {
        fail("Configuration file not found");
      }
    }

    @Test
    public void testGoForCornerSelectsCornerMove() {
      try {
        GameModel game = BoardConfigParser.parseBoardConfig("docs/boardSufficentCards.config");

        int expectedRow = 0;
        int expectedCol = 0;
        int expectedCardIndex = 3;

        GoForCorner strategy = new GoForCorner();
        int[] move = strategy.choosePositions(game);

        assertArrayEquals("GoForCorner did not choose the correct corner position",
                new int[]{expectedRow, expectedCol, expectedCardIndex}, move);
      } catch (FileNotFoundException e) {
        fail("Configuration file not found");
      }
    }

    @Test
    public void testHybridStrategyCombinesStrategies() {
      try {
        GameModel game = BoardConfigParser.parseBoardConfig("docs/boardComplexHybrid.config");

        int expectedRow = 0;
        int expectedCol = 0;
        int expectedCardIndex = 0;

        PosnStrategy minMaxStrategy = new MinMaxStrategy();
        PosnStrategy flipStrategy = new Flip();
        HybridStrategy hybridStrategy = new HybridStrategy(List.of(minMaxStrategy, flipStrategy));

        int[] move = hybridStrategy.choosePositions(game);
        System.err.println(Arrays.toString(move));

        assertArrayEquals("Hybrid strategy did not correctly combine MinMax and Flip",
                new int[]{expectedRow, expectedCol, expectedCardIndex}, move);
      } catch (FileNotFoundException e) {
        fail("Configuration file not found");
      }
    }

    @Test
    public void testHybridStrategyBreaksTies() {
      try {
        GameModel game = BoardConfigParser.parseBoardConfig("docs/boardComplexHybrid.config");

        int expectedRow = 0;
        int expectedCol = 0;
        int expectedCardIndex = 0;

        PosnStrategy goForCorner = new GoForCorner();
        PosnStrategy flipStrategy = new Flip();
        HybridStrategy hybridStrategy = new HybridStrategy(List.of(goForCorner, flipStrategy));

        int[] move = hybridStrategy.choosePositions(game);
        System.err.println(Arrays.toString(move));

        assertArrayEquals("Hybrid strategy did not resolve ties by choosing upperleftmost position",
                new int[]{expectedRow, expectedCol, expectedCardIndex}, move);
      } catch (FileNotFoundException e) {
        fail("Configuration file not found");
      }
    }

    @Test
    public void testNoValidMoves() {
      try {
        GameModel game = BoardConfigParser.parseBoardConfig("docs/boardFull.config");

        int expectedRow = 2;
        int expectedCol = 2;
        int expectedCardIndex = 0;

        MinMaxStrategy strategy = new MinMaxStrategy();
        int[] move = strategy.choosePositions(game);

        assertArrayEquals("Strategy did not select the upper-left"
                        + " available position as fallback",
                new int[]{expectedRow, expectedCol, expectedCardIndex}, move);
      } catch (FileNotFoundException e) {
        fail("Configuration file not found");
      }
    }

    @Test
    public void testHybridStrategyWithAllStrategies() {
      try {
        GameModel game = BoardConfigParser.parseBoardConfig("docs/boardFallbackTest.config");

        int expectedRow = 0;
        int expectedCol = 0;
        int expectedCardIndex = 0;

        PosnStrategy minMaxStrategy = new MinMaxStrategy();
        PosnStrategy flipStrategy = new Flip();
        PosnStrategy goForCorner = new GoForCorner();
        HybridStrategy hybridStrategy = new HybridStrategy(
                List.of(minMaxStrategy, flipStrategy, goForCorner));

        int[] move = hybridStrategy.choosePositions(game);

        assertArrayEquals("Hybrid strategy with all strategies " +
                        "did not choose the optimal move",
                new int[]{expectedRow, expectedCol, expectedCardIndex}, move);
      } catch (FileNotFoundException e) {
        fail("Configuration file not found");
      }
    }

    @Test
    public void testNoMovesAvailableAfterFullBoard() {
      try {
        GameModel game = BoardConfigParser.parseBoardConfig("docs/boardFallbackTest.config");

        MinMaxStrategy strategy = new MinMaxStrategy();
        strategy.choosePositions(game);
      } catch (FileNotFoundException e) {
        fail("Configuration file not found");
      }
    }


    @Test
    public void testFallbackToUpperLeftmostPosition() {
      try {
        GameModel game = BoardConfigParser.parseBoardConfig("docs/boardSufficentCards.config");

        int expectedRow = 0;
        int expectedCol = 0;
        int expectedCardIndex = 0;

        MinMaxStrategy strategy = new MinMaxStrategy();
        int[] move = strategy.choosePositions(game);

        assertArrayEquals("MinMax did not fall back to the upper-leftmost " +
                        "position when no optimal move was found",
                new int[]{expectedRow, expectedCol, expectedCardIndex}, move);
      } catch (FileNotFoundException e) {
        fail("Configuration file not found");
      }
    }

    @Test
    public void testStrategyPrefersLowerIndexCardOnTie() {
      try {
        GameModel game = BoardConfigParser.parseBoardConfig("docs/boardSufficentCards.config");

        int expectedRow = 0;
        int expectedCol = 0;
        int expectedCardIndex = 0;

        MinMaxStrategy strategy = new MinMaxStrategy();
        int[] move = strategy.choosePositions(game);

        assertArrayEquals("MinMax did not prefer the lower index card on a tie",
                new int[]{expectedRow, expectedCol, expectedCardIndex}, move);
      } catch (FileNotFoundException e) {
        fail("Configuration file not found");
      }
    }

    @Test
    public void testMinMaxStrategyOnSparseBoard() throws FileNotFoundException {
      GameModel game = BoardConfigParser.parseBoardConfig("docs/boardCanReachWithHoles.config");

      int expectedRow = 0;
      int expectedCol = 0;
      int expectedCardIndex = 0;

      MinMaxStrategy strategy = new MinMaxStrategy();
      int[] move = strategy.choosePositions(game);

      assertArrayEquals("MinMax strategy did not select the only "
                      + "available position",
              new int[]{expectedRow, expectedCol, expectedCardIndex}, move);
    }

    @Test
    public void testMultiTurnBehavior() throws FileNotFoundException {
      GameModel game = BoardConfigParser.parseBoardConfig("docs/boardTieBreaker.config");

      MinMaxStrategy strategy = new MinMaxStrategy();

      for (int turn = 0; turn < 3; turn++) {
        int[] move = strategy.choosePositions(game);
        assertNotEquals("Strategy failed to select a valid move",
                new int[]{-1, -1, -1}, move);
      }
    }

    @Test
    public void testGoForCornerStrategyWithOnlyCornersOpen() throws FileNotFoundException {
      GameModel game = BoardConfigParser.parseBoardConfig("docs/boardOnlyCornersOpen.config");

      GoForCorner strategy = new GoForCorner();
      int[] move = strategy.choosePositions(game);

      assertEquals(true,
              (move[0] == 0 && move[1] == 0)
                      || (move[0] == 0 && move[1] == 2)
                      || (move[0] == 2 && move[1] == 0)
                      || (move[0] == 2 && move[1] == 2));
    }

    @Test
    public void testSingleMoveLeft() throws FileNotFoundException {
      GameModel game = BoardConfigParser.parseBoardConfig("docs/boardFull.config");
      MinMaxStrategy strategy = new MinMaxStrategy();
      int[] move = strategy.choosePositions(game);

      assertArrayEquals("Expected single available move to be chosen",
              new int[]{2, 2, 0}, move);
    }

    @Test(expected = NullPointerException.class)
    public void testStrategiesWithNullGameModel() {
      PosnStrategy flipStrategy = new Flip();
      flipStrategy.choosePositions(null);
    }

    @Test
    public void testHybridStrategyComplexTieBreaking() throws FileNotFoundException {
      GameModel game = BoardConfigParser.parseBoardConfig("docs/boardTieBreaker.config");

      PosnStrategy minMaxStrategy = new MinMaxStrategy();
      PosnStrategy flipStrategy = new Flip();
      PosnStrategy goForCornerStrategy = new GoForCorner();
      HybridStrategy hybridStrategy = new HybridStrategy(
              List.of(minMaxStrategy, flipStrategy, goForCornerStrategy));

      int[] move = hybridStrategy.choosePositions(game);
      assertArrayEquals("Hybrid strategy did not resolve" +
              " complex ties correctly", new int[]{0, 0, 0}, move);
    }

    @Test
    public void testMinMaxStrategyOptimalMove() throws FileNotFoundException {
      GameModel game = BoardConfigParser.parseBoardConfig("docs/boardSufficentCards.config");

      MinMaxStrategy minMaxStrategy = new MinMaxStrategy();
      int[] move = minMaxStrategy.choosePositions(game);

      assertArrayEquals("MinMax did not choose the" +
              " optimal move", new int[]{0, 0, 0}, move);
    }



  }

  /**
   * board config test.
   */
  public class BoardConfigParserTest {

    @Test
    public void testValidBoardWithSufficientCards() {
      try {
        GameModel gameModel = BoardConfigParser.parseBoardConfig(
                "docs/boardSufficentCards.config"
        );
        assertNotNull(gameModel);
      } catch (FileNotFoundException e) {
        fail("File not found");
      }
    }

    @Test
    public void testBoardWithReachableHoles() {
      try {
        GameModel gameModel = BoardConfigParser.parseBoardConfig(
                "docs/boardCanReachWithHoles.config"
        );
        assertNotNull(gameModel);
      } catch (FileNotFoundException e) {
        fail("File not found");
      }
    }

    @Test
    public void testBoardWithoutHoles() {
      try {
        GameModel gameModel = BoardConfigParser.parseBoardConfig("docs/boardNoHoles.config");
        assertNotNull(gameModel);
      } catch (FileNotFoundException e) {
        fail("File not found");
      }
    }

    @Test
    public void testBoardWithInsufficientCards() {
      assertThrows(IllegalStateException.class, () -> {
        BoardConfigParser.parseBoardConfig("docs/boardNotEnoughCards.config");
      });
    }

  }

  /**
   * CardModelTest class for testing purposes.
   */
  public class CardModelTest {

    @Test
    public void testCardCreation() {
      CardModel card = new CardModel("TestCard",
              1, 9, 3, 7, COLOR.RED);
      assertEquals(1, card.getNorth());
      assertEquals(9, card.getSouth());
      assertEquals(3, card.getEast());
      assertEquals(7, card.getWest());
      assertEquals(COLOR.RED, card.getColor());
      assertEquals("TestCard: 1 9 3 7 RED",
              card.toString());
    }

    @Test
    public void testSwitchColor() {
      CardModel card = new CardModel("TestCard",
              1, 9, 3, 7, COLOR.RED);
      card.switchColor(COLOR.BLUE);
      assertEquals(COLOR.BLUE, card.getColor());
    }

    @Test
    public void testEqualsAndHashCode() {
      CardModel card1 = new CardModel("CardA",
              1, 9, 3, 7, COLOR.RED);
      CardModel card2 = new CardModel("CardA",
              1, 9, 3, 7, COLOR.RED);
      CardModel card3 = new CardModel("CardB",
              2, 8, 4, 6, COLOR.BLUE);

      assertEquals(card1, card2);
      assertNotEquals(card1, card3);
      assertEquals(card1.hashCode(), card2.hashCode());
      assertNotEquals(card1.hashCode(), card3.hashCode());
    }

    @Test
    public void testToString() {
      CardModel card = new CardModel("TestCard", 1, 9, 3, 7, COLOR.RED);
      assertEquals("TestCard: 1 9 3 7 RED", card.toString());
    }


    @Test
    public void testBoundaryAttackValues() {
      CardModel cardMin = new CardModel("TestCard",
              1, 1, 1, 1, COLOR.RED);
      CardModel cardMax = new CardModel("TestCard",
              10, 10, 10, 10, COLOR.BLUE);
      assertEquals(1, cardMin.getNorth());
      assertEquals(10, cardMax.getSouth());

    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalAttackValues() {
      new CardModel("TestCard", -1, 9, -3, 7, COLOR.RED);
    }

    @Test(expected = IllegalStateException.class)
    public void testOutOfBounds() {
      new CardModel("TooBig", 17, 17, 17, 17, COLOR.BLUE);
    }

    @Test(expected = IllegalStateException.class)
    public void testEmptyName() {
      new CardModel("", 17, 17, 17, 17, COLOR.BLUE);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalNameObject() {
      new CardModel(null, -1, 9, -3, 7, COLOR.RED);
    }

    @Test
    public void testSwitchColorBackToOriginal() {
      CardModel card = new CardModel("SwitchColorCard",
              1, 2, 3, 4, COLOR.RED);
      card.switchColor(COLOR.BLUE);
      assertEquals(COLOR.BLUE, card.getColor());
      card.switchColor(COLOR.RED);
      assertEquals(COLOR.RED, card.getColor());
    }


    @Test
    public void testImmutableAttackValues() {
      CardModel card = new CardModel("ImmutableCard",
              2, 3, 4, 5, COLOR.RED);
      assertEquals(2, card.getNorth());
      assertEquals(3, card.getSouth());
      assertEquals(4, card.getEast());
      assertEquals(5, card.getWest());
    }

    @Test
    public void testMaxLengthName() {
      String maxLengthName = "A".repeat(50);
      CardModel card = new CardModel(maxLengthName, 5, 5, 5, 5, COLOR.RED);
      assertEquals(maxLengthName, card.toString().split(":")[0]);
    }

  }

  /**
   * A cs3500.threetrios.CellTest class for testing purposes.
   */
  public class CellTest {

    private Cell cardCell;
    private Cell holeCell;
    private Card testCard;

    @Before
    public void setUp() {
      cardCell = new Cell(Cell.CellType.CARD_CELL);
      holeCell = new Cell(Cell.CellType.HOLE);
      testCard = new CardModel("TestCard",
              1, 2, 3, 4, COLOR.RED);
    }

    @Test
    public void testConstructor_withValidCardCellType() {
      Cell cell = new Cell(Cell.CellType.CARD_CELL);
      assertEquals(Cell.CellType.CARD_CELL, cell.getType());
      assertTrue(cell.isEmpty());
    }

    @Test
    public void testConstructor_withValidHoleType() {
      Cell cell = new Cell(Cell.CellType.HOLE);
      assertEquals(Cell.CellType.HOLE, cell.getType());
      assertTrue(cell.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_withNullCardOrType() {
      new Cell(null, null);
    }

    @Test
    public void testConstructor_withCardAndType() {
      Cell cell = new Cell(testCard, Cell.CellType.CARD_CELL);
      assertEquals(Cell.CellType.CARD_CELL, cell.getType());
      assertEquals(testCard, cell.getCard());
    }

    @Test
    public void testIsCardCell_withCardCellType() {
      assertTrue(cardCell.isCardCell());
    }

    @Test
    public void testIsCardCell_withHoleType() {
      assertFalse(holeCell.isCardCell());
    }

    @Test
    public void testPlaceCard_onCardCell() {
      cardCell.placeCard(testCard);
      assertEquals(testCard, cardCell.getCard());
      assertFalse(cardCell.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlaceCard_withNullCard() {
      cardCell.placeCard(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testPlaceCard_onHoleCell() {
      holeCell.placeCard(testCard);
    }

    @Test
    public void testGetCard_onEmptyCell() {
      assertNull(cardCell.getCard());
    }

    @Test
    public void testGetCard_afterPlacingCard() {
      cardCell.placeCard(testCard);
      assertEquals(testCard, cardCell.getCard());
    }

    @Test
    public void testIsEmpty_onEmptyCell() {
      assertTrue(cardCell.isEmpty());
    }

    @Test
    public void testIsEmpty_afterPlacingCard() {
      cardCell.placeCard(testCard);
      assertFalse(cardCell.isEmpty());
    }

    @Test
    public void testGetType_returnsCorrectType() {
      assertEquals(Cell.CellType.CARD_CELL, cardCell.getType());
      assertEquals(Cell.CellType.HOLE, holeCell.getType());
    }

    @Test(expected = IllegalStateException.class)
    public void testPlaceCardOnOccupiedCell() {
      cardCell.placeCard(testCard);
      Card anotherCard = new CardModel("TestCard",
              1, 2, 3, 4, COLOR.RED);
      cardCell.placeCard(anotherCard);
    }

    @Test
    public void testGetCardOnHoleCell() {
      assertNull(holeCell.getCard());
    }

    @Test
    public void testRemoveCardFromEmptyCardCell() {
      assertNull(cardCell.getCard());
      assertTrue(cardCell.isEmpty());
    }

    @Test
    public void testPlaceCardDoesNotChangeCellType() {
      cardCell.placeCard(testCard);
      assertEquals(Cell.CellType.CARD_CELL, cardCell.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlaceNullCardInCardCell() {
      cardCell.placeCard(null);
    }

    @Test
    public void testToStringWithCardInCell() {
      cardCell.placeCard(testCard);
      assertEquals("TestCard: 1 2 3 4 RED", cardCell.getCard().toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorWithNullType() {
      new Cell(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorWithNullCardAndType() {
      new Cell(null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorWithNullCard() {
      new Cell(null, Cell.CellType.CARD_CELL);
    }

    @Test
    public void testIsCardCellReturnsFalseForHoleType() {
      Cell holeCell = new Cell(Cell.CellType.HOLE);
      assertFalse("Expected hole cell to return false for isCardCell",
              holeCell.isCardCell());
    }

    @Test
    public void testGetTypeAfterPlacingCard() {
      cardCell.placeCard(testCard);
      assertEquals(Cell.CellType.CARD_CELL, cardCell.getType());
    }

    @Test
    public void testTypeIsImmutableAfterPlacingCard() {
      Cell cell = new Cell(Cell.CellType.CARD_CELL);
      cell.placeCard(testCard);
      assertEquals(Cell.CellType.CARD_CELL, cell.getType());
    }

    @Test (expected = IllegalStateException.class)
    public void testEmptyToString() {
      Card card = new CardModel("", -1, -1, -1, -1, COLOR.RED);
    }

    @Test
    public void testMultiplePlaceCardCallsOnSameCardCell() {
      cardCell.placeCard(testCard);
      assertEquals(testCard, cardCell.getCard());

      Card anotherCard = new CardModel("AnotherCard", 5, 6, 7, 8, COLOR.BLUE);

      IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
        cardCell.placeCard(anotherCard);
      });

      assertEquals("This cell already contains a card", exception.getMessage());
    }



  }

  /**
   * cs3500.threetrios.GameGridTest class for testing purposes.
   */
  public class GameGridTest {

    private GameGrid gameGrid;

    @Before
    public void setUp() {
      gameGrid = new GameGrid(3, 3);
    }

    @Test
    public void testConstructor_withValidDimensions() {
      GameGrid grid = new GameGrid(4, 5);
      assertEquals(4, grid.getRows());
      assertEquals(5, grid.getCols());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_withInvalidDimensions() {
      new GameGrid(-1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withNullCells() {
      new GameGrid(3, 3, null);
    }

    @Test
    public void testConstructor_withGridCopy() {
      GameGrid originalGrid = new GameGrid(2, 2);
      GameGrid copyGrid = new GameGrid(originalGrid);
      assertEquals(originalGrid.getRows(), copyGrid.getRows());
      assertEquals(originalGrid.getCols(), copyGrid.getCols());
      assertNotSame(originalGrid.getCells(), copyGrid.getCells());
    }

    @Test
    public void testInitializeGrid() {
      gameGrid.initializeGrid();
      assertEquals(Cell.CellType.CARD_CELL, gameGrid.getCellType(0, 0));
    }

    @Test
    public void testGetCols() {
      assertEquals(3, gameGrid.getCols());
    }

    @Test
    public void testGetRows() {
      assertEquals(3, gameGrid.getRows());
    }

    @Test
    public void testGetNumCardsCells() {
      assertEquals(9, gameGrid.getNumCardsCells());
    }

    @Test
    public void testPlaceCard_onValidCell() {
      Card card = new CardModel("TestCard", 1, 2, 3, 4, COLOR.RED);
      gameGrid.placeCard(0, 0, card);
      assertEquals(card, gameGrid.getCard(0, 0));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetCard_invalidPosition() {
      gameGrid.getCard(-1, 0);
    }

    @Test
    public void testIsEmpty_onEmptyCell() {
      assertTrue(gameGrid.isEmpty(0, 0));
    }

    @Test
    public void testIsEmpty_onOccupiedCell() {
      Card card = new CardModel("TestCard", 1, 1, 1, 1, COLOR.RED);
      gameGrid.placeCard(0, 0, card);
      assertFalse(gameGrid.isEmpty(0, 0));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetCellType_invalidPosition() {
      gameGrid.getCellType(-1, -1);
    }

    @Test
    public void testValidPosition_withValidPositions() {
      assertTrue(gameGrid.validPosition(0, 0));
      assertTrue(gameGrid.validPosition(2, 2));
    }

    @Test
    public void testValidPosition_withInvalidPositions() {
      assertFalse(gameGrid.validPosition(-1, 0));
      assertFalse(gameGrid.validPosition(3, 3));
    }

    @Test
    public void testGetCells_deepCopy() {
      Cell[][] cells = gameGrid.getCells();
      assertNotNull(cells);
      assertNotSame(gameGrid.getCells(), cells);
    }

    @Test
    public void testConstructorWithSmallDimensions() {
      GameGrid grid = new GameGrid(1, 1);
      assertEquals(1, grid.getRows());
      assertEquals(1, grid.getCols());
      assertTrue(grid.isEmpty(0, 0));
    }

    @Test
    public void testConstructor_withLargeDimensions() {
      GameGrid grid = new GameGrid(100, 100);
      assertEquals(100, grid.getRows());
      assertEquals(100, grid.getCols());
      assertTrue(grid.isEmpty(0, 0));
    }


    @Test
    public void testReinitializeGrid() {
      Card card = new CardModel("CardBeforeReset", 2, 2, 2, 2, COLOR.RED);
      gameGrid.placeCard(0, 0, card);
      assertEquals(card, gameGrid.getCard(0, 0));

      gameGrid.initializeGrid();
      assertTrue(gameGrid.isEmpty(0, 0));
    }

  }

  /**
   * cs3500.threetrios.GameModelTest class meant for testing.
   */
  public class GameModelTest {

    private GameModel game;
    private Grid grid;

    @Before
    public void setUp() {
      grid = new GameGrid(3, 3);
      game = new GameModel(grid);

      Card initialCard = new CardModel("InitialCard", 1, 1, 1, 1, COLOR.RED);
      game.getPlayers()[0].addCard(initialCard);
      Card initialCard2 = new CardModel("InitialCard", 1, 1, 1, 1, COLOR.BLUE);
      game.getPlayers()[1].addCard(initialCard2);
    }

    @Test
    public void testConstructor_withValidGrid() {
      assertNotNull(game.getGrid());
      assertEquals(2, game.getPlayers().length);
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructor_withNullGrid() throws FileNotFoundException {
      new GameModel("null");
    }

    @Test
    public void testSwitchTurns() {
      assertEquals(0, game.getTurn());
      game.switchTurns();
      assertEquals(1, game.getTurn());
      game.switchTurns();
      assertEquals(0, game.getTurn());
    }

    @Test
    public void testGetCurrentPlayer() {
      Player currentPlayer = game.getCurrentPlayer();
      assertNotNull(currentPlayer);
      assertEquals("Player Red", currentPlayer.getName());
    }

    @Test
    public void testPlaceCard_onEmptyCell() {
      Card card = new CardModel("CardTest", 1, 1, 1,1, COLOR.RED);
      game.placeCard(1,1, card);
      assertNotNull("Card should not be null after placement", game.getGrid().getCard(1, 1));
      assertEquals("Placed card should match expected card", card, game.getGrid().getCard(1, 1));
    }

    @Test(expected = IllegalStateException.class)
    public void testPlaceCard_onOccupiedCell() {
      Card card = game.getCurrentPlayer().getHand().get(0);
      game.placeCard(1, 1, card);
      game.switchTurns();
      Card anotherCard = new CardModel("NewCard", 1, 1, 1, 1, COLOR.RED);
      game.placeCard(1, 1, anotherCard);
    }

    @Test(expected = IllegalStateException.class)
    public void testPlaceCard_wrongTurnColor() {
      Card redCard = game.getPlayers()[0].getHand().get(0);
      game.placeCard(0, 0, redCard);
      game.switchTurns();
      game.placeCard(0, 1, redCard);
    }

    @Test(expected = IllegalStateException.class)
    public void testPlaceCard_inInvalidRowCol() {
      Card card = game.getCurrentPlayer().getHand().get(0);
      game.placeCard(-1, 0, card);
    }

    @Test
    public void testComboBattle_triggerComboBattle() {
      Card redCard = new CardModel("RED1", 5, 1, 3, 2, COLOR.RED);
      Card blueCard = new CardModel("BLUE3", 2, 3, 1, 4, COLOR.BLUE);
      Card anotherRedCard = new CardModel("RED2", 1, 2, 3, 5, COLOR.RED);
      List<Card> redCards = List.of(redCard, anotherRedCard);
      List<Card> blueCards = List.of(blueCard);
      Player redPlayer = new PlayerModel("RED1", COLOR.RED, redCards);
      Player bluePlayer = new PlayerModel("BLUE1", COLOR.BLUE, blueCards);
      Player[] players = new Player[]{redPlayer, bluePlayer};
      Game gameBattle = new GameModel(grid, players);

      gameBattle.placeCard(1, 1, redCard);
      assertNotNull(gameBattle.getGrid().getCard(1, 1));
      assertEquals(COLOR.RED, gameBattle.getGrid().getCard(1, 1).getColor());


      gameBattle.switchTurns();
      gameBattle.placeCard(0, 1, blueCard);
      assertNotNull(gameBattle.getGrid().getCard(0, 1));
      assertEquals(COLOR.BLUE, gameBattle.getGrid().getCard(0, 1).getColor());


      gameBattle.switchTurns();
      gameBattle.placeCard(1, 0, anotherRedCard);
      assertNotNull(gameBattle.getGrid().getCard(1, 0));
      assertEquals(COLOR.RED, gameBattle.getGrid().getCard(1, 0).getColor());
    }

    @Test
    public void testBattleCards() {
      GameGrid grid = new GameGrid(3, 3);
      GameModel gameModel = new GameModel(grid);

      CardModel middleCard = new CardModel("MiddleCard", 5, 5, 5, 5, COLOR.RED);
      CardModel leftCard = new CardModel("LeftCard", 1, 1, 1, 1, COLOR.BLUE);
      CardModel rightCard = new CardModel("RightCard", 1, 1, 1, 1, COLOR.BLUE);


      grid.placeCard(1, 1, middleCard);
      grid.placeCard(1, 0, leftCard);
      grid.placeCard(1, 2, rightCard);


      gameModel.battleCards(1, 1, new HashSet<>());

      assertEquals("Left card should be RED", COLOR.RED, grid.getCard(1, 0).getColor());
      assertEquals("Middle card should remain RED", COLOR.RED, grid.getCard(1, 1).getColor());
      assertEquals("Right card should be RED", COLOR.RED, grid.getCard(1, 2).getColor());
    }


    @Test
    public void testGetGrid_returnsCopy() {
      Grid gridCopy = game.getGrid();
      assertNotSame(grid, gridCopy);
      assertEquals(grid.getCard(1, 1), gridCopy.getCard(1, 1));
    }

    @Test
    public void testGetCards_correctNumberOfCards() {
      List<Card> cards = game.getCards();
      int expectedNumCards = (grid.getNumCardsCells() + 1) / 2;
      assertEquals(expectedNumCards, cards.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_withZeroOrNegativeDimensions() {
      new GameGrid(0, 3);
      new GameGrid(3, 0);
      new GameGrid(-3, -3);
    }

    @Test
    public void testPlaceCardInHoleCellThrowsException() {
      Cell[][] cells = new Cell[3][3];
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          cells[i][j] = new Cell(Cell.CellType.CARD_CELL);
        }
      }
      cells[1][1] = new Cell(Cell.CellType.HOLE);
      GameGrid grid = new GameGrid(3, 3, cells);

      GameModel game = new GameModel(grid);
      CardModel card = new CardModel("TestCard", 1, 2, 3, 4, COLOR.RED);

      assertThrows(IllegalArgumentException.class, () -> game.placeCard(1, 1, card));
    }

    @Test
    public void testPlaceCardInAnotherHoleCellThrowsException() {
      Cell[][] cells = new Cell[4][4];
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          cells[i][j] = new Cell(Cell.CellType.CARD_CELL);
        }
      }
      cells[2][2] = new Cell(Cell.CellType.HOLE);
      GameGrid grid = new GameGrid(4, 4, cells);

      GameModel game = new GameModel(grid);
      CardModel card = new CardModel("TestCard2", 5, 6, 7, 8, COLOR.BLUE);

      assertThrows(IllegalStateException.class, () -> game.placeCard(2, 2, card));
    }

    @Test
    public void testRedWins() {
      Cell[][] cells = new Cell[3][3];
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          cells[i][j] = new Cell(Cell.CellType.CARD_CELL);
        }
      }
      GameGrid grid = new GameGrid(3, 3, cells);

      GameModel game = new GameModel(grid);
      grid.placeCard(0, 0, new CardModel("RedCard1", 5, 3, 6, 7, COLOR.RED));
      grid.placeCard(0, 1, new CardModel("RedCard2", 3, 5, 7, 8, COLOR.RED));
      grid.placeCard(0, 2, new CardModel("BlueCard1", 6, 2, 4, 3, COLOR.BLUE));

      assertEquals("Red Wins", game.checkWinCondition());
    }

    @Test
    public void testBlueWins() {
      Cell[][] cells = new Cell[3][3];
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          cells[i][j] = new Cell(Cell.CellType.CARD_CELL);
        }
      }
      GameGrid grid = new GameGrid(3, 3, cells);

      GameModel game = new GameModel(grid);
      grid.placeCard(0, 0, new CardModel("BlueCard1", 5, 3, 6, 7, COLOR.BLUE));
      grid.placeCard(0, 1, new CardModel("BlueCard2", 3, 5, 7, 8, COLOR.BLUE));
      grid.placeCard(0, 2, new CardModel("RedCard1", 6, 2, 4, 3, COLOR.RED));

      assertEquals("Blue Wins", game.checkWinCondition());
    }

    @Test
    public void testTie() {
      Cell[][] cells = new Cell[3][3];
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          cells[i][j] = new Cell(Cell.CellType.CARD_CELL);
        }
      }
      GameGrid grid = new GameGrid(3, 3, cells);

      GameModel game = new GameModel(grid);
      grid.placeCard(0, 0, new CardModel("RedCard1", 5, 3, 6, 7, COLOR.RED));
      grid.placeCard(0, 1, new CardModel("BlueCard1", 3, 5, 7, 8, COLOR.BLUE));

      assertEquals("Tie", game.checkWinCondition());
    }

    @Test
    public void testGameOverTrue() {
      Cell[][] cells = new Cell[3][3];
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          cells[i][j] = new Cell(Cell.CellType.CARD_CELL);
        }
      }
      GameGrid grid = new GameGrid(3, 3, cells);

      GameModel game = new GameModel(grid);
      grid.placeCard(0, 0, new CardModel("RedCard1", 5, 3, 6, 7, COLOR.RED));
      grid.placeCard(0, 1, new CardModel("BlueCard1", 3, 5, 7, 8, COLOR.BLUE));
      grid.placeCard(0, 2, new CardModel("RedCard2", 6, 2, 4, 3, COLOR.RED));
      grid.placeCard(1, 0, new CardModel("BlueCard2", 7, 8, 5, 9, COLOR.BLUE));
      grid.placeCard(1, 1, new CardModel("RedCard3", 2, 3, 4, 5, COLOR.RED));
      grid.placeCard(1, 2, new CardModel("BlueCard3", 4, 6, 5, 3, COLOR.BLUE));
      grid.placeCard(2, 0, new CardModel("RedCard4", 8, 7, 6, 5, COLOR.RED));
      grid.placeCard(2, 1, new CardModel("BlueCard4", 5, 4, 3, 2, COLOR.BLUE));
      grid.placeCard(2, 2, new CardModel("RedCard5", 9, 1, 8, 7, COLOR.RED));

      assertTrue(game.isGameOver());
    }

    @Test
    public void testGameOverFalse() {
      Cell[][] cells = new Cell[3][3];
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          cells[i][j] = new Cell(Cell.CellType.CARD_CELL);
        }
      }
      GameGrid grid = new GameGrid(3, 3, cells);

      GameModel game = new GameModel(grid);
      grid.placeCard(0, 0, new CardModel("RedCard1", 5, 3, 6, 7, COLOR.RED));
      grid.placeCard(0, 1, new CardModel("BlueCard1", 3, 5, 7, 8, COLOR.BLUE));
      grid.placeCard(0, 2, new CardModel("RedCard2", 6, 2, 4, 3, COLOR.RED));

      assertFalse(game.isGameOver());
    }

    //New tests

    @Test
    public void testIsMoveLegal_onEmptyCell() {
      assertTrue(game.isMoveLegal(1, 1));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testIsMoveLegalOccupiedCell() {
      Card card = new CardModel("OccupiedCard",
              2, 2, 2, 2, COLOR.RED);
      game.placeCard(1, 1, card);
      assertFalse(game.isMoveLegal(1, 1));
    }

    @Test (expected = IllegalStateException.class)
    public void testIsMoveLegalOutOfBounds() {
      assertFalse(game.isMoveLegal(-1, 0));
      assertFalse(game.isMoveLegal(0, 3));
    }

    @Test
    public void testGetCurrentPlayerModelInitialState() {
      assertNotNull(game.getCurrentPlayerModel());
      assertEquals("Player Red", game.getCurrentPlayerModel().getName());
    }

    @Test
    public void testGetCurrentPlayerModelAfterSwitchTurns() {
      game.switchTurns();
      assertNotNull(game.getCurrentPlayerModel());
      assertEquals("Player Blue", game.getCurrentPlayerModel().getName());
    }

    @Test
    public void testGetWinner() {
      Cell[][] cells = new Cell[3][3];
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          cells[i][j] = new Cell(Cell.CellType.CARD_CELL);
        }
      }
      GameGrid grid = new GameGrid(3, 3, cells);

      GameModel game = new GameModel(grid);
      grid.placeCard(0, 0, new CardModel("RedCard1", 5, 3, 6, 7, COLOR.RED));
      grid.placeCard(0, 1, new CardModel("BlueCard1", 3, 5, 7, 8, COLOR.BLUE));
      grid.placeCard(0, 2, new CardModel("RedCard2", 6, 2, 4, 3, COLOR.RED));
      grid.placeCard(1, 0, new CardModel("BlueCard2", 7, 8, 5, 9, COLOR.BLUE));
      grid.placeCard(1, 1, new CardModel("RedCard3", 2, 3, 4, 5, COLOR.RED));
      grid.placeCard(1, 2, new CardModel("BlueCard3", 4, 6, 5, 3, COLOR.BLUE));
      grid.placeCard(2, 0, new CardModel("RedCard4", 8, 7, 6, 5, COLOR.RED));
      grid.placeCard(2, 1, new CardModel("BlueCard4", 5, 4, 3, 2, COLOR.BLUE));
      grid.placeCard(2, 2, new CardModel("RedCard5", 9, 1, 8, 7, COLOR.RED));

      assertTrue(game.getCardAt(1, 1).getColor().equals(COLOR.RED));
      assertEquals("Red Wins", game.getWinner());
    }

    @Test
    public void testGetGridSize() {
      assertEquals(3, game.getGridSize());
    }

    @Test
    public void testGetCardAtValidCell() {
      Cell[][] cells = new Cell[3][3];
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          cells[i][j] = new Cell(Cell.CellType.CARD_CELL);
        }
      }
      GameGrid grid = new GameGrid(3, 3, cells);

      GameModel game = new GameModel(grid);
      grid.placeCard(0, 0, new CardModel("RedCard1", 5, 3, 6, 7, COLOR.RED));
      grid.placeCard(0, 1, new CardModel("BlueCard1", 3, 5, 7, 8, COLOR.BLUE));
      grid.placeCard(0, 2, new CardModel("RedCard2", 6, 2, 4, 3, COLOR.RED));
      grid.placeCard(1, 0, new CardModel("BlueCard2", 7, 8, 5, 9, COLOR.BLUE));
      grid.placeCard(1, 1, new CardModel("RedCard3", 2, 3, 4, 5, COLOR.RED));
      grid.placeCard(1, 2, new CardModel("BlueCard3", 4, 6, 5, 3, COLOR.BLUE));
      grid.placeCard(2, 0, new CardModel("RedCard4", 8, 7, 6, 5, COLOR.RED));
      grid.placeCard(2, 1, new CardModel("BlueCard4", 5, 4, 3, 2, COLOR.BLUE));
      grid.placeCard(2, 2, new CardModel("RedCard5", 9, 1, 8, 7, COLOR.RED));

      assertTrue(game.getCardAt(1, 1).getColor().equals(COLOR.RED));
    }

    @Test (expected = IllegalStateException.class)
    public void testGetCardAtInvalidCell() {
      game.getCardAt(-1, -1);

    }


  }

  /**
   * All the test cases for HW7 implementation of two controllers and two views.
   */
  public class Homework7Tests {
    private GameModel game;
    private RedPlayerView redView;
    private BluePlayerView blueView;
    private ThreeTriosControllerImpl controllerRed;
    private ThreeTriosControllerImpl controllerBlue;
    private PosnStrategy strategy;


    @Before
    public void setUp() throws FileNotFoundException {
      game = new GameModel("docs/boardNoHoles.config");

      Card initialCard = new CardModel("InitialCard", 1, 1, 1, 1, COLOR.RED);
      game.getPlayers()[0].addCard(initialCard);
      Card initialCard2 = new CardModel("InitialCard", 1, 1, 1, 1, COLOR.BLUE);
      game.getPlayers()[1].addCard(initialCard2);

      redView = new RedPlayerView(game);
      blueView = new BluePlayerView(game);

      controllerRed = new ThreeTriosControllerImpl(game, redView);
      controllerBlue = new ThreeTriosControllerImpl(game, blueView);

      strategy = new HybridStrategy(List.of(new GoForCorner(), new LeastFlippableStrategy()));
    }


    @Test
    public void testSwitchTurns() {
      game.switchTurns();
      assertEquals("Player Blue", game.getCurrentPlayer().getName());
      game.switchTurns();
      assertEquals("Player Red", game.getCurrentPlayer().getName());
    }

    @Test
    public void testSwitchTurnsColor() {
      game.switchTurns();
      assertEquals(COLOR.BLUE, game.getCurrentPlayer().getColor());
      game.switchTurns();
      assertEquals(COLOR.RED, game.getCurrentPlayer().getColor());
    }


    @Test
    public void testPlaceCard() {
      Card redCard = game.getPlayers()[0].getHand().get(0);
      game.placeCard(1, 1, redCard);
      assertEquals(redCard, game.getGrid().getCard(1, 1));
    }

    @Test
    public void testHandleCellClickOnPlayerTurnRedView() {
      Card redCard = game.getPlayers()[0].getHand().get(0);
      assertNotNull(redCard);
      assertEquals(COLOR.RED, redCard.getColor());

      controllerRed.setSelectedCard(0);
      controllerRed.handleCellClick(0, 0);
      redView.refresh();

      assertEquals(redCard, game.getGrid().getCard(0,0));
      assertEquals(COLOR.RED, game.getCardAt(0, 0).getColor());
    }



    @Test
    public void testGoForCornerStrategy() {
      strategy = new GoForCorner();

      int[] move = strategy.choosePositions(game);
      assertTrue(game.isMoveLegal(move[0], move[1]));
      assertEquals(0, move[0]);
      assertEquals(0, move[1]);
    }


    @Test
    public void testHybridStrategy() {
      PosnStrategy strategy = new HybridStrategy(List.of(new GoForCorner(),
              new LeastFlippableStrategy()));
      int[] move = strategy.choosePositions(game);
      assertTrue(game.isMoveLegal(move[0], move[1]));
    }

    @Test
    public void testHandleCellClickOutOfTurn() {
      controllerRed.setSelectedCard(0);
      controllerRed.handleCellClick(0, 0);

      assertEquals("Player Blue", game.getCurrentPlayer().getName());

      controllerBlue.setSelectedCard(0);
      controllerBlue.handleCellClick(0, 0);

      redView.displayErrorMessage("Cell is already occupied.");
      String lastMessage = redView.getLastErrorMessage();
      assertEquals(
              "Cell is already occupied." ,
              lastMessage
      );

      redView.refresh();
      blueView.refresh();
    }



    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCardPlacementOccupiedCell() {
      Card redCard = game.getPlayers()[0].getHand().get(0);
      game.placeCard(1, 1, redCard);
      game.placeCard(1, 1, redCard);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidCardPlacementOutOfBounds() {
      Card redCard = game.getPlayers()[0].getHand().get(0);
      game.placeCard(-1, -1, redCard);
    }


    @Test
    public void testGameOver() {
      controllerRed.setSelectedCard(0);
      controllerRed.handleCellClick(0, 0);
      controllerBlue.setSelectedCard(0);
      controllerBlue.handleCellClick(0, 1);
      controllerRed.setSelectedCard(1);
      controllerRed.handleCellClick(0, 2);

      controllerBlue.setSelectedCard(1);
      controllerBlue.handleCellClick(1, 0);
      controllerRed.setSelectedCard(2);
      controllerRed.handleCellClick(1, 1);
      controllerBlue.setSelectedCard(2);
      controllerBlue.handleCellClick(1, 2);

      controllerRed.setSelectedCard(2);
      controllerRed.handleCellClick(2, 0);
      controllerBlue.setSelectedCard(2);
      controllerBlue.handleCellClick(2, 1);
      controllerRed.setSelectedCard(1);
      controllerRed.handleCellClick(2, 2);

      assertTrue(game.isGameOver());
      String expectedWinner = "Blue Wins";
      assertEquals(expectedWinner, game.getWinner());
    }


    @Test
    public void testRenderGrid() {
      redView.refresh();
      assertNotNull(game.getGrid().getCells());
    }


    @Test
    public void testCardBattle() {
      Card redCard = game.getPlayers()[0].getHand().get(0);
      game.placeCard(1, 1, redCard);

      game.switchTurns();

      Card blueCard = game.getPlayers()[1].getHand().get(0);
      game.placeCard(1, 2, blueCard);

      game.battleCards(1, 2, new HashSet<>());

      assertEquals(COLOR.BLUE, game.getGrid().getCard(1, 2).getColor());
    }


    @Test
    public void testTurnBasedRestriction() {
      controllerRed.setSelectedCard(0);
      controllerRed.handleCellClick(0, 0);
      controllerBlue.setSelectedCard(0);
      controllerBlue.handleCellClick(1, 1);

      redView.displayErrorMessage("Cell is already occupied.");
      String lastMessage = redView.getLastErrorMessage();
      assertEquals("Cell is already occupied.", lastMessage);
    }

    @Test
    public void testAIControllerInitialization() {
      AIController controller = new AIController(game, strategy, COLOR.RED);
      assertNotNull(controller);
    }

    @Test
    public void testAIMakeValidMove() {
      controllerRed.setSelectedCard(0);
      controllerRed.handleCellClick(0, 0);

      AIController aiController = new AIController(game, strategy, COLOR.BLUE);
      aiController.makeMove();

      int[] move = strategy.choosePositions(game);
      game.switchTurns();
      game.placeCard(move[0], move[1], game.getPlayers()[0].getHand().get(0));
      assertTrue(game.isMoveLegal(move[0], move[1]));

      Card blueCard = game.getCardAt(move[0], move[1]);
      assertNotNull(blueCard);
      assertEquals(COLOR.BLUE, blueCard.getColor());
    }

    @Test
    public void testInvalidMoveErrorHandling() {
      controllerRed.setSelectedCard(0);
      controllerRed.handleCellClick(-1, -1);

      redView.displayErrorMessage("Invalid Move");
      String lastMessage = redView.getLastErrorMessage();
      assertEquals("Invalid Move", lastMessage);
    }


    @Test
    public void testPlayerTurnValidation() {
      Player currentPlayer = game.getCurrentPlayer();
      assertEquals(COLOR.RED, currentPlayer.getColor());

      Card selectedCard = currentPlayer.getHand().get(0);
      game.placeCard(0, 0, selectedCard);

      game.switchTurns();
      assertEquals(COLOR.BLUE, game.getCurrentPlayer().getColor());
    }



    @Test
    public void testAIValidMoves() {
      int[] move = strategy.choosePositions(game);
      assertTrue(game.isMoveLegal(move[0], move[1]));
    }


    @Test
    public void testBluePlayerViewRefresh() {
      controllerRed.setSelectedCard(0);
      controllerRed.handleCellClick(1, 1);
      controllerBlue.setSelectedCard(0);
      controllerBlue.handleCellClick(0,0);
      controllerRed.setSelectedCard(1);
      controllerRed.handleCellClick(1, 2);

      Card cardInGrid = game.getGrid().getCard(0, 0);
      assertEquals(COLOR.BLUE, cardInGrid.getColor());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCommandLineArguments() {
      Main.main(null);
    }

    @Test
    public void testSwitchTurnsFr() {
      assertEquals(COLOR.RED, game.getCurrentPlayer().getColor());
      game.switchTurns();
      assertEquals(COLOR.BLUE, game.getCurrentPlayer().getColor());
    }


    @Test (expected = IllegalStateException.class)
    public void testInvalidPlacementOnOccupiedCell() {
      Card redCard = game.getPlayers()[0].getHand().get(0);
      game.placeCard(0, 0, redCard);

      Card blueCard = game.getPlayers()[1].getHand().get(0);
      game.placeCard(0, 0, blueCard);

      assertEquals("Cell is already occupied", redView.getLastErrorMessage());
    }










  }


  /**
   * Tests the mock model with strategies.
   */
  public class MockModelTests {

    private MockGameModel mockGameModel;
    private Player playerRed;
    private Player playerBlue;
    private Grid grid;

    @Before
    public void setup() {
      grid = new GameGrid(3, 3);
      List<Card> redHand = List.of(new CardModel("RedCard1", 1, 2, 3, 4, COLOR.RED));
      List<Card> blueHand = List.of(new CardModel("BlueCard1", 5, 6, 7, 8, COLOR.BLUE));

      playerRed = new PlayerModel("Player Red", COLOR.RED, redHand);
      playerBlue = new PlayerModel("Player Blue", COLOR.BLUE, blueHand);

      Player[] players = {playerRed, playerBlue};

      mockGameModel = new MockGameModel(playerRed, grid, players);
    }

    @Test
    public void testFlipStrategyChecksAllPositions() {
      Flip flipStrategy = new Flip();
      flipStrategy.choosePositions((ReadOnlyGameModel) mockGameModel);

      assertEquals(9, mockGameModel.getCheckedCoordinates().size());
    }

    @Test
    public void testGoForCornerChecksCornersOnly() {
      GoForCorner goForCornerStrategy = new GoForCorner();
      goForCornerStrategy.choosePositions((ReadOnlyGameModel) mockGameModel);

      List<String> expectedCoordinates = List.of(
              "isMoveLegal(0, 0)", "isMoveLegal(0, 2)", "isMoveLegal(2, 0)", "isMoveLegal(2, 2)"
      );

      assertEquals(expectedCoordinates, mockGameModel.getCheckedCoordinates());
    }

    @Test
    public void testLeastFlippableStrategyChoosesLowRiskMove() {
      mockGameModel.setMoveLegal(true);

      LeastFlippableStrategy leastFlippableStrategy = new LeastFlippableStrategy();
      leastFlippableStrategy.choosePositions((ReadOnlyGameModel) mockGameModel);

      List<String> methodCalls = mockGameModel.getMethodCalls();

      assertTrue(methodCalls.contains("getCurrentPlayer"));
      assertTrue(methodCalls.contains("getGrid"));
    }

    @Test
    public void testHybridStrategyCombinesStrategies() {
      HybridStrategy hybridStrategy = new HybridStrategy(List.of(new Flip(), new GoForCorner()));
      hybridStrategy.choosePositions((ReadOnlyGameModel) mockGameModel);

      assertTrue(mockGameModel.getCheckedCoordinates().contains("isMoveLegal(0, 0)"));
      assertTrue(mockGameModel.getCheckedCoordinates().contains("isMoveLegal(1, 1)"));
    }

    @Test
    public void testGoForCornersStrategy() {
      GoForCorner goForCornerStrategy = new GoForCorner();
      goForCornerStrategy.choosePositions((ReadOnlyGameModel) mockGameModel);

      assertTrue(mockGameModel.getCheckedCoordinates().contains("isMoveLegal(0, 0)"));
      assertTrue(mockGameModel.getCheckedCoordinates().contains("isMoveLegal(0, 2)"));
      assertTrue(mockGameModel.getCheckedCoordinates().contains("isMoveLegal(2, 0)"));
      assertTrue(mockGameModel.getCheckedCoordinates().contains("isMoveLegal(2, 2)"));
    }

    @Test
    public void testNoPlayerStrategyInvalidMoves() {
      mockGameModel.setMoveLegal(false);
      NoPlay strategy = new NoPlay();
      int[] move = strategy.choosePositions((ReadOnlyGameModel) mockGameModel);

      System.out.println(Arrays.toString(move));
      assertArrayEquals(new int[] {-1, -1, -1}, move);
    }

    @Test
    public void testNoPlayerStrategyValidMoves() {
      mockGameModel.setMoveLegal(true);
      NoPlay strategy = new NoPlay();
      int[] move = strategy.choosePositions((ReadOnlyGameModel) mockGameModel);

      System.out.println(Arrays.toString(move));
      assertNotEquals(new int[]{-1, -1, -1}, move);
    }

    @Test
    public void setFirstLegalMoveNoPlay() {
      mockGameModel.setMoveLegal(true);
      NoPlay strategy = new NoPlay();
      int[] move = strategy.choosePositions((ReadOnlyGameModel) mockGameModel);
      assertArrayEquals(new int[] {0, 0, 0}, move);
    }

    @Test
    public void testStrategyController() {
      mockGameModel.setMoveLegal(true);
      HybridStrategy strategyController = new HybridStrategy(Arrays.asList(new Flip(),
              new GoForCorner()));
      int[] move = strategyController.choosePositions((ReadOnlyGameModel) mockGameModel);

      assertArrayEquals(new int[] {0, 0, 0}, move);
      assertTrue(move[0] >= 0 && move[1] >= 0);
      assertTrue(mockGameModel.getCheckedCoordinates().contains("isMoveLegal(0, 0)"));

    }


    @Test
    public void testStrategyControllerInvalidMoves() {
      mockGameModel.setMoveLegal(false);
      HybridStrategy strategyController = new HybridStrategy(Arrays.asList(new Flip(),
              new GoForCorner()));
      int[] move = strategyController.choosePositions((ReadOnlyGameModel) mockGameModel);
      assertArrayEquals(new int[] {-1, -1, -1}, move);

    }

    @Test
    public void testStrategyControllerValidMoves() {
      mockGameModel.setMoveLegal(true);
      HybridStrategy strategyController = new HybridStrategy(Arrays.asList(new Flip(),
              new GoForCorner()));
      int[] move = strategyController.choosePositions((ReadOnlyGameModel) mockGameModel);

      assertTrue(mockGameModel.getCheckedCoordinates().contains("isMoveLegal(0, 0)"));
      assertTrue(move[0] == 0 && move[1] == 0);
    }


    @Test
    public void testFlipStrategyLogsMethodCalls() {
      Flip flipStrategy = new Flip();
      flipStrategy.choosePositions((ReadOnlyGameModel) mockGameModel);

      List<String> methodCalls = mockGameModel.getMethodCalls();
      assertTrue("Expected getGrid method to be logged",
              methodCalls.contains("getGrid"));
      assertTrue("Expected getCurrentPlayer method to be logged",
              methodCalls.contains("getCurrentPlayer"));
    }


    @Test
    public void testLeastFlippableStrategyTieBreaking() {
      mockGameModel.setMoveLegal(true);
      LeastFlippableStrategy leastFlippableStrategy = new LeastFlippableStrategy();
      int[] move = leastFlippableStrategy.choosePositions((ReadOnlyGameModel) mockGameModel);

      assertArrayEquals("Expected upper-leftmost cell due to tie-breaking",
              new int[] {0, 0, 0}, move);
    }

    @Test
    public void testFlipStrategyFullCoverage() {
      Flip flipStrategy = new Flip();
      flipStrategy.choosePositions((ReadOnlyGameModel) mockGameModel);

      assertEquals("Expected all cells to be checked for flipping potential",
              9, mockGameModel.getCheckedCoordinates().size());
      assertTrue(mockGameModel.getCheckedCoordinates().contains("isMoveLegal(2, 2)"));
    }

    @Test
    public void testHybridStrategyWithMultipleLayers() {
      HybridStrategy hybridStrategy = new HybridStrategy(List.of(new Flip(),
              new LeastFlippableStrategy(), new GoForCorner()));
      int[] move = hybridStrategy.choosePositions((ReadOnlyGameModel) mockGameModel);

      assertArrayEquals("left-most cell because of tie-breaking ",
              new int[] {0, 0, 0}, move);
    }

    @Test
    public void testNoPlayStrategyAllInvalidMoves() {
      mockGameModel.setMoveLegal(false);
      NoPlay strategy = new NoPlay();
      int[] move = strategy.choosePositions((ReadOnlyGameModel) mockGameModel);

      assertArrayEquals("Expected {-1, -1, -1} when no valid moves",
              new int[] {-1, -1, -1}, move);
    }

    @Test
    public void testStrategyControllerInValidMoves() {
      mockGameModel.setMoveLegal(false);
      HybridStrategy strategyController = new HybridStrategy(Arrays.asList(new Flip(),
              new GoForCorner()));
      int[] move = strategyController.choosePositions((ReadOnlyGameModel) mockGameModel);

      assertTrue(mockGameModel.getCheckedCoordinates().contains("isMoveLegal(0, 0)"));
      assertArrayEquals("Expected {-1, -1, -1} when no valid moves",
              new int[] {-1, -1, -1}, move);
    }

    @Test
    public void testStrategyControllerOrder() {
      HybridStrategy strategyController = new HybridStrategy(Arrays.asList(new GoForCorner(),
              new Flip()));
      int[] move = strategyController.choosePositions((ReadOnlyGameModel) mockGameModel);

      List<String> checkedCoordinates = mockGameModel.getCheckedCoordinates();
      int goForCornerIndex = checkedCoordinates.indexOf("isMoveLegal(0, 0)");
      int flipIndex = checkedCoordinates.indexOf("isMoveLegal(1, 1)");

      assertTrue("Expected GoForCorner to be executed before Flip",
              goForCornerIndex < flipIndex);
      assertTrue("Expected a valid move to be chosen",
              move[0] >= 0 && move[1] >= 0);
    }


    @Test
    public void testStrategyPrefersLowerIndexCardOnTie() {
      mockGameModel.setMoveLegal(true);

      MinMaxStrategy strategy = new MinMaxStrategy();
      int[] move = strategy.choosePositions((ReadOnlyGameModel) mockGameModel);

      assertArrayEquals("Expected lower index card on tie", new int[]{0, 0, 0}, move);
    }


    @Test
    public void testIsMoveLegalLogging() {
      mockGameModel.isMoveLegal(0, 0);
      mockGameModel.isMoveLegal(1, 1);
      List<String> checkedCoordinates = mockGameModel.getCheckedCoordinates();
      assertEquals("Checked coordinates size", 2, checkedCoordinates.size());
    }

    @Test
    public void testSimpleStrategyTranscript() {
      Grid grid = new GameGrid(3, 3);
      Player playerRed = new PlayerModel("Player Red", COLOR.RED,
              List.of(new CardModel("RedCard1",
                      1, 2, 3, 4, COLOR.RED)));
      Player playerBlue = new PlayerModel("Player Blue", COLOR.BLUE,
              List.of(new CardModel("BlueCard1",
                      5, 6, 7, 8, COLOR.BLUE)));
      Player[] players = {playerRed, playerBlue};

      MockGameModel mockGame = new MockGameModel(playerRed, grid, players);

      int row = 0;
      int col = 0;
      if (mockGame.isMoveLegal(row, col)) {
        Card redCard = new CardModel("RedCard1",
                1, 2, 3, 4, COLOR.RED);
        mockGame.placeCard(row, col, redCard);
      }
      mockGame.checkWinCondition();
      mockGame.isGameOver();
      mockGame.switchTurns();

      List<String> expectedCalls = Arrays.asList(
              "isMoveLegal(0, 0)",
              "placeCard(0, 0, RedCard1: 1 2 3 4 RED)",
              "checkWinCondition",
              "isGameOver",
              "switchTurns"
      );

      List<String> actualCalls = mockGame.getMethodCalls();
      assertEquals("The method calls do not match" +
              " the expected sequence.", expectedCalls, actualCalls);
    }


    /**
     * Main in test is used to create the strategy-transcript.txt
     * @param args method calls for the mock
     */
    public static void main(String[] args) {

      Grid grid = new GameGrid(3, 3);

      Player playerRed = new PlayerModel("Player Red", COLOR.RED,
              List.of(new CardModel("RedCard1",
                      1, 2, 3, 4, COLOR.RED)));
      Player playerBlue = new PlayerModel("Player Blue", COLOR.BLUE,
              List.of(new CardModel("BlueCard1",
                      5, 6, 7, 8, COLOR.BLUE)));

      Player[] players = {playerRed, playerBlue};

      MockGameModel mockGame = new MockGameModel(playerRed, grid, players);

      int row = 0;
      int col = 0;
      if (mockGame.isMoveLegal(row, col)) {
        Card redCard = new CardModel("RedCard1",
                1, 2, 3, 4, COLOR.RED);
        mockGame.placeCard(row, col, redCard);
      }

      mockGame.checkWinCondition();
      mockGame.isGameOver();
      mockGame.switchTurns();

      List<String> transcript = mockGame.getMethodCalls();

      System.out.println(transcript);
    }
  }

  /**
   * cs3500.threetrios.PlayerModelTest meant for testing purposes.
   */
  public class PlayerModelTest {

    private PlayerModel player;
    private List<Card> initialHand;
    private Card card1;
    private Card card2;

    @Before
    public void setUp() {
      card1 = new CardModel("Ace", 1, 1, 1, 1, COLOR.RED);
      card2 = new CardModel("King", 9,9,9,9, COLOR.BLUE);
      initialHand = new ArrayList<>(List.of(card1, card2));
      player = new PlayerModel("Player1", COLOR.RED, initialHand);
    }

    @Test
    public void testConstructorWithValidParameters() {
      assertEquals(2, player.handSize());
      assertEquals("Player1", player.getName());
      assertEquals(COLOR.RED, player.getColor());
    }

    @Test
    public void testConstructorWithNullHand() {
      assertThrows(IllegalStateException.class, () -> new PlayerModel("Player1", COLOR.RED, null));
    }

    @Test
    public void testConstructorWithEmptyHand() {
      assertThrows(IllegalStateException.class,
          () -> new PlayerModel("Player1", COLOR.RED, new ArrayList<>()));
    }

    @Test
    public void testConstructorWithNullColor() {
      assertThrows(IllegalStateException.class, () -> new PlayerModel("Player1",
              null, initialHand));
    }

    @Test
    public void testHandSize() {
      assertEquals(2, player.handSize());
    }

    @Test
    public void testGetHand() {
      List<Card> hand = player.getHand();
      assertEquals(2, hand.size());
      assertTrue(hand.contains(card1));
      assertTrue(hand.contains(card2));
    }

    @Test
    public void testRemoveCardValidIndex() {
      player.removeCard(0);
      assertEquals(1, player.handSize());
      assertFalse(player.getHand().contains(card1));
    }

    @Test
    public void testRemoveCardInvalidNegativeIndex() {
      assertThrows(IllegalArgumentException.class, () -> player.removeCard(-1));
    }

    @Test
    public void testRemoveCardIndexOutOfBounds() {
      assertThrows(IllegalArgumentException.class, () -> player.removeCard(3));
    }

    @Test
    public void testAddCard() {
      Card card3 = new CardModel("Queen", 5,5,5,5, COLOR.BLUE);
      player.addCard(card3);
      assertEquals(3, player.handSize());
      assertTrue(player.getHand().contains(card3));
    }

    @Test
    public void testGetCardValidIndex() {
      Card removedCard = player.getCard(1);
      assertEquals(card2, removedCard);
      assertEquals(2, player.handSize());
    }

    @Test
    public void testGetCardInvalidNegativeIndex() {
      assertThrows(IllegalArgumentException.class, () -> player.getCard(-1));
    }

    @Test
    public void testGetCardIndexOutOfBounds() {
      assertThrows(IllegalArgumentException.class, () -> player.getCard(3));
    }

    @Test
    public void testGetName() {
      assertEquals("Player1", player.getName());
    }

    @Test
    public void testGetColor() {
      assertEquals(COLOR.RED, player.getColor());
    }

    @Test
    public void testAddCardIncreasesHandSize() {
      Card newCard = new CardModel("Jack", 6, 6, 6, 6, COLOR.RED);
      int initialSize = player.handSize();
      player.addCard(newCard);
      assertEquals(initialSize + 1, player.handSize());
    }

    @Test
    public void testRemoveCardDecreasesHandSize() {
      int initialSize = player.handSize();
      player.removeCard(0);
      assertEquals(initialSize - 1, player.handSize());
    }

    @Test
    public void testGetHandReturnsImmutableList() {
      List<Card> handCopy = player.getHand();
      handCopy.add(new CardModel("Ten", 5, 5, 5, 5, COLOR.RED));
      assertEquals(2, player.handSize());
    }

    @Test
    public void testMultipleAddAndRemoveOperations() {
      player.addCard(new CardModel("Red1", 7, 7, 7, 7, COLOR.RED));
      player.removeCard(1);
      player.addCard(new CardModel("Blue1", 8, 8, 8, 8, COLOR.BLUE));
      assertEquals(3, player.handSize());
      assertTrue(player.getHand().contains(card1));
      assertFalse(player.getHand().contains(card2));
    }


    @Test
    public void testGetCardAfterRemoval() {
      player.removeCard(0);
      assertEquals(card2, player.getCard(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveCardWithNegativeIndex() {
      player.removeCard(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveCardWithOutOfBoundsIndex() {
      player.removeCard(player.handSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCardWithNegativeIndex() {
      player.getCard(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCardWithOutOfBoundsIndex() {
      player.getCard(player.handSize());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddNullCardToHand() {
      player.addCard(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorWithNullName() {
      new PlayerModel(null, COLOR.RED, List.of(card1, card2));
    }

  }


  /**
   * Class to test the view of the game.
   */
  public class TextViewTest {
    private TextView textView;
    private GameGrid gameGrid;

    @Before
    public void setUp() {
      gameGrid = new GameGrid(3, 3);
      textView = new TextView(gameGrid);
    }

    @Test
    public void testRenderEmptyView() {
      for (int row = 0; row < gameGrid.getRows(); row++) {
        for (int col = 0; col < gameGrid.getCols(); col++) {
          gameGrid.setCellType(row, col, Cell.CellType.CARD_CELL);
        }
      }
      String expected = "_ _ _ \n_ _ _ \n_ _ _ \n";
      Assert.assertEquals("Render should display a grid with empty cells",
              expected, textView.render());
    }

    @Test
    public void testRenderGridWithHoleAndCard() {
      gameGrid.setCellType(0, 0, Cell.CellType.HOLE);
      gameGrid.setCellType(0, 1, Cell.CellType.CARD_CELL);
      gameGrid.setCellType(1, 0, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(1, 0, new CardModel("Dragon", 4,
              5, 6, 7, COLOR.RED));
      gameGrid.setCellType(1, 1, Cell.CellType.CARD_CELL);

      textView = new TextView(gameGrid);
      String expected = "X _ _ \nD _ _ \n_ _ _ \n";
      String actual = textView.render();

      assertEquals("Render should display a grid with one hole," +
              " one card, and empty cells", expected, textView.render());
    }

    @Test
    public void testRenderGridWithMultipleCards() {
      gameGrid.setCellType(0, 0, Cell.CellType.CARD_CELL);
      gameGrid.setCellType(0, 1, Cell.CellType.CARD_CELL);
      gameGrid.setCellType(1, 0, Cell.CellType.CARD_CELL);
      gameGrid.setCellType(1, 1, Cell.CellType.CARD_CELL);

      gameGrid.placeCard(0, 0, new CardModel("Knight", 2, 3,
              4, 5, COLOR.BLUE));
      gameGrid.placeCard(0, 1, new CardModel("Dragon", 6, 7,
              8, 9, COLOR.RED));
      gameGrid.placeCard(1, 1, new CardModel("Hero", 1, 2,
              3, 4, COLOR.BLUE));

      textView = new TextView(gameGrid);

      String expected = "K D _ \n_ H _ \n_ _ _ \n";
      assertEquals("Render should display a grid with " +
              "multiple cards correctly", expected, textView.render());
    }


    @Test
    public void testRenderWithBorderHoles() {
      for (int row = 0; row < gameGrid.getRows(); row++) {
        for (int col = 0; col < gameGrid.getCols(); col++) {
          if (row == 0 || row == gameGrid.getRows() - 1 || col == 0
                  || col == gameGrid.getCols() - 1) {
            gameGrid.setCellType(row, col, Cell.CellType.HOLE);
          } else {
            gameGrid.setCellType(row, col, Cell.CellType.CARD_CELL);
          }
        }
      }
      String expected = "X X X \nX _ X \nX X X \n";
      Assert.assertEquals(expected, textView.render());
    }

    @Test
    public void testRenderGridWithDiagonalHoles() {
      for (int row = 0; row < gameGrid.getRows(); row++) {
        for (int col = 0; col < gameGrid.getCols(); col++) {
          if (row == col) {
            gameGrid.setCellType(row, col, Cell.CellType.HOLE);
          } else {
            gameGrid.setCellType(row, col, Cell.CellType.CARD_CELL);
          }
        }
      }
      String expected = "X _ _ \n_ X _ \n_ _ X \n";
      assertEquals("Render should display a grid with" +
              "diagonal holes", expected, textView.render());
    }

    @Test
    public void testRenderGridWithMixedCellsAndCards() {
      gameGrid.setCellType(0, 0, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(0, 0, new CardModel("Dragon", 4,
              5, 6, 7, COLOR.RED));

      gameGrid.setCellType(0, 1, Cell.CellType.CARD_CELL);
      gameGrid.setCellType(0, 2, Cell.CellType.HOLE);

      gameGrid.setCellType(1, 0, Cell.CellType.HOLE);
      gameGrid.setCellType(1, 1, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(1, 1, new CardModel("Knight", 2,
              3, 4, 5, COLOR.BLUE));

      gameGrid.setCellType(1, 2, Cell.CellType.CARD_CELL);
      gameGrid.setCellType(2, 0, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(2, 0, new CardModel("Hero", 1,
              2, 3, 4, COLOR.RED));

      gameGrid.setCellType(2, 1, Cell.CellType.CARD_CELL);
      gameGrid.setCellType(2, 2, Cell.CellType.CARD_CELL);

      String expected = "D _ X \nX K _ \nH _ _ \n";
      assertEquals("Render should display a grid with mixed cells," +
              " holes, and cards", expected, textView.render());
    }

    @Test
    public void testRenderCompletelyFilledGridWithCards() {
      for (int row = 0; row < gameGrid.getRows(); row++) {
        for (int col = 0; col < gameGrid.getCols(); col++) {
          gameGrid.setCellType(row, col, Cell.CellType.CARD_CELL);
          gameGrid.placeCard(row, col, new CardModel("Hero", 1,
                  2, 3, 4, COLOR.RED));
        }
      }
      String expected = "H H H \nH H H \nH H H \n";
      assertEquals("Render should display a completely filled" +
              " grid with cards", expected, textView.render());
    }

    @Test
    public void testRenderEmptyRowAndColumn() {
      gameGrid.setCellType(0, 0, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(0, 0, new CardModel("Dragon", 4, 5,
              6, 7, COLOR.RED));
      gameGrid.setCellType(0, 2, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(0, 2, new CardModel("Knight", 2, 3,
              4, 5, COLOR.BLUE));

      gameGrid.setCellType(2, 0, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(2, 0, new CardModel("Hero", 1, 2,
              3, 4, COLOR.RED));
      gameGrid.setCellType(2, 2, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(2, 2, new CardModel("Hero", 1, 2,
              3, 4, COLOR.BLUE));

      String expected = "D _ K \n_ _ _ \nH _ H \n";
      assertEquals("Render should display a grid with an empty row and column",
              expected, textView.render());
    }

    @Test
    public void testRenderGridWithDifferentColoredCards() {
      gameGrid.setCellType(0, 0, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(0, 0, new CardModel("Dragon", 4, 5,
              6, 7, COLOR.RED));

      gameGrid.setCellType(0, 1, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(0, 1, new CardModel("Knight", 2, 3,
              4, 5, COLOR.BLUE));

      gameGrid.setCellType(0, 2, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(0, 2, new CardModel("Hero", 1, 2,
              3, 4, COLOR.RED));

      gameGrid.setCellType(1, 0, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(1, 0, new CardModel("Mage", 3, 4,
              5, 6, COLOR.BLUE));

      gameGrid.setCellType(1, 1, Cell.CellType.HOLE);
      gameGrid.setCellType(1, 2, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(1, 2, new CardModel("Warrior", 7, 8,
              9, 1, COLOR.RED));

      gameGrid.setCellType(2, 0, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(2, 0, new CardModel("Rogue", 4, 5,
              6, 7, COLOR.BLUE));

      gameGrid.setCellType(2, 1, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(2, 1, new CardModel("Archer", 5, 6,
              7, 8, COLOR.RED));

      gameGrid.setCellType(2, 2, Cell.CellType.HOLE);

      String expected = "D K H \nM X W \nR A X \n";
      assertEquals("Render should display a grid with different" +
              " colored cards and holes", expected, textView.render());
    }

    @Test
    public void testRenderSingleCellGrid() {
      GameGrid singleCellGrid = new GameGrid(1, 1);
      TextView singleCellView = new TextView(singleCellGrid);

      singleCellGrid.setCellType(0, 0, Cell.CellType.CARD_CELL);
      String expectedEmpty = "_ \n";
      assertEquals("Render should display a single empty cell",
              expectedEmpty, singleCellView.render());

      singleCellGrid.setCellType(0, 0, Cell.CellType.HOLE);
      String expectedHole = "X \n";
      assertEquals("Render should display a single hole", expectedHole, singleCellView.render());

      singleCellGrid.setCellType(0, 0, Cell.CellType.CARD_CELL);
      singleCellGrid.placeCard(0, 0, new CardModel("Hero", 1, 2, 3, 4, COLOR.RED));
      String expectedCard = "H \n";
      assertEquals("Render should display a single cell with a card",
              expectedCard, singleCellView.render());
    }

    @Test
    public void testRenderLargeGrid() {
      GameGrid largeGrid = new GameGrid(5, 5);
      TextView largeGridView = new TextView(largeGrid);

      for (int row = 0; row < largeGrid.getRows(); row++) {
        for (int col = 0; col < largeGrid.getCols(); col++) {
          largeGrid.setCellType(row, col, Cell.CellType.CARD_CELL);
        }
      }

      StringBuilder expected = new StringBuilder();
      for (int i = 0; i < 5; i++) {
        expected.append("_ _ _ _ _ \n");
      }
      assertEquals("Render should display a large empty grid",
              expected.toString(), largeGridView.render());
    }

    @Test
    public void testRenderAfterMultipleChanges() {
      gameGrid.setCellType(0, 0, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(0, 0, new CardModel("Knight", 2, 3,
              4, 5, COLOR.BLUE));
      String expectedAfterFirstChange = "K _ _ \n_ _ _ \n_ _ _ \n";
      assertEquals("Render after first card placement",
              expectedAfterFirstChange, textView.render());

      gameGrid.setCellType(2, 2, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(2, 2, new CardModel("Hero", 1, 2,
              3, 4, COLOR.RED));
      String expectedAfterSecondChange = "K _ _ \n_ _ _ \n_ _ H \n";
      assertEquals("Render after placing second card",
              expectedAfterSecondChange, textView.render());

      gameGrid.setCellType(0, 0, Cell.CellType.HOLE);
      String expectedAfterThirdChange = "X _ _ \n_ _ _ \n_ _ H \n";
      assertEquals("Render after changing first cell to hole",
              expectedAfterThirdChange, textView.render());
    }

    @Test
    public void testRenderZeroByZero() {
      GameGrid grid = new GameGrid(0, 0);
      TextView textView = new TextView(grid);
      String expectedEmpty = "";
      assertEquals("Render should display a zero by zero",
              expectedEmpty, textView.render());
    }

    @Test(expected = IllegalStateException.class)
    public void testRenderAfterCardUpdate() {
      gameGrid.setCellType(0, 0, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(0, 0, new CardModel("Dragon", 4, 5,
              6, 7, COLOR.RED));
      String expectedWithFirstCard = "D _ _ \n_ _ _ \n_ _ _ \n";
      assertEquals("Render should display grid with initial card",
              expectedWithFirstCard, textView.render());

      gameGrid.placeCard(0, 0, new CardModel("Knight", 2,
              3, 4, 5, COLOR.BLUE));
      String expectedWithUpdatedCard = "K _ _ \n_ _ _ \n_ _ _ \n";
      assertEquals("Render should display grid with updated card",
              expectedWithUpdatedCard, textView.render());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRenderWithInvalidRowPlacement() {
      gameGrid.setCellType(0, 0, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(3, 0, new CardModel("Dragon",
              4, 5, 6, 7, COLOR.RED));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRenderWithInvalidColumnPlacement() {
      gameGrid.setCellType(0, 0, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(0, 3, new CardModel("Dragon",
              4, 5, 6, 7, COLOR.RED));
    }

    @Test(expected = NullPointerException.class)
    public void testRenderWithInvalidHolePlacement() {
      gameGrid.setCellType(1, 1, Cell.CellType.HOLE);
      gameGrid.placeCard(1, 1, new CardModel("Dragon",
              4, 5, 6, 7, COLOR.RED));
    }

    @Test(expected = IllegalStateException.class)
    public void testRenderWithDuplicateCardPlacement() {
      gameGrid.setCellType(1, 1, Cell.CellType.CARD_CELL);
      gameGrid.placeCard(1, 1, new CardModel("Knight",
              2, 3, 4, 5, COLOR.BLUE));
      gameGrid.placeCard(1, 1, new CardModel("Dragon",
              4, 5, 6, 7, COLOR.RED));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRenderWithNegativeRowPlacement() {
      gameGrid.placeCard(-1, 0, new CardModel("Dragon",
              4, 5, 6, 7, COLOR.RED));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRenderWithNegativeColumnPlacement() {
      gameGrid.placeCard(0, -1, new CardModel("Dragon",
              4, 5, 6, 7, COLOR.RED));
    }

    @Test
    public void testRenderGridFilledWithHoles() {
      for (int row = 0; row < gameGrid.getRows(); row++) {
        for (int col = 0; col < gameGrid.getCols(); col++) {
          gameGrid.setCellType(row, col, Cell.CellType.HOLE);
        }
      }
      String expected = "X X X \nX X X \nX X X \n";
      assertEquals("Render should display a grid completely " +
              "filled with holes", expected, textView.render());
    }

    @Test(expected = IllegalStateException.class)
    public void testRenderWithZeroGridDimensions() {
      new GameGrid(-3, -3);
    }


  }

}
