package com.itmo.contraindications.services;

import com.itmo.contraindications.dto.request.ContraindicationRequest;
import com.itmo.contraindications.dto.request.ContraindicationSearchRequest;
import com.itmo.contraindications.models.Contraindication;

import java.util.Set;

public interface ContraindicationService {
    Set<Contraindication> search(ContraindicationSearchRequest request);

    Contraindication.ContraindicationSeverity getMaxSeverity(Set<Contraindication> resultSet);

    void add(ContraindicationRequest request);
}
