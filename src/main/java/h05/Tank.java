package h05;

public class Tank implements Refuelling{

    public enum RefuellingMode {
        FULL, SHOT, AMOUNT;
    }

    private final FuelType fuelType;
    private RefuellingMode refuellingMode = RefuellingMode.FULL;

    public Tank(FuelType fuelType){
        this.fuelType = fuelType;
    }

    @Override
    public FuelType getFuelType() {
        return fuelType;
    }

    public void selectRefuellingMode(RefuellingMode refuellingMode){
        this.refuellingMode = refuellingMode;
    }

    @Override
    public void refuel(Plane plane, double amount) {
        if(plane.getFuelType() != fuelType){
            return;
        }

        double missingFuel = plane.getFuelCapacity() - plane.getCurrentFuelLevel();
        if(refuellingMode == RefuellingMode.FULL){
            plane.refuel(missingFuel);
        } else if (refuellingMode == RefuellingMode.SHOT) {
            double shotAmount = 0.05 * plane.getFuelCapacity();

            if(shotAmount < missingFuel){
                plane.refuel(shotAmount);
            }
        } else  { // refuellingMode == RefuellingMode.AMOUNT
            double actualAmount = Math.min(amount, missingFuel);
            plane.refuel(actualAmount);
        }
    }
}
