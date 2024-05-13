package de.htwberlin.service;

import de.htwberlin.enums.Suit;
import de.htwberlin.model.Card;

public interface RuleEngineInterface {

    /**
     * Checks if the card played is valid based on the current top card of the discard pile.
     *
     * @param card the card being played
     * @param topCard the current top card on the discard pile
     * @return true if the play is valid, otherwise false
     */
    boolean checkValidCard(Card card, Card topCard);

    /**
     * Handles the specific rules when a Jack is played.
     * # Ein weiterer Bube darf nicht auf einem Buben abgelegt werden
     */
    void playJack(Suit wishedSuit);

    /**
     * Handles the specific rules when a 7 is played.
     */
    void play7();

    /**
     * Handles the specific rules when an 8 is played.
     */
    void play8();

    /**
     * Handles the specific rules when an Ace is played.
     */
    void playAce();

}
