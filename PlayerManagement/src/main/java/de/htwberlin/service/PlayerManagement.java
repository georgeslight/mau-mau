package de.htwberlin.service;

import de.htwberlin.model.Card;
import de.htwberlin.model.Player;

import java.util.*;

public class PlayerManagement implements PlayerManagerInterface {
    @Override
    public Player createPlayer() {
        List<Card> hand = new ArrayList<>();
        Player player = new Player(hand);
        player.setName("Player");
        return player;
    }

    @Override
    public List<Card> sortPlayersCards(Player player) {
        List<Card> hand = new ArrayList<>(player.getHand());
        hand.sort(Comparator.comparing(Card::getRank)); // Sortieren nach Rang
        return new ArrayList<>(hand); // Neue Liste erstellen und sortierte Karten hinzufügen
    }

    @Override
    public void surrender(Player player) {

    }

    @Override
    public void mau(Player player) {

    }

    @Override
    public void lostMau(Player player) {

    }

    /**
     * Berechnet die Gesamtpunktzahl eines Spielers.
     *
     * @param player der Spieler, dessen Gesamtpunktzahl berechnet werden soll
     * @return die Gesamtpunktzahl des Spielers
     */

    public int calculateTotalScore(Player player) {
        return Arrays.stream(player.getScore()).sum();
    }



    /**
     * Fügt eine Karte zur Hand eines Spielers hinzu.
     *
     * @param player der Spieler, dem die Karte hinzugefügt werden soll
     * @param newCard  die Karte, die hinzugefügt werden soll
     */
    public void addCardToHand(Player player, Card newCard) {
        player.getHand().add(newCard);
    }


    /**
     * Entfernt eine Karte aus der Hand eines Spielers.
     *
     * @param player der Spieler, aus dessen Hand die Karte entfernt werden soll
     * @param cardToRemove  die Karte, die entfernt werden soll
     */
    public void removeCardFromHand(Player player, Card cardToRemove) {
        player.getHand().remove(cardToRemove);
    }
}

