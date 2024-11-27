# Three Trios Game

## Overview
Three Trios Game is a two-player strategic card game played on a customizable board/grid. Players take turns placing cards on the grid, attempting to flip the opponent's cards by leveraging attack values. The player with the most cards at the end of the game wins.

## Quick Start

### Prerequisites
- **Java Development Kit (JDK)**: Ensure JDK 11+ is installed
- **Java Swing**: Required for the GUI

### Project Structure
The project is organized into packages and classes to support 
the model-view-controller (MVC)  for the Three Trios Game.

- `src/cs3500.threetrios`
   - `ai`: Contains classes related to AI strategies for solo play mode.
      - `Flip`: Represents a specific AI strategy focused on maximizing card flips.
      - `GoForCorner`: AI strategy that prioritizes moves targeting corner positions.
      - `HybridStrategy`: Combines multiple strategies to determine the optimal move.
      - `LeastFlippableStrategy`: AI strategy that chooses cards and positions that are least likely to be flipped by the opponent.
      - `MinMaxStrategy`: Implements a Minimax-based decision-making strategy to minimize the maximum flip potential for the opponent.
      - `NoPlay`: Represents a strategy where the AI chooses not to play, or a placeholder for no valid moves.
      - `PosnStrategy`: Interface or abstract class for position-based strategy logic.
      - `StrategyController`: Controls and coordinates the use of different AI strategies.

   - `card`: Manages card-related components.
      - `Card`: Interface representing a general card with attributes and behaviors.
      - `CardModel`: Implements the Card interface, handling specific card properties like attack values.
      - `COLOR`: Enum representing possible colors for the cards.
      - `NUMBER`: Enum representing possible number values on cards.

   - `game`: Contains core game mechanics and grid management.
      - `Cell`: Represents individual cells on the game grid (either playable or non-playable).
      - `Game`: Core game logic, coordinating gameplay flow and state.
      - `GameGrid`: Manages the layout and structure of the game grid, including cells.
      - `GameModel`: Main interface defining the game model’s behaviors and data access.
      - `Grid`: Additional grid-related functionality (potentially a helper or superclass for GameGrid).
      - `ReadOnlyGameModel`: Provides a read-only view of the game model, ensuring the view cannot modify the game state.

   - `GUI`: Implements the graphical user interface for the game.
      - `Features`: Defines various interactive features for the GUI.
      - `GridPanel`: Handles the display and layout of the grid within the GUI.
      - `Main`: Entry point of the program, initializing the model, view, and controller.
      - `ThreeTriosControllerImpl`: Implementation of the game controller, managing interactions between the model and view.
      - `ThreeTriosGameController`: Interface for the game controller, specifying controller behavior.
      - `ThreeTriosGameView`: Interface defining the capabilities of the game’s view component.
      - `ThreeTriosViewImpl`: Implementation of the view interface, managing the visual aspects of the game.

   - `parser`: Handles configuration file parsing.
      - `BoardConfigParser`: Reads and processes configuration files to set up the game grid and card details.

   - `player`: Manages player-specific functionality.
      - `Player`: Interface for general player behaviors.
      - `PlayerModel`: Implementation of the Player interface, managing player attributes such as hands and score.

   - `view`: Contains classes for alternative or auxiliary views of the game.
      - `TextView`: Provides a text-based representation of the game state, useful for debugging or non-GUI implementations.

### Features
- **Visual View**: A GUI built with Java Swing, 
enabling players to interact with the game grid and cards through mouse clicks
- **Game Model**: Divided into mutable and read-only interfaces to maintain
the integrity of the MVC, preventing the view from modifying the game state
- **Strategies**: Initial AI strategies for single-player mode:
    - **Maximize Flip**: Selects moves to flip the most opponent cards.
    - **Corner Preference**: Prioritizes corner positions on the grid,
  where cards are less likely to be flipped.

## Setup
1. **Download Starter Files**  
   Download the starter code from `code.zip`
and extract it into your project directory.

2. **Clone the Repository**  
   Clone the repository to your local machine

3. **Compile the Project**  
   Compile using Maven

## Running the Game
1. **Launch the Game**  
   Run the main class to start the game:
   This will open the game window with the initial grid and player hands

2. **Game Controls**
    - **Select a Card**: Click on a card in the player’s hand to highlight it
    - **Place a Card**: Click on a grid cell to place the selected card
    - **Deselect a Card**: Click the same card again or choose a different card
    - **Console Logging**: The game view logs coordinates and player 
   actions to the console for debugging.

3. **Choose Game Mode**
    - **Two-Player Mode**
    - **AI Mode**: Play against a simple AI

4. **Gameplay Instructions**
    - Place cards strategically on the grid
    - Flip opponent cards by utilizing attack values
    - The player with the most cards on the grid at the end wins

## Components
- **GameGrid**: Manages the grid and card placement.
- **Cell**: Represents individual grid cells (playable or non-playable)
- **CardModel**: Contains card attack values and determines winning conditions in card battles
- **Player**: Manages player attributes, including each player’s hand of cards
- **BoardConfigParser**: Parses configuration files to set up the game grid and card list
- **TextView**: Provides a user-friendly, text-based representation of the grid

