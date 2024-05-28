package h05;

import java.util.HashSet;
import java.util.Set;

public class Airspace {

    private static Airspace INSTANCE = null;


    public static Airspace get(){
        if(INSTANCE == null){
            INSTANCE = new Airspace();
        }
        return INSTANCE;
    }


    private final Set<Flying> flyingInAirspace = new HashSet<>();

    private Airspace(){

    }

    void register(Flying flying){
        flyingInAirspace.add(flying);
    }

    void deregister(Flying flying){
        flyingInAirspace.remove(flying);
    }


    void scanAirspace(){
        System.out.println("Scanning...");
        for (Flying flying : flyingInAirspace){
            System.out.print(flying.getIdentifier());

            if(flying instanceof PassengerPlane passengerPlane){
                System.out.print(" (" + passengerPlane.getPeopleCount() + " PAX)");
            }

            System.out.println();
        }
    }
}