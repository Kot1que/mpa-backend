package com.itmo.mpa.aspect

import com.itmo.mpa.entity.LongIdEntity
import com.itmo.mpa.service.TranslationService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class TranslationAspect(
        private val translationService: TranslationService
) {
    @Around("@annotation(translationSupport)")
    fun process(
            joinPoint: ProceedingJoinPoint,
            translationSupport: TranslationSupport
    ): Any {
        println("aspect")
        val servletRequest = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val language = servletRequest.request.getHeader(HttpHeaders.ACCEPT_LANGUAGE) ?: return joinPoint.proceed()

        val targetObject = joinPoint.target
        val targetObjectClassName = targetObject.javaClass.simpleName
        println("Class name: $targetObjectClassName")
        for (field in translationSupport.fields) {
            val fieldForReplace = targetObject.javaClass.getField(field)

            val key = "$targetObjectClassName.$field"
            var id: Long
            if (targetObject is LongIdEntity) {
                id = targetObject.id
            } else {
                throw RuntimeException("Class is not supported")
            }

            println("Trying: $key $id $language")
            val translation = translationService.getTranslationRaw(key, id, language) ?: continue
            val value = translation.value
            ReflectionUtils.setField(fieldForReplace, targetObject, value)
        }

        return joinPoint.proceed()
    }
}