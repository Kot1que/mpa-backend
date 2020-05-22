package com.itmo.mpa.controller

import com.itmo.mpa.dto.request.MedicineRequest
import com.itmo.mpa.dto.response.MedicineResponse
import com.itmo.mpa.service.MedicineService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@Api("/medicines")
@RequestMapping("medicines")
class MedicineController (private val medicineService: MedicineService){
    @PostMapping
    @ApiOperation("Create new medicine")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody medicineRequest: MedicineRequest): Unit = medicineService.add(medicineRequest)
}