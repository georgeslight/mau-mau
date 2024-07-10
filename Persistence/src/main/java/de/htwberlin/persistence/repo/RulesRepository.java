package de.htwberlin.persistence.repo;

import de.htwberlin.rulesmanagement.api.model.Rules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RulesRepository extends JpaRepository<Rules, Long> {
}
