package cs3500.threetrios.player;

import java.util.List;

import cs3500.threetrios.card.COLOR;
import cs3500.threetrios.card.Card;

/**
 * Represents a player in the game with methods to manage their hand of cards.
 */
public interface Player {

  /**
   * Gets the name of the player.
   *
   * @return the player's name
   */
  String getName();

  /**
   * Gets the size of the player's hand.
   *
   * @return the number of cards in the player's hand
   */
  int handSize();

  /**
   * Returns the list of cards in the player's hand.
   *
   * @return the player's hand as a list of cards
   */
  List<Card> getHand();

  /**
   * Removes a card from the player's hand at the specified index.
   *
   * @param index the position of the card to remove
   */
  void removeCard(int index);

  /**
   * Adds a card to the player's hand.
   *
   * @param card the card to add to the hand
   */
  void addCard(Card card);

  /**
   * Gets a card from the player's hand at the specified index.
   *
   * @param index the position of the card to retrieve
   * @return the card at the specified index
   */
  Card getCard(int index);

  /**
   * getting the color.
   * @return color.
   */
  COLOR getColor();
}
