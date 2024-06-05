package de.htwberlin.impl.service;

import de.htwberlin.api.service.PlayerManagerInterface;
import de.htwberlin.api.model.Card;
import de.htwberlin.api.model.Player;

import java.util.*;

public class PlayerService implements PlayerManagerInterface {

    private CardService cardService;

    public PlayerService() {
        this.cardService = new CardService();
    }

    @Override
    public Player createPlayer(String name, List<Card> hand) {
        return null;
    }

    @Override
    public List<Card> sortPlayersCards(Player player) {
        return null;
    }

    @Override
    public void endRound(Player player) {

    }

    @Override
    public void mau(Player player) {

    }

    @Override
    public void lostMau(Player player) {

    }
    public CardService getCardService() {
        return cardService;
    }

    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }
}

