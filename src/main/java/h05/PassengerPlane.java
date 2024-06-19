package h05;

public class PassengerPlane extends Plane implements CarriesPassengers {

    protected static final char AVERAGE_PEOPLE_WEIGHT = 95;
    protected static final char AVERAGE_LUGGAGE_WEIGHT = 15;

    private int passengerCount = 0;

    private final int crewCount;

    public PassengerPlane(String aircraftRegistration, int baseWeight, FuelType fuelType, double fuelCapacity, int crewCount) {
        super(aircraftRegistration, baseWeight, fuelType, fuelCapacity);
        this.crewCount = crewCount;
    }

    @Override
    public void board(int passengerCount) {
        this.passengerCount += passengerCount;
    }

    @Override
    public void disembark() {
        this.passengerCount = 0;
    }

    @Override
    public int getPassengerCount() {
        return passengerCount;
    }

    @Override
    protected double mass() {
        return baseWeight + (passengerCount + crewCount) * AVERAGE_PEOPLE_WEIGHT + passengerCount * AVERAGE_LUGGAGE_WEIGHT;
    }
}
