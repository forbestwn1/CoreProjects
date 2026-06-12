package com.airline.manager.model.flight;

import com.airline.manager.model.seat.NoSeatAvailableException;
import com.airline.manager.model.seat.Seat;

import java.util.Set;

record Section(Set<Seat> seats) {

	public Seat reserveSeat() throws NoSeatAvailableException {
		Seat seat = seats.stream().filter(Seat::isAvailable).findFirst().orElseThrow(NoSeatAvailableException::new);
		seat.reserve();
		return seat;
	}

	public long countAvailableSeats() {
		return seats.stream().filter(Seat::isAvailable).count();
	}
}
