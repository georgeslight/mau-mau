package de.htwberlin.persistence.repo;

import de.htwberlin.cardsmanagement.api.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CRUD Operations
 * Spring Data JPA simplifies the implementation of the typical CRUD operations (Create, Read, Update, Delete) by
 * providing repository interfaces that handle these operations. By extending specific repository interfaces, you can
 * leverage out-of-the-box methods to manage your entities without writing any boilerplate code.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}
