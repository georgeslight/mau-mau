package de.htwberlin.impl.service;

import de.htwberlin.api.service.RuleEngineInterface;
import de.htwberlin.api.enums.Rank;
import de.htwberlin.api.enums.Suit;
import de.htwberlin.api.model.Card;
import de.htwberlin.api.model.Rules;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleService implements RuleEngineInterface {

    private Rules rules = new Rules();


    @Override
    public Integer getStartingCards() {
        return 5;
    }

    @Override
    public boolean isValidMove(Card card, Card topCard) {
        if (card.getRank().equals(Rank.JACK) & topCard.getRank().equals(Rank.JACK)) return false;
        if (card.getRank().equals(topCard.getRank())
            || card.getSuit().equals(topCard.getSuit())
            || card.getSuit().equals(rules.getWishCard()))
            return true;
        return false;
    }

    @Override
    public Integer calculateNextPlayerIndex(Integer currentPlayerIndex, Integer playerCount) {
        int counts = 1;
        if (rules.isSkipNextPlayerTurn()) counts++;
        if (rules.isCanPlayAgain()) counts = 0;
        if (!rules.isGameDirection())counts = counts * -1;

        return currentPlayerIndex + counts;
    }

    @Override
    public void applySpecialCardsEffect(Card card) {
        switch (card.getRank()) {
            case SEVEN:
                rules.setCardsTObeDrawn(rules.getCardsToBeDrawn() + 2);
                break;
            case EIGHT:
                rules.setSkipNextPlayerTurn(true);
                rules.setCardsTObeDrawn(0);
                break;
            case ACE:
                rules.setCanPlayAgain(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void applyJackSpecialEffect(Card card, Suit wishedSuit) {
        rules.setWishCard(wishedSuit);
    }


    @Override
    public Integer calculateScore(List<Card> cards) {
        return 0;
    }
    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }
}
