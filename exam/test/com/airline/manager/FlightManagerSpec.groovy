package com.airline.manager

import com.airline.manager.model.flight.Flight
import spock.lang.Specification

import static com.airline.manager.model.flight.FlightBuilder.aFlight

class FlightManagerSpec extends Specification {

	def "finds flight"() {
		given:
			FlightManager manager = new FlightManager()
			Flight flight = aFlight().withFlightNo("LO1533").createFlight()
		when:
			manager.addNewFlight(flight)
		then:
			manager.findFlight("LO1533").isPresent()
	}

}
