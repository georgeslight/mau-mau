package de.htwberlin.service;

import de.htwberlin.enums.Rank;
import de.htwberlin.enums.Suit;
import de.htwberlin.model.Deck;
import de.htwberlin.model.Card;

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
    Deck createDeck();
}
