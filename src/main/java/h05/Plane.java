package h05;

public abstract class Plane implements Flying{

    private final String  aircraftRegistration;

    protected final int baseWeight;

    private double currentFuelLevel;

    private final FuelType fuelType;
    private final double maxFuelLevel;

    public Plane(String aircraftRegistration, int baseWeight, FuelType fuelType, double maxFuelLevel){
        this.aircraftRegistration = aircraftRegistration;
        this.baseWeight = baseWeight;
        this.fuelType = fuelType;
        this.currentFuelLevel = 0;
        this.maxFuelLevel = maxFuelLevel;
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
        double intendedConsumption = distance * getFuelConsumptionPerKilometer();

        double possibleConsumption = Math.min(intendedConsumption, currentFuelLevel);
        currentFuelLevel -= possibleConsumption;

        double actualDistance = possibleConsumption / getFuelConsumptionPerKilometer();
        return actualDistance;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public double getMaxFuelLevel() {
        return maxFuelLevel;
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
