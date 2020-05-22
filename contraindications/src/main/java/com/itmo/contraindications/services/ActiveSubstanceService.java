package com.itmo.contraindications.services;

import com.itmo.contraindications.dto.ActiveSubstanceDto;
import com.itmo.contraindications.models.ActiveSubstance;

public interface ActiveSubstanceService {
    ActiveSubstance findById(Integer id);

    ActiveSubstance add(ActiveSubstanceDto activeSubstanceDto);
}
