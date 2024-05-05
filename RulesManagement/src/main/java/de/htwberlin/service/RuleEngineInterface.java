package de.htwberlin.service;

import de.htwberlin.model.Card;
import de.htwberlin.model.Player;

public interface RuleEngineInterface {

    /**
     * Applies effects of special cards like skipping turns or reversing game direction.
     *
     * @param card the special card whose effects need to be applied
     */
    void applySpecialCardEffects(Card card);

    /**
     * Checks if the card played is valid based on the current top card of the discard pile.
     *
     * @param card the card being played
     * @param topCard the current top card on the discard pile
     * @return true if the play is valid, otherwise false
     */
    boolean checkValidCard(Card card, Card topCard);

    /**
     * Checks if the specified player has won the game.
     *
     * @param player to check for winning condition
     * @return true if the player has won, otherwise false
     */
    boolean checkWinner(Player player);

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
