package com.itmo.mpa.service

import com.itmo.mpa.dto.request.TranslationRequest
import com.itmo.mpa.dto.response.TranslationResponse
import com.itmo.mpa.entity.Translation

interface TranslationService {
    fun getTranslation(key: String, entityId: Long, language: String): TranslationResponse?

    fun getTranslationRaw(key: String, entityId: Long, language: String): Translation?

    fun getTranslation(id: Long): TranslationResponse?

    fun getTranslationRaw(id: Long): Translation?

    fun updateTranslation(request: TranslationRequest): TranslationResponse

    fun deleteTranslation(id: Long)

    fun addTranslation(request: TranslationRequest): TranslationResponse
}