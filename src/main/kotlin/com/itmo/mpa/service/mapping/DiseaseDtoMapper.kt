package com.itmo.mpa.service.mapping

import com.itmo.mpa.dto.request.DiseaseRequest
import com.itmo.mpa.dto.response.DiseaseResponse
import com.itmo.mpa.entity.Disease
import com.itmo.mpa.translation.translate

fun DiseaseRequest.toEntity() = Disease().also {
    it.name = this.name!!
}

fun Disease.toResponse() =
        apply {
            translate(this, "name")
        }
        .run {
            DiseaseResponse(
                    id = this.id,
                    name = this.name
            )
        }

