package de.htwberlin.persistence.repo;

import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.rulesmanagement.api.model.Rules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Query Annotation
 * For more complex queries, you can use the @Query annotation to define JPQL or native SQL queries.
 */
@Repository
public interface RulesRepository extends JpaRepository<Rules, Long> {

    // JPQL Queries
    @Query("SELECT r FROM Rules r WHERE r.cardsToBeDrawn > ?1")
    List<Rules> findRulesWithMoreCardsToBeDrawnThan(int cards);

    // JPQL Queries with Named Parameters
    @Query("SELECT r FROM Rules r WHERE r.wishCard = :wishCard")
    List<Rules> findRulesByWishCard(@Param("wishCard") Suit wishCard);

    // JPQL Queries Joining Tables
    @Query("SELECT r FROM Rules r JOIN GameState gs ON r.id = gs.rules.id WHERE gs.id = :gameId")
    Rules findRulesByGameId(@Param("gameId") Long gameId);

    // Native SQL Queries
    @Query(value = "SELECT * FROM Rules r WHERE r.skipNextPlayerTurn = true", nativeQuery = true)
    List<Rules> findRulesWhereSkipNextPlayerTurnIsTrue();

    // Native SQL Queries with Parameter
    @Query(value = "SELECT * FROM Rules r WHERE r.canPlayAgain = :canPlayAgain", nativeQuery = true)
    List<Rules> findRulesWhereCanPlayAgain(@Param("canPlayAgain") boolean canPlayAgain);
}
