package cs3500.threetrios.gui;

/**
 * Represents the view interface for the Three Trios game, responsible for displaying
 * game information and handling user interface updates.
 */
public interface ThreeTriosGameView {

  /**
   * Sets the features or controls for the view, allowing it to communicate with
   * the game logic.
   *
   * @param features the Features object that provides the control actions.
   */
  void setFeatures(Features features);

  /**
   * Refreshes the view to reflect the current game state.
   */
  void refresh();

  /**
   * Displays a message to indicate that the game has ended.
   */
  void displayGameOverMessage();

  /**
   * Displays the current player's turn information.
   *
   * @param player a String representing the current player's name or details.
   */
  void displayCurrentPlayer(String player);

  /**
   * Displays an error message in the view.
   *
   * @param message a String representing the error message to display.
   */
  void displayErrorMessage(String message);

  /**
   * Checks if this view belongs to the red player.
   * @return is it is RedView
   */
  boolean isRedPlayerView();

  /**
   * Checks if this view belongs to the blue player.
   * @return is it is BlueView
   */
  boolean isBluePlayerView();

}
