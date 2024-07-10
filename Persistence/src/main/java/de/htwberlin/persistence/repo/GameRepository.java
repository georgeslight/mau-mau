package de.htwberlin.persistence.repo;

import de.htwberlin.gameengine.api.model.GameState;
import de.htwberlin.playermanagement.api.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface GameRepository extends JpaRepository<GameState, Long>, GameRepositoryCustom {

    @Query("SELECT p FROM GameState g JOIN g.players p WHERE g.id = ?1")
    Collection<Player> findAllPlayerByGameId(Long gameId);
}
