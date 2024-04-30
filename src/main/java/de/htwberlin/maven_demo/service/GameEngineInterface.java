package de.htwberlin.maven_demo.service;


import de.htwberlin.maven_demo.model.GameState;
import de.htwberlin.maven_demo.model.Player;

public interface GameEngineInterface {

    /**
     * Initializes the game with the specified number of players.
     * Sets up the deck, shuffles it, and distributes cards to each player.
     *
     * @param numberOfPlayers the number of players in the game
     * @return GameState the state of the game after initialization
     */
    GameState initializeGame(int numberOfPlayers);

    /**
     * Starts the turn for the specified player.
     *
     * @param player the index of the player whose turn is to start
     */
    void startTurn(Player player);

    /**
     * Moves the game control to the next player in the sequence.
     */
    void nextPlayer();

    /**
     * Ends the game and performs any cleanup necessary.
     *
     * @param game the current game state to end
     */
    void endGame(GameState game);
}
