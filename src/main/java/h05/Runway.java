package h05;

public class Runway {

    private final int runwayLength;

    /*
    TODO: Man könnte sich noch überlegen, wie Flugzeugtypen, die hier landen können, festgesetzt werden
     */
    public Runway(int runwayLength){
        this.runwayLength = runwayLength;
    }

    public int getRunwayLength(){
        return runwayLength;
    }
    /*
    TODO: hier müsste man sich noch eine bessere Formel für die Landung überlegen
    */
    public static double calculateLandingDistance(double weight){
        return weight/40;
    }

    public boolean canLand(Plane plane){
        return calculateLandingDistance(plane.mass()) <= runwayLength;
    }

    /*
    TODO: Man könnte auch stattdessen die land() methode in plane erweitern und diese hier weglassen
     */
    public void land(Plane plane){
        if(canLand(plane)){
            plane.land();
            System.out.println("Plane " + plane.getIdentifier() + " has landed successfully.");
        } else {
            System.out.println("Plane " + plane.getIdentifier() + " could not land. The runway is too short.");
        }
    }


}
