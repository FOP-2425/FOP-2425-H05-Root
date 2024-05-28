package h05;

import java.util.Stack;

public class CombinedPlane extends PassengerPlane implements CarriesCargo{

    private final Stack<Character> freight = new Stack<>();


    public CombinedPlane(String aircraftRegistration, int baseWeight, FuelType fuelType, double maxFuelLevel) {
        super(aircraftRegistration, baseWeight, fuelType, maxFuelLevel);
    }

    @Override
    public void loadFreight(char cargoWeight) {
        freight.push(cargoWeight);
    }

    @Override
    public boolean containsMoreFreight() {
        return !freight.empty();
    }

    @Override
    public char unloadNextFreight() {
        return freight.pop();
    }

    @Override
    protected double mass() {
        int sumOfFreight = 0;
        for(char f : freight){
            sumOfFreight += f;
        }


        return super.mass() + sumOfFreight;
    }
}
