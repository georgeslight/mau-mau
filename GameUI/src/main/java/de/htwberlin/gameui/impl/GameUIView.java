package de.htwberlin.gameui.impl;

import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.gameengine.api.model.GameState;
import de.htwberlin.gameui.exception.InvalidNameException;
import de.htwberlin.gameui.exception.InvalidNumberOfPlayersException;
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
        int numberOfPlayers = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                numberOfPlayers = scanner.nextInt();

                if (numberOfPlayers < 2 || numberOfPlayers > 4) {
                    throw new InvalidNumberOfPlayersException("Invalid number. Please enter a number between 2 and 4.");
                } else {
                    validInput = true;
                }
            } catch (InvalidNumberOfPlayersException e) {
                System.out.println(YELLOW + e.getMessage() + RESET);
            } catch (InputMismatchException e) {
                System.out.println(RED + "Invalid input. Please enter a numeric value." + RESET);
                scanner.next(); // Clear the invalid input from the scanner buffer
            }
        }

        return numberOfPlayers;
    }

    public String getPlayerName() {
        String playerName = "";
        try {
            System.out.println(CYAN + "Enter your name: " + RESET);
            playerName = scanner.nextLine().trim();
            if (playerName == null || playerName.isEmpty() || playerName.contains(" ")) {
                throw new InvalidNameException("Name must be a non-empty single word without spaces.");
            }
        } catch (InvalidNameException e) {
            System.out.println(RED + "Invalid input: " + e.getMessage() + RESET);
            return this.getPlayerName();
        }
        return playerName;
    }

    public void showPlayers(List<Player> players) {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("Cannot shuffle an empty or null deck");
        }
        System.out.println(PURPLE + "\nPlayers:" + RESET);
        players.forEach(player -> System.out.println(player.getName()));
    }

    public void showCurrentPlayerInfo(Player player) {
        System.out.println();
        System.out.println(PURPLE + "----- " + player.getName() + "'s turn" + RESET);
    }
    public void showActivePlayersHand(Player player) {
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

    public Integer showCreateOrJoinGameMessage() {
        System.out.println(CYAN + "Do you want to create a new game or join an existing game?");
        System.out.println("1: Create a new game");
        System.out.println("2: Join an existing game" + RESET);

        int choice = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice == 1 || choice == 2) {
                    validInput = true;
                } else {
                    System.out.println(RED + "Invalid choice. Please enter 1 or 2." + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid input. Please enter a number." + RESET);
            }
        }

        return choice;
    }


    public GameState getGame(List<GameState> games) {
        System.out.println(CYAN + "Select a game to continue: " + RESET);
        IntStream.range(0, games.size())
                .forEach(i -> System.out.println((i + 1) + ": " + games.get(i).getId()+ " - " + games.get(i).getPlayers().size() + " players" + " - Host:" + games.get(i).getPlayers().get(0).getName() ) );

        return games.get(scanner.nextInt() - 1);
    }

    public int getWaitingForPlayersToJoin() {
        System.out.println(CYAN + "Enter 1 if you want to wait for more players to join, or 0 to start the game:" + RESET);
        return scanner.nextInt();
    }

//    public void showWaitingForOtherPlayersToPlay(GameState gameState) {
//        System.out.println(PURPLE + "Waiting for other players to play..." + RESET);
//        System.out.println("Players who have played: ");
//        gameState.getPlayers().forEach(player -> {
//            if (player.getHand().isEmpty()) {
//                System.out.println(player.getName() + " has finished playing.");
//            }
//        });
//    }
    public void showWaitingForPlayer(Player currentPlayer) {
        System.out.println(PURPLE + "Waiting for " + currentPlayer.getName() + " to make a move..." + RESET);
    }

    public void showVirtualPlayersTurn(Player currentPlayer) {
        System.out.println(PURPLE + "Virtual " + currentPlayer.getName() + "'s turn." + RESET);
    }

    public void showFailedToJoinGame() {
        System.out.println(RED + "Failed to join the game. Please try again." + RESET);
    }

    public void showInvalidPlayerNameMessage() {
        System.out.println(RED + "Invalid player name. Please enter a valid name." + RESET);
    }
}
