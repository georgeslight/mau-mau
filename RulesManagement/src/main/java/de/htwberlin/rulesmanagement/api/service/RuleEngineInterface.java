package de.htwberlin.rulesmanagement.api.service;

import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.rulesmanagement.api.model.Rules;

import java.util.List;

/**
 * Die RuleEngineInterface-Schnittstelle definiert die Regeln und Logik des Spiels.
 * Sie überprüft die Gültigkeit von Spielzügen, berechnet den nächsten Spieler,
 * wendet spezielle Karteneffekte an und berechnet die Punktzahlen der Karten.
 */
public interface RuleEngineInterface {

    /**
     * Überprüft, ob der gespielte Zug gültig ist, basierend auf der aktuellen obersten Karte des Ablagestapels und den Spielregeln.
     *
     * @param card die gespielte Karte
     * @param topCard die aktuelle oberste Karte auf dem Ablagestapel
     * @param rules die Spielregeln
     * @return true, wenn der Zug gültig ist, andernfalls false
     */
    boolean isValidMove(Card card, Card topCard, Rules rules);

    /**
     * Berechnet den Index des nächsten Spielers basierend auf dem aktuellen Spielerindex und der Gesamtzahl der Spieler.
     *
     * @param currentPlayerIndex der Index des aktuellen Spielers
     * @param playerCount die Gesamtzahl der Spieler
     * @param rules die Spielregeln
     * @return der Index des nächsten Spielers
     */
    Integer calculateNextPlayerIndex(Integer currentPlayerIndex, Integer playerCount, Rules rules);

    /**
     * Handhabt die spezifischen Regeln, wenn eine Karte mit speziellen Effekten gespielt wird.
     *
     * @param card die gespielte Karte
     * @param rules die Spielregeln
     * Jack: ändert die gewünschte Farbe
     * Seven: der nächste Spieler zieht 2 Karten (cardsToBeDrawn + 2)
     * Eight: überspringt den Zug des nächsten Spielers und setzt cardsToBeDrawn auf 0 zurück
     * Ace: der Spieler kann eine weitere Karte spielen
     */
    void applySpecialCardsEffect(Card card, Rules rules);

    /**
     * Wendet den speziellen Effekt der Bube-Karte an, indem die gewünschte Farbe gesetzt wird.
     *
     * @param card die gespielte Karte
     * @param wishedSuit die gewünschte Farbe
     * @param rules die Spielregeln
     */
    void applyJackSpecialEffect(Card card, Suit wishedSuit, Rules rules);

    /**
     * Berechnet die Punktzahl der gegebenen Karten.
     *
     * @param cards die Karten, deren Punktzahl berechnet werden soll
     * @return die Summe der Punktzahlen der Karten
     */
    Integer calculateScore(List<Card> cards);
}
