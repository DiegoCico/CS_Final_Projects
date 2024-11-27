package cs3500.threetrios.game;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import cs3500.threetrios.card.COLOR;
import cs3500.threetrios.card.Card;
import cs3500.threetrios.card.CardModel;
import cs3500.threetrios.parser.BoardConfigParser;
import cs3500.threetrios.player.Player;
import cs3500.threetrios.player.PlayerModel;

/**
 * Represents the model for the Three Trios Game. Manages the game grid,
 * players, card placements, turns, and game logic, including card battles
 * and win conditions
 * Class Invariants:
 * The grid and players are never null.
 * The game always has exactly two players.
 * The `turn` variable is always 0 or 1, representing which player's turn it is.
 * Each player has exactly (N+1)/2 cards in their hand,
 * where N is the number of card cells in the grid.
 */
public class GameModel implements Game, ReadOnlyGameModel {

  private final Grid grid;
  private final Player[] players;
  private int turn;
  private Random rand = new Random();

  /**
   * Constructs a GameModel with a specified grid and players.
   *
   * @param grid            the game grid
   * @param players         an array of two players
   * @throws IllegalArgumentException         if grid or players are null,
   *            or if players array length is not 2
   */
  public GameModel(Grid grid, Player[] players) {
    if (grid == null || players == null || players.length != 2) {
      throw new IllegalArgumentException("Grid and Players cannot be null");
    }

    this.grid = grid;
    this.players = players;
    this.turn = 0;
  }

  /**
   * Constructs a GameModel by parsing a configuration file to initialize the grid and players.
   *
   * @param path the file path for the configuration
   * @throws FileNotFoundException if the configuration file is not found
   */
  public GameModel(String path) throws FileNotFoundException {
    GameModel parsedGame = BoardConfigParser.parseBoardConfig(path);
    this.grid = parsedGame.getGrid();
    this.players = parsedGame.getPlayers();
    this.turn = 0;
  }

  /**
   * Constructs a GameModel with a grid and a deck of cards, distributing the cards among players.
   *
   * @param grid the game grid
   * @param deck the deck of cards parsed from the configuration
   * @throws IllegalArgumentException if grid or deck is null, or if card distribution fails
   */
  public GameModel(Grid grid, List<Card> deck) {
    if (grid == null || deck == null) {
      throw new IllegalArgumentException("Grid and sufficient cards are required.");
    }

    this.grid = grid;
    this.turn = 0;

    int numCardsEachPlayer = (grid.getNumCardsCells() + 1) / 2;
    List<Card> redPlayerCards = drawCards(deck, numCardsEachPlayer, COLOR.RED);
    List<Card> bluePlayerCards = drawCards(deck, numCardsEachPlayer, COLOR.BLUE);

    if (redPlayerCards.size() != bluePlayerCards.size()) {
      throw new IllegalArgumentException("Red cards are not the same size as Blue cards");
    }

    this.players = new Player[] {
      new PlayerModel("Player Red", COLOR.RED, redPlayerCards),
      new PlayerModel("Player Blue", COLOR.BLUE, bluePlayerCards)
    };
  }

  /**
   * Constructs a GameModel with a specified grid, initializing players with default cards.
   *
   * @param grid the game grid
   * @throws IllegalArgumentException if grid is null
   */
  public GameModel(Grid grid) {
    if (grid == null) {
      throw new IllegalArgumentException("Grid and Players cannot be null");
    }

    this.grid = grid;
    this.players = new Player[] {
      new PlayerModel("Player Red", COLOR.RED, getCards()),
      new PlayerModel("Player Blue", COLOR.BLUE, getCards())
    };
    this.turn = 0;
  }

  /**
   * Draws a specified number of cards for a player and assigns a color to each card.
   *
   * @param deck the available deck of cards
   * @param numCards the number of cards to draw
   * @param playerColor the color to assign to each card
   * @return a list of drawn cards with the assigned color
   */
  private List<Card> drawCards(List<Card> deck, int numCards, COLOR playerColor) {
    List<Card> playerHand = new ArrayList<>();

    for (int i = 0; i < numCards && !deck.isEmpty(); i++) {

      CardModel card = (CardModel) deck.remove(0);
      card.switchColor(playerColor);
      playerHand.add(card);
    }
    return playerHand;
  }

  /**
   * Switches turns between players.
   *
   * @throws IllegalArgumentException if the current turn is invalid
   */
  @Override
  public void switchTurns() {
    if (turn == 0 || turn == 1) {
      turn = 1 - turn;
    } else {
      throw new IllegalArgumentException("Invalid turn");
    }
  }

  /**
   * Places a card at the specified row and column on the grid.
   *
   * @param row the row to place the card
   * @param col the column to place the card
   * @param card the card to place
   * @throws IllegalStateException if it is not the current player's turn or if the cell is occupied
   */
  @Override
  public void placeCard(int row, int col, Card card) {
    Player currentPlayer = players[turn];

    if (!card.getColor().equals(currentPlayer.getColor())) {
      throw new IllegalStateException("It is not the current player's turn.");
    }

    if (grid.validPosition(row, col)) {
      currentPlayer.removeCard(currentPlayer.getHand().indexOf(card));

      grid.placeCard(row, col, card);
    } else {
      throw new IllegalStateException("Cannot place a card in an occupied or invalid cell.");
    }
  }

  /**
   * Retrieves all the cards in the game.
   *
   * @return a list of cards representing each player's hand
   */
  @Override
  public List<Card> getCards() {
    int numCardCells = grid.getNumCardsCells();
    int numCardsEachPlayer = (numCardCells + 1) / 2;
    List<Card> cards = new ArrayList<>();

    for (int i = 0; i < numCardsEachPlayer; i++) {
      int north = getRandomCardValue();
      int south = getRandomCardValue();
      int east = getRandomCardValue();
      int west = getRandomCardValue();

      CardModel card = new CardModel(getRandomName(), north, south, east, west, COLOR.RED);
      cards.add(card);
    }
    return cards;
  }

