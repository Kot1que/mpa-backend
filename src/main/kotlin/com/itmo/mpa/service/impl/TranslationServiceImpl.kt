package com.itmo.mpa.service.impl

import com.itmo.mpa.dto.request.TranslationRequest
import com.itmo.mpa.dto.response.TranslationResponse
import com.itmo.mpa.entity.Translation
import com.itmo.mpa.repository.TranslationRepository
import com.itmo.mpa.service.TranslationService
import com.itmo.mpa.service.exception.NotFoundException
import com.itmo.mpa.service.mapping.toResponse

import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class TranslationServiceImpl(
        private val translationRepository: TranslationRepository
) : TranslationService {
    override fun getTranslation(key: String, entityId: Long, language: String): TranslationResponse {
        return this.translationRepository.findByKeyNameAndLanguageAndEntityId(key, language, entityId).toResponse()
    }

    override fun getTranslationRaw(key: String, entityId: Long, language: String): Translation {
        return this.translationRepository.findByKeyNameAndLanguageAndEntityId(key, language, entityId)
    }

    override fun getTranslation(id: Long): TranslationResponse {
        return this.translationRepository.findById(id)
                .orElseThrow { NotFoundException("Translation not found") }.toResponse()
    }

    override fun getTranslationRaw(id: Long): Translation {
        return this.translationRepository.findById(id)
                .orElseThrow { NotFoundException("Translation not found") }
    }

    override fun updateTranslation(request: TranslationRequest): TranslationResponse {
        val translation = this.translationRepository.findById(request.id)
                .orElseThrow{ NotFoundException("Translation not found") }

        translation.keyName = request.keyName
        translation.entityId = request.entityId
        translation.language = request.language
        translation.value = request.value

        return this.translationRepository.save(translation).toResponse()
    }

    override fun deleteTranslation(id: Long) {
        this.translationRepository.deleteById(id)
    }

    override fun addTranslation(request: TranslationRequest): TranslationResponse {
        val translation = Translation()
        translation.value = request.value
        translation.language = request.language
        translation.entityId = request.entityId
        translation.keyName = request.keyName

        return this.translationRepository.save(translation).toResponse()
    }
}