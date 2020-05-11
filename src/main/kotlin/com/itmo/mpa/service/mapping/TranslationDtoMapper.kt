package com.itmo.mpa.service.mapping

import com.itmo.mpa.dto.request.TranslationRequest
import com.itmo.mpa.dto.response.TranslationResponse
import com.itmo.mpa.entity.Translation

fun TranslationRequest.toEntity() = Translation().also {
    it.id = id;
    it.entityId = entityId;
    it.keyName = keyName;
    it.language = language;
    it.value = value;
}

fun Translation.toResponse() = TranslationResponse(
        id = this.id,
        keyName = this.keyName,
        entityId = this.entityId,
        language = this.language,
        value = this.value
)