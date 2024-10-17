package h05;

import com.google.common.util.concurrent.AtomicDouble;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.EnumConstantLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static h05.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;

@TestForSubmission
public class TankerPlaneTest {

    @Test
    public void testClassHeader() {
        TypeLink tankerPlaneLink = TANKER_PLANE_LINK.get();
        assertEquals(PLANE_LINK.get().reflection(), tankerPlaneLink.superType().reflection(), emptyContext(), result ->
            "TankerPlane does not extend Plane");
        assertTrue(tankerPlaneLink.interfaces().contains(REFUELLING_LINK.get()), emptyContext(), result ->
            "TankerPlane does not implement Refuelling");
    }

    @Test
    public void testFields() {
        FieldLink availableAmountLink = TANKER_PLANE_AVAILABLE_AMOUNT_LINK.get();
        assertTrue((availableAmountLink.modifiers() & Modifier.PRIVATE) != 0, emptyContext(), result ->
            "Field availableAmount was not declared private");
        assertTrue((availableAmountLink.modifiers() & Modifier.FINAL) != 0, emptyContext(), result ->
            "Field availableAmount was not declared final");
        assertEquals(double[].class, availableAmountLink.type().reflection(), emptyContext(), result ->
            "Type of field availableAmount is incorrect");

        String aircraftRegistration = "D-FLOP";
        int baseWeight = 500;
        Enum<?> fuelType = FUEL_TYPE_JET_B_LINK.get().constant();
        double fuelCapacity = 200;
        Context context = contextBuilder()
            .add("aircraftRegistration", aircraftRegistration)
            .add("baseWeight", baseWeight)
            .add("fuelType", fuelType)
            .add("fuelCapacity", fuelCapacity)
            .build();

        Object tankerPlaneInstance = callObject(
            () -> TANKER_PLANE_CONSTRUCTOR_LINK.get().invoke(aircraftRegistration, baseWeight, fuelType, fuelCapacity),
            context,
            result -> "An exception occurred while invoking constructor of TankerPlane");
        double[] availableAmount = availableAmountLink.get(tankerPlaneInstance);
        assertNotNull(availableAmount, context, result -> "Field availableAmount is null");
        assertEquals(FUEL_TYPE_CONSTANTS.get().length, availableAmount.length, context, result ->
            "Field availableAmount does not have correct length");
    }

    @Test
    public void testConstructor() {
        String aircraftRegistration = "D-FLOP";
        int baseWeight = 500;
        Enum<?> fuelType = FUEL_TYPE_JET_B_LINK.get().constant();
        double fuelCapacity = 200;
        Context context = contextBuilder()
            .add("aircraftRegistration", aircraftRegistration)
            .add("baseWeight", baseWeight)
            .add("fuelType", fuelType)
            .add("fuelCapacity", fuelCapacity)
            .build();

        Object tankerPlaneInstance = callObject(
            () -> TANKER_PLANE_CONSTRUCTOR_LINK.get().invoke(aircraftRegistration, baseWeight, fuelType, fuelCapacity),
            context,
            result -> "An exception occurred while invoking constructor of TankerPlane");
        assertEquals(aircraftRegistration, PLANE_AIRCRAFT_REGISTRATION_LINK.get().get(tankerPlaneInstance), context, result ->
            "Field aircraftRegistration has incorrect value");
        assertEquals(baseWeight, PLANE_BASE_WEIGHT_LINK.get().get(tankerPlaneInstance), context, result ->
            "Field baseWeight has incorrect value");
        assertEquals(fuelType, PLANE_FUEL_TYPE_LINK.get().get(tankerPlaneInstance), context, result ->
            "Field fuelType has incorrect value");
        assertEquals(fuelCapacity, PLANE_FUEL_CAPACITY_LINK.get().get(tankerPlaneInstance), context, result ->
            "Field fuelCapacity has incorrect value");
    }

    @Test
    public void testLoadFuel() {
        List<? extends Enum<?>> fuelTypes = Arrays.stream(FUEL_TYPE_CONSTANTS.get()).map(EnumConstantLink::constant).toList();
        double[] availableAmount = new double[fuelTypes.size()];
        Arrays.fill(availableAmount, 42);
        Object tankerPlaneInstance = Mockito.mock(TANKER_PLANE_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);
        TANKER_PLANE_AVAILABLE_AMOUNT_LINK.get().set(tankerPlaneInstance, availableAmount);

        for (Enum<?> fuelType : fuelTypes) {
            double amount = 1295;
            Context context = contextBuilder()
                .add("availableAmount", availableAmount)
                .add("fuelType", fuelType)
                .add("amount", amount)
                .build();

            call(() -> TANKER_PLANE_LOAD_FUEL_LINK.get().invoke(tankerPlaneInstance, fuelType, amount), context, result ->
                "An exception occurred while invoking loadFuel(FuelType, double): " + result.cause());
            assertEquals((double) 1337, availableAmount[fuelType.ordinal()], context, result ->
                "loadFuel(FuelType, double) did not update the fuel amount for fuel type" + fuelType);
        }
    }

    @Test
    public void testMass() {
        String aircraftRegistration = "D-FLOP";
        int baseWeight = 500;
        Enum<?> fuelType = FUEL_TYPE_JET_A_LINK.get().constant();
        double fuelCapacity = 200;
        Context context = contextBuilder()
            .add("aircraftRegistration", aircraftRegistration)
            .add("baseWeight", baseWeight)
            .add("fuelType", fuelType)
            .add("fuelCapacity", fuelCapacity)
            .build();

        Object tankerPlaneInstance = callObject(
            () -> TANKER_PLANE_CONSTRUCTOR_LINK.get().invoke(aircraftRegistration, baseWeight, fuelType, fuelCapacity),
            context,
            result -> "An exception occurred while invoking constructor of TankerPlane");
        double totalFuelAmount = 0;
        double[] availableAmount = new double[FUEL_TYPE_CONSTANTS.get().length];
        for (int i = 0; i < availableAmount.length; i++) {
            totalFuelAmount += availableAmount[i] = (i + 1) * 100;
        }
        TANKER_PLANE_AVAILABLE_AMOUNT_LINK.get().set(tankerPlaneInstance, availableAmount);
        context = contextBuilder()
            .add("baseWeight", baseWeight)
            .add("availableAmount", availableAmount)
            .build();

        double returnValue = callObject(() -> PLANE_MASS_LINK.get().invoke(tankerPlaneInstance), context, result ->
            "An exception occurred while invoking mass()");
        assertEquals(baseWeight + totalFuelAmount, returnValue, context, result ->
            "mass() did not return the correct value");
    }

    @Test
    public void testRefuelPlane() {
        Object tankerPlaneInstance = Mockito.mock(TANKER_PLANE_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);
        double[] availableAmount = new double[FUEL_TYPE_CONSTANTS.get().length];
        Arrays.fill(availableAmount, 200);
        TANKER_PLANE_AVAILABLE_AMOUNT_LINK.get().set(tankerPlaneInstance, availableAmount);
        Enum<?> fuelType = FUEL_TYPE_JET_A_LINK.get().constant();
        double fuelCapacity = 200;
        double initialFuelLevel = 100;
        AtomicDouble refuelAmount = new AtomicDouble(-1);
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

        call(() -> REFUELLING_REFUEL_PLANE_LINK.get().invoke(tankerPlaneInstance, planeInstance), context, result ->
            "An exception occurred while invoking refuelPlane(Plane)");
        assertEquals(fuelCapacity - initialFuelLevel, refuelAmount.get(), context, result ->
            "refuelPlane(Plane) did not pass the correct value to plane.refuel(double)");
    }
}