### Configuration Files
Configuration files in `docs/` contain both card and grid settings.
They may be updated to support other game setups

**Example Invariant for Grading**:
```java
// INVARIANT: Each player’s hand is filled with exactly (N+1)/2 cards
// where N is the number of card cells on the grid.
int numCardsEachPlayer = (numCardCells + 1) / 2;
```
# Extra Credit: Advanced Strategies and Hybrid Strategy
## Additional Strategies
### Located under the `ai/` folder
1. `LeastFlippableStrategy`: class
Minimizes the risk of being flipped by placing cards in low-risk positions.
2. `GoForCornerStrategy`: class
Prioritizes corner placements to secure more permanent control on the grid.
3. `HybridStrategy`: class
The HybridStrategy combines multiple strategies to make decisions based on prioritized criteria. By chaining strategies, HybridStrategy can balance aggressive and defensive moves. 
For example, it can use FlipMaxStrategy to maximize flips and fall back to GoForCornerStrategy for defensive placement if there’s a tie.

## Tests
### Located in the `test/` folder
- **`AIStrategiesTest`**: Validates each strategy independently to ensure correct behavior under various board configurations.
  - **`HybridStrategyTest`**: Confirms that HybridStrategy successfully combines FlipMaxStrategy and GoForCornerStrategy, correctly resolving ties with the upper-leftmost rule.
- **`MockModelTests`**: Used mocked methods to simulate the game grid and test strategies in isolation, verifying coordinates and strategy moves.


## Changes for Part 2
- Split the `GameModel` interface into `ReadOnlyGameModel` for view-only access and a mutable `GameModel` for the controller to interact with.
- Added `isMoveLegal`, `evaluateFlipPotential`, and `getCurrentPlayerScore` methods to support gameplay logic in the model.

## Next Steps
- **GUI and Controller**: Currently in progress.
- **Features Class**: Implement a features class to enhance game functionality
- **ReadOnlyModel for GUI**: Complete the read-only model to support the GUI view
- **Strategy Design Pattern**: Implement strategies as discussed in lectures,
using the strategy design pattern
- **Capture screenshots**, update documentation, and prepare for submission.


# Photos
**WARNING** THE IMAGES MAY NOT SHOW UP DUE TO THE FILE BEING TOO LARGE WITH THEM.
### Start of the game
![alt text](../img/StartOfGame.png)

### Card Selected Red Player
![alt text](../img/CardSelectedRedPlayer.png)

### Card Selected Blue Player
![alt text](../img/CardSelectedBluePlayer.png)

### Cards Played Both Players Hand Decreased
![alt text](../img/CardsPlayedBothPlayersHandDecreased.png)


## Changes for Part 3

#### IMAGES ARE IN THE COMPRESSED FOLDER UNDER docs/img.zip
#### .JAR FILE WASENT ABLE TO INCLUDE IT BECAUSE OF MEMORY

Independent Controllers: Each player (human or AI) has a dedicated controller,
managing actions and turn-based restrictions.

AI Strategies: AI players support multiple strategies 
(Flip, GoForCorner, Hybrid), configurable via command-line arguments. 

Event Handling: Controllers handle view actions (card selection, cell clicks)
and model notifications (turn changes, game-end signals).

Error Notifications: Invalid actions trigger pop-ups, 
and turn-specific notifications improve gameplay clarity.

Testing and Integration: Added unit and integration tests for
model, view, and controller interactions, ensuring robust functionality.

Game Initialization: startGame ensures all components are
set up, and the first player is notified in the title of the panel.

Views: There are two views one for the RED(human) player and one for the BLUE(ai or human) player to enable
multi-player game style.

Controller: Mediates interactions between the model and the view one controller for AI the other for human

Turn-Based Management:
   - Players are restricted to act only during their turn.
   - Notifications in title are used to inform players about whose turn it is.
   - Invalid actions during the opponent's turn are handled with error messages.

Player-Action and Model-Status Interfaces
Features Interface: Used by views to communicate user interactions like selecting cards or placing them on the grid.
Model Listener Interface: Used by the model to notify controllers of game updates, 
such as turn changes or game-over conditions. 
Enable asynchronous communication between the MVC components,
to ensure smooth gameplay.

Configuring the Game via Command-Line
The program accepts a minimum of two to four command-line arguments:

Player 1 Type: "human" or "ai"
Player 2 Type: "human" or "ai"
Player 1 AI Strategy (Optional): "strategy1", "strategy2", or "hybrid"
Player 2 AI Strategy (Optional): "strategy1", "strategy2", or "hybrid"

More tests for GameModel:
isMoveLegal(row, col), 
getCurrentPlayerModel(), 
getWinner(), 
getGridSize(),
getCardAt(row, col)


## Submission Includes
Source Code: All files for the model, view, controller, and AI strategies.
README File: Explains the project structure, gameplay, and implementation details.
Screenshots: Demonstrates game functionality, including:
- Game start.
- Player actions.
- Game end with the winner displayed.
Tests: Comprehensive test suite covering all components.
JAR File: A runnable JAR file for easy game execution.