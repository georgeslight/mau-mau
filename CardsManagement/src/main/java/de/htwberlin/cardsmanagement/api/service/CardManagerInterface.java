package de.htwberlin.cardsmanagement.api.service;

import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;

import java.util.List;

/**
 * Schnittstelle zur Verwaltung von Karten.
 */
public interface CardManagerInterface {

    /**
     * Mischt das Kartendeck und gibt es zurück.
     *
     * @param deck die Liste der zu mischenden Karten
     * @return das gemischte Deck
     */
    List<Card> shuffle(List<Card> deck);


    /**
     * Erstellt eine neue Karte mit der angegebenen Farbe (Suit) und dem Rang (Rank).
     *
     * @param suit die Farbe der Karte
     * @param rank der Rang der Karte
     * @return die erstellte Karte
     */
    Card createCard(Suit suit, Rank rank);


    /**
     * Erstellt ein vollständiges Kartendeck.
     *
     * @return das erstellte Deck
     */
    List<Card> createDeck();
}
