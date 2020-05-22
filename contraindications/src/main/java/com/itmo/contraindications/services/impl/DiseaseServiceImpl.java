package com.itmo.contraindications.services.impl;

import com.itmo.contraindications.dto.DiseaseDto;
import com.itmo.contraindications.models.Disease;
import com.itmo.contraindications.repositories.DiseaseRepository;
import com.itmo.contraindications.services.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiseaseServiceImpl implements DiseaseService {
    private final DiseaseRepository diseaseRepository;

    @Autowired
    public DiseaseServiceImpl(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Override
    public Disease add(DiseaseDto diseaseDto) {
        Disease disease = new Disease();

        System.out.println(diseaseDto);

        disease.setId(diseaseDto.getId());
        disease.setName(diseaseDto.getName());

        return this.diseaseRepository.save(disease);
    }

    @Override
    public Disease getById(Integer id) {
        return this.diseaseRepository.findById(id).orElseThrow(() -> new RuntimeException("Disease not found"));
    }
}
