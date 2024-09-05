package h06;

import org.sourcegrade.jagr.api.rubric.*;
import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.criterion;

public class H05_RubricProvider implements RubricProvider {

    private static final Criterion H5_1_1 = Criterion.builder()
        .shortDescription("H5.1.1 | Flying Interface")
        .maxPoints(1)
        .criterion("The Flying interface is implemented correctly.")
        .build();

    private static final Criterion H5_1_2 = Criterion.builder()
        .shortDescription("H5.1.2 | FuelType")
        .maxPoints(2)
        .addChildCriteria(
            criterion("The FuelType enum is implemented correctly and with the correct values.")
            criterion("The Constructor of the FuelType enum is implemented correctly.")
        )
        .build();

    private static final Criterion H5_1_3 = Criterion.builder()
        .shortDescription("H5.1.3 | Plane")
        .maxPoints(6)
        .addChildCriteria(
            criterion("The Interface is correctly implemented into the Plane class.")
            criterion("The Plane class has the correct attributes.")
            criterion("The mass method functions correctly.")
            criterion("Refuel prints the right message.")
            criterion("The getFuelConsumptionPerKilometer method is implemented correctly.")
            criterion("The fly and takeoff methods are implemented correctly.")
        )
        .build();

    private static final Criterion H5_1_4 = Criterion.builder()
        .shortDescription("H5.1.4 | Weather Balloon")
        .maxPoints(1)
        .addChildCriteria(
            criterion("THe WeatherBalloon class and its methods are implemented correctly.")
        )
        .build();

    private static final Criterion H5_2_1 = Criterion.builder()
        .shortDescription("H5.2.1 | CarriesPassengers Interface")
        .maxPoints(4)
        .addChildCriteria(
            criterion("The CarriesPassengers interface is implemented correctly.")
            criterion("The PassengerPlane class correctly implements the CarriesPassengers interface.")
            criterion("The board and disembark methods and getPassengerCount are implemented correctly.")
            criterion("The constants and constructor are implemented correctly.")
        )
        .build();

    private static final Criterion H5_2_2 = Criterion.builder()
        .shortDescription("H5.2.2 | CarriesCargo Interface")
        .maxPoints(4)
        .addChildCriteria(
            criterion("The CarriesCargo interface is implemented correctly.")
            criterion("The CargoPlane class correctly implements the CarriesCargo interface.")
            criterion("The mass method and constructor are implemented correctly.")
            criterion("The loadContainer, hasFreightLoaded and unloadNextContainer methods are implemented correctly.")
        )
        .build();

    private static final Criterion H5_2_3 = Criterion.builder()
        .shortDescription("H5.2.3 | CombinedPlane")
        .maxPoints(2)
        .addChildCriteria(
            criterion("The CombinedPlane class correctly implements the CarriesPassengers and CarriesCargo interfaces.")
            criterion("The class is completly correct.")
        )
        .build();

    private static final Criterion H5_3_1 = Criterion.builder()
        .shortDescription("H5.3.1 | Refuelling")
        .maxPoints(1)
        .criterion("The Refuelling Interface is implemented correctly.")
        .build();

    private static final Criterion H5_3_2 = Criterion.builder()
        .shortDescription("H5.3.2 | Tank")
        .maxPoints(2)
        .addChildCriteria(
            criterion("The Tank class is implemented correctly and with the right attributes.")
            criterion("The refuelPlane method is implemented correctly.")
        )
        .build();

    private static final Criterion H5_3_3 = Criterion.builder()
        .shortDescription("H5.3.3 | TankerPlane")
        .maxPoints(2)
        .addChildCriteria(
            criterion("The TankerPlane class implements the right interfaces and the constructor is implemented correctly.")
            criterion("The loadFuel, mass and refuelPlane methods are implemented correctly.")
        )
        .build();

    private static final Criterion H5_4 = Criterion.builder()
        .shortDescription("H5.4 | Runway")
        .maxPoints(2)
        .addChildCriteria(
            criterion("The Runway class is implemented correctly and with the right attributes.")
            criterion("The methods land method is implemented correctly.")
        )
        .build();

    private static final Criterion H5_5 = Criterion.builder()
        .shortDescription("H5.5 | Airspace")
        .maxPoints(1)
        .criterion("The scanAirspace method is implemented correctly.")
        .build();

    private static final Criterion H5_6 = Criterion.builder()
        .shortDescription("H5.6 | Spielwiese")
        .maxPoints(2)
        .addChildCriteria(
            criterion("At least 10 points are implemented correctly.")
            criterion("All points are implemented correctly")
        )
        .build();

}
