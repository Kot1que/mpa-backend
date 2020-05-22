package com.itmo.mpa.dto.request

import javax.validation.constraints.NotEmpty

data class ActiveSubstanceRequest (
        @field:NotEmpty
        val name: String?
)
