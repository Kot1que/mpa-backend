package com.itmo.contraindications.controllers;

import com.itmo.contraindications.dto.DiseaseDto;
import com.itmo.contraindications.dto.MedicineDto;
import com.itmo.contraindications.dto.mapper.DiseaseMapper;
import com.itmo.contraindications.models.Disease;
import com.itmo.contraindications.models.Medicine;
import com.itmo.contraindications.services.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/disease")
public class DiseaseController {
    private final DiseaseService diseaseService;
    private final DiseaseMapper diseaseMapper;

    @Autowired
    public DiseaseController(DiseaseService diseaseService, DiseaseMapper diseaseMapper) {
        this.diseaseService = diseaseService;
        this.diseaseMapper = diseaseMapper;
    }

    @PostMapping
    public DiseaseDto add(@RequestBody DiseaseDto diseaseDto) {
        Disease disease = this.diseaseService.add(diseaseDto);

        return this.diseaseMapper.toDto(disease);
    }

    @GetMapping("/{id}")
    public DiseaseDto getById(@PathVariable Integer id) {
        Disease disease = this.diseaseService.getById(id);

        if (disease == null) {
            return null;
        }

        return this.diseaseMapper.toDto(disease);
    }
}
