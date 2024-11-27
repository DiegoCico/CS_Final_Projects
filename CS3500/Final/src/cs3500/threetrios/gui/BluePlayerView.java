package cs3500.threetrios.gui;

import java.awt.Color;

import cs3500.threetrios.card.COLOR;
import cs3500.threetrios.game.ReadOnlyGameModel;

/**
 * View implementation dedicated to controlling the Blue Player.
 */
public class BluePlayerView extends ThreeTriosViewImpl {

  private String lastErrorMessage;

  /**
   * Constructs the Blue Player view with the game model.
   *
   * @param model the game model
   */
  public BluePlayerView(ReadOnlyGameModel model) {
    super(model);
    setTitle("Three Trios - Blue Player");
    getContentPane().setBackground(Color.CYAN);
  }

  /**
   * Refreshes the view to reflect the current game state.
   */
  @Override
  public void refresh() {
    updatePlayerPanel(COLOR.BLUE, getBluePlayerPanel());
    getGridPanel().repaint();
    revalidate();
    repaint();
  }

  /**
   * Overrides to only display updates related to the Blue Player.
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
