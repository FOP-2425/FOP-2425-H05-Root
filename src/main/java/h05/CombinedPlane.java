package h05;


public class CombinedPlane extends PassengerPlane implements CarriesCargo{

    private final Stack containers = new Stack();


    public CombinedPlane(String aircraftRegistration, int baseWeight, FuelType fuelType, double maxFuelLevel, int crew) {
        super(aircraftRegistration, baseWeight, fuelType, maxFuelLevel, crew);
    }

    @Override
    public void loadContainer(int cargoWeight) {
        containers.push(cargoWeight);
    }

    @Override
    public boolean hasFreightLoaded() {
        return !containers.empty();
    }

    @Override
    public int unloadNextContainer() {
        return containers.pop();
    }

    @Override
    protected double mass() {
        return super.mass() + containers.getSum();
    }
}
