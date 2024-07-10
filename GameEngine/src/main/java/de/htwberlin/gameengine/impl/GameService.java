package de.htwberlin.gameengine.impl;

import de.htwberlin.cardsmanagement.api.service.CardManagerInterface;
import de.htwberlin.gameengine.api.service.GameManagerInterface;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.gameengine.api.model.GameState;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.playermanagement.api.service.PlayerManagerInterface;
import de.htwberlin.rulesmanagement.api.model.Rules;
import de.htwberlin.rulesmanagement.api.service.RuleEngineInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GameService implements GameManagerInterface {

    private static final Logger LOGGER = LogManager.getLogger(GameService.class);

    private PlayerManagerInterface playerManagerInterface;
    private CardManagerInterface cardManagerInterface;
    private RuleEngineInterface ruleEngineInterface;

    @Autowired
    public GameService(PlayerManagerInterface playerManagerInterface, CardManagerInterface cardManagerInterface, RuleEngineInterface ruleEngineInterface) {
        this.playerManagerInterface = playerManagerInterface;
        this.cardManagerInterface = cardManagerInterface;
        this.ruleEngineInterface = ruleEngineInterface;
    }

    public GameService() {
    }

    @Override
    public GameState initializeGame(String playerName, int numberOfPlayers) {
        GameState game = new GameState();
        List<Card> deck = cardManagerInterface.shuffle(cardManagerInterface.createDeck());

        // Create main player
        List<Card> firstPlayerHand = IntStream.range(0, 5)
                .mapToObj(i -> deck.remove(deck.size() - 1))
                .collect(Collectors.toList());
        Player firstPlayer = playerManagerInterface.createPlayer(playerName, firstPlayerHand);

        // Create remaining players with default names
        List<Player> players = IntStream.range(1, numberOfPlayers)
                .mapToObj(i -> {
                    List<Card> hand = IntStream.range(0, 5)
                            .mapToObj(j -> deck.remove(deck.size() - 1))
                            .collect(Collectors.toList());
                    return playerManagerInterface.createPlayer("Player " + (i + 1), hand);
                })
                .collect(Collectors.toList());

        players.add(0, firstPlayer);
        game.setPlayers(players);
        List<Card> discardPile = new ArrayList<>();
        discardPile.add(deck.remove(deck.size() - 1));
        game.setDiscardPile(discardPile);
        game.setRules(new Rules());
        game.setGameRunning(true);

        game.setDeck(deck);

        game.setCurrentPlayerIndex(0);
//        game.setNextPlayerIndex(1);

        return game;
    }

    @Override
    public Player nextPlayer(GameState gameState) {
        int nextPlayerIndex = ruleEngineInterface.calculateNextPlayerIndex(gameState.getCurrentPlayerIndex(), gameState.getPlayers().size(), gameState.getRules());
        gameState.setCurrentPlayerIndex(nextPlayerIndex);
        return gameState.getPlayers().get(nextPlayerIndex);
    }

    @Override
    public void calcRankingPoints(GameState game) {
        game.getPlayers().forEach( player -> {
            // Updated rankingPoints with the sum of all scores
            player.setRankingPoints(player.getScore().stream().reduce(0, Integer::sum));
        });
    }

    @Override
    public Player getWinner(GameState game) {
        return game.getPlayers().stream().max(Comparator.comparingInt(Player::getRankingPoints))
                .orElse(null);
    }

    @Override
    public void endRound(GameState game) {
        game.getPlayers().forEach( player -> {
            // Updated score
            player.getScore().add(ruleEngineInterface.calculateScore(player.getHand()));
            // Updated rankingPoints with the sum of all scores
            player.setRankingPoints(player.getScore().stream().reduce(0, Integer::sum));
        });
        // Clear the discard pile
        game.getDiscardPile().clear();
        // Shuffle the deck
        List<Card> deck = cardManagerInterface.shuffle(cardManagerInterface.createDeck());

        // Distribute new cards to the players
        game.setDeck(deck);
        game.getPlayers().forEach(player -> {
            List<Card> hand = IntStream.range(0, 5)
                    .mapToObj(j -> game.getDeck().remove(game.getDeck().size() - 1))
                    .collect(Collectors.toList());
            player.setHand(hand);
        });

        List<Card> discardPile = new ArrayList<>();
        discardPile.add(deck.remove(deck.size() - 1));
        game.setDiscardPile(discardPile);

        // Set the current player index to 0
        game.setCurrentPlayerIndex(0);
    }

    @Override
    public Card drawCard(GameState game, Player player) {
        // Check if deck is empty
        if (game.getDeck().isEmpty()) {
            this.reshuffleDeck(game);
        }

        Card drawCard = game.getDeck().remove(game.getDeck().size() - 1);
        player.getHand().add(drawCard);
        return drawCard;
    }

    private void reshuffleDeck(GameState game) {
        LOGGER.info("Deck is empty, reshuffling Deck");

        // Save the last card from the discard pile
        Card lastCard = game.getDiscardPile().remove(game.getDiscardPile().size() - 1);

        // Create new discard pile with only last card
        List<Card> newDiscard = new ArrayList<>();
        newDiscard.add(lastCard);

        // Move all cards from the discard pile to deck
        List<Card> newDeck = game.getDiscardPile();

        // Set and shuffle the new deck
        game.setDeck(cardManagerInterface.shuffle(newDeck));

        // Set the new discard pile
        game.setDiscardPile(newDiscard);
    }

    @Override
    public void playCard(Player player, Card card, GameState gameState) {
        if (player.getHand().remove(card)) {
            gameState.getDiscardPile().add(card);
        } else {
            throw new IllegalArgumentException("The player does not have the specified card.");
        }
    }

    @Override
    public boolean checkEmptyHand(Player player) {
        return player.getHand().isEmpty();
    }

}

