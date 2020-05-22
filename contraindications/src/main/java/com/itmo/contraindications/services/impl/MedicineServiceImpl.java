package com.itmo.contraindications.services.impl;

import com.itmo.contraindications.dto.MedicineDto;
import com.itmo.contraindications.models.Medicine;
import com.itmo.contraindications.repositories.MedicineRepository;
import com.itmo.contraindications.services.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;

    @Autowired
    public MedicineServiceImpl(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public Medicine add(MedicineDto medicineDto) {
        Medicine medicine = new Medicine();
        medicine.setId(medicineDto.getId());
        medicine.setName(medicineDto.getName());

        return this.medicineRepository.save(medicine);
    }

    @Override
    public Medicine getById(Integer id) {
        return this.medicineRepository.findById(id).orElseThrow(() -> new RuntimeException("Medicine not found"));
    }
}
