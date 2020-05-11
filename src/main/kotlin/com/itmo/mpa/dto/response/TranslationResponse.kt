package com.itmo.mpa.dto.response

data class TranslationResponse (
        val id: Long,
        val keyName: String,
        val language: String,
        val entityId: Long,
        val value: String
)

