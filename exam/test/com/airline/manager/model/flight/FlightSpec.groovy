package com.airline.manager.model.flight

import com.airline.manager.model.seat.Seat
import spock.lang.Specification

class FlightSpec extends Specification {

	def "should reserve available seat"() {
		given:
			Flight flight = FlightBuilder.aFlight().addSeats(Grade.ECONOMY).createFlight()
		when:
			Seat reservedSeat = flight.reserveSeat(Grade.ECONOMY)
		then:
			reservedSeat.status == Seat.Status.RESERVED
	}
}
