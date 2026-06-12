package com.airline.manager.model.flight;

import com.airline.manager.model.seat.Seat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FlightBuilder {

	private String flightNo;
	private String origin;
	private String destination;
	private HashMap<Grade, Set<Seat>> seats = new HashMap<>();

	public static FlightBuilder aFlight() {
		return new FlightBuilder();
	}

	public FlightBuilder withFlightNo(String flightNo) {
		this.flightNo = flightNo;
		return this;
	}

	public FlightBuilder withOrigin(String origin) {
		this.origin = origin;
		return this;
	}

	public FlightBuilder withDestination(String destination) {
		this.destination = destination;
		return this;
	}

	public FlightBuilder addSeats(Grade grade) {
		var seatsForGrade = new HashSet<Seat>();
		seatsForGrade.add(new Seat("1A"));
		seatsForGrade.add(new Seat("1B"));
		seats.put(grade, seatsForGrade);
		return this;
	}

	public Flight createFlight() {
		return new Flight(flightNo, origin, destination, seats);
	}
}