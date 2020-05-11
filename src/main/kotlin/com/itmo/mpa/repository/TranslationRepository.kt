package com.itmo.mpa.repository

import com.itmo.mpa.entity.Translation
import org.springframework.data.repository.CrudRepository

interface TranslationRepository : CrudRepository<Translation, Long> {
    fun findByKeyNameAndLanguageAndEntityId(keyName: String, language: String, entityId: Long): Translation
    fun removeByKeyNameAndLanguageAndEntityId(keyName: String, language: String, entityId: Long)
}