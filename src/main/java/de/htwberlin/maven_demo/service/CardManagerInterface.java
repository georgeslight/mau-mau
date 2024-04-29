package de.htwberlin.maven_demo.service;

import de.htwberlin.maven_demo.model.Card;

public interface CardManagerInterface {

    /**
     * Checks if the card played is valid based on the current top card of the discard pile.
     *
     * @param card the card being played
     * @param topCard the current top card on the discard pile
     * @return true if the play is valid, otherwise false
     */
    boolean checkValidCard(Card card, Card topCard);

}
