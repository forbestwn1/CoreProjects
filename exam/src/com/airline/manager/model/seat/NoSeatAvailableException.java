package com.airline.manager.model.seat;

public final class NoSeatAvailableException extends Exception {

	public NoSeatAvailableException() {
		super("No seat available for reservation");
	}

	public NoSeatAvailableException(String message) {
		super(message);
	}
}
