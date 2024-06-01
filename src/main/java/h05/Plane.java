package h05;

public abstract class Plane implements Flying {

    private final String  aircraftRegistration;

    protected final int baseWeight;

    private double currentFuelLevel;

    private final FuelType fuelType;
    private final double fuelCapacity;

    public Plane(String aircraftRegistration, int baseWeight, FuelType fuelType, double fuelCapacity){
        this.aircraftRegistration = aircraftRegistration;
        this.baseWeight = baseWeight;
        this.fuelType = fuelType;
        this.currentFuelLevel = 0;
        this.fuelCapacity = fuelCapacity;
    }


    protected abstract double mass();

    protected double getFuelConsumptionPerKilometer(){
        return mass() * fuelType.getConsumptionMultiplicator();
    }

    public void takeOff(){
        Airspace.get().register(this);
    }

    public void land(){
        Airspace.get().deregister(this);
    }

    public double fly(double distance){

        // if the plane does not have enough fuel to fly the distance, it will not fly
        if(!hasEnoughFuel(distance)){
            return 0;
        }
//        double intendedConsumption = distance * getFuelConsumptionPerKilometer();
//
//        double possibleConsumption = Math.min(intendedConsumption, currentFuelLevel);
//        currentFuelLevel -= possibleConsumption;
        currentFuelLevel -= distance * getFuelConsumptionPerKilometer();
        //returns remaining fuel after the flight

        return currentFuelLevel;
    }

    public boolean hasEnoughFuel(double distance){
        return currentFuelLevel >= distance * getFuelConsumptionPerKilometer();
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public double getFuelCapacity() {
        return fuelCapacity;
    }

    public double getCurrentFuelLevel() {
        return currentFuelLevel;
    }

    public void refuel(double amount){
        currentFuelLevel += amount;
    }

    @Override
    public String getIdentifier(){
        return aircraftRegistration;
    }
}
