package h05;

public class Tank implements Refuelling{

    private final FuelType fuelType;

    public Tank(FuelType fuelType){
        this.fuelType = fuelType;
    }

    @Override
    public void refuel(Plane plane) {
        if(plane.getFuelType() != fuelType){
            return;
        }

        double missingFuel = plane.getFuelCapacity() - plane.getCurrentFuelLevel();
        plane.refuel(missingFuel);

    }
}
