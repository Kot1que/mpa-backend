package com.itmo.mpa.repository

import com.itmo.mpa.entity.Translation
import org.springframework.data.repository.CrudRepository
import org.springframework.lang.Nullable

interface TranslationRepository : CrudRepository<Translation, Long> {
    @Nullable
    fun findByKeyNameAndLanguageAndEntityId(keyName: String, language: String, entityId: Long): Translation

    fun removeByKeyNameAndLanguageAndEntityId(keyName: String, language: String, entityId: Long)
}