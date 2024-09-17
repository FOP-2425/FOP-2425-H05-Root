package h05;

import org.sourcegrade.jagr.api.rubric.*;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;

import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.criterion;

@SuppressWarnings("unused")
public class H05_RubricProvider implements RubricProvider {

    private static final Criterion H5_1_1 = Criterion.builder()
        .shortDescription("H5.1.1 | Flying Interface")
        .maxPoints(1)
        .addChildCriteria(
            criterion("Das Flying-Interface ist korrekt implementiert.",
                JUnitTestRef.ofMethod(() -> FlyingTest.class.getDeclaredMethod("testHeader")),
                JUnitTestRef.ofMethod(() -> FlyingTest.class.getDeclaredMethod("testMethods")))
        )
        .build();

    private static final Criterion H5_1_2 = Criterion.builder()
        .shortDescription("H5.1.2 | FuelType")
        .maxPoints(2)
        .addChildCriteria(
            criterion("Das FuelType-Enum ist korrekt implementiert und enthält die richtigen Werte.",
                JUnitTestRef.ofMethod(() -> FuelTypeTest.class.getDeclaredMethod("testHeader")),
                JUnitTestRef.ofMethod(() -> FuelTypeTest.class.getDeclaredMethod("testFields")),
                JUnitTestRef.ofMethod(() -> FuelTypeTest.class.getDeclaredMethod("testEnumConstants", JsonParameterSet.class))),
            criterion("Der Konstruktor des FuelType-Enums ist korrekt implementiert.",
                JUnitTestRef.ofMethod(() -> FuelTypeTest.class.getDeclaredMethod("testConstructor")))
        )
        .build();

    private static final Criterion H5_1_3 = Criterion.builder()
        .shortDescription("H5.1.3 | Flugzeug")
        .maxPoints(6)
        .addChildCriteria(
            criterion("Das Interface ist korrekt in die Flugzeugklasse implementiert.",
                JUnitTestRef.ofMethod(() -> PlaneTest.class.getDeclaredMethod("testHeader"))),
            criterion("Die Flugzeugklasse hat die richtigen Attribute.",
                JUnitTestRef.ofMethod(() -> PlaneTest.class.getDeclaredMethod("testFields"))),
            criterion("Methode mass funktioniert korrekt.",
                JUnitTestRef.ofMethod(() -> PlaneTest.class.getDeclaredMethod("testMass"))),
            criterion("Methode refuel gibt die richtige Nachricht aus.",
                JUnitTestRef.ofMethod(() -> PlaneTest.class.getDeclaredMethod("testRefuel"))),
            criterion("Methode getFuelConsumptionPerKilometer ist korrekt implementiert.",
                JUnitTestRef.ofMethod(() -> PlaneTest.class.getDeclaredMethod("testGetFuelConsumptionPerKilometer"))),
            criterion("Die Methoden fly und takeOff sind korrekt implementiert.",
                JUnitTestRef.ofMethod(() -> PlaneTest.class.getDeclaredMethod("testFly")),
                JUnitTestRef.ofMethod(() -> PlaneTest.class.getDeclaredMethod("testTakeOff")))
        )
        .build();

    private static final Criterion H5_1_4 = Criterion.builder()
        .shortDescription("H5.1.4 | Wetterballon")
        .maxPoints(1)
        .addChildCriteria(
            criterion("Die WeatherBalloon-Klasse und ihre Methoden sind korrekt implementiert.",
                JUnitTestRef.ofMethod(() -> WeatherBalloonTest.class.getDeclaredMethod("testHeader")),
                JUnitTestRef.ofMethod(() -> WeatherBalloonTest.class.getDeclaredMethod("testStart")),
                JUnitTestRef.ofMethod(() -> WeatherBalloonTest.class.getDeclaredMethod("testPop")),
                JUnitTestRef.ofMethod(() -> WeatherBalloonTest.class.getDeclaredMethod("testGetIdentifier")))
        )
        .build();

    private static final Criterion H5_1 = Criterion.builder()
        .shortDescription("H5.1 | Basis")
        .addChildCriteria(H5_1_1, H5_1_2, H5_1_3, H5_1_4)
        .build();

