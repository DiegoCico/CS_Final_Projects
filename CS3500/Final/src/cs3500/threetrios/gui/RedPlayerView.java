package cs3500.threetrios.gui;

import java.awt.Color;

import cs3500.threetrios.card.COLOR;
import cs3500.threetrios.game.ReadOnlyGameModel;

/**
 * View implementation dedicated to controlling the Red Player.
 */
public class RedPlayerView extends ThreeTriosViewImpl {

  private String lastErrorMessage;

  /**
   * Constructs the Red Player view with the game model.
   * @param model the game model
   */
  public RedPlayerView(ReadOnlyGameModel model) {
    super(model);
    setTitle("Three Trios - Red Player");
    getContentPane().setBackground(Color.PINK);
  }

  /**
   * Refreshes the view to reflect the current game state.
   */
  @Override
  public void refresh() {
    // Only update the red player panel
    updatePlayerPanel(COLOR.RED, getRedPlayerPanel());
    getGridPanel().repaint();
    revalidate();
    repaint();
  }

  /**
   * Overrides to only display updates related to the Red Player.
   *
   * @param message the error message to display
   */
  @Override
  public void displayErrorMessage(String message) {
    lastErrorMessage = message;
    super.displayErrorMessage("Red Player: " + message + "\n" + lastErrorMessage);
  }

  /**
   * Retrieves the error message on the popup panel.
   * @return the error message
   */
  public String getLastErrorMessage() {
    return lastErrorMessage;
  }
}
