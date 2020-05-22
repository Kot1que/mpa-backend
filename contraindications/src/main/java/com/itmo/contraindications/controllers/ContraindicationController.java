package com.itmo.contraindications.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.itmo.contraindications.dto.mapper.ContraindicationMapper;
import com.itmo.contraindications.dto.request.ContraindicationSearchRequest;
import com.itmo.contraindications.dto.response.ContraindicationSearchResponse;
import com.itmo.contraindications.models.Contraindication;
import com.itmo.contraindications.services.ContraindicationService;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ContraindicationController {
    private final ContraindicationService contraindicationService;
    private final ContraindicationMapper contraindicationMapper;

    public ContraindicationController(
            ContraindicationService contraindicationService,
            ContraindicationMapper contraindicationMapper
    ) {
        this.contraindicationService = contraindicationService;
        this.contraindicationMapper = contraindicationMapper;
    }

    @GetMapping("/search")
    public ContraindicationSearchResponse search(
            @RequestBody ContraindicationSearchRequest request
    ) {
        Set<Contraindication> resultSet = this.contraindicationService.search(request);

        Contraindication.ContraindicationSeverity maxSeverity = this.contraindicationService.getMaxSeverity(resultSet);

        ContraindicationSearchResponse response = new ContraindicationSearchResponse();

        if (maxSeverity != null) {
            response.setMaxSeverity(maxSeverity.getReadable());
        }

        response.setContraindications(
                resultSet.stream().map(contraindicationMapper::toDto).collect(Collectors.toSet())
        );

        return response;
    }
}
