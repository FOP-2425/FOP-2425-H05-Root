package h05;

public class PassengerPlane extends Plane implements CarriesPassengers {

    protected static final char AVERAGE_PASSENGER_WEIGHT = 95;
    protected static final char AVERAGE_LUGGAGE_WEIGHT = 15;

    private int passengerCount = 0;

    private final int crewCount;

    public PassengerPlane(String aircraftRegistration, int baseWeight, FuelType fuelType, double maxFuelLevel, int crew) {
        super(aircraftRegistration, baseWeight, fuelType, maxFuelLevel);
        this.crewCount = crew;
    }

    @Override
    public void board(int passengerCount) {
        this.passengerCount += passengerCount;
    }

    @Override
    public void disembark() {
        this.passengerCount = 0;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    @Override
    protected double mass() {
        return baseWeight + passengerCount * AVERAGE_PASSENGER_WEIGHT + crewCount * AVERAGE_PASSENGER_WEIGHT + passengerCount * AVERAGE_LUGGAGE_WEIGHT;
    }
}
