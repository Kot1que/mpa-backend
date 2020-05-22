package com.itmo.contraindications.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContraindicationDto {
    private Integer id;

    private String description;

    private String severity;

    private String frequency;

    private String food;

    private Integer minAge;

    private Integer maxAge;

    private String sex;

    private String infoSource;

    private String source;

    private String target;

    private String type;
}
