package h05;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicBoolean;

import static h05.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class RunwayTest {

    @Test
    public void testClassHeader() {
        TypeLink runwayLink = RUNWAY_LINK.get();
        assertTrue((runwayLink.modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Class Runway is not declared public");
    }

    @Test
    public void testFields() {
        FieldLink runwayLengthLink = RUNWAY_RUNWAY_LENGTH_LINK.get();
        assertTrue((runwayLengthLink.modifiers() & Modifier.PRIVATE) != 0, emptyContext(), result ->
            "Field runwayLength is not declared private");
        assertTrue((runwayLengthLink.modifiers() & Modifier.FINAL) != 0, emptyContext(), result ->
            "Field runwayLength is not declared final");
        assertEquals(int.class, runwayLengthLink.type().reflection(), emptyContext(), result ->
            "Type of field runwayLength is incorrect");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testLand(boolean canLand) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            int runwayLength = canLand ? 3000 : 0;
            Object runwayInstance = Mockito.mock(RUNWAY_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);
            RUNWAY_RUNWAY_LENGTH_LINK.get().set(runwayInstance, runwayLength);
            String aircraftRegistration = "D-DFOP";
            Enum<?> fuelType = FUEL_TYPE_JET_A_LINK.get().constant();
            double planeMass = 1000;
            AtomicBoolean calledLand = new AtomicBoolean(false);
            Object planeInstance = Mockito.mock(PLANE_LINK.get().reflection(), invocation -> {
                if (invocation.getMethod().equals(PLANE_GET_IDENTIFIER_LINK.get().reflection())) {
                    return aircraftRegistration;
                } else if (invocation.getMethod().equals(PLANE_GET_FUEL_TYPE_LINK.get().reflection())) {
                    return fuelType;
                } else if (invocation.getMethod().equals(PLANE_MASS_LINK.get().reflection())) {
                    return planeMass;
                } else if (invocation.getMethod().equals(PLANE_LAND_LINK.get().reflection())) {
                    calledLand.set(true);
                }
                return Mockito.RETURNS_DEFAULTS.answer(invocation);
            });
            Context context = contextBuilder()
                .add("runwayLength", runwayLength)
                .add("plane.getIdentifier()", aircraftRegistration)
                .add("plane.mass()", planeMass)
                .build();

            outputStream.reset();
            call(() -> RUNWAY_LAND_LINK.get().invoke(runwayInstance, planeInstance), context, result ->
                "An exception occurred while invoking land(Plane)");
            if (canLand) {
                assertTrue(calledLand.get(), context, result -> "Method did not call land(Plane) but was supposed to");
                assertEquals("Plane %s has landed successfully.".formatted(aircraftRegistration),
                    outputStream.toString().strip(),
                    context,
                    result -> "Method printed wrong message to System.out");
            } else {
                assertFalse(calledLand.get(), context, result -> "Method called land(Plane) when it was not supposed to");
                assertEquals("Plane %s could not land. The runway is too short.".formatted(aircraftRegistration),
                    outputStream.toString().strip(),
                    context,
                    result -> "Method printed wrong message to System.out");
            }
        } finally {
            System.setOut(oldOut);
        }
    }
}
