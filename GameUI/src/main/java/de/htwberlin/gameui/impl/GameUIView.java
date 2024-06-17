package de.htwberlin.gameui.impl;

import de.htwberlin.cardmanagement.api.enums.Suit;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.cardmanagement.api.model.Card;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

@Service
public class GameUIView {

    private final Scanner scanner = new Scanner(System.in);

    public void showWelcomeMessage() {
        System.out.println("Welcome to Mau Mau!");
    }

    public int getNumberOfPlayers() {
        System.out.println("Enter the number of players (2-4): ");
        int numberOfPlayers = scanner.nextInt();
        while (numberOfPlayers < 2 || numberOfPlayers > 4) {
            System.out.println("Invalid number. Please enter a number between 2 and 4: ");
            numberOfPlayers = scanner.nextInt();
        }
        return numberOfPlayers;
    }

    public String getPlayerName() {
        System.out.println("Enter your name: ");
        return scanner.next();
    }

    public void showPlayers(List<Player> players) {
        players.forEach(player -> System.out.println(player.getName()));
    }

    public void showCurrentPlayerInfo(Player player) {
        System.out.println(player.getName() + "'s turn. Your hand: ");
        IntStream.range(0, player.getHand().size())
                .forEach(i -> System.out.println(i + ": " + player.getHand().get(i)));
    }

    public void showTopCard(Card card) {
        System.out.println("Top card on the discard pile: " + card);
    }

    public void showAccumulatedDrawCount(int count) {
        System.out.println("You need to draw " + count + " cards, or play another 7.");
    }

    public String promptCardChoice() {
        System.out.println("Enter the index of the card you want to play, or 'draw' to draw a card:");
        return scanner.next();
    }

    public void showInvalidMoveMessage() {
        System.out.println("Invalid move. The chosen card does not match the top card.");
    }

    public void showInvalidInputMessage() {
        System.out.println("Invalid input. Please enter a valid card index or 'draw'.");
    }

    public void showPlayedCard(Player currentPlayer, Card playedCard) {
        System.out.println(currentPlayer.getName() + " played: " + playedCard);
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
                System.out.println("InputMismatchException: Invalid input entered.\n" + e);
                scanner.next(); // Clear the invalid input
            }
        }
    }

    public void showDrawnCard(Player currentPlayer, Card drawnCard) {
        System.out.println(currentPlayer.getName() + " drew a card: " + drawnCard);
    }

    public void showWishedSuit(Player currentPlayer, Suit wishedSuit) {
        System.out.println(currentPlayer.getName() + " wishes for " + wishedSuit);
    }

    public void showWinner(Player currentPlayer) {
        System.out.println(currentPlayer.getName() + " wins the game!");
    }
}
