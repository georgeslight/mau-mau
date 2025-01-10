package de.htwberlin.playermanagement.impl;

import de.htwberlin.cardsmanagement.impl.CardComparator;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.playermanagement.api.exception.EmptyHandException;
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
    public Player createPlayer(String name, List<Card> hand, boolean isVirtual) {
        return new Player(name, hand, isVirtual);
    }

    @Override
    public void sortPlayersCards(Player player) {
        if (player.getHand() == null || player.getHand().isEmpty()) {
            throw new EmptyHandException("Player " + player.getName() + " has no cards in their hand.");
        }
        player.getHand().sort(new CardComparator());
    }

    @Override
    public void mau(Player player) {
        if (player.getHand().size() == 2) {
            player.setSaidMau(true);
        }
    }
}

