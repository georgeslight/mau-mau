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

    @Override
    public int calculateTotalScore(Player player) {
        return 0;
    }
}

