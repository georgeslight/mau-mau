package de.htwberlin.impl.service;

import de.htwberlin.api.enums.Suit;
import de.htwberlin.api.service.CardManagerInterface;
import de.htwberlin.api.service.PlayerManagerInterface;
import de.htwberlin.api.model.Card;
import de.htwberlin.api.model.Player;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerService implements PlayerManagerInterface {

    private CardService cardService;

    public PlayerService() {
        this.cardService = new CardService();
    }

    @Override
    public Player createPlayer(String name, List<Card> hand) {
        return new Player(name, hand);
    }

    @Override
    public List<Card> sortPlayersCards(Player player) {
        List<Card> hand = player.getHand();
        //for (Suit suit : Suit.values())
            //if hand contains card with suit, then sort all cards with that suit
            //hand.sort(Comparator.comparing(Card::getRank));

            //if(hand.contains(Card::getSuit)) return hand;
          return hand;
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

