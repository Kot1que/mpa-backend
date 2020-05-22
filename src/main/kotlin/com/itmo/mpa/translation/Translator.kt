package com.itmo.mpa.translation

import com.itmo.mpa.entity.LongIdEntity
import com.itmo.mpa.service.TranslationService
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.annotation.PostConstruct
import kotlin.reflect.KClass

private lateinit var translator: Translator

fun <T: LongIdEntity> LongIdEntity.translate(targetObject: T, vararg fields: String) {
    val translationService = translator.translationService
    val servletRequest = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
    val language = servletRequest.request.getHeader(HttpHeaders.ACCEPT_LANGUAGE) ?: return
    val targetObjectClassName = targetObject.javaClass.simpleName

    println("Class name: $targetObjectClassName")
    for (field in fields) {
        val fieldForReplace = targetObject.javaClass.getField(field)

        val key = "$targetObjectClassName.$field"
        val id: Long = this.id

        println("Trying: $key $id $language")
        val translation = translationService.getTranslationRaw(key, id, language) ?: continue
        val value = translation.value
        ReflectionUtils.setField(fieldForReplace, targetObject, value)
    }
}

@Component
class Translator(
        val translationService: TranslationService
) {
    @PostConstruct
    private fun init() {
        translator = this
    }
}