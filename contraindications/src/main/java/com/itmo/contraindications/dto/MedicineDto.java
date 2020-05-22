package com.itmo.contraindications.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDto {
    private Integer id;

    private String name;

    private Set<ContraindicationDto> contraindication;
}
