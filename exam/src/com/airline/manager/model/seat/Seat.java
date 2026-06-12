package com.airline.manager.model.seat;

import java.util.Objects;

public class Seat {

	private final String number;

	private Status status = Status.AVAILABLE;

	public Seat(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public Status getStatus() {
		return status;
	}

	public boolean isAvailable() {
		return status == Status.AVAILABLE;
	}

	public void reserve() {
		status = Status.RESERVED;
	}

	public enum Status {
		AVAILABLE, RESERVED
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Seat seat)) return false;
		return Objects.equals(number, seat.number) &&
				status == seat.status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(number, status);
	}

	@Override
	public String toString() {
		return "Seat{number='%s', status=%s}".formatted(number, status);
	}
}
