package com.s63d.simulator

import com.s63d.simulator.domain.LocationEvent
import com.s63d.simulator.domain.api.TripResponse
import com.s63d.simulator.domain.database.RoutesItem
import com.s63d.simulator.utils.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.geo.Point
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import kotlin.math.roundToInt

class SimulatorTask(private val route: RoutesItem, private val carTracker: String) {

    companion object {
        val restTemplate = RestTemplate()
        val rabbitTemplate = RabbitTemplate()
    }
    @Value("\${real-life:30}")
    val REAL_LIFE_TIME = 30

    @Value("\${sleep:0}")
    val SLEEP_TIME = 0

    @Value("\${endpoint:https://api.ersols.online/v1}")
    val endpoint = ""


    private val logger = LoggerFactory.getLogger(this::class.java)
    private var tripId: Long = -1

    private var timeLeft = REAL_LIFE_TIME
    private lateinit var currentLocation: Point

    fun loadTripId() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val map = LinkedMultiValueMap<String, String>()
        map.add("carTrackerId", carTracker)
        val request = HttpEntity<MultiValueMap<String, String>>(map, headers)

        logger.info("Loading trip id for carTracker $carTracker")
        try {
            val response = restTemplate.postForObject("$endpoint/trips", request, TripResponse::class.java)!!
            logger.info("Loaded trip id ${response.id} for tracker $carTracker")
            tripId = response.id
        } catch(ex: Exception) {
            logger.error(ex.toString())
            throw ex
        }
    }


    suspend fun run() = launch {
        val steps = route.legs[0].steps
        logger.info("Starting route, duration: ${route.legs[0].duration.text}, distance: ${route.legs[0].distance.text}")
        currentLocation = steps[0].polyline.coordinates[0]

        for (step in steps) {
            val stepDuration = step.duration.value

            if (stepDuration <= timeLeft) {
                timeLeft -= stepDuration
                currentLocation = step.endLocation
                updateLocation()
                delay(SLEEP_TIME)
            } else {
                val metersASecond: Int = step.distance.value / step.duration.value

                val destinations = mutableListOf<Point>()
                destinations.addAll(step.polyline.coordinates.filterNot { it == currentLocation })
                while (destinations.isNotEmpty()) {
                    val dest = destinations[0]
                    val dist = distance(currentLocation, dest)
                    val timeTaken = (dist / metersASecond).roundToInt()

                    if (timeTaken <= timeLeft) {
                        timeLeft -= timeTaken
                        currentLocation = dest
                        destinations.removeAt(0)
                    } else {
                        val allowedDistance = timeLeft * metersASecond
                        val normalized = (dest - currentLocation) / dist.toFloat()
                        currentLocation += (normalized * allowedDistance.toFloat())
                        timeLeft = 0
                    }

                    if (timeLeft == 0) {
                        timeLeft = REAL_LIFE_TIME
                        updateLocation()
                        delay(SLEEP_TIME)
                    }
                }
            }
        }
        finishRoute()
        logger.info("Finished route")
    }

    private fun updateLocation() {
        val event = LocationEvent(tripId, currentLocation.y, currentLocation.x)
        rabbitTemplate.convertAndSend("AT", "location", event)

        if(logger.isDebugEnabled) {
            logger.debug("Sent location to exchange 'AT' with routing key 'location'")
            logger.debug(event.toString())
        }
    }

    private fun finishRoute() {

    }

}