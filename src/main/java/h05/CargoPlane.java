package h05;

public class CargoPlane extends Plane implements CarriesCargo{

/*
    Stacks are a type of data structure that follows the Last In First Out (LIFO) principle.
    Explanation will be found in Task description.
 */
    private final CargoStack containers = new CargoStack();

    public CargoPlane(String aircraftRegistration, int baseWeight, FuelType fuelType, double maxFuelLevel) {
        super(aircraftRegistration, baseWeight, fuelType, maxFuelLevel);
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
        return baseWeight + containers.getSum();
    }
}
