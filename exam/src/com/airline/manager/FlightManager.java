package com.airline.manager;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.airline.manager.model.flight.Flight;

public class FlightManager {

	private Set<Flight> flights = new HashSet<>();

	public boolean addNewFlight(Flight flight) {
		return flights.add(flight);
	}

	public Optional<Flight> findFlight(String flightNo) {
		return flights.stream().filter(f->f.getFlightNo().equals(flightNo)).findFirst();
	}

}