    private static final Criterion H5_2_1 = Criterion.builder()
        .shortDescription("H5.2.1 | CarriesPassengers Interface")
        .maxPoints(4)
        .addChildCriteria(
            criterion("Das CarriesPassengers-Interface ist korrekt implementiert."),
            criterion("Die PassengerPlane-Klasse implementiert das CarriesPassengers-Interface korrekt."),
            criterion("Die Methoden board, disembark und getPassengerCount sind korrekt implementiert."),
            criterion("Die Konstanten und der Konstruktor sind korrekt implementiert.")
        )
        .build();

    private static final Criterion H5_2_2 = Criterion.builder()
        .shortDescription("H5.2.2 | CarriesCargo Interface")
        .maxPoints(4)
        .addChildCriteria(
            criterion("Das CarriesCargo-Interface ist korrekt implementiert."),
            criterion("Die CargoPlane-Klasse implementiert das CarriesCargo-Interface korrekt."),
            criterion("Die Masse-Methode und der Konstruktor sind korrekt implementiert."),
            criterion("Die Methoden loadContainer, hasFreightLoaded und unloadNextContainer sind korrekt implementiert.")
        )
        .build();

    private static final Criterion H5_2_3 = Criterion.builder()
        .shortDescription("H5.2.3 | Kombiniertes Flugzeug")
        .maxPoints(2)
        .addChildCriteria(
            criterion("Die CombinedPlane-Klasse implementiert die Interfaces CarriesPassengers und CarriesCargo korrekt."),
            criterion("Die Klasse ist vollständig korrekt.")
        )
        .build();

    private static final Criterion H5_2 = Criterion.builder()
        .shortDescription("H5.2 | Basis")
        .addChildCriteria(H5_2_1, H5_2_2, H5_2_3)
        .build();

    private static final Criterion H5_3_1 = Criterion.builder()
        .shortDescription("H5.3.1 | Betankung")
        .maxPoints(1)
        .addChildCriteria(
            criterion("Das Refuelling-Interface ist korrekt implementiert.")
        )
        .build();

    private static final Criterion H5_3_2 = Criterion.builder()
        .shortDescription("H5.3.2 | Tank")
        .maxPoints(2)
        .addChildCriteria(
            criterion("Die Tank-Klasse ist korrekt implementiert und enthält die richtigen Attribute."),
            criterion("Die Methode refuelPlane ist korrekt implementiert.")
        )
        .build();

    private static final Criterion H5_3_3 = Criterion.builder()
        .shortDescription("H5.3.3 | Tanker-Flugzeug")
        .maxPoints(2)
        .addChildCriteria(
            criterion("Die TankerPlane-Klasse implementiert die richtigen Interfaces und der Konstruktor ist korrekt implementiert."),
            criterion("Die Methoden loadFuel, mass und refuelPlane sind korrekt implementiert.")
        )
        .build();

    private static final Criterion H5_3 = Criterion.builder()
        .shortDescription("H5.3 | Tanken")
        .addChildCriteria(H5_3_1, H5_3_2, H5_3_3)
        .build();

    private static final Criterion H5_4 = Criterion.builder()
        .shortDescription("H5.4 | Start- und Landebahn")
        .maxPoints(2)
        .addChildCriteria(
            criterion("Die Runway-Klasse ist korrekt implementiert und enthält die richtigen Attribute."),
            criterion("Die Methode land ist korrekt implementiert.")
        )
        .build();

    private static final Criterion H5_5 = Criterion.builder()
        .shortDescription("H5.5 | Luftraum")
        .maxPoints(1)
        .addChildCriteria(
            criterion("Die Methode scanAirspace ist korrekt implementiert.")
        )
        .build();

    private static final Criterion H5_6 = Criterion.builder()
        .shortDescription("H5.6 | Spielwiese")
        .maxPoints(2)
        .addChildCriteria(
            criterion("Mindestens 10 Punkte sind korrekt implementiert."),
            criterion("Alle Punkte sind korrekt implementiert.")
        )
        .build();

    @Override
    public Rubric getRubric() {
        return Rubric.builder()
            .title("H05 | TBA")  // TODO: replace
            .addChildCriteria(H5_1, H5_2, H5_3, H5_4, H5_5, H5_6)
            .build();
    }
}
