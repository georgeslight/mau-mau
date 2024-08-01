package de.htwberlin.persistence.repo;

import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.playermanagement.api.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Query Annotation
 * Spring Data JPA bietet eine leistungsfähige Möglichkeit, Abfragen direkt in der Repository-Schnittstelle unter Verwendung von Methodennamen zu definieren.
 * Repository zur Verwaltung von Spielerinformationen.
 * Bietet Methoden zum Abrufen von Spielern basierend auf verschiedenen Kriterien.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    /**
     * Findet Spieler basierend auf ihrem Namen.
     *
     * @param name der Name des Spielers
     * @return eine Liste von Spielern mit dem angegebenen Namen
     */
    List<Player> findByName(String name);


    /**
     * Findet Spieler mit mehr als einer bestimmten Anzahl an Ranglistenpunkten.
     *
     * @param rankingPoints die Mindestanzahl an Ranglistenpunkten
     * @return eine Liste von Spielern mit mehr als der angegebenen Anzahl an Ranglistenpunkten
     */
    List<Player> findByRankingPointsGreaterThan(Integer rankingPoints);


    /**
     * Findet Spieler, die eine bestimmte Kartenfarbe in der Hand haben.
     *
     * @param suit die Farbe der Karten
     * @return eine Liste von Spielern mit der angegebenen Kartenfarbe in der Hand
     */
    List<Player> findByHandSuit(Suit suit);


    /**
     * Findet Spieler, die "Mau" gesagt haben.
     *
     * @param saidMau true, wenn der Spieler "Mau" gesagt hat
     * @return eine Liste von Spielern, die "Mau" gesagt haben
     */
    List<Player> findBySaidMau(boolean saidMau);


    /**
     * Findet alle Spieler eines Spiels basierend auf der Spiel-ID.
     *
     * @param gameId die ID des Spiels
     * @return eine Liste von Spielern, die dem angegebenen Spiel zugeordnet sind
     */
    @Query(value = "SELECT p.* FROM player p JOIN gamestate gs ON p.game_id = gs.id WHERE gs.id = :gameId", nativeQuery = true)
    List<Player> findPlayerByGameId(@Param("gameId") Long gameId);
}
