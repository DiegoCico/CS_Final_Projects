package cs3500.threetrios.card;

/**
 * Represents a general card with a color and number, and
 * provides methods to access these properties.
 */
public interface Card {

  /**
   * Switches the card's color.
   *
   * @param color the new color
   */
  void switchColor(COLOR color);

  /**
   * Gets the card color.
   *
   * @return return the enum of the card color.
   */
  COLOR getColor();

  /**
   * gets the integer for south.
   *
   * @return integer for south.
   */
  int getSouth();

  /**
   * gets the integer for north.
   *
   * @return integer for north.
   */
  int getNorth();

  /**
   * gets the integer for east.
   *
   * @return integer for east.
   */
  int getEast();

  /**
   * gets the integer for west.
   *
   * @return integer for west.
   */
  int getWest();

  /**
   *  name of the  card.
   * @return name of the card.
   */
  String getName();
}
