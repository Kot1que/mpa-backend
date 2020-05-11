package com.itmo.mpa.aspect

import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner
import org.slf4j.LoggerFactory
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import kotlin.reflect.full.declaredMemberProperties
import kotlin.system.exitProcess

@Component
class TranslationAnnotationPostInitVerifier {
    private val logger = LoggerFactory.getLogger(javaClass)

    /*
        Scans for all methods annotated with TranslationSupport annotation and verifies all fields provided in
        annotation that they are exist in the class of an annotated method.
     */
    @EventListener
    fun handleContextRefreshEvent(contextRefreshedEvent: ContextRefreshedEvent) {
        val reflections = Reflections("com.itmo.mpa", MethodAnnotationsScanner())
        val annotatedMethods = reflections.getMethodsAnnotatedWith(TranslationSupport::class.java)

        logger.debug("Started scanning fields for annotation.")
        var errorsExist = false

        for (annotatedMethod in annotatedMethods) {
            logger.debug("Method: ${annotatedMethod.name}")
            val annotation = annotatedMethod.getDeclaredAnnotation(TranslationSupport::class.java)
            val annotationFields = annotation.fields
            val declaredFields = annotation.entityClass.declaredMemberProperties.map{it.name}
            for (field in annotationFields) {
                if (!declaredFields.contains(field)) {
                    logger.error("Field $field is not declared in ${annotatedMethod.declaringClass.simpleName}")
                    errorsExist = true
                }
            }
        }

        if (errorsExist) {
            exitProcess(1);
        }
    }
}