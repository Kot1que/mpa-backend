package com.itmo.contraindications.services.impl;

import com.itmo.contraindications.dto.request.ContraindicationRequest;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Service;
import com.itmo.contraindications.dto.request.ContraindicationSearchRequest;
import com.itmo.contraindications.models.ActiveSubstance;
import com.itmo.contraindications.models.Contraindication;
import com.itmo.contraindications.models.Disease;
import com.itmo.contraindications.models.Medicine;
import com.itmo.contraindications.repositories.ActiveSubstanceRepository;
import com.itmo.contraindications.repositories.ContraindicationRepository;
import com.itmo.contraindications.repositories.DiseaseRepository;
import com.itmo.contraindications.repositories.MedicineRepository;
import com.itmo.contraindications.services.ContraindicationService;

import java.util.*;
import java.util.stream.Collectors;

import static com.itmo.contraindications.models.Contraindication.ContraindicationType.*;

@Service
public class ContraindicationServiceImpl implements ContraindicationService
{
    private final ContraindicationRepository contraindicationRepository;
    private final MedicineRepository medicineRepository;
    private final DiseaseRepository diseaseRepository;
    private final ActiveSubstanceRepository activeSubstanceRepository;

    public ContraindicationServiceImpl(
            ContraindicationRepository contraindicationRepository,
            MedicineRepository medicineRepository,
            DiseaseRepository diseaseRepository,
            ActiveSubstanceRepository activeSubstanceRepository
    ) {
        this.contraindicationRepository = contraindicationRepository;
        this.medicineRepository = medicineRepository;
        this.diseaseRepository = diseaseRepository;
        this.activeSubstanceRepository = activeSubstanceRepository;
    }

    public Set<Contraindication> search(ContraindicationSearchRequest request) {
        Boolean extended = request.getExtendedSearch();

        Set<Contraindication> contraindications = this.findContraindications(request);

        if (extended != null && extended) {
            contraindications.addAll(this.findExtended(request));
            return this.processContraindications(contraindications, request, true);
        }

        return this.processContraindications(contraindications, request, false);
    }

    private Set<Contraindication> findContraindications(ContraindicationSearchRequest request) {
        Set<Disease> diseases = this.getDiseasesFromRequest(request);

        Set<Medicine> medicines = new HashSet<>();
        if (request.getMedicines() != null) {
            medicines = getMedicinesFromRequest(request);
        }

        Set<ActiveSubstance> activeSubstances = new HashSet<>();

        if (request.getActiveSubstances() != null) {
            activeSubstances = getActiveSubstancesFromRequest(request);
        }

        List<Contraindication> medicineResult = contraindicationRepository.findAlByMedicineSourceIsIn(medicines);
        List<Contraindication> activeSubstanceResult =
                contraindicationRepository.findAllByActiveSubstanceSourceIsIn(activeSubstances);
        List<Contraindication> diseaseResult = contraindicationRepository.findAllByDiseaseTargetIsIn(diseases);

        Set<Contraindication> results = new HashSet<>();
        results.addAll(medicineResult);
        results.addAll(activeSubstanceResult);
        results.addAll(diseaseResult);

        return results;
    }

