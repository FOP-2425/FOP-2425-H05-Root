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

        PassengerPlane passengerPlane = new PassengerPlane("GAG-67", 10000, FuelType.JetA, 1070000, 5);
        passengerPlane.board(100);
        passengerPlane.takeOff();
        passengerPlane.land();
        Airspace.get().scanAirspace();
        passengerPlane.disembark(100);

        CargoPlane cargoPlane = new CargoPlane("D-AFFF", 8000, FuelType.JetB, 1500000);
        cargoPlane.loadContainer(1000);

    }
}
