package h05;

public class PassengerPlane extends Plane implements CarriesPeople{

    protected static final char AVERAGE_PASSENGER_WEIGHT = 95;
    private int peopleCount = 0;

    public PassengerPlane(String aircraftRegistration, int baseWeight, FuelType fuelType, double maxFuelLevel) {
        super(aircraftRegistration, baseWeight, fuelType, maxFuelLevel);
    }

    @Override
    public void board(int peopleCount) {
        this.peopleCount += peopleCount;
    }

    @Override
    public void embark(int peopleCount) {
        this.peopleCount -= peopleCount;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    @Override
    protected double mass() {
        return baseWeight + peopleCount * AVERAGE_PASSENGER_WEIGHT;
    }
}
