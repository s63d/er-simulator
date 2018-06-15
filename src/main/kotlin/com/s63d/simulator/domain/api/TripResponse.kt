package com.s63d.simulator.domain.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class TripResponse (val id: Long = 0)