package de.htwberlin.api.service;

import de.htwberlin.api.model.Card;
import de.htwberlin.api.model.GameState;
import de.htwberlin.api.model.Player;

public interface GameManagerInterface {

    /**
     * Initializes the game with the specified number of players.
     * Sets up the deck, shuffles it, and distributes cards to each player.
     *
     * @param numberOfPlayers the number of players in the game
     * @return GameState the state of the game after initialization
     */
    GameState initializeGame(int numberOfPlayers);

    /**
     * Moves the game control to the next player in the sequence.
     */
    Player nextPlayer(GameState gameState);

    /**
     * Calculates the winner and displays the result
     *
     * @param game the current game state to end
     * @return the Winner
     */
    Player endGame(GameState game);

    /**
     * Handles the action of a player drawing a card from the draw pile.
     *
     * @param player the index of the player who is drawing a card
     */
    Card drawCard(GameState gameState, Player player);

    /**
     * Allows a player to play a card from their hand onto the discard pile.
     *
     * @param player the player playing the card
     * @param card the card to be played
     */
    void playCard(Player player, Card card, GameState gameState);

    /**
     * Checks if the specified player has won the game.
     *
     * @param player to check for winning condition
     * @return true if the player has won, otherwise false
     */
    boolean checkWinner(Player player);
}
