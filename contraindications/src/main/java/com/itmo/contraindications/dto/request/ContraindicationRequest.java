package com.itmo.contraindications.dto.request;

import lombok.Data;

@Data
public class ContraindicationRequest {
    private String description;

    private String severity;

    private String frequency;

    private String food;

    private Integer minAge;

    private Integer maxAge;

    private String sex;

    private String infoSource;

    private Integer sourceId;

    private Integer targetId;

    private String type;
}
