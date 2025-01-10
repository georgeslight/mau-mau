package de.htwberlin.gameengine.api.service;

import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.gameengine.api.model.GameState;
import de.htwberlin.gameengine.exception.EmptyPileException;
import de.htwberlin.playermanagement.api.model.Player;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Schnittstelle zur Verwaltung des Spielzustands und des Spielablaufs.
 */
public interface GameManagerInterface {

    /**
     * Initialisiert das Spiel mit der angegebenen Anzahl von Spielern.
     * Bereitet das Kartendeck vor, mischt es und verteilt Karten an jeden Spieler.
     *
     * @param playerName der Name des Hauptspielers
     * @param numberOfPlayers die Anzahl der Spieler im Spiel
     * @return der Spielzustand nach der Initialisierung
     */
    GameState initializeGame(String playerName, int numberOfPlayers);

    /**
     * Wechselt die Spielkontrolle zum nächsten Spieler in der Reihenfolge.
     *
     * @param gameState der aktuelle Spielzustand
     * @return der nächste Spieler
     */
    Player nextPlayer(GameState gameState);

    /**
     * Berechnet die Ranglistenpunkte für jeden Spieler.
     *
     * @param game der aktuelle Spielzustand
     */
    void calcRankingPoints(GameState game);

    /**
     * Beendet die aktuelle Runde und startet eine neue.
     *
     * @param game der aktuelle Spielzustand
     */
    void endRound(GameState game);

    /**
     * Handelt das Ziehen einer Karte durch einen Spieler vom Zugstapel.
     *
     * @param gameState der aktuelle Spielzustand
     * @param player der Spieler, der eine Karte zieht
     * @return die gezogene Karte
     */
    Card drawCard(GameState gameState, Player player);

    /**
     * Erlaubt einem Spieler, eine Karte aus seiner Hand auf den Ablagestapel zu legen.
     *
     * @param player der Spieler, der die Karte spielt
     * @param card die zu spielende Karte
     * @param gameState der aktuelle Spielzustand
     */
    void playCard(Player player, Card card, GameState gameState);

    /**
     * Überprüft, ob der angegebene Spieler das Spiel gewonnen hat.
     * Handhabt die Strafe für einen Spieler, der es versäumt, "Mau" zu rufen, wenn er nur noch eine Karte hat.
     *
     * @param player der zu überprüfende Spieler
     * @return true, wenn der Spieler gewonnen hat, andernfalls false
     */
    boolean checkEmptyHand(Player player);

    /**
     * Gibt den Spieler mit den höchsten Ranglistenpunkten zurück.
     *
     * @param gameState der aktuelle Spielzustand
     * @return der Spieler mit den höchsten Punkten
     */
    Player getWinner(GameState gameState);


    /**
     * Mischt das Deck neu, indem Karten vom Ablagestapel zurück ins Deck gelegt werden.
     * Der Stapel wird gemischt, um die Kartenreihenfolge zu randomisieren.
     *
     * @param game der aktuelle Spielzustand
     */
    void reshuffleDeck(GameState game);


    @Transactional
    List<Player> getSortedPlayersList(GameState gameState);
}
