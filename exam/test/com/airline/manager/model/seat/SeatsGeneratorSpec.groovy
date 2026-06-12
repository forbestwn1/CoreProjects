package com.airline.manager.model.seat

import spock.lang.Specification

class SeatsGeneratorSpec extends Specification {

    private SeatsGenerator seatsGenerator = new SeatsGenerator()

    def 'basic test'() {
        expect:
        seatsGenerator.createSeats(2, 4, 1) ==
                [new Seat("1A"), new Seat("1B"), new Seat("1C"), new Seat("1D"),
                 new Seat("2A"), new Seat("2B"), new Seat("2C"), new Seat("2D")] as Set
    }

}
