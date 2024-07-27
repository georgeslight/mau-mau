package de.htwberlin.virtualplayer.api.service;

import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.rulesmanagement.impl.RuleService;

import java.util.List;

public interface VirtualPlayerInterface {


    void makeMove();

    /**
     * choose a card to play
     * @param player
     * @param topCard
     * @param ruleService
     * @return the card to play
     */
    Card decideCardToPlay(Player player, Card topCard, RuleService ruleService);

    /**
     * decide a suit to play
     * @param player
     * @param ruleService
     */
    void decideSuit(Player player, RuleService ruleService);

    /**
     * decide weither to draw a card from the deck or not
     * @param player
     */
    void drawCard(Player player);

    /**
     * decide weither to say "Mau" or not
     * @param player
     */
    void sayMau(Player player);

}
