package com.itmo.contraindications.dto.request;

import lombok.Data;
import com.itmo.contraindications.dto.ActiveSubstanceDto;
import com.itmo.contraindications.dto.MedicineDto;
import com.itmo.contraindications.models.Disease;

import java.util.Set;

@Data
public class ContraindicationSearchRequest {
    private Integer age;

    private String sex;

    private Set<Disease> diseases;

    private Set<MedicineDto> medicines;

    private Set<ActiveSubstanceDto> activeSubstances;

    private Boolean extendedSearch;
}
