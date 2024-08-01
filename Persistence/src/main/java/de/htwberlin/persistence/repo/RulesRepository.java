package de.htwberlin.persistence.repo;

import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.rulesmanagement.api.model.Rules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Für komplexere Abfragen k die @Query-Annotation verwenden, um JPQL- oder native SQL-Abfragen zu definieren.
 * Repository zur Verwaltung von Spielregeln.
 * Bietet Methoden zum Abrufen von Regeln basierend auf verschiedenen Kriterien.
 */
@Repository
public interface RulesRepository extends JpaRepository<Rules, Long> {


    /**
     * Findet Regeln, bei denen mehr Karten als eine bestimmte Anzahl gezogen werden müssen.
     * JPQL Queries
     *
     * @param cards die Mindestanzahl an zu ziehenden Karten
     * @return eine Liste von Regeln, bei denen mehr Karten als angegeben gezogen werden müssen
     */
    @Query("SELECT r FROM Rules r WHERE r.cardsToBeDrawn > ?1")
    List<Rules> findRulesWithMoreCardsToBeDrawnThan(int cards);


    /**
     * Findet Regeln basierend auf der gewünschten Kartenfarbe.
     * JPQL Queries with Named Parameters
     *
     * @param wishCard die gewünschte Kartenfarbe
     * @return eine Liste von Regeln mit der angegebenen gewünschten Kartenfarbe
     */
    @Query("SELECT r FROM Rules r WHERE r.wishCard = :wishCard")
    List<Rules> findRulesByWishCard(@Param("wishCard") Suit wishCard);


    /**
     * Findet Regeln basierend auf der Spiel-ID.
     * JPQL Queries Joining Tables
     *
     * @param gameId die ID des Spiels
     * @return die Regeln des angegebenen Spiels
     */
    @Query("SELECT r FROM Rules r JOIN GameState gs ON r.id = gs.rules.id WHERE gs.id = :gameId")
    Rules findRulesByGameId(@Param("gameId") Long gameId);


    /**
     * Findet Regeln, bei denen der nächste Spieler übersprungen wird.
     * Native SQL Queries
     *
     * @return eine Liste von Regeln, bei denen der nächste Spieler übersprungen wird
     */
    @Query(value = "SELECT * FROM Rules r WHERE r.skipNextPlayerTurn = true", nativeQuery = true)
    List<Rules> findRulesWhereSkipNextPlayerTurnIsTrue();


    /**
     * Findet Regeln, bei denen der Spieler erneut spielen kann.
     * Native SQL Queries with Parameter
     *
     * @param canPlayAgain true, wenn der Spieler erneut spielen kann
     * @return eine Liste von Regeln, bei denen der Spieler erneut spielen kann
     */
    @Query(value = "SELECT * FROM Rules r WHERE r.canPlayAgain = :canPlayAgain", nativeQuery = true)
    List<Rules> findRulesWhereCanPlayAgain(@Param("canPlayAgain") boolean canPlayAgain);
}
