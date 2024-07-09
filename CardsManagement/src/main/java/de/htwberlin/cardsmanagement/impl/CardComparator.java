package de.htwberlin.cardsmanagement.impl;

import de.htwberlin.cardsmanagement.api.model.Card;

import java.util.*;

public class CardComparator implements Comparator<Card> {

    @Override
    public int compare(Card c1, Card c2) {
        int suitComparison = c1.getSuit().compareTo(c2.getSuit());
        if (suitComparison != 0) {
            return suitComparison;
        } else {
            return c1.getRank().compareTo(c2.getRank());
        }
    }
}
