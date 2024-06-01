package h05;

import java.util.Stack;

public class CargoPlane extends Plane implements CarriesCargo{

/*
    Stacks are a type of data structure that follows the Last In First Out (LIFO) principle.
    Explanation will be found in Task description.
 */
    private final Stack<Integer> freight = new Stack<>();

    public CargoPlane(String aircraftRegistration, int baseWeight, FuelType fuelType, double maxFuelLevel) {
        super(aircraftRegistration, baseWeight, fuelType, maxFuelLevel);
    }

    @Override
    public void loadContainer(int cargoWeight) {
        freight.push(cargoWeight);
    }

    @Override
    public boolean hasFreightLoaded() {
        return !freight.empty();
    }

    @Override
    public int unloadNextContainer() {
        return freight.pop();
    }

    @Override
    protected double mass() {
        int sumOfFreight = 0;
        for(int f : freight){
            sumOfFreight += f;
        }


        return baseWeight + sumOfFreight;
    }
}
