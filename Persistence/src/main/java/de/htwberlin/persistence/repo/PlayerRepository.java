package de.htwberlin.persistence.repo;

import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.playermanagement.api.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Query Methods
 * Spring Data JPA provides a powerful way to define queries directly in the repository interface using method names.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByName(String name);
    List<Player> findByRankingPointsGreaterThan(Integer rankingPoints);
    List<Player> findByHandSuit(Suit suit);
    List<Player> findBySaidMau(boolean saidMau);

    // For saveGame
    @Query(value = "SELECT p.* FROM player p JOIN gamestate gs ON p.game_id = gs.id WHERE gs.id = :gameId", nativeQuery = true)
    List<Player> findPlayerByGameId(@Param("gameId") Long gameId);
}
