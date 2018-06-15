package com.s63d.simulator

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import kotlin.math.log

@Component
class SimulatorComponent : CommandLineRunner {
    private val logger = LoggerFactory.getLogger(this::class.java)!!

    @Value("\${sleep:5000}")
    val SLEEP_TIME = 5000

    @Value("\${real-life:30}")
    val REAL_LIFE_TIME = 30

    @Value("\${trips:10}")
    val TRIPS_PER_CARTRACKER = 10

    @Value("\${carTrackers:}")
    val carTrackers = listOf<String>()


    @Value("\${endpoint:https://api.ersols.online/v1}")
    val endpoint = ""

    private fun printConfig() {
        logger.info("")
        logger.info("")
        logger.info("Simulator config")
        logger.info("  CarTrackers: $carTrackers")
        logger.info("  Max trips for a cartracker: $TRIPS_PER_CARTRACKER")
        logger.info("  Real life seconds: $REAL_LIFE_TIME")
        logger.info("  API endpoint: $endpoint")
        logger.info("")
        logger.info("")
    }
    override fun run(vararg args: String?) {
        printConfig()
    }
}