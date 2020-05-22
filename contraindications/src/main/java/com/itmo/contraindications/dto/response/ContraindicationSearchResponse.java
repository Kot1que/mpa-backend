package com.itmo.contraindications.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itmo.contraindications.dto.ContraindicationDto;
import lombok.Data;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContraindicationSearchResponse {
    private String maxSeverity;

    private Set<ContraindicationDto> contraindications;
}
