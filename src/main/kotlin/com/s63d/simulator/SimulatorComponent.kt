package com.s63d.simulator

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class SimulatorComponent : CommandLineRunner {

    @Value("\${sleep:5000}")
    val SLEEP_TIME = 5000

    @Value("\${real-life:30}")
    val REAL_LIFE_TIME = 30

    @Value("\${trips:10}")
    val TRIPS_PER_CARTRACKER = 10

    @Value("\${carTrackers:}")
    val carTrackers = listOf<String>()



    override fun run(vararg args: String?) {
        println("I'm sleeping for $SLEEP_TIME")
        println("The cartrackers: $carTrackers")
    }
}