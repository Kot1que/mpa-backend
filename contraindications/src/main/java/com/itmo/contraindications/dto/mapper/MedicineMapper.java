package com.itmo.contraindications.dto.mapper;

import com.itmo.contraindications.dto.MedicineDto;
import com.itmo.contraindications.models.Medicine;
import org.springframework.stereotype.Component;

@Component
public class MedicineMapper {
    public MedicineDto toDto(Medicine medicine) {
        MedicineDto dto = new MedicineDto();

        dto.setId(medicine.getId());
        dto.setName(medicine.getName());

        return dto;
    }
}
