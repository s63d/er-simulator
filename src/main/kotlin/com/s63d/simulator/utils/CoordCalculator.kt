package com.s63d.simulator.utils

import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.geo.GeoJsonPoint

/**
 * returns distance between 2 points
 */
fun distance(c1: GeoJsonPoint, c2: GeoJsonPoint): Double {
    val theta = c1.x - c2.x
    var dist = Math.sin(deg2rad(c1.y)) * Math.sin(deg2rad(c2.y)) + Math.cos(deg2rad(c1.y)) * Math.cos(deg2rad(c2.y)) * Math.cos(deg2rad(theta))
    dist = Math.acos(dist)
    dist = rad2deg(dist)
    dist *= 60.0 * 1.1515
    return Math.round(dist * 1.609344 * 1000).toDouble()
}
fun distance(c1: Point, c2: Point) = distance(GeoJsonPoint(c1.x, c1.y), GeoJsonPoint(c2.x, c2.y))

private fun deg2rad(deg: Double)=  deg * Math.PI / 180.0

private fun rad2deg(rad: Double) = rad * 180 / Math.PI

operator fun Point.plus(v: Float) = Point(x + v, y + v)
operator fun Point.minus(v: Float) = Point(x - v, y - v)
operator fun Point.times(v: Float) = Point(x * v, y * v)
operator fun Point.div(v: Float) = Point(x / v, y / v)

operator fun Point.plus(v: Point) = Point(x + v.x, y + v.y)
operator fun Point.minus(v: Point) = Point(x - v.x, y - v.y)
operator fun Point.times(v: Point) = Point(x * v.x, y * v.y)
operator fun Point.div(v: Point) = Point(x / v.x, y / v.y)