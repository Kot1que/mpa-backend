package com.itmo.contraindications.repositories;

import org.springframework.data.repository.CrudRepository;
import com.itmo.contraindications.models.ActiveSubstance;

public interface ActiveSubstanceRepository extends CrudRepository<ActiveSubstance, Integer> {
}
