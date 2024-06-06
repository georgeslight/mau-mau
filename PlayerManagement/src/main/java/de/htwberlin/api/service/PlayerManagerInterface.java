package de.htwberlin.api.service;

import de.htwberlin.api.model.Card;
import de.htwberlin.api.model.Player;

import java.util.List;

public interface PlayerManagerInterface {

    /**
     * Creates a player and adds them to the game.
     * @return new Player
     */
    Player createPlayer(String name, List<Card> hand);

    /**
     * Sort cards in the player's hand to optimize game experience.
     * @param player the player whose cards to sort
     * @return list of the sorted Cards -> Player's hand
     */
    List<Card> sortPlayersCards(Player player);



    /**
     * Called when a player has only one card left and declares "Mau".
     * @param player the player declaring Mau
     */ // todo all maybe change name to sayOneCardLeft()
    void mau(Player player);
}
