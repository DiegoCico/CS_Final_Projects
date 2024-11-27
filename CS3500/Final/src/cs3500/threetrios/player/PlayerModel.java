package cs3500.threetrios.player;

import java.util.ArrayList;
import java.util.List;
import cs3500.threetrios.card.COLOR;
import cs3500.threetrios.card.Card;

/**
 * Represents a player in the Three Trios game with a hand of cards, a name, and an assigned color.
 * Provides methods for managing the player's hand and retrieving player information.
 */
public class PlayerModel implements Player {

  private List<Card> hand;
  private final COLOR color;
  private final String name;

  /**
   * Constructs a {@code PlayerModel} with the specified name, color, and hand of cards.
   *
   * @param name  the name of the player
   * @param color the color associated with the player
   * @param hand  the initial hand of cards for the player
   * @throws IllegalStateException if the hand is null or empty
   */
  public PlayerModel(String name, COLOR color, List<Card> hand) {
    if (hand == null || hand.isEmpty()) {
      throw new IllegalStateException("Hand cannot be null or empty.");
    }
    if (color == null) {
      throw new IllegalStateException("Color cannot be null.");
    }
    if (name == null || name.isEmpty()) {
      throw new IllegalStateException("Name cannot be null or empty.");
    }
    this.name = name;
    this.color = color;
    this.hand = new ArrayList<>(hand);
  }

  /**
   * Constructs a {@code PlayerModel} as a copy of another player model.
   *
   * @param model the player model to copy
   */
  public PlayerModel(Player model) {
    this.name = model.getName();
    this.color = model.getColor();
    this.hand = model.getHand();
  }

  /**
   * Returns the number of cards in the player's hand.
   *
   * @return the number of cards in the player's hand
   */
  @Override
  public int handSize() {
    return hand.size();
  }

  /**
   * Returns a copy of the player's hand.
   *
   * @return a new list containing the cards in the player's hand
   */
  @Override
  public List<Card> getHand() {
    return new ArrayList<>(hand);
  }

  /**
   * Removes a card from the player's hand at the specified index.
   *
   * @param index the index of the card to remove
   * @throws IllegalArgumentException if the index is out of bounds
   */
  @Override
  public void removeCard(int index) {
    if (index < 0 || index >= hand.size()) {
      throw new IllegalArgumentException("Index out of bounds.");
    }
    hand.remove(index);
  }

  /**
   * Adds a card to the player's hand.
   *
   * @param card the card to add to the hand
   * @throws IllegalStateException if the card is null
   */
  @Override
  public void addCard(Card card) {
    if (card == null) {
      throw new IllegalStateException("Card cannot be null.");
    }
    hand.add(card);
  }

  /**
   * Retrieves the card from the player's hand at the specified index.
   *
   * @param index the index of the card to retrieve
   * @return the card at the specified index
   * @throws IllegalArgumentException if the index is out of bounds
   */
  @Override
  public Card getCard(int index) {
    if (index < 0 || index >= hand.size()) {
      throw new IllegalArgumentException("Index out of bounds.");
    }
    return hand.get(index);
  }

  /**
   * Returns the player's name.
   *
   * @return the name of the player
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Returns the player's color.
   *
   * @return the color associated with the player
   */
  @Override
  public COLOR getColor() {
    return this.color;
  }
}
