package de.htwberlin.gameui.impl;

import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.gameengine.api.model.GameState;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.cardsmanagement.api.model.Card;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

@Component
public class GameUIView {

    private final Scanner scanner = new Scanner(System.in);

    // ANSI escape codes for colors
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";

    public void showWelcomeMessage() {
        System.out.println("Welcome to Mau Mau!");
        this.showInstructions();
    }

    public void showInstructions() {
        System.out.println(PURPLE + "Instructions: " + RESET);
        System.out.println("1. Each player is dealt 5 cards.");
        System.out.println("2. The goal is to get rid of all your cards.");
        System.out.println("3. You can play a card if it matches the rank or suit of the top card on the discard pile.");
        System.out.println("4. Special cards have special rules.");
        System.out.println("5. Don't forget to say 'Mau' when you have one card left!\n");
    }

    public int getNumberOfPlayers() {
        System.out.println(CYAN + "Enter the number of players (2-4): " + RESET);
        int numberOfPlayers = scanner.nextInt();
        while (numberOfPlayers < 2 || numberOfPlayers > 4) {
            System.out.println(YELLOW + "Invalid number. Please enter a number between 2 and 4: " + RESET);
            numberOfPlayers = scanner.nextInt();
        }
        return numberOfPlayers;
    }

    public String getPlayerName() {
        System.out.println(CYAN + "Enter your name: " + RESET);
        return scanner.next();
    }

    public void showPlayers(List<Player> players) {
        System.out.println(PURPLE + "\nPlayers:" + RESET);
        players.forEach(player -> System.out.println(player.getName()));
    }

    public void showCurrentPlayerInfo(Player player) {
        System.out.println();
        System.out.println(PURPLE + "----- " + player.getName() + "'s turn. Your hand: -----" + RESET);
        IntStream.range(0, player.getHand().size())
                .forEach(i -> System.out.println((i + 1) + ": " + YELLOW + player.getHand().get(i) + RESET));
    }

    public void showTopCard(Card card) {
        System.out.println("Top card on the discard pile: " + YELLOW + card + RESET);
    }

    public void showAccumulatedDrawCount(int count) {
        System.out.println(PURPLE + "You need to draw " + count + " cards, or play another 7." + RESET);
    }

    public String promptCardChoice() {
        System.out.println( CYAN + "Enter the index of the card you want to play, or 'd' to draw a card:");
        System.out.println("Enter 'm' (mau) before playing your card if you will have one card left!" + RESET);
        return scanner.next();
    }

    public void showInvalidMoveMessage() {
        System.out.println(RED + "Invalid move. The chosen card does not match the top card." + RESET);
    }

    public void showInvalidInputMessage() {
        System.out.println(RED + "Invalid input. Please enter a valid card index or 'd' (draw)." + RESET);
    }

    public void showPlayedCard(Player currentPlayer, Card playedCard) {
        System.out.println(currentPlayer.getName() + " played: " + YELLOW + playedCard + RESET);
    }

    public Suit getPlayerWishedSuit(Player player) {
        while (true) {
            System.out.println(CYAN + player.getName() + ", choose a suit for your wish:" + RESET);
            System.out.println("1: " + YELLOW + "♣" + RESET);
            System.out.println("2: " + YELLOW + "♦" + RESET);
            System.out.println("3: " + YELLOW + "♥" + RESET);
            System.out.println("4: " + YELLOW + "♠" + RESET);

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
                        System.out.println(RED + "Invalid input. Please enter a number between 1 and 4." + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "InputMismatchException: Invalid input entered.\n" + e + RESET);
                scanner.next(); // Clear the invalid input
            }
        }
    }

    public void showDrawnCard(Player currentPlayer, Card drawnCard) {
        System.out.println(currentPlayer.getName() + " drew a card: " +  YELLOW + drawnCard + RESET);
    }

    public void showWishedSuit(Player currentPlayer, Suit wishedSuit) {
        System.out.println(currentPlayer.getName() + " wishes for " + YELLOW + wishedSuit + RESET);
    }

    public void showWinner(Player currentPlayer) {
        System.out.println(BLUE + "\n----- " + currentPlayer.getName() + " wins the round! -----\n" + RESET);
    }

    public void showMauMessage(Player player) {
        System.out.println(PURPLE + player.getName() +" just said Mau!" + RESET);
    }

    public void showMauFailureMessage(Player player) {
        System.out.println(PURPLE + player.getName() + " failed to say 'Mau' and has to draw two cards!" + RESET);
    }

    public void showEndGame(GameState gameState, Player winner) {
        System.out.println(PURPLE + "\nThe game has ended!" + RESET);
        System.out.println("Final Ranking Points: ");
        this.showRankingPoints(gameState);

        if (winner != null) {
            System.out.println("\n\n");
            System.out.println(BLUE + "----- The winner is " + winner.getName().toUpperCase() + " with " + winner.getRankingPoints() + " points! -----" + RESET);
            System.out.println("\n\n");
        } else {
            System.out.println("No winner could be determined.");
        }
    }

    public void showRankingPoints(GameState gameState) {
        System.out.println("Ranking Points: ");
        gameState.getPlayers().forEach(player -> {
            System.out.println(player.getName() + ": " + player.getRankingPoints() + " points.");
        });
    }
}
