package de.htwberlin.service;



import de.htwberlin.model.Card;
import de.htwberlin.model.GameState;
import de.htwberlin.model.Player;

import java.util.List;

public interface PlayerManagerInterface {

    /**
     * Creates a player and adds them to the game.
     * @param gameState Game where the Player should be added to
     * @return new Player
     */
    Player createPlayer(GameState gameState);

    /**
     * Sort cards in the player's hand to optimize game experience.
     * @param player the player whose cards to sort
     * @return list of the sorted Cards -> Player's hand
     */
    List<Card> sortPlayersCards(Player player);

    /**
     * Allows a player to surrender from the game, potentially affecting game dynamics.
     * @param player the player who is surrendering
     */
    void surrender(Player player);

    /**
     * Called when a player has only one card left and declares "Mau".
     * @param player the player declaring Mau
     */
    void mau(Player player);

    /**
     * Handles the penalty for a player who fails to call "Mau" when they have one card left.
     * @param player the player who failed to declare Mau
     */
    void lostMau(Player player);

}
