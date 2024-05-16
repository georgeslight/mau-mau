package de.htwberlin.service;

import de.htwberlin.enums.Suit;
import de.htwberlin.model.Card;

import java.util.List;

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
     * calculates the index of the next player based on the current player index and the total number of players.
     *
     * @return the index of the next player
     */
    Integer calculateNextPlayerIndex(Integer currentPlayerIndex, Integer playerCount);
    /**
     * @return the number of cards each player should start with
     */
    Integer getStartingCards();
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

    /**
     * Calculates the score of cards given.
     * @param cards
     * @return sum of the scores of the cards
     */
    Integer calculateScore(List<Card> cards);

}
