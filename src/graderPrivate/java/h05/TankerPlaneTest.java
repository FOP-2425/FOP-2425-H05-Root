package h05;

import com.google.common.util.concurrent.AtomicDouble;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;
import org.tudalgo.algoutils.transform.util.headers.FieldHeader;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static h05.TestUtils.assertDoubleEquals;
import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;

@TestForSubmission
public class TankerPlaneTest {

    @AfterEach
    public void tearDown() {
        resetAll();
    }

    @Test
    public void testClassHeader() {
        ClassHeader originalClassHeader = getOriginalClassHeader(TankerPlane.class);
        assertEquals(Plane.class, originalClassHeader.getSuperType(), emptyContext(), result ->
            "TankerPlane does not extend Plane");
        assertTrue(originalClassHeader.getInterfaceTypes().contains(Refuelling.class), emptyContext(), result ->
            "TankerPlane does not implement Refuelling");
    }

    @Test
    public void testFields() {
        FieldHeader availableAmount = getOriginalFieldHeader(TankerPlane.class, "availableAmount");
        assertTrue(Modifier.isPrivate(availableAmount.modifiers()), emptyContext(), result ->
            "Field 'availableAmount' was not declared private");
        assertTrue(Modifier.isFinal(availableAmount.modifiers()), emptyContext(), result ->
            "Field 'availableAmount' was not declared final");
        assertEquals(double[].class, availableAmount.getType(), emptyContext(), result ->
            "Type of field 'availableAmount' is incorrect");

        String aircraftRegistration = "D-FLOP";
        int baseWeight = 500;
        FuelType fuelType = FuelType.JetB;
        double fuelCapacity = 200;
        Context context = contextBuilder()
            .add("aircraftRegistration", aircraftRegistration)
            .add("baseWeight", baseWeight)
            .add("fuelType", fuelType)
            .add("fuelCapacity", fuelCapacity)
            .build();

        Delegation.disable(MethodHeader.of(TankerPlane.class, String.class, int.class, FuelType.class, double.class));
        TankerPlane tankerPlaneInstance = callObject(
            () -> new TankerPlane(aircraftRegistration, baseWeight, fuelType, fuelCapacity),
            context,
            result -> "An exception occurred while invoking constructor of TankerPlane");
        double[] availableAmountValue = FieldHeader.of(TankerPlane.class, "availableAmount").getValue(tankerPlaneInstance);
        assertNotNull(availableAmount, context, result -> "Field 'availableAmount' is null");
        assertEquals(FuelType.values().length, availableAmountValue.length, context,
            result -> "Field 'availableAmount' does not have correct length");
    }

    @Test
    public void testConstructor() {
        String aircraftRegistration = "D-FLOP";
        int baseWeight = 500;
        FuelType fuelType = FuelType.JetB;
        double fuelCapacity = 200;
        Context context = contextBuilder()
            .add("aircraftRegistration", aircraftRegistration)
            .add("baseWeight", baseWeight)
            .add("fuelType", fuelType)
            .add("fuelCapacity", fuelCapacity)
            .build();

        Delegation.disable(MethodHeader.of(TankerPlane.class, String.class, int.class, FuelType.class, double.class));
        TankerPlane tankerPlaneInstance = callObject(
            () -> new TankerPlane(aircraftRegistration, baseWeight, fuelType, fuelCapacity),
            context,
            result -> "An exception occurred while invoking constructor of TankerPlane");

        assertEquals(aircraftRegistration,
            FieldHeader.of(Plane.class, "aircraftRegistration").getValue(tankerPlaneInstance),
            context,
            result -> "Field aircraftRegistration has incorrect value");
        assertEquals(baseWeight,
            FieldHeader.of(Plane.class, "baseWeight").getValue(tankerPlaneInstance),
            context,
            result -> "Field baseWeight has incorrect value");
        assertEquals(fuelType,
            FieldHeader.of(Plane.class, "fuelType").getValue(tankerPlaneInstance),
            context,
            result -> "Field fuelType has incorrect value");
        assertEquals(fuelCapacity,
            FieldHeader.of(Plane.class, "fuelCapacity").getValue(tankerPlaneInstance),
            context,
            result -> "Field fuelCapacity has incorrect value");
    }

    @Test
    public void testLoadFuel() {
        double[] availableAmount = new double[FuelType.values().length];
        Arrays.fill(availableAmount, 42);
        TankerPlane tankerPlaneInstance = new TankerPlane("D-ABCD", 0, FuelType.JetA, 1000);
        FieldHeader.of(TankerPlane.class, "availableAmount").setValue(tankerPlaneInstance, availableAmount);

        Delegation.disable(MethodHeader.of(TankerPlane.class, "loadFuel", FuelType.class, double.class));
        for (FuelType fuelType : FuelType.values()) {
            double amount = 1295;
            Context context = contextBuilder()
                .add("availableAmount", availableAmount)
                .add("fuelType", fuelType)
                .add("amount", amount)
                .build();

            call(() -> tankerPlaneInstance.loadFuel(fuelType, amount), context, result ->
                "An exception occurred while invoking loadFuel(FuelType, double): " + result.cause());
            assertDoubleEquals(1337, availableAmount[fuelType.ordinal()], context, result ->
                "loadFuel(FuelType, double) did not update the fuel amount for fuel type" + fuelType);
        }
    }

    @Test
    public void testMass() {
        String aircraftRegistration = "D-FLOP";
        int baseWeight = 500;
        FuelType fuelType = FuelType.JetA;
        double fuelCapacity = 200;

        TankerPlane tankerPlaneInstance = new TankerPlane(aircraftRegistration, baseWeight, fuelType, fuelCapacity);
        double totalFuelAmount = 0;
        double[] availableAmount = new double[FuelType.values().length];
        for (int i = 0; i < availableAmount.length; i++) {
            totalFuelAmount += availableAmount[i] = (i + 1) * 100;
        }
        FieldHeader.of(TankerPlane.class, "availableAmount").setValue(tankerPlaneInstance, availableAmount);
        Context context = contextBuilder()
            .add("baseWeight", baseWeight)
            .add("availableAmount", availableAmount)
            .build();

        Delegation.disable(MethodHeader.of(TankerPlane.class, "mass"));
        double returnValue = callObject(tankerPlaneInstance::mass, context, result ->
            "An exception occurred while invoking mass()");
        assertDoubleEquals(baseWeight + totalFuelAmount, returnValue, context, result ->
            "mass() did not return the correct value");
    }

    @Test
    public void testRefuelPlane() throws ReflectiveOperationException {
        TankerPlane tankerPlaneInstance = new TankerPlane("D-ABCD", 0, FuelType.JetA, 1000);
        double[] availableAmount = new double[FuelType.values().length];
        Arrays.fill(availableAmount, 200);
        TankerPlane.class.getDeclaredField("availableAmount").set(tankerPlaneInstance, availableAmount);
        FuelType fuelType = FuelType.JetA;
        double fuelCapacity = 200;
        double initialFuelLevel = 100;
        AtomicDouble refuelAmount = new AtomicDouble(-1);
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

        Delegation.disable(MethodHeader.of(TankerPlane.class, "refuelPlane", Plane.class));
        call(() -> tankerPlaneInstance.refuelPlane(planeInstance), context, result ->
            "An exception occurred while invoking refuelPlane(Plane)");
        assertDoubleEquals(fuelCapacity - initialFuelLevel, refuelAmount.get(), context, result ->
            "refuelPlane(Plane) did not pass the correct value to plane.refuel(double)");
    }
}
