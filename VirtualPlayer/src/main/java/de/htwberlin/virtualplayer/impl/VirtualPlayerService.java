package de.htwberlin.virtualplayer.impl;

import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.cardsmanagement.impl.CardComparator;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.rulesmanagement.impl.RuleService;
import de.htwberlin.virtualplayer.api.service.VirtualPlayerInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class VirtualPlayerService implements VirtualPlayerInterface {

    private static final Logger LOGGER = LogManager.getLogger(VirtualPlayerService.class);


    @Override
    public void makeMove() {

        LOGGER.info("Virtual player makes a move");
    }

    @Override
    public Card decideCardToPlay(Player player, Card topCard, RuleService ruleService) {
        return null;
    }

    @Override
    public void decideSuit(Player player, RuleService ruleService) {

    }

    @Override
    public void drawCard(Player player) {

    }

    @Override
    public void sayMau(Player player) {

    }
}

