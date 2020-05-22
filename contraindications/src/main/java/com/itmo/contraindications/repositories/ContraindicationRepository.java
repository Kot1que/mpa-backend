package com.itmo.contraindications.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.itmo.contraindications.models.ActiveSubstance;
import com.itmo.contraindications.models.Contraindication;
import com.itmo.contraindications.models.Disease;
import com.itmo.contraindications.models.Medicine;

import java.util.List;
import java.util.Set;

@Repository
public interface ContraindicationRepository extends CrudRepository<Contraindication, Integer> {
    List<Contraindication> findAllByActiveSubstanceSource(ActiveSubstance activeSubstance);

    List<Contraindication> findAllByActiveSubstanceSourceIsIn(Set<ActiveSubstance> activeSubstances);

    List<Contraindication> findAlByMedicineSource(Medicine medicine);

    List<Contraindication> findAlByMedicineSourceIsIn(Set<Medicine> medicines);

    List<Contraindication> findAllByDiseaseTarget(Disease disease);

    List<Contraindication> findAllByDiseaseTargetIsIn(Set<Disease> diseases);
}
