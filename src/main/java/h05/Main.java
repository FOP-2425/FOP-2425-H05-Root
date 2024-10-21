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
        Airspace airspace = Airspace.get();
        airspace.scanAirspace();

        /*
        Die konkreten Zahlen sind noch nicht final und können sich ändern.
         */
        Runway runway01 = new Runway(2000);
        Runway runway02 = new Runway(4000);

        WeatherBalloon weatherBalloon = new WeatherBalloon(99);
        weatherBalloon.start();

        Tank jetATank = new Tank(FuelType.JetA);
        Tank jetBTank = new Tank(FuelType.JetB);
        TankerPlane tankerPlane = new TankerPlane("D-ABCD", 10000, FuelType.JetA, 1000);
        tankerPlane.loadFuel(FuelType.AvGas, 100000);
        tankerPlane.loadFuel(FuelType.Biokerosin, 100000);

        PassengerPlane passengerPlane = new PassengerPlane("GAG-67", 10000, FuelType.JetA, 1700, 5);
        jetATank.refuel(passengerPlane);
        passengerPlane.board(100);
        passengerPlane.takeOff();

        airspace.scanAirspace();

        CargoPlane cargoPlane = new CargoPlane("D-AFFF", 8000, FuelType.JetB, 1500);
        cargoPlane.loadContainer(1000);
        jetBTank.refuel(cargoPlane);

        passengerPlane.disembark();

        airspace.scanAirspace();

        cargoPlane.takeOff();
        cargoPlane.fly(1000);

        airspace.scanAirspace();

        CombinedPlane combinedPlane = new CombinedPlane("D-ABBB", 9000, FuelType.AvGas, 10700, 5);
        tankerPlane.refuel(combinedPlane);

        combinedPlane.board(30);
        combinedPlane.loadContainer(400);
        combinedPlane.takeOff();
        combinedPlane.fly(3000);
        airspace.scanAirspace();

        runway01.land(combinedPlane);
        runway02.land(cargoPlane);

        airspace.scanAirspace();

        weatherBalloon.pop();

        airspace.scanAirspace();
    }
}
