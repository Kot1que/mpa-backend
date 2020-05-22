package com.itmo.contraindications.controllers;

import com.itmo.contraindications.dto.MedicineDto;
import com.itmo.contraindications.dto.mapper.MedicineMapper;
import com.itmo.contraindications.models.Medicine;
import com.itmo.contraindications.services.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicine")
public class MedicineController {
    private final MedicineService medicineService;
    private final MedicineMapper medicineMapper;

    @Autowired
    public MedicineController(MedicineService medicineService, MedicineMapper medicineMapper) {
        this.medicineService = medicineService;
        this.medicineMapper = medicineMapper;
    }

    @PostMapping
    public MedicineDto add(MedicineDto medicineDto) {
        Medicine medicine = this.medicineService.add(medicineDto);

        return this.medicineMapper.toDto(medicine);
    }

    @GetMapping("/{id}")
    public MedicineDto getById(@PathVariable Integer id) {
        Medicine medicine = this.medicineService.getById(id);

        if (medicine == null) {
            return null;
        }

        return this.medicineMapper.toDto(medicine);
    }
}
