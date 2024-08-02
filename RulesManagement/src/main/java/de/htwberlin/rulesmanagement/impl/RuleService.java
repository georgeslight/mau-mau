package de.htwberlin.rulesmanagement.impl;

import de.htwberlin.rulesmanagement.api.service.RuleEngineInterface;
import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.rulesmanagement.api.model.Rules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleService implements RuleEngineInterface {

    private static final Logger LOGGER = LogManager.getLogger(RuleService.class);

    @Override
    public boolean isValidMove(Card card, Card topCard, Rules rules) {
        // normal rules
        boolean isValid = card.getSuit().equals(topCard.getSuit()) || card.getRank().equals(topCard.getRank());
        // 7 was played
        if (topCard.getRank().equals(Rank.SEVEN) && rules.getCardsToBeDrawn() > 0) {
            if (card.getRank().equals(Rank.SEVEN)) return true;
            else if (card.getRank().equals(Rank.EIGHT)) return true;
            else {
            LOGGER.info("Either have to draw, or play another 7");
            return false;
            }
        }

        // If 9 was played, Nine can be played on any card
        if (card.getRank().equals(Rank.NINE)) return true;

        // If Jack was played
        if (topCard.getRank().equals(Rank.JACK) && rules.getWishCard() != null) {
            if (card.getSuit().equals(rules.getWishCard())) {
                rules.setWishCard(null);
                return true;
            } else {
                LOGGER.info("Must play a card of the wished suit: {}", rules.getWishCard());
                return false;
            }
        }
        // Jack rules
        // J can be played on any card except another Jack
        if (card.getRank().equals(Rank.JACK)) {
            if (topCard.getRank().equals(Rank.JACK) && rules.getWishCard() != null) {
                LOGGER.warn("Cannot play Jack on Jack");
                return false;
            }
            return true;
        }
        return isValid;
    }

    @Override
    public Integer calculateNextPlayerIndex(Integer currentPlayerIndex, Integer playerCount, Rules rules) {
        int counts = 1;
        // play 8 (skips next player)
        if (rules.isSkipNextPlayerTurn()) {
            counts++;
            LOGGER.info("Skipped next player!");
            // reset state
            rules.setSkipNextPlayerTurn(false);
        }

        // play A (can play again)
        if (rules.isCanPlayAgain()) {
            counts = 0;
            // reset state
            rules.setCanPlayAgain(false);
        }

        int nextPlayerIndex = (currentPlayerIndex + counts) % playerCount;

        if (nextPlayerIndex < 0) {
            nextPlayerIndex += playerCount;
        }
        return nextPlayerIndex;
    }

    @Override
    public void applySpecialCardsEffect(Card card,Rules rules) {
        switch (card.getRank()) {
            case SEVEN:
                rules.setCardsToBeDrawn(rules.getCardsToBeDrawn() + 2);
                break;
            case EIGHT:
                rules.setSkipNextPlayerTurn(true);
                rules.setCardsToBeDrawn(0);
                break;
            case ACE:
                rules.setCanPlayAgain(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void applyJackSpecialEffect(Card card, Suit wishedSuit, Rules rules) {
        rules.setWishCard(wishedSuit);
    }


    @Override
    public Integer calculateScore(List<Card> cards) {
        int score = 0;
        for (Card card : cards) {
            switch (card.getRank()) {
                case SEVEN:
                    score -= 7;
                    break;
                case EIGHT:
                    score -= 8;
                    break;
                case NINE:
                    score -= 9;
                    break;
                case TEN:
                    score -= 10;
                    break;
                case JACK:
                    score -= 2;
                    break;
                case QUEEN:
                    score -= 3;
                    break;
                case KING:
                    score -= 4;
                    break;
                case ACE:
                    score -= 11;
                    break;
            }
        }
        return score;
    }
}
