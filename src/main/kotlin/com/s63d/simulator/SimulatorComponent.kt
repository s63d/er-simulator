package com.s63d.simulator

import com.s63d.simulator.repositories.RouteRepository
import com.s63d.simulator.utils.random
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.*

@Component
class SimulatorComponent(private val routeRepository: RouteRepository) : CommandLineRunner {
    private val logger = LoggerFactory.getLogger(this::class.java)!!

    @Value("\${sleep:5000}")
    val SLEEP_TIME = 5000

    @Value("\${real-life:30}")
    val REAL_LIFE_TIME = 30

    @Value("\${trips:10}")
    val TRIPS_PER_CARTRACKER = 10

    @Value("\${carTrackers:[]}")
    val carTrackers = arrayOf<String>()


    @Value("\${endpoint:https://api.ersols.online/v1}")
    val endpoint = ""

    private fun printConfig() {
        logger.info("")
        logger.info("")
        logger.info("Simulator config")
        logger.info("  CarTrackers: ${carTrackers.joinToString()}")
        logger.info("  Max trips for a cartracker: $TRIPS_PER_CARTRACKER")
        logger.info("  Real life seconds: $REAL_LIFE_TIME")
        logger.info("  Sleep time: $SLEEP_TIME")
        logger.info("  API endpoint: $endpoint")
        logger.info("")
        logger.info("")
    }
    override fun run(vararg args: String?) {
        printConfig()

        if(carTrackers.isEmpty()) {
            logger.error("No car trackers provided! Use the args --carTrackers=a,b,c")
            return
        }

        logger.info("Loading routes..")
        val routes = routeRepository.findAll()
        logger.info("Loaded ${routes.size} routes!")

        launch {
            for (carTracker in carTrackers) {
                val totalTrips = Random().nextInt(TRIPS_PER_CARTRACKER)
                logger.info("CarTracker: $carTracker")
                logger.info("  - Total trips: $totalTrips")
                for (i in 0 until totalTrips) {
                    async {
                        val route = routes.random()
                        logger.info("    - Route ${route.id}")
                        val task = SimulatorTask(route, carTracker)
                        task.loadTripId()
                        task.run()
                    }
                }
                logger.info("")
            }
        }

        Thread.currentThread().join()

    }
}