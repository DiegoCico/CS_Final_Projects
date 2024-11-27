package cs3500.threetrios.card;

import java.util.Objects;

/**
 * Represents a card with four numbered sides (north, south, east, west) and a color.
 */
public class CardModel implements Card {

  private final String name;
  private final NUMBER north;
  private final NUMBER south;
  private final NUMBER east;
  private final NUMBER west;
  private COLOR color;

  /**
   * Creates a {CardModel} with specified numbers and color.
   *
   * @param north the north side number (1-9 or A)
   * @param south the south side number (1-9 or A)
   * @param east the east side number (1-9 or A)
   * @param west the west side number (1-9 or A)
   * @param color the card's color
   * @throws IllegalStateException if an invalid number is provided
   */
  public CardModel(String name, int north, int south, int east, int west, COLOR color) {
    try {
      this.north = NUMBER.fromValue(north);
      this.south = NUMBER.fromValue(south);
      this.east = NUMBER.fromValue(east);
      this.west = NUMBER.fromValue(west);
      this.color = color;
      this.name = name;
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Invalid number. Must be between 1-9 or A.");
    }
  }

  /**
   * getting north.
   * @return the number on the north side
   */
  public int getNorth() {
    return north.getValue();
  }

  /**
   * getting south.
   * @return the number on the south side
   */
  public int getSouth() {
    return south.getValue();
  }

  /**
   * getting east.
   * @return the number on the east side
   */
  public int getEast() {
    return east.getValue();
  }

  /**
   * getting west.
   * @return the number on the west side
   */
  public int getWest() {
    return west.getValue();
  }

  /**
   * getting color.
   * @return the card's color
   */
  public COLOR getColor() {
    return color;
  }

  /**
   * Switches the card's color.
   *
   * @param color the new color
   */
  @Override
  public void switchColor(COLOR color) {
    this.color = color;
  }

  /**
   * Checks if two {CardModel} objects are equal.
   *
   * @param obj the object to compare
   * @return {@code true} if the cards are equal, {@code false} otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    CardModel other = (CardModel) obj;
    return north == other.north
            && south == other.south
            && east == other.east
            && west == other.west
            && color == other.color;
  }

  /**
   * hashcode for cardmodel.
   * @return the hash code of the card.
   */
  @Override
  public int hashCode() {
    return Objects.hash(north, south, east, west, color);
  }

  /**
   * to string for card.
   * @return a string representation of the card.
   */
  public String toString() {
    return name + ": " + north.getValue() + " "
            + south.getValue() + " "
            + east.getValue() + " "
            + west.getValue() + " "
            + color;
  }

  /**
   *  name of the  card.
   * @return name of the card.
   */
  public String getName() {
    return name;
  }
}