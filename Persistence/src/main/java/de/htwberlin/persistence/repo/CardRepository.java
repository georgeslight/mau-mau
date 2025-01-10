package de.htwberlin.persistence.repo;

import de.htwberlin.cardsmanagement.api.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CRUD Operations
 * Spring Data JPA vereinfacht die Implementierung der typischen CRUD-Operationen (Create, Read, Update, Delete) durch
 * Repository-Schnittstellen bereitstellt, die diese Operationen verarbeiten. Durch die Erweiterung spezifischer Repository-Schnittstellen können Sie
 * Out-of-the-Box-Methoden nutzen, um Ihre Entitäten zu verwalten, ohne Boilerplate-Code schreiben zu müssen.
 * Repository zur Verwaltung von Kartendaten.
 * Bietet Methoden zum Abrufen von Karten basierend auf verschiedenen Spielzuständen.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    /**
     * Findet alle Karten eines Spielers basierend auf der Spiel-ID.
     *
     * @param gameId die ID des Spiels
     * @return eine Liste von Karten, die dem Spieler im angegebenen Spiel zugeordnet sind
     */
    @Query(value = "SELECT c.* FROM card c JOIN player p ON c.player_id = p.id WHERE p.game_id = :gameId", nativeQuery = true)
    List<Card> findPlayerCardsByGameId(@Param("gameId") Long gameId);

    /**
     * Findet alle Karten im Ablagestapel basierend auf der Spiel-ID.
     *
     * @param gameId die ID des Spiels
     * @return eine Liste von Karten, die im Ablagestapel des angegebenen Spiels sind
     */
    @Query(value = "SELECT c.* FROM card c JOIN gamestate gs ON c.pile_id = gs.id WHERE gs.id = :gameId", nativeQuery = true)
    List<Card> findPileCardsByGameId(@Param("gameId") Long gameId);

    /**
     * Findet alle Karten im Deck basierend auf der Spiel-ID.
     *
     * @param gameId die ID des Spiels
     * @return eine Liste von Karten, die im Deck des angegebenen Spiels sind
     */
    @Query(value = "SELECT c.* FROM card c JOIN gamestate gs ON c.deck_id = gs.id WHERE gs.id = :gameId", nativeQuery = true)
    List<Card> findDeckCardsByGameId(@Param("gameId") Long gameId);
}
