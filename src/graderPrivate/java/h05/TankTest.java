package h05;

import com.google.common.util.concurrent.AtomicDouble;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;
import org.tudalgo.algoutils.transform.util.headers.FieldHeader;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static h05.TestUtils.assertDoubleEquals;
import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class TankTest {

    @AfterEach
    public void tearDown() {
        resetAll();
    }

    @Test
    public void testClassHeader() {
        ClassHeader originalClassHeader = getOriginalClassHeader(Tank.class);
        assertTrue(originalClassHeader.getInterfaceTypes().contains(Refuelling.class), emptyContext(), result ->
            "Class Tank does not implement interface Refuelling");
    }

    @Test
    public void testFields() {
        FieldHeader fuelType = getOriginalFieldHeader(Tank.class, "fuelType");
        assertEquals(FuelType.class, fuelType.getType(), emptyContext(), result ->
            "Field 'fuelType' does not have type FuelType");
    }

    @Test
    public void testRefuelPlane() {
        FuelType fuelType = FuelType.JetA;
        double fuelCapacity = 200;
        double initialFuelLevel = 42;
        AtomicDouble refuelAmount = new AtomicDouble(-1);
        Tank tankInstance = new Tank(fuelType);
        Plane planeInstance = new Plane("D-ABCD", 0, fuelType, fuelCapacity) {
            @Override
            protected double mass() {
                return 0;
            }

            @Override
            public void refuel(double amount) {
                refuelAmount.set(amount);
            }
        };
        FieldHeader.of(Plane.class, "currentFuelLevel").setValue(planeInstance, initialFuelLevel);
        Context context = contextBuilder()
            .add("plane.fuelType", fuelType)
            .add("plane.fuelCapacity", fuelCapacity)
            .add("plane.currentFuelLevel", initialFuelLevel)
            .build();

        Delegation.disable(MethodHeader.of(Tank.class, "refuelPlane", Plane.class));
        call(() -> tankInstance.refuelPlane(planeInstance), context, result ->
            "An exception occurred while invoking refuelPlane(Plane)");
        assertDoubleEquals(fuelCapacity - initialFuelLevel, refuelAmount.get(), context, result ->
            "refuelPlane(Plane) did not pass the correct value to plane.refuel(double)");
    }

    @Test
    public void testRefuelPlane_ErrorMessage() {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            FuelType tankFuelType = FuelType.JetA;
            FuelType planeFuelType = FuelType.Biokerosin;
            Tank tankInstance = new Tank(tankFuelType);
            Plane planeInstance = new Plane("D-ABCD", 0, planeFuelType, 1000) {
                @Override
                protected double mass() {
                    return 0;
                }
            };
            Context context = contextBuilder()
                .add("Tank FuelType", tankFuelType)
                .add("Plane FuelType", planeFuelType)
                .build();

            outputStream.reset();
            Delegation.disable(MethodHeader.of(Tank.class, "refuelPlane", Plane.class));
            call(() -> tankInstance.refuelPlane(planeInstance), context, result ->
                "An exception occurred while invoking refuelPlane(Plane)");
            assertTrue(outputStream.size() > 0, context, result -> "Nothing was written to System.out");
        } finally {
            System.setOut(oldOut);
        }
    }
}
