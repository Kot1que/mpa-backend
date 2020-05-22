package com.itmo.contraindications.services;

import com.itmo.contraindications.dto.MedicineDto;
import com.itmo.contraindications.models.Medicine;

public interface MedicineService {
    Medicine add(MedicineDto medicineDto);

    Medicine getById(Integer id);
}