  /**
   * Generates a random name for a card.
   *
   * @return a random name as a String
   */
  private String getRandomName() {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder randomName = new StringBuilder();

    for (int i = 0; i < 5; i++) {
      int index = this.rand.nextInt(characters.length());
      randomName.append(characters.charAt(index));
    }

    return randomName.toString();
  }

  /**
   * Generates a random card value between 1 and 10.
   *
   * @return a random integer between 1 and 10
   */
  private int getRandomCardValue() {
    return this.rand.nextInt(10) + 1;
  }

  /**
   * Returns a copy of the current player.
   *
   * @return the current player
   */
  @Override
  public Player getCurrentPlayer() {
    return new PlayerModel(players[turn]);
  }

  /**
   * Returns a copy of the current game grid.
   *
   * @return the game grid
   */
  @Override
  public Grid getGrid() {
    return new GameGrid(this.grid);
  }

  /**
   * Conducts a battle between the card placed at the specified row and column
   * and adjacent cards.
   *
   * @param row the row where the card is located
   * @param col the column where the card is located
   */
  @Override
  public void battleCards(int row, int col, Set<Card> flippedCards) {
    Card placedCard = grid.getCard(row, col);
    flippedCards.add(placedCard);

    int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    int[] opposingSides = {2, 3, 0, 1};

    for (int i = 0; i < directions.length; i++) {
      int newRow = row + directions[i][0];
      int newCol = col + directions[i][1];

      if (grid.validPosition(newRow, newCol)) {
        Card adjacentCard = grid.getCard(newRow, newCol);

        if (adjacentCard != null
                && adjacentCard.getColor() != placedCard.getColor()
                && !flippedCards.contains(adjacentCard)) {
          int placedCardAttack = getAttackValue(placedCard, i);
          int adjacentCardAttack = getAttackValue(adjacentCard, opposingSides[i]);

          System.out.println("Placed card attack: "
                  + placedCardAttack + ", Adjacent card attack: "
                  + adjacentCardAttack + " (Direction: " + i + ")");

          if (placedCardAttack > adjacentCardAttack) {
            adjacentCard.switchColor(placedCard.getColor());
            System.out.println("Flipping card at " + newRow + ", " + newCol);

            flippedCards.add(adjacentCard);
            battleCards(newRow, newCol, flippedCards);
          }
        }
      }
    }
  }

  /**
   * Gets a certain attack value associated with a Card and direction.
   *
   * @param card a game card
   * @param direction a direction
   * @return the value of the card in the specified direction
   */
  private int getAttackValue(Card card, int direction) {
    switch (direction) {
      case 0: return card.getNorth();
      case 1: return card.getEast();
      case 2: return card.getSouth();
      case 3: return card.getWest();
      default: throw new IllegalArgumentException("Invalid direction");
    }
  }

  /**
   * Gets the current array of players.
   *
   * @return the array of players
   */
  @Override
  public Player[] getPlayers() {
    return players;
  }

  /**
   * Gets the current player's turn.
   *
   * @return the player's turn
   */
  @Override
  public int getTurn() {
    return this.turn;
  }

  /**
   * Checks if the game has ended by comparing the number of cards owned by each player.
   *
   * @return "Red Wins", "Blue Wins", or "Tie" based on card ownership
   */
  @Override
  public String checkWinCondition() {
    int redCount = 0;
    int blueCount = 0;

    for (int row = 0; row < grid.getRows(); row++) {
      for (int col = 0; col < grid.getCols(); col++) {
        Card card = grid.getCard(row, col);
        if (card != null) {
          if (card.getColor() == COLOR.RED) {
            redCount++;
          } else if (card.getColor() == COLOR.BLUE) {
            blueCount++;
          }
        }
      }
    }

    if (redCount > blueCount) {
      return "Red Wins";
    }
    else if (blueCount > redCount) {
      return "Blue Wins";
    }
    else {
      return "Tie";
    }
  }

  /**
   * Checks if the game is over by verifying that all card cells are filled.
   *
   * @return true if all card cells are filled, false otherwise
   */
  public boolean isGameOver() {
    for (int row = 0; row < grid.getRows(); row++) {
      for (int col = 0; col < grid.getCols(); col++) {
        if (grid.getCard(row, col) == null
                && grid.getCellType(row, col) == Cell.CellType.CARD_CELL) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Gets the size of the grid.
   *
   * @return the number of rows in the grid
   */
  @Override
  public int getGridSize() {
    return grid.getRows();
  }

  /**
   * Gets the card located at a specific row and column on the grid.
   *
   * @param row the row to check
   * @param col the column to check
   * @return the card at the specified location
   */
  @Override
  public Card getCardAt(int row, int col) {
    return grid.getCard(row, col);
  }

  /**
   * Determines the winner based on card ownership.
   *
   * @return the result of the game: "Red Wins", "Blue Wins", or "Tie"
   */
  @Override
  public String getWinner() {
    return checkWinCondition();
  }

  /**
   * Checks if a move is legal based on the current state of the game.
   *
   * @param row the row to check
   * @param col the column to check
   * @return true if the move is legal, false otherwise
   */
  @Override
  public boolean isMoveLegal(int row, int col) {
    return !isGameOver() && grid.getCard(row, col) == null
            && grid.getCellType(row, col) == Cell.CellType.CARD_CELL;
  }

  /**
   * Gets the current player model.
   *
   * @return the current player model
   */
  public Player getCurrentPlayerModel() {
    return players[turn];
  }

}
