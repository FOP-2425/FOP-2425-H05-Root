package h05;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;
import org.tudalgo.algoutils.transform.util.headers.FieldHeader;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class RunwayTest {

    @AfterEach
    public void tearDown() {
        resetAll();
    }

    @Test
    public void testClassHeader() {
        ClassHeader originalClassHeader = getOriginalClassHeader(Runway.class);
        assertTrue(Modifier.isPublic(originalClassHeader.modifiers()), emptyContext(), result ->
            "Class Runway is not declared public");
    }

    @Test
    public void testFields() {
        FieldHeader runwayLength = assertNotNull(getOriginalFieldHeader(Runway.class, "runwayLength"), emptyContext(),
            result -> "Could not find field 'runwayLength'");
        assertTrue(Modifier.isPrivate(runwayLength.modifiers()), emptyContext(), result ->
            "Field 'runwayLength' is not declared private");
        assertTrue(Modifier.isFinal(runwayLength.modifiers()), emptyContext(), result ->
            "Field 'runwayLength' is not declared final");
        assertEquals(int.class, runwayLength.getType(), emptyContext(), result ->
            "Type of field 'runwayLength' is incorrect");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testLand(boolean canLand) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            int runwayLength = canLand ? 3000 : 0;
            Runway runwayInstance = new Runway(runwayLength);
            String aircraftRegistration = "D-DFOP";
            FuelType fuelType = FuelType.JetA;
            double planeMass = 1000;
            AtomicBoolean calledLand = new AtomicBoolean(false);
            Plane planeInstance = new Plane(aircraftRegistration, 0, fuelType, 1000) {
                @Override
                protected double mass() {
                    return planeMass;
                }

                @Override
                public void land() {
                    calledLand.set(true);
                }
            };
            Context context = contextBuilder()
                .add("runwayLength", runwayLength)
                .add("plane.getIdentifier()", aircraftRegistration)
                .add("plane.mass()", planeMass)
                .build();

            outputStream.reset();
            Delegation.disable(MethodHeader.of(Runway.class, "land", Plane.class));
            call(() -> runwayInstance.land(planeInstance), context, result ->
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
