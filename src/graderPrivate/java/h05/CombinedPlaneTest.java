package h05;

import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import static h05.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class CombinedPlaneTest {

    @Test
    public void testClassHeader() {
        TypeLink combinedPlaneLink = COMBINED_PLANE_LINK.get();
        assertTrue(CARRIES_PASSENGERS_LINK.get().reflection().isAssignableFrom(combinedPlaneLink.reflection()), emptyContext(), result ->
            "Class CombinedPlane does not implement CarriesPassengers (directly or indirectly)");
        assertTrue(CARRIES_CARGO_LINK.get().reflection().isAssignableFrom(combinedPlaneLink.reflection()), emptyContext(), result ->
            "Class CombinedPlane does not implement CarriesCargo (directly or indirectly)");
    }

    @Test
    public void testConstructor() {
        String aircraftRegistration = "D-FLOP";
        int baseWeight = 500;
        Enum<?> fuelType = FUEL_TYPE_JET_B_LINK.get().constant();
        double fuelCapacity = 200;
        int crewCount = 5;
        Context context = contextBuilder()
            .add("aircraftRegistration", aircraftRegistration)
            .add("baseWeight", baseWeight)
            .add("fuelType", fuelType)
            .add("fuelCapacity", fuelCapacity)
            .add("crewCount", crewCount)
            .build();

        Object combinedPlaneInstance = callObject(
            () -> COMBINED_PLANE_CONSTRUCTOR_LINK.get().invoke(aircraftRegistration, baseWeight, fuelType, fuelCapacity, crewCount),
            context,
            result -> "An exception occurred while invoking constructor of CombinedPlane");
        assertEquals(aircraftRegistration, PLANE_AIRCRAFT_REGISTRATION_LINK.get().get(combinedPlaneInstance), context, result ->
            "Field aircraftRegistration has incorrect value");
        assertEquals(baseWeight, PLANE_BASE_WEIGHT_LINK.get().get(combinedPlaneInstance), context, result ->
            "Field baseWeight has incorrect value");
        assertEquals(fuelType, PLANE_FUEL_TYPE_LINK.get().get(combinedPlaneInstance), context, result ->
            "Field fuelType has incorrect value");
        assertEquals(fuelCapacity, PLANE_FUEL_CAPACITY_LINK.get().get(combinedPlaneInstance), context, result ->
            "Field fuelCapacity has incorrect value");
        assertEquals(crewCount, PASSENGER_PLANE_CREW_COUNT_LINK.get().get(combinedPlaneInstance), context, result ->
            "Field crewCount has incorrect value");
    }
}
