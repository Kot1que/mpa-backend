package com.itmo.contraindications.dto.mapper;

import org.springframework.stereotype.Component;
import com.itmo.contraindications.dto.ContraindicationDto;
import com.itmo.contraindications.models.Contraindication;

@Component
public class ContraindicationMapper {
    public ContraindicationDto toDto(Contraindication contraindication) {
        ContraindicationDto dto = new ContraindicationDto();

        dto.setId(contraindication.getId());

        dto.setDescription(contraindication.getDescription());
        dto.setFood(contraindication.getFood());

        if (contraindication.getSex() != null) {
            dto.setSex(contraindication.getSex().getReadable());
        }

        if (contraindication.getSeverity() != null) {
            dto.setSeverity(contraindication.getSeverity().getReadable());
        }

        if (contraindication.getFrequency() != null) {
            dto.setFrequency(contraindication.getFrequency().getReadable());
        }

        dto.setMinAge(contraindication.getMinAge());
        dto.setMaxAge(contraindication.getMaxAge());

        dto.setInfoSource(contraindication.getInfoSource());

        if (contraindication.getActiveSubstanceSource() != null) {
            dto.setSource(contraindication.getActiveSubstanceSource().getName());
        } else if (contraindication.getMedicineSource() != null) {
            dto.setSource(contraindication.getMedicineSource().getName());
        }

        if (contraindication.getDiseaseTarget() != null) {
            dto.setTarget(contraindication.getDiseaseTarget().getName());
        } else if (contraindication.getMedicineTarget() != null) {
            dto.setTarget(contraindication.getMedicineTarget().getName());
        } else if (contraindication.getActiveSubstanceTarget() != null) {
            dto.setTarget(contraindication.getActiveSubstanceTarget().getName());
        }

        return dto;
    }
}
