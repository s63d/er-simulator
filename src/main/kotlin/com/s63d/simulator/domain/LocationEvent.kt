package com.s63d.simulator.domain

import java.io.Serializable

data class LocationEvent(var tripId: Long, var lat: Double, var lon: Double) : Serializable