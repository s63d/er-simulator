package com.s63d.simulator.domain.api

import org.springframework.data.mongodb.core.geo.GeoJsonPoint

data class Coordinate (val lat:Double = 0.0, val lng:Double = 0.0) {
    fun toGeoJsonPoint() : GeoJsonPoint {
        return GeoJsonPoint(lng, lat)
    }
}