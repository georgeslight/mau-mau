package de.htwberlin.playermanagement.api.service;

import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.playermanagement.api.model.Player;

import java.util.List;

/**
 * Die PlayerManagerInterface-Schnittstelle verwaltet die Spieler innerhalb des Spiels.
 * Sie ermöglicht das Erstellen von Spielern, das Sortieren der Karten in deren Hand
 * und das Handhaben der "Mau"-Aktion, wenn ein Spieler nur noch eine Karte hat.
 */
public interface PlayerManagerInterface {

    /**
     * Erstellt einen neuen Spieler mit dem angegebenen Namen und einer Hand von Karten.
     *
     * @param name der Name des Spielers
     * @param hand die Handkarten des Spielers
     * @return der erstellte Spieler
     */
    Player createPlayer(String name, List<Card> hand, boolean isVirtual);

    /**
     * Sortiert die Karten in der Hand des Spielers, um das Spielerlebnis zu optimieren.
     *
     * @param player der Spieler, dessen Karten sortiert werden sollen
     */
    void sortPlayersCards(Player player);

    /**
     * Wird aufgerufen, wenn ein Spieler nur noch eine Karte hat und "Mau" ruft.
     *
     * @param player der Spieler, der "Mau" ruft
     */
    void mau(Player player);
}
