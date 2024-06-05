package h05;

import java.util.Stack;

public class CombinedPlane extends PassengerPlane implements CarriesCargo{

    private final Stack<Integer> freight = new Stack<>();


    public CombinedPlane(String aircraftRegistration, int baseWeight, FuelType fuelType, double maxFuelLevel, int crew) {
        super(aircraftRegistration, baseWeight, fuelType, maxFuelLevel, crew);
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

        return super.mass() + sumOfFreight;
    }
}
