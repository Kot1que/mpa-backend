package com.itmo.mpa.service.mapping

import com.itmo.mpa.dto.request.ActiveSubstanceRequest
import com.itmo.mpa.dto.request.DiseaseRequest
import com.itmo.mpa.dto.response.ActiveSubstanceResponse
import com.itmo.mpa.dto.response.DiseaseResponse
import com.itmo.mpa.entity.Disease
import com.itmo.mpa.entity.medicine.ActiveSubstance

fun ActiveSubstanceRequest.toEntity() = ActiveSubstance().also {
    it.name = this.name!!
}

fun ActiveSubstance.toResponse() = ActiveSubstanceResponse(
        id = this.id,
        name = this.name
)
