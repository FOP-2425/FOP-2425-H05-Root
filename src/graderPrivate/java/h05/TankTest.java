package h05;

import com.google.common.util.concurrent.AtomicDouble;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static h05.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class TankTest {

    @Test
    public void testClassHeader() {
        TypeLink tankLink = TANK_LINK.get();
        assertTrue(tankLink.interfaces().contains(REFUELLING_LINK.get()), emptyContext(), result ->
            "Class Tank does not implement interface Refuelling");
    }

    @Test
    public void testFields() {
        FieldLink fuelTypeLink = TANK_FUEL_TYPE_LINK.get();
        assertEquals(FUEL_TYPE_LINK.get().reflection(), fuelTypeLink.type().reflection(), emptyContext(), result ->
            "Field fuelType does not have type FuelType");
    }

    @Test
    public void testRefuelPlane() {
        Enum<?> fuelType = FUEL_TYPE_JET_A_LINK.get().constant();
        double fuelCapacity = 200;
        double initialFuelLevel = 42;
        AtomicDouble refuelAmount = new AtomicDouble(-1);
        Object tankInstance = Mockito.mock(TANK_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);
        TANK_FUEL_TYPE_LINK.get().set(tankInstance, fuelType);
        Object planeInstance = Mockito.mock(PLANE_LINK.get().reflection(), invocation -> {
            if (invocation.getMethod().equals(PLANE_GET_FUEL_TYPE_LINK.get().reflection())) {
                return fuelType;
            } else if (invocation.getMethod().equals(PLANE_GET_FUEL_CAPACITY_LINK.get().reflection())) {
                return fuelCapacity;
            } else if (invocation.getMethod().equals(PLANE_GET_CURRENT_FUEL_LEVEL_LINK.get().reflection())) {
                return initialFuelLevel;
            } else if (invocation.getMethod().equals(PLANE_REFUEL_LINK.get().reflection())) {
                refuelAmount.set(invocation.getArgument(0));
            }
            return Mockito.RETURNS_DEFAULTS.answer(invocation);
        });
        Context context = contextBuilder()
            .add("plane.getFuelType()", fuelType)
            .add("plane.getFuelCapacity()", fuelCapacity)
            .add("plane.getCurrentFuelLevel()", initialFuelLevel)
            .build();

        call(() -> REFUELLING_REFUEL_PLANE_LINK.get().invoke(tankInstance, planeInstance), context, result ->
            "An exception occurred while invoking refuelPlane(Plane)");
        assertEquals(fuelCapacity - initialFuelLevel, refuelAmount.get(), context, result ->
            "refuelPlane(Plane) did not pass the correct value to plane.refuel(double)");
    }

    @Test
    public void testRefuelPlane_ErrorMessage() {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            Enum<?> tankFuelType = FUEL_TYPE_JET_A_LINK.get().constant();
            Enum<?> planeFuelType = FUEL_TYPE_BIOKEROSIN_LINK.get().constant();
            Object tankInstance = Mockito.mock(TANK_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);
            TANK_FUEL_TYPE_LINK.get().set(tankInstance, tankFuelType);
            Object planeInstance = Mockito.mock(PLANE_LINK.get().reflection(), invocation -> {
                if (invocation.getMethod().equals(PLANE_GET_FUEL_TYPE_LINK.get().reflection())) {
                    return planeFuelType;
                } else {
                    return Mockito.RETURNS_DEFAULTS.answer(invocation);
                }
            });
            Context context = contextBuilder()
                .add("Tank FuelType", tankFuelType)
                .add("Plane FuelType", planeFuelType)
                .build();

            outputStream.reset();
            call(() -> REFUELLING_REFUEL_PLANE_LINK.get().invoke(tankInstance, planeInstance), context, result ->
                "An exception occurred while invoking refuelPlane(Plane)");
            assertTrue(outputStream.size() > 0, context, result -> "Nothing was written to System.out");
        } finally {
            System.setOut(oldOut);
        }
    }
}
