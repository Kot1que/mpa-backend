package com.itmo.mpa.dto.request

data class TranslationRequest (
        val id: Long,
        val keyName: String,
        val language: String,
        val entityId: Long,
        val value: String
)