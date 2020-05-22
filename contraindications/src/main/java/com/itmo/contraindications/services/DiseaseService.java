package com.itmo.contraindications.services;

import com.itmo.contraindications.dto.DiseaseDto;
import com.itmo.contraindications.models.Disease;

public interface DiseaseService {
    Disease add(DiseaseDto diseaseDto);

    Disease getById(Integer id);
}
