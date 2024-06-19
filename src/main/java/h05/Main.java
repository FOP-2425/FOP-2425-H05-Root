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

        /*
        Die konkreten Zahlen sind noch nicht final und können sich ändern.
        TODO: Wie kann instanceof genutzt werden?
         */
        Runway runway01 = new Runway(2000);
        Runway runway02 = new Runway(4000);

        WeatherBalloon weatherBalloon = new WeatherBalloon(99);
        weatherBalloon.start();

        Tank jetATank = new Tank(FuelType.JetA);
        Tank jetBTank = new Tank(FuelType.JetB);
        TankerPlane tankerPlane = new TankerPlane("D-ABCD", 10000, FuelType.JetA, 1000000);
        tankerPlane.loadFuel(FuelType.AvGas, 100000);
        tankerPlane.loadFuel(FuelType.Biokerosin, 100000);

        PassengerPlane passengerPlane = new PassengerPlane("GAG-67", 10000, FuelType.JetA, 1070000, 5);
        jetATank.refuel(passengerPlane);
        passengerPlane.board(100);
        passengerPlane.takeOff();

        Airspace.get().scanAirspace();

        CargoPlane cargoPlane = new CargoPlane("D-AFFF", 8000, FuelType.JetB, 15000000);
        cargoPlane.loadContainer(1000);
        jetBTank.refuel(cargoPlane);

        passengerPlane.disembark();

        cargoPlane.takeOff();
        cargoPlane.fly(1000);

        CombinedPlane combinedPlane = new CombinedPlane("D-ABCD", 9000, FuelType.AvGas, 1070000, 5);
        tankerPlane.refuel(combinedPlane);

        combinedPlane.board(30);
        combinedPlane.loadContainer(400);
        combinedPlane.takeOff();
        combinedPlane.fly(3000);
        Airspace.get().scanAirspace();


        if(combinedPlane instanceof PassengerPlane){
            runway01.land(combinedPlane);
        }else {
            runway02.land(combinedPlane);
        }
        runway02.land(cargoPlane);

        Airspace.get().scanAirspace();
        weatherBalloon.pop();
        Airspace.get().scanAirspace();


    }
}
