package de.htwberlin.cardmanagement.api.service;

import de.htwberlin.cardmanagement.api.enums.Rank;
import de.htwberlin.cardmanagement.api.enums.Suit;
import de.htwberlin.cardmanagement.api.model.Card;

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
