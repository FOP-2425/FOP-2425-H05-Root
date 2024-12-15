package h05;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;
import org.tudalgo.algoutils.transform.util.headers.FieldHeader;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class CombinedPlaneTest {

    @AfterEach
    public void tearDown() {
        resetAll();
    }

    @Test
    public void testClassHeader() {
        ClassHeader originalClassHeader = getOriginalClassHeader(CombinedPlane.class);
        assertEquals(PassengerPlane.class, originalClassHeader.getSuperType(), emptyContext(), result ->
            "Class CombinedPlane does not extend PassengerPlane");
        assertTrue(originalClassHeader.getInterfaceTypes().contains(CarriesCargo.class), emptyContext(), result ->
            "Class CombinedPlane does not implement CarriesCargo");
    }

    @Test
    public void testConstructor() {
        String aircraftRegistration = "D-FLOP";
        int baseWeight = 500;
        FuelType fuelType = FuelType.JetB;
        double fuelCapacity = 200;
        int crewCount = 5;
        Context context = contextBuilder()
            .add("aircraftRegistration", aircraftRegistration)
            .add("baseWeight", baseWeight)
            .add("fuelType", fuelType)
            .add("fuelCapacity", fuelCapacity)
            .add("crewCount", crewCount)
            .build();

        Delegation.disable(MethodHeader.of(CombinedPlane.class, String.class, int.class, FuelType.class, double.class, int.class));
        CombinedPlane combinedPlaneInstance = callObject(
            () -> new CombinedPlane(aircraftRegistration, baseWeight, fuelType, fuelCapacity, crewCount),
            context,
            result -> "An exception occurred while invoking constructor of CombinedPlane");

        assertEquals(aircraftRegistration,
            FieldHeader.of(Plane.class, "aircraftRegistration").getValue(combinedPlaneInstance),
            context,
            result -> "Field aircraftRegistration has incorrect value");
        assertEquals(baseWeight,
            FieldHeader.of(Plane.class, "baseWeight").getValue(combinedPlaneInstance),
            context,
            result -> "Field baseWeight has incorrect value");
        assertEquals(fuelType,
            FieldHeader.of(Plane.class, "fuelType").getValue(combinedPlaneInstance),
            context,
            result -> "Field fuelType has incorrect value");
        assertEquals(fuelCapacity,
            FieldHeader.of(Plane.class, "fuelCapacity").getValue(combinedPlaneInstance),
            context,
            result -> "Field fuelCapacity has incorrect value");
        assertEquals(crewCount,
            FieldHeader.of(PassengerPlane.class, "crewCount").getValue(combinedPlaneInstance),
            context,
            result -> "Field crewCount has incorrect value");
    }
}
