package de.htwberlin.impl.service;

import de.htwberlin.api.enums.Suit;
import de.htwberlin.api.model.GameState;
import de.htwberlin.api.model.Player;
import de.htwberlin.api.model.Card;
import de.htwberlin.api.service.CardManagerInterface;
import de.htwberlin.api.service.GameManagerInterface;
import de.htwberlin.api.service.PlayerManagerInterface;
import de.htwberlin.api.service.RuleEngineInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * IO, Logging
 */
@Service
public class GameUIView {

    private static final Logger LOGGER = LogManager.getLogger(GameUIView.class);

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
        System.out.println("Enter the number of players (2-4): ");
        int numberOfPlayers = scanner.nextInt();
        while (numberOfPlayers < 2 || numberOfPlayers > 4) {
            System.out.println("Invalid number. Please enter a number between 2 and 4: ");
            numberOfPlayers = scanner.nextInt();
        }
        return numberOfPlayers;
    }

    private String getPlayerName() {
        System.out.println("Enter your name: ");
        return scanner.next();

    }

    public GameState init() {
        System.out.println("Welcome to Mau Mau!");
        int numberOfPlayers = this.numberOfPlayers();
        String playerName = this.getPlayerName();
        GameState gameState = gameService.initializeGame(playerName, numberOfPlayers);
        gameState.getPlayers().forEach(player -> System.out.println(player.getName()));
        return gameState;
    }

    public void displayHand(Player player) {
        playerService.sortPlayersCards(player);
        IntStream.range(0, player.getHand().size())
                .forEach(i -> System.out.println(i + ": " + player.getHand().get(i)));
    }

    public Card getPlayerCardChoice(Player player, Card topCard) {
        while (true) {
            System.out.println("Enter the index of the card you want to play, or 'draw' to draw a card:");
            String input = scanner.next();
            if (input.equalsIgnoreCase("draw")) {
                return null;
            }
            try {
                int cardIndex = Integer.parseInt(input);
                Card chosenCard = player.getHand().get(cardIndex);
                if (ruleService.isValidMove(chosenCard, topCard)) {
                    return chosenCard;
                } else {
                    System.out.println("Invalid move. The chosen card does not match the top card.");
                }
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.out.println("Invalid input. Please enter a valid card index or 'draw'.");
            }
        }
    }

    public Suit getPlayerWishedSuit(Player player) {
        while (true) {
            System.out.println(player.getName() + ", choose a suit for your wish:");
            System.out.println("1: CLUBS");
            System.out.println("2: DIAMONDS");
            System.out.println("3: HEARTS");
            System.out.println("4: SPADES");

            try {
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        return Suit.CLUBS;
                    case 2:
                        return Suit.DIAMONDS;
                    case 3:
                        return Suit.HEARTS;
                    case 4:
                        return Suit.SPADES;
                    default:
                        System.out.println("Invalid input. Please enter a number between 1 and 4.");
                }
            } catch (InputMismatchException e) {
                LOGGER.error("InputMismatchException: Invalid input entered.", e);
                scanner.next(); // Clear the invalid input
            }
        }
    }
}
