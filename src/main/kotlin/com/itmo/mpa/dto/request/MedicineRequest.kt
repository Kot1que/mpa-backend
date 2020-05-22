package com.itmo.mpa.dto.request

import javax.validation.constraints.NotEmpty

data class MedicineRequest (
        @field:NotEmpty
        val name: String?,

        val activeSubstances: Set<ActiveSubstanceRequest>?
)