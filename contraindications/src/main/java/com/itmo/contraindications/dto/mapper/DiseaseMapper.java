package com.itmo.contraindications.dto.mapper;

import com.itmo.contraindications.dto.DiseaseDto;
import com.itmo.contraindications.models.Disease;
import org.springframework.stereotype.Component;

@Component
public class DiseaseMapper {
    public DiseaseDto toDto(Disease disease) {
        DiseaseDto dto = new DiseaseDto();

        dto.setId(disease.getId());
        dto.setName(disease.getName());

        return dto;
    }
}
