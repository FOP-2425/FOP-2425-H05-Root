package h05;

public class TankerPlane extends Plane implements Refuelling{


    private final double[] availableAmount = new double[FuelType.values().length];

    public TankerPlane(String aircraftRegistration, int baseWeight, FuelType fuelType, double maxFuelLevel) {
        super(aircraftRegistration, baseWeight, fuelType, maxFuelLevel);
    }


    public void loadFuel(FuelType fuelType, double amount){
        availableAmount[fuelType.ordinal()] += amount;
    }

    @Override
    protected double mass() {
        double totalAmount = 0;
        for (int i = 0; i < FuelType.values().length; i++){
            totalAmount += availableAmount[i];
        }

        return baseWeight + totalAmount;
    }


    @Override
    public void refuel(Plane plane) {
        double missingFuel = plane.getFuelCapacity() - plane.getCurrentFuelLevel();
        double actualAmount = Math.min(availableAmount[plane.getFuelType().ordinal()], missingFuel);
        availableAmount[plane.getFuelType().ordinal()] -= actualAmount;
        plane.refuel(actualAmount);
    }
}
