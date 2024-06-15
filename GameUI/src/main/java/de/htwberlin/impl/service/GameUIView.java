package de.htwberlin.impl.service;

import de.htwberlin.api.model.GameState;
import de.htwberlin.api.model.Player;
import de.htwberlin.api.service.CardManagerInterface;
import de.htwberlin.api.service.GameManagerInterface;
import de.htwberlin.api.service.PlayerManagerInterface;
import de.htwberlin.api.service.RuleEngineInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * IO, Logging
 */
@Service
public class GameUIView {

    Scanner scanner = new Scanner(System.in);

    private final CardManagerInterface cardService;
    private final PlayerManagerInterface playerService;
    private final RuleEngineInterface ruleService;
    private final GameManagerInterface gameService;

    @Autowired
    public GameUIView (GameManagerInterface gameManagerInterface, CardManagerInterface cardManagerInterface, PlayerManagerInterface playerManagerInterface, RuleEngineInterface ruleEngineInterface) {
        this.gameService = gameManagerInterface;
        this.cardService = cardManagerInterface;
        this.playerService = playerManagerInterface;
        this.ruleService = ruleEngineInterface;
    }

    private int numberOfPlayers() {
        System.out.println("Give the number of Players");
        int numberOfPlayers = scanner.nextInt();
        if (numberOfPlayers > 4) {
            System.out.println("Max number of Players 4");
            this.numberOfPlayers();
        }
        if (numberOfPlayers < 2) {
            System.out.println("Min number of Players 2");
            this.numberOfPlayers();
        }
        return numberOfPlayers;
    }

    private String getPlayerName() {
        System.out.println("What's your name?");
        return scanner.next();

    }

    public void print() {
        System.out.println("GameUI is running");
        int numberOfPlayers = this.numberOfPlayers();
        String playerName = this.getPlayerName();
        GameState gameState = gameService.initializeGame(playerName, numberOfPlayers);
        gameState.getPlayers().forEach(player -> System.out.println(player.getName()));
    }
}
