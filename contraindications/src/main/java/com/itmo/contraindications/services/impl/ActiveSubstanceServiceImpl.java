package com.itmo.contraindications.services.impl;

import com.itmo.contraindications.dto.ActiveSubstanceDto;
import com.itmo.contraindications.models.ActiveSubstance;
import com.itmo.contraindications.repositories.ActiveSubstanceRepository;
import com.itmo.contraindications.services.ActiveSubstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActiveSubstanceServiceImpl implements ActiveSubstanceService {
    private final ActiveSubstanceRepository activeSubstanceRepository;

    @Autowired
    public ActiveSubstanceServiceImpl(ActiveSubstanceRepository activeSubstanceRepository) {
        this.activeSubstanceRepository = activeSubstanceRepository;
    }

    @Override
    public ActiveSubstance findById(Integer id) {
        return this.activeSubstanceRepository.findById(id).orElseThrow(() -> new RuntimeException("Active substance not found"));
    }

    @Override
    public ActiveSubstance add(ActiveSubstanceDto activeSubstanceDto) {
        ActiveSubstance activeSubstance = new ActiveSubstance();

        activeSubstance.setId(activeSubstanceDto.getId());
        activeSubstance.setName(activeSubstanceDto.getName());

        return this.activeSubstanceRepository.save(activeSubstance);
    }
}
