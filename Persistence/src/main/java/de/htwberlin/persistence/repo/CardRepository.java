package de.htwberlin.persistence.repo;

import de.htwberlin.cardsmanagement.api.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CRUD Operations
 * Spring Data JPA simplifies the implementation of the typical CRUD operations (Create, Read, Update, Delete) by
 * providing repository interfaces that handle these operations. By extending specific repository interfaces, you can
 * leverage out-of-the-box methods to manage your entities without writing any boilerplate code.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    // For saveGame
    @Query(value = "SELECT c.* FROM card c JOIN player p ON c.player_id = p.id WHERE p.game_id = :gameId", nativeQuery = true)
    List<Card> findPlayerCardsByGameId(@Param("gameId") Long gameId);

    // For saveGame
    @Query(value = "SELECT c.* FROM card c JOIN gamestate gs ON c.pile_id = gs.id WHERE gs.id = :gameId", nativeQuery = true)
    List<Card> findPileCardsByGameId(@Param("gameId") Long gameId);

    // For saveGame
    @Query(value = "SELECT c.* FROM card c JOIN gamestate gs ON c.deck_id = gs.id WHERE gs.id = :gameId", nativeQuery = true)
    List<Card> findDeckCardsByGameId(@Param("gameId") Long gameId);
}
