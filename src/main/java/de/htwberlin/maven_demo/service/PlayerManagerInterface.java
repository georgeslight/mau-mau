package de.htwberlin.maven_demo.service;

import de.htwberlin.maven_demo.model.Card;
import de.htwberlin.maven_demo.model.Player;

public interface PlayerManagerInterface {


    /**
     * Allows a player to play a card from their hand onto the discard pile.
     *
     * @param card the card to be played
     */
    void playCard(Player player, Card card);

    /**
     * Handles the action of a player drawing a card from the draw pile.
     *
     * @param player the index of the player who is drawing a card
     */
    void drawCard(int player);


    /**
     *
     * Handles the discarding of a card to the discard pile.
     *
     * @param card the card to be discarded
     */
    void discardCard(Card card);

    /**
     * Allows a player to surrender from the game, potentially affecting game dynamics.
     */
    void surrender(Player player);


}
