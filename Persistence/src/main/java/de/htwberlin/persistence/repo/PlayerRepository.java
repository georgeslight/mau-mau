package de.htwberlin.persistence.repo;

import de.htwberlin.playermanagement.api.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>, CustomPlayerRepository {

//    // JPQL query to find all players with a gameState
//    @Query("SELECT p FROM Player p WHERE p.game_id IS NOT NULL")
//    Collection<Player> findAllActivePlayerS();
//
//    // Native query to find all players with a gameState
//    @Query(
//            value = "SELECT * FROM Player p WHERE p.game_id IS NOT NULL",
//            nativeQuery = true)
//    Collection<Player> findAllActivePlayersNative();
}
