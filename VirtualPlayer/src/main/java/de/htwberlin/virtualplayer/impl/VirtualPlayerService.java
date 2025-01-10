package de.htwberlin.virtualplayer.impl;

import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.rulesmanagement.api.model.Rules;
import de.htwberlin.rulesmanagement.api.service.RuleEngineInterface;
import de.htwberlin.virtualplayer.api.service.VirtualPlayerInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class VirtualPlayerService implements VirtualPlayerInterface {

    private static final Logger LOGGER = LogManager.getLogger(VirtualPlayerService.class);
    private final Random random = new Random();

    @Override
    public Card decideCardToPlay(Player player, Card topCard, RuleEngineInterface ruleService, Rules rules) {

        List<Card> hand = player.getHand();
        for (Card card : hand) {
            if (ruleService.isValidMove(card, topCard, rules)) {
                return card;
            }
        }
        return null;
    }

    @Override
    public Suit decideSuit(Player player, RuleEngineInterface ruleService) {
        Suit[] suits = Suit.values();
        return suits[random.nextInt(suits.length)];
    }

    @Override
    public boolean shouldDrawCard(Player player, Card topCard, RuleEngineInterface ruleService, Rules rules) {
        Card cardToPlay = decideCardToPlay(player, topCard, ruleService, rules);
        return cardToPlay == null;
    }

    @Override
    public boolean shouldSayMau(Player player) {
        return player.getHand().size() == 2;
    }
}
