package de.htwberlin.impl.service;

import de.htwberlin.api.service.CardManagerInterface;
import de.htwberlin.api.service.GameManagerInterface;
import de.htwberlin.api.model.Card;
import de.htwberlin.api.model.GameState;
import de.htwberlin.api.model.Player;
import de.htwberlin.api.service.PlayerManagerInterface;
import de.htwberlin.api.service.RuleEngineInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GameService implements GameManagerInterface {

    private static final Logger LOGGER = LogManager.getLogger(GameService.class);

    private final PlayerManagerInterface playerManagerInterface;
    private final CardManagerInterface cardManagerInterface;
    private final RuleEngineInterface ruleEngineInterface;
    @Autowired
    public GameService(PlayerManagerInterface playerManagerInterface, CardManagerInterface cardManagerInterface, RuleEngineInterface ruleEngineInterface) {
        this.playerManagerInterface = playerManagerInterface;
        this.cardManagerInterface = cardManagerInterface;
        this.ruleEngineInterface = ruleEngineInterface;
    }


    @Override
    public GameState initializeGame(int numberOfPlayers) {
        GameState game = new GameState();
        Stack<Card> deck = cardManagerInterface.shuffle(cardManagerInterface.createDeck());
        List<Player> players = IntStream.range(0, numberOfPlayers)
                .mapToObj(i -> {
                    List<Card> hand = IntStream.range(0, 5)
                            .mapToObj(j -> deck.pop())
                            .collect(Collectors.toList());
                    return playerManagerInterface.createPlayer("Player " + i, hand);
                })
                .collect(Collectors.toList());

        game.setPlayers(players);
        Stack<Card> discardPile = new Stack<>();
        discardPile.push(deck.pop());
        game.setDiscardPile(discardPile);

        game.setDeck(deck);

        game.setCurrentPlayerIndex(0);
        game.setNextPlayerIndex(1);

        return game;
    }

    @Override
    public Player nextPlayer(GameState gameState) {
        int nextPlayerIndex = ruleEngineInterface.calculateNextPlayerIndex(gameState.getCurrentPlayerIndex(), gameState.getPlayers().size());
        gameState.setCurrentPlayerIndex(nextPlayerIndex);
        return gameState.getPlayers().get(nextPlayerIndex);
    }

    @Override
    public Player endGame(GameState game) {
        game.getPlayers().forEach( player -> {
            // Updated rankingPoints with the sum of all scores
            player.setRankingPoints(player.getScore().stream().reduce(0, Integer::sum));
        });
        // Find the player with the highest ranking points
        return game.getPlayers().stream().max(Comparator.comparingInt(Player::getRankingPoints))
                .orElse(null);
    }

    @Override
    public void endRound(GameState game) {
        game.getPlayers().forEach( player -> {
            // Updated rankingPoints with the sum of all scores
            player.setRankingPoints(player.getScore().stream().reduce(0, Integer::sum));
        });
        // Clear the discard pile
        game.getDiscardPile().clear();
        // Shuffle the deck
        Stack<Card> deck = cardManagerInterface.shuffle(cardManagerInterface.createDeck());

        // Distribute new cards to the players
        game.setDeck(deck);
        game.getPlayers().forEach(player -> {
            List<Card> hand = IntStream.range(0, 5)
                    .mapToObj(j -> game.getDeck().pop())
                    .collect(Collectors.toList());
            player.setHand(hand);
        });

        Stack<Card> discardPile = new Stack<>();
        discardPile.push(deck.pop());
        game.setDiscardPile(discardPile);

        // Set the current player index to 0
        game.setCurrentPlayerIndex(0);
        // Set the next player index to 1
        game.setNextPlayerIndex(1);
    }

    @Override
    public Card drawCard(GameState game, Player player) {
        if (game.getDeck().empty()) throw new IllegalStateException("Cannot draw from an empty deck");
        Card drawCard = game.getDeck().pop();
        player.getHand().add(drawCard);
        return drawCard;
    }

    @Override
    public void playCard(Player player, Card card, GameState gameState) {
        if (player.getHand().remove(card)) {
            gameState.getDiscardPile().push(card);
        } else {
            throw new IllegalArgumentException("The player does not have the specified card.");
        }
    }

    @Override
    public boolean checkWinner(Player player) {
        return player.getHand().isEmpty() && player.isSaidMau();
    }

}

