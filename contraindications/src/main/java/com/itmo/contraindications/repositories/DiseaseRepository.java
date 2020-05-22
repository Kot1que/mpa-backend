package com.itmo.contraindications.repositories;

import org.springframework.data.repository.CrudRepository;
import com.itmo.contraindications.models.Disease;

public interface DiseaseRepository extends CrudRepository<Disease, Integer> {
}
