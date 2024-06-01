package h05;

/**
 * Main entry point in executing the program.
 */
public class Main {
    /**
     * Main entry point in executing the program.
     *
     * @param args program arguments, currently ignored
     */
    public static void main(String[] args) {

        WeatherBalloon weatherBalloon = new WeatherBalloon(99);
        weatherBalloon.start();

        Tank jetATank = new Tank(FuelType.JetA);
        Tank jetBTank = new Tank(FuelType.JetB);
        Tank avGasTank = new Tank(FuelType.AvGas);
        Tank biokerosinTank = new Tank(FuelType.Biokerosin);

        PassengerPlane passengerPlane = new PassengerPlane("GAG-67", 10000, FuelType.JetA, 1070000, 5);
        jetATank.refuel(passengerPlane, 1000000);
        passengerPlane.board(100);
        passengerPlane.takeOff();
        passengerPlane.land();

        Airspace.get().scanAirspace();

        passengerPlane.disembark();

        CargoPlane cargoPlane = new CargoPlane("D-AFFF", 8000, FuelType.JetB, 15000000);
        cargoPlane.loadContainer(1000);
        jetBTank.refuel(cargoPlane, 10000000);
        cargoPlane.takeOff();
        cargoPlane.fly(1000);

        CombinedPlane combinedPlane = new CombinedPlane("D-ABCD", 9000, FuelType.AvGas, 1070000, 5);

        avGasTank.refuel(combinedPlane, 1000000);
        combinedPlane.board(30);
        combinedPlane.loadContainer(400);
        combinedPlane.takeOff();
        combinedPlane.fly(3000);

        Airspace.get().scanAirspace();

        combinedPlane.disembark();

        combinedPlane.land();
        cargoPlane.land();

        Airspace.get().scanAirspace();

        weatherBalloon.pop();


        Airspace.get().scanAirspace();


    }
}
