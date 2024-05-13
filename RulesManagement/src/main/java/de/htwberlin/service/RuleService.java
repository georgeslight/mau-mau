package de.htwberlin.service;

import de.htwberlin.enums.Suit;
import de.htwberlin.model.Card;

public class RuleService implements RuleEngineInterface {


    @Override
    public boolean checkValidCard(Card card, Card topCard) {
//        todo
        return false;
    }

    @Override
    public Integer calculateNextPlayerIndex(Integer currentPlayerIndex, Integer playerCount) {
        return null;
    }

    @Override
    public void playJack(Suit wishedSuit) {
//        todo
    }

    @Override
    public void play7() {
//        todo
    }

    @Override
    public void play8() {
//        todo
    }

    @Override
    public void playAce() {
//        todo
    }
}
