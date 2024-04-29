package de.htwberlin.maven_demo.service;

import de.htwberlin.maven_demo.model.Card;
import de.htwberlin.maven_demo.model.Player;

public interface RuleEngineInterface {

    /**
     * Applies effects of special cards like skipping turns or reversing game direction.
     *
     * @param card the special card whose effects need to be applied
     */
    void applySpecialCardEffects(Card card);

    /**
     * Called when a player has only one card left and declares "Mau".
     */
    void mau(Player player);

    /**
     * Handles the penalty for a player who fails to call "Mau" when they have one card left.
     */
    void lostMau(Player player);

    /**
     * Handles the specific rules when a Jack is played.
     */
    void playJack();

    /**
     * Handles the specific rules when a 7 is played.
     */
    void play7();

    /**
     * Handles the specific rules when an 8 is played.
     */
    void play8();

    /**
     * Handles the specific rules when an Ace is played.
     */
    void playAce();
}
