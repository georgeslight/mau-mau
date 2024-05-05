package de.htwberlin.service;

import de.htwberlin.model.Deck;
import de.htwberlin.model.Card;
import de.htwberlin.model.GameState;
import de.htwberlin.model.Player;

public interface CardManagerInterface {

    /**
     * Shuffles the deck and returns it.
     *
     * @param deck the deck to shuffle
     * @return the shuffled deck
     */
    Deck shuffle(Deck deck);

    /**
     * Allows a player to play a card from their hand onto the discard pile.
     *
     * @param player the player playing the card
     * @param card the card to be played
     */
    void playCard(Player player, Card card, GameState gameState);

    /**
     * Handles the action of a player drawing a card from the draw pile.
     *
     * @param player the index of the player who is drawing a card
     */
    void drawCard(int player);

    /**
     * When the draw pile is empty, shuffles the discard pile into it to create a new draw pile.
     */
    void shuffleDiscardPileIntoDrawPile();

    /**
     * Handles the discarding of a card to the discard pile.
     *
     * @param card the card to be discarded
     */
    void discardCard(Card card);
}
