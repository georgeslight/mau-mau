package de.htwberlin.playermanagement.impl;

import de.htwberlin.cardmanagement.impl.CardComparator;
import de.htwberlin.cardmanagement.api.model.Card;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.playermanagement.api.service.PlayerManagerInterface;
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
    public void sortPlayersCards(Player player) {
        player.getHand().sort(new CardComparator());
    }

    /**
     * Comparator.comparing(Card::getSuit): This sorts the cards based on their suit. By default, the enum order is used, which is CLUBS, DIAMONDS, HEARTS, SPADES as defined in the Suit enum.
     * .thenComparing(Card::getRank): This sorts the cards based on their rank within the suit groups.
     * @param player the player whose cards to sort
     * @return sortedHand
     */
//    @Override
//    public List<Card> sortPlayersCards(Player player) {
//        List<Card> hand = player.getHand();
//        hand.sort(Comparator
//                .comparing(Card::getSuit, Comparator.comparingInt(this::suitOrder))
//                .thenComparing(Card::getRank));
//        player.setHand(hand);
//        return hand;
//    }

//    private int suitOrder(Suit suit) {
//        switch (suit) {
//            case CLUBS:
//                return 0;
//            case DIAMONDS:
//                return 1;
//            case HEARTS:
//                return 2;
//            case SPADES:
//                return 3;
//            default:
//                throw new IllegalArgumentException("Unknown suit: " + suit);
//        }
//    }

    @Override
    public void mau(Player player) {
        if (player.getHand().size() == 2) {
            player.setSaidMau(true);
        }
    }
}

