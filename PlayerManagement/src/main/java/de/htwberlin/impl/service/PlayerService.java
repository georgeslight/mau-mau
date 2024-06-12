package de.htwberlin.impl.service;

import de.htwberlin.api.service.PlayerManagerInterface;
import de.htwberlin.api.model.Card;
import de.htwberlin.api.model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerService implements PlayerManagerInterface {

    private static final Logger LOGGER = LogManager.getLogger(PlayerService.class);

    @Override
    public Player createPlayer(String name, List<Card> hand) {
        return new Player(name, hand);
    }

    @Override
    public List<Card> sortPlayersCards(Player player) {
        List<Card> hand = player.getHand();
        Collections.sort(hand, new CardComparator());
        for(Card card : hand){
            System.out.println(card.getSuit() + " " + card.getRank());
        }
        return hand;
    }

    @Override
    public void mau(Player player) {
        if (player.getHand().size() == 1) {
            player.setSaidMau(true);
        }
    }
}

