package com.itmo.contraindications.services.impl;

import com.itmo.contraindications.dto.MedicineDto;
import com.itmo.contraindications.models.ActiveSubstance;
import com.itmo.contraindications.models.Medicine;
import com.itmo.contraindications.repositories.MedicineRepository;
import com.itmo.contraindications.services.ActiveSubstanceService;
import com.itmo.contraindications.services.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;
    private final ActiveSubstanceService activeSubstanceService;

    @Autowired
    public MedicineServiceImpl(MedicineRepository medicineRepository, ActiveSubstanceService activeSubstanceService) {
        this.medicineRepository = medicineRepository;
        this.activeSubstanceService = activeSubstanceService;
    }

    @Override
    public Medicine add(MedicineDto medicineDto) {
        Medicine medicine = new Medicine();
        medicine.setId(medicineDto.getId());
        medicine.setName(medicineDto.getName());

        System.out.println(medicineDto);

        if (medicineDto.getActiveSubstances() == null) {
            return this.medicineRepository.save(medicine);
        }

        this.addActiveSubstances(medicineDto, medicine);

        System.out.println(medicine);

        return this.medicineRepository.save(medicine);
    }

    private void addActiveSubstances(MedicineDto medicineDto, Medicine medicine) {
        Set<ActiveSubstance> activeSubstanceSet = new HashSet<>();

        medicineDto.getActiveSubstances().forEach(activeSubstanceDto -> {
            try {
                ActiveSubstance activeSubstance = activeSubstanceService.findById(activeSubstanceDto.getId());
                activeSubstanceSet.add(activeSubstance);
            } catch (RuntimeException ex) {
                activeSubstanceSet.add(this.activeSubstanceService.add(activeSubstanceDto));
            }
        });

        medicine.setActiveSubstances(activeSubstanceSet);
    }

    @Override
    public Medicine getById(Integer id) {
        return this.medicineRepository.findById(id).orElseThrow(() -> new RuntimeException("Medicine not found"));
    }
}
