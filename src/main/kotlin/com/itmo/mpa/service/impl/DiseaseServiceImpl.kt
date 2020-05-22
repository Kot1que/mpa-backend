package com.itmo.mpa.service.impl

import com.itmo.mpa.dto.request.DiseaseRequest
import com.itmo.mpa.dto.response.DiseaseResponse
import com.itmo.mpa.dto.response.MedicineResponse
import com.itmo.mpa.entity.Disease
import com.itmo.mpa.repository.DiseaseRepository
import com.itmo.mpa.service.DiseaseService
import com.itmo.mpa.service.impl.entityservice.DiseaseEntityService
import com.itmo.mpa.service.mapping.toEntity
import com.itmo.mpa.service.mapping.toResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import java.net.URI

@Service
class DiseaseServiceImpl(
    private val diseaseRepository: DiseaseRepository,
    private val diseaseEntityService: DiseaseEntityService
) : DiseaseService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun createDisease(diseaseRequest: DiseaseRequest) {
        logger.info("createDisease: Create a new disease from request: {}", diseaseRequest)
        val disease = diseaseRepository.save(diseaseRequest.toEntity())

        this.addDiseaseToContraindicationsService(disease)
    }

    fun addDiseaseToContraindicationsService(disease: Disease) {
        logger.info("Making request to add disease to contraindications service: $disease")

        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val json = JSONObject()
        json.put("id", disease.id)
        json.put("name", disease.name)

        val httpEntity = HttpEntity<String>(json.toString(), headers)
        restTemplate.postForObject("http://contraindications:8080/disease", httpEntity, String::class.java)
    }

    override fun getMedicineByDiseaseId(diseaseId: Long): List<MedicineResponse> {
        logger.info("getMedicineByDiseaseId: Query medicine for disease from database")
        return diseaseEntityService.findDisease(diseaseId).medicines
                .map { it.toResponse() }
                .also { logger.debug("getMedicineByDiseaseId {} returned {}", diseaseId, it) }
    }

    override fun getAll(): List<DiseaseResponse> {
        logger.info("getAll: Query all patients from the database")
        return diseaseRepository.findAll().map { it.toResponse() }
                .also { logger.debug("getAll returned {}", it) }
    }
}
