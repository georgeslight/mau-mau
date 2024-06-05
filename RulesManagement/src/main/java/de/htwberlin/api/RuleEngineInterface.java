package de.htwberlin.api;

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
    boolean isValidMove(Card card, Card topCard);
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
     */

    /**
     * Handles the specific rules when an 8 is played.
     */

    /**
     * Handles the specific rules when a Card with special effects is played.
     * @param card the card being played
     * jack: change the wished suit
     * seven: next player draws 2 cards (Cardstobedrawn + 2)
     * eight: skip next player's turn & reset cardsToBeDrawn to 0
     * ace: player can play another card
     */

    void applySpecialCardsEffect(Card card);

    /**
     * applies jack special effect
     * @param card the card being played
     * @param wishedSuit the suit wished by the player
     */
    void applyJackSpecialEffect(Card card, Suit wishedSuit);

    /**
     * Calculates the score of cards given.
     * @param cards
     * @return sum of the scores of the cards
     */
    Integer calculateScore(List<Card> cards);

}
