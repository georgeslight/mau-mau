package de.htwberlin.virtualplayer.api.service;

import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.rulesmanagement.api.model.Rules;
import de.htwberlin.rulesmanagement.api.service.RuleEngineInterface;
import de.htwberlin.rulesmanagement.impl.RuleService;

public interface VirtualPlayerInterface {

    Card decideCardToPlay(Player player, Card topCard, RuleEngineInterface ruleService, Rules rules);

    /**
     * Decide a suit to play.
     * @param player
     * @param ruleService
     */
    Suit decideSuit(Player player, RuleEngineInterface ruleService);

    /**
     * Decide whether to draw a card from the deck or not.
     * @param player
     */
    boolean shouldDrawCard(Player player, Card topCard, RuleEngineInterface ruleService, Rules rules);

    /**
     * Decide whether to say "Mau" or not.
     * @param player
     */
    boolean shouldSayMau(Player player);
}
