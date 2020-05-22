package com.itmo.mpa.service.impl

import com.itmo.mpa.dto.request.MedicineRequest
import com.itmo.mpa.dto.response.ActiveSubstanceResponse
import com.itmo.mpa.dto.response.AppropriateMedicineResponse
import com.itmo.mpa.entity.Contradiction
import com.itmo.mpa.entity.Patient
import com.itmo.mpa.entity.Status
import com.itmo.mpa.entity.medicine.ActiveSubstance
import com.itmo.mpa.entity.medicine.Medicine
import com.itmo.mpa.repository.ContradictionsRepository
import com.itmo.mpa.repository.MedicineRepository
import com.itmo.mpa.service.MedicineService
import com.itmo.mpa.service.PredicateService
import com.itmo.mpa.service.impl.entityservice.PatientStatusEntityService
import com.itmo.mpa.service.mapping.toEntity
import com.itmo.mpa.service.mapping.toResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.lang.RuntimeException

@Service
class MedicineServiceImpl(
    private val patientStatusEntityService: PatientStatusEntityService,
    private val contradictionsRepository: ContradictionsRepository,
    private val predicateService: PredicateService,
    private val medicineRepository: MedicineRepository
) : MedicineService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun getAppropriateMedicine(patientId: Long): List<AppropriateMedicineResponse> {
        val (draft, patient) = patientStatusEntityService.requireDraftWithPatient(patientId)
        return contradictionsRepository.findAll()
                .groupBy { it.medicine.id }
                .map { (_, contradictions) -> contradictions.formResponse(patient, draft) }
                .also { logger.debug("getAvailableTransitions: result {}", it) }
    }

    override fun add(medicineRequest: MedicineRequest) {
        var medicine = Medicine()
        medicine.name = medicineRequest.name!!

        medicine.activeSubstance = medicineRequest.activeSubstances?.map {
            it.toEntity()
        }?.toSet() ?: HashSet()

        medicine = this.medicineRepository.save(medicine)

        this.addMedicineToContraindicationsService(medicine)
    }

    private fun addMedicineToContraindicationsService(medicine: Medicine) {
        logger.info("Making request to add medicine to contraindications service: $medicine")

        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val json = JSONObject()
        json.put("id", medicine.id)
        json.put("name", medicine.name)

        val activeSubstances = HashSet<JSONObject>()
        val activeSubstancesArray = JSONArray()

        medicine.activeSubstance.forEach {
            val dto = it.toResponse()
            val activeSubstanceJsonObject = JSONObject()

            activeSubstanceJsonObject.put("id", dto.id)
            activeSubstanceJsonObject.put("name", dto.name)

            activeSubstancesArray.put(activeSubstanceJsonObject)
//            activeSubstances.add(activeSubstanceJsonObject)
        }

        json.put("activeSubstances", activeSubstancesArray)

        logger.info("Serialized medicine json:\n $json")

        val httpEntity = HttpEntity<String>(json.toString(), headers)
        restTemplate.postForObject("http://contraindications:8080/medicine", httpEntity, String::class.java)
    }

    private fun List<Contradiction>.formResponse(
        patient: Patient,
        draft: Status
    ): AppropriateMedicineResponse {
        val medicineResponse = this.first().medicine.toResponse()
        val notRecommendedResults = this.map { it.isNotRecommended(patient, draft) }
        val notRecommendedList = notRecommendedResults.map { it.first }
        val errors = notRecommendedResults.mapNotNull { it.second }
        val isNotRecommended = when {
            errors.isNotEmpty() -> null
            else -> notRecommendedList.contains(true)
        }
        val errorsList = when {
            errors.isNotEmpty() -> errors
            else -> null
        }
        return AppropriateMedicineResponse(medicineResponse, isNotRecommended, errorsList)
    }

    private fun Contradiction.isNotRecommended(
        patient: Patient,
        draft: Status
    ): Pair<Boolean?, String?> {
        return predicateService.testPredicate(patient, draft, predicate)
                .mapLeft { it.toString() }
                .fold({ null to it }, { it to null })
    }


}
