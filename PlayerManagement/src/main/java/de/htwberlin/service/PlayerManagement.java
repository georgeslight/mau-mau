package de.htwberlin.service;

import de.htwberlin.model.Card;
import de.htwberlin.model.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerManagement implements PlayerManagerInterface{
    @Override
    public Player createPlayer() {
        List<Card> hand = new ArrayList<>();
        Player player = new Player(hand);
        player.setName("Player");
        return player;     }

    @Override
    public List<Card> sortPlayersCards(Player player) {
        return null;
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

}
