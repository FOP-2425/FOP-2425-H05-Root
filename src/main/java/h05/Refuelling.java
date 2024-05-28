package h05;

public interface Refuelling {

    FuelType getFuelType();

    void refuel(Plane plane, double amount);
}
