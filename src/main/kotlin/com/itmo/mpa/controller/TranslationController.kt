package com.itmo.mpa.controller

import com.itmo.mpa.dto.request.TranslationRequest
import com.itmo.mpa.dto.response.TranslationResponse
import com.itmo.mpa.service.TranslationService
import com.itmo.mpa.service.exception.NotFoundException
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@Api("/translations")
@RequestMapping("/translations")
class TranslationController (
        private val translationService: TranslationService
) {
    @PostMapping
    @ApiOperation("Creates a translation for given entity and language.")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
            @Valid @RequestBody translationRequest: TranslationRequest
    ): TranslationResponse = translationService.addTranslation(translationRequest)

    @PutMapping
    @ApiOperation("Updates the translation")
    @ResponseStatus(HttpStatus.OK)
    fun update(
            @Valid @RequestBody translationRequest: TranslationRequest
    ): TranslationResponse = translationService.addTranslation(translationRequest)

    @GetMapping("{id}")
    @ApiOperation("Get translation with given id")
    @ResponseStatus(HttpStatus.OK)
    fun get(
            @PathVariable id: Long
    ): TranslationResponse = translationService.getTranslation(id) ?: throw NotFoundException("Translation not found")

    @DeleteMapping("{id}")
    @ApiOperation("Deletes the translation")
    @ResponseStatus(HttpStatus.OK)
    fun delete(
            @PathVariable id: Long
    ) = translationService.deleteTranslation(id)
}