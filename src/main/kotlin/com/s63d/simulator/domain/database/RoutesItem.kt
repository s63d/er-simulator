package com.s63d.simulator.domain.database

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonLineString
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "routes")
data class RoutesItem(val summary: String = "",
                      val copyrights: String = "",

                      val legs: List<LegsItem>,
                      val bounds: GeoJsonPolygon,
                      val overviewPolyline: GeoJsonLineString) {
    @Id
    lateinit var id: ObjectId
}