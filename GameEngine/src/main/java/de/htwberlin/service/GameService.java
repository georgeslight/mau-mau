package de.htwberlin.service;

import de.htwberlin.model.Card;
import de.htwberlin.model.GameState;
import de.htwberlin.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService implements GameManagerInterface {

    private PlayerService playerService;
    private CardService cardService;
    private RuleService ruleService;

    @Autowired
    public GameService(PlayerService playerService, CardService cardService, RuleService ruleService) {
        super();
        this.playerService = playerService;
        this.cardService = cardService;
        this.ruleService = ruleService;
    }
    public GameService() {
        super();
    }
    public PlayerService getPlayerService() {
        return playerService;
    }
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }
    public CardService getCardService() {
        return cardService;
    }
    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }
    public RuleService getRuleService() {
        return ruleService;
    }
    public void setRuleService(RuleService ruleService) {
        this.ruleService = ruleService;
    }


    @Override
    public GameState initializeGame(int numberOfPlayers) {
//        todo
        return null;
    }

    @Override
    public Player nextPlayer(GameState gameState) {
//        todo
        return null;
    }

    @Override
    public Player endGame(GameState game) {
//        todo
        return null;
    }

    @Override
    public Card drawCard(int player) {
//        todo
        return null;
    }

    @Override
    public void playCard(Player player, Card card, GameState gameState) {
//        todo
    }

    @Override
    public boolean checkWinner(Player player) {
//        todo
        return false;
    }
}
