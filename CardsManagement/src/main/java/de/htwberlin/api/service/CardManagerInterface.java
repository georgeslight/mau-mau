package de.htwberlin.api.service;

import de.htwberlin.api.enums.Rank;
import de.htwberlin.api.enums.Suit;
import de.htwberlin.api.model.Card;

import java.util.Stack;

public interface CardManagerInterface {

    /**
     * Shuffles the deck and returns it.
     *
     * @param deck the stack of cards to shuffle
     * @return the shuffled deck
     */
    Stack<Card> shuffle(Stack<Card> deck);

    /**
     *
     * @return Card
     */
    Card createCard(Suit suit, Rank rank);

    /**
     *
     * @return Deck
     */
    Stack<Card> createDeck();
}
