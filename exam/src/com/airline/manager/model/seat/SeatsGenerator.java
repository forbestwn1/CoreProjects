package com.airline.manager.model.seat;

import java.util.HashSet;
import java.util.Set;

public class SeatsGenerator {

	private static final String[] seats = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
	
	public Set<Seat> createSeats(int rows, int seatsInRow, int firstRowNumber) {
		Set<Seat> out = new HashSet<>();
		
		int rowNum = firstRowNumber;
		for(int i=0; i<rows; i++) {
			for(int j=0; j<seatsInRow; j++) {
				out.add(new Seat(String.valueOf(rowNum)+seats[j]));
			}
			rowNum++;
		}
		
		return out;
	}
}
