package com.itmo.contraindications.repositories;

import org.springframework.data.repository.CrudRepository;
import com.itmo.contraindications.models.Medicine;

public interface MedicineRepository extends CrudRepository<Medicine, Integer> {
}
