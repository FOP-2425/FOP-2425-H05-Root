package h05;

public class Tank implements Refuelling{

    private final FuelType fuelType;

    public Tank(FuelType fuelType){
        this.fuelType = fuelType;
    }

    @Override
    public void refuelPlane(Plane plane) {
        if(plane.getFuelType() != fuelType){
            System.out.println("Incompatible fuel types, not refuelling");
            return;
        }

        double missingFuel = plane.getFuelCapacity() - plane.getCurrentFuelLevel();
        plane.refuel(missingFuel);
    }
}