    private Set<Medicine> getMedicinesFromRequest(ContraindicationSearchRequest request) {
        return request.getMedicines()
                .stream()
                .map(medicineDto -> this.medicineRepository.findById(medicineDto.getId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<ActiveSubstance> getActiveSubstancesFromRequest(ContraindicationSearchRequest request) {
        return request.getActiveSubstances()
                .stream()
                .map(activeSubstanceDto -> this.activeSubstanceRepository
                        .findById(activeSubstanceDto.getId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<Disease> getDiseasesFromRequest(ContraindicationSearchRequest request) {
        return request.getDiseases()
                .stream()
                .map(disease -> this.diseaseRepository.findById(disease.getId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<Contraindication> processContraindications(
            Set<Contraindication> contraindications,
            ContraindicationSearchRequest request,
            boolean extended) {
        Integer age = request.getAge();
        Contraindication.ContraindicationSex sex = null;

        Set<Disease> diseases = request.getDiseases();
        Set<ActiveSubstance> activeSubstances = this.getActiveSubstancesFromRequest(request);
        Set<Medicine> medicines = this.getMedicinesFromRequest(request);

        if (extended) {
            medicines.forEach(medicine -> {
                activeSubstances.addAll(medicine.getActiveSubstances());
            });
        }

        if (request.getSex() != null) {
            sex = Contraindication.ContraindicationSex.valueOf(request.getSex());
        }

        Set<Contraindication> resultSet = new HashSet<>();

        for (Contraindication contraindication : contraindications) {
            if (contraindication.getActiveSubstanceTarget() != null
                    && activeSubstances.contains(contraindication.getActiveSubstanceTarget())
            ) {
                resultSet.add(contraindication);
            }

            if (contraindication.getMedicineTarget() != null
                    && medicines.contains(contraindication.getMedicineTarget())
            ) {
                resultSet.add(contraindication);
            }

            if (contraindication.getDiseaseTarget() != null
                    && diseases.contains(contraindication.getDiseaseTarget())
            ) {
                if (contraindication.getMedicineSource() != null) {
                    if (medicines.contains(contraindication.getMedicineSource())) {
                        resultSet.add(contraindication);
                    }
                }

                if (contraindication.getActiveSubstanceSource() != null) {
                    if (activeSubstances.contains(contraindication.getActiveSubstanceSource())) {
                        resultSet.add(contraindication);
                    }
                }
            }

            if (contraindication.getSex() != null && contraindication.getSex().equals(sex)) {
                resultSet.add(contraindication);
            }

            if (contraindication.getFood() != null) {
                resultSet.add(contraindication);
            }

            if (contraindication.getMinAge() != null || contraindication.getMaxAge() != null) {
                if (contraindication.getMinAge() == null) {
                    if (age > contraindication.getMaxAge()) {
                        resultSet.add(contraindication);
                    }
                } else {
                    if (age < contraindication.getMinAge()) {
                        resultSet.add(contraindication);
                    }
                }
            }
        }

        return resultSet;
    }

    private Set<Contraindication> findExtended(ContraindicationSearchRequest request) {
        Set<Medicine> medicines = this.getMedicinesFromRequest(request);
        Set<ActiveSubstance> activeSubstances = new HashSet<>();
        medicines.forEach(medicine -> activeSubstances.addAll(medicine.getActiveSubstances()));
        return new HashSet<>(this.contraindicationRepository.findAllByActiveSubstanceSourceIsIn(activeSubstances));
    }

    @Override
    public Contraindication.ContraindicationSeverity getMaxSeverity(Set<Contraindication> resultSet) {
        if (resultSet.size() == 0) {
            return null;
        }

        int max = resultSet.stream().mapToInt(
                value -> value.getSeverity() != null ? value.getSeverity().getSeverityNumber() : -1
        ).max().orElse(-1);

        if (max == -1) {
            return null;
        }

        return Contraindication.ContraindicationSeverity.getFromNumber(max);
    }

    @Override
    public void add(ContraindicationRequest request) {
        if (request.getSourceId() == null) {
            throw new RuntimeException("Source cannot be null");
        }

        Integer id = request.getSourceId();

        String type = request.getType();

        Contraindication contraindication = new Contraindication();

        try {
            Contraindication.ContraindicationType contraindicationType =
                    Contraindication.ContraindicationType.valueOf(type);

            contraindication.setType(contraindicationType);

        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new RuntimeException("Contraindication type unrecognized");
        }

        contraindication.setDescription(request.getDescription());
        contraindication.setFood(request.getFood());
        contraindication.setInfoSource(request.getInfoSource());
        contraindication.setMinAge(request.getMinAge());
        contraindication.setMaxAge(request.getMaxAge());

        try {
            Contraindication.ContraindicationSeverity severity =
                    Contraindication.ContraindicationSeverity.valueOf(request.getSeverity());

            contraindication.setSeverity(severity);
        } catch (IllegalArgumentException | NullPointerException ex) {
            if (request.getSeverity() != null)
                throw new RuntimeException("Severity unrecognized");
        }

        try {
            Contraindication.ContraindicationFrequency frequency =
                    Contraindication.ContraindicationFrequency.valueOf(request.getFrequency());

            contraindication.setFrequency(frequency);
        } catch (IllegalArgumentException | NullPointerException ex) {
            if (request.getFrequency() != null)
                throw new RuntimeException("Frequency unrecognized");
        }

        try {
            Contraindication.ContraindicationSex sex =
                    Contraindication.ContraindicationSex.valueOf(request.getSex());
            contraindication.setSex(sex);
        } catch (IllegalArgumentException | NullPointerException ex ) {
            if (request.getSex() != null)
                throw new RuntimeException("Sex unrecognized");
        }

        Medicine medicine;
        Disease disease;
        ActiveSubstance activeSubstance;

        switch (contraindication.getType()) {
            case MEDICINE_TO_DISEASE:
                if (request.getTargetId() == null) {
                    throw new RuntimeException("Target disease is required for this type");
                }

                medicine = this.medicineRepository.findById(request.getSourceId())
                        .orElseThrow(() -> new RuntimeException("Medicine with given id is not found"));

                disease = this.diseaseRepository.findById(request.getTargetId())
                        .orElseThrow(() -> new RuntimeException("Disease with given id is not found"));

                contraindication.setMedicineSource(medicine);
                contraindication.setDiseaseTarget(disease);

                break;
            case ACTIVE_SUBSTANCE_TO_DISEASE:
                if (request.getTargetId() == null) {
                    throw new RuntimeException("Target disease is required for this type");
                }

                activeSubstance = this.activeSubstanceRepository.findById(request.getSourceId())
                        .orElseThrow(() -> new RuntimeException("Active substance with given id is not found"));

                disease = this.diseaseRepository.findById(request.getTargetId())
                        .orElseThrow(() -> new RuntimeException("Disease with given id is not found"));

                contraindication.setActiveSubstanceSource(activeSubstance);
                contraindication.setDiseaseTarget(disease);

                break;
            case MEDICINE_TO_SEX:
                if (contraindication.getSex() == null) {
                    throw new RuntimeException("Sex is required for this type");
                }

                medicine = this.medicineRepository.findById(request.getSourceId())
                        .orElseThrow(() -> new RuntimeException("Medicine with given id is not found"));

                contraindication.setMedicineSource(medicine);
                break;
            case ACTIVE_SUBSTANCE_TO_SEX:
                activeSubstance = this.activeSubstanceRepository.findById(request.getSourceId())
                        .orElseThrow(() -> new RuntimeException("Active substance with given id is not found"));

                if (request.getSex() == null) {
                    throw new RuntimeException("Sex is required for this type");
                }

                contraindication.setActiveSubstanceSource(activeSubstance);

                break;
            case MEDICINE_TO_FOOD:
                medicine = this.medicineRepository.findById(request.getSourceId())
                        .orElseThrow(() -> new RuntimeException("Medicine with given id is not found"));

                if (request.getFood() == null) {
                    throw new RuntimeException("Food is required for this type");
                }

                contraindication.setMedicineSource(medicine);

                break;
            case ACTIVE_SUBSTANCE_TO_FOOD:
                activeSubstance = this.activeSubstanceRepository.findById(request.getSourceId())
                        .orElseThrow(() -> new RuntimeException("Active substance with given id is not found"));

                if (request.getFood() == null) {
                    throw new RuntimeException("Food is required for this type");
                }

                contraindication.setActiveSubstanceSource(activeSubstance);
                break;
            case MEDICINE_TO_AGE:
                medicine = this.medicineRepository.findById(request.getSourceId())
                        .orElseThrow(() -> new RuntimeException("Medicine with given id is not found"));

                if (request.getMinAge() == null && request.getMaxAge() == null) {
                    throw new RuntimeException("Age is required for this type");
                }

                contraindication.setMedicineSource(medicine);

                break;
            case ACTIVE_SUBSTANCE_TO_AGE:
                activeSubstance = this.activeSubstanceRepository.findById(request.getSourceId())
                        .orElseThrow(() -> new RuntimeException("Active substance with given id is not found"));

                if (request.getMinAge() == null && request.getMaxAge() == null) {
                    throw new RuntimeException("Age is required for this type");
                }

                contraindication.setActiveSubstanceSource(activeSubstance);

                break;
            case ACTIVE_SUBSTANCE_TO_ACTIVE_SUBSTANCE:
                activeSubstance = this.activeSubstanceRepository.findById(request.getSourceId())
                        .orElseThrow(() -> new RuntimeException("Active substance with given id is not found"));

                if (request.getTargetId() == null) {
                    throw new RuntimeException("Target active substance is required for this type");
                }

                ActiveSubstance targetActiveSubstance = this.activeSubstanceRepository.findById(request.getTargetId())
                        .orElseThrow(() -> new RuntimeException("Active substance with given id is not found"));

                contraindication.setActiveSubstanceSource(activeSubstance);
                contraindication.setActiveSubstanceTarget(targetActiveSubstance);

                break;
        }

        this.contraindicationRepository.save(contraindication);
    }
}
