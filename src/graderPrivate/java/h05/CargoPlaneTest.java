package h05;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;
import org.tudalgo.algoutils.transform.util.headers.FieldHeader;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static h05.TestUtils.assertDoubleEquals;
import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;

@TestForSubmission
public class CargoPlaneTest {

    @AfterEach
    public void tearDown() {
        resetAll();
    }

    @Test
    public void testClassHeader() {
        ClassHeader originalClassHeader = getOriginalClassHeader(CargoPlane.class);
        assertTrue(originalClassHeader.getInterfaceTypes().contains(CarriesCargo.class), emptyContext(),
            result -> "Class CargoPlane does not implement interface CarriesCargo");
    }

    @Test
    public void testConstructor() {
        String aircraftRegistration = "D-DFOP";
        int baseWeight = 100;
        FuelType fuelType = FuelType.JetA;
        double fuelCapacity = 50;
        Context context = contextBuilder()
            .add("aircraftRegistration", aircraftRegistration)
            .add("baseWeight", baseWeight)
            .add("fuelType", fuelType)
            .add("fuelCapacity", fuelCapacity)
            .build();

        Delegation.disable(MethodHeader.of(CargoPlane.class, String.class, int.class, FuelType.class, double.class));
        CargoPlane cargoPlaneInstance = callObject(
            () -> new CargoPlane(aircraftRegistration, baseWeight, fuelType, fuelCapacity),
            context,
            result -> "An exception occurred while invoking constructor of CargoPlane");

        assertEquals(aircraftRegistration,
            FieldHeader.of(Plane.class, "aircraftRegistration").getValue(cargoPlaneInstance),
            context,
            result -> "Field aircraftRegistration has incorrect value");
        assertEquals(baseWeight,
            FieldHeader.of(Plane.class, "baseWeight").getValue(cargoPlaneInstance),
            context,
            result -> "Field baseWeight has incorrect value");
        assertEquals(fuelType,
            FieldHeader.of(Plane.class, "fuelType").getValue(cargoPlaneInstance),
            context,
            result -> "Field fuelType has incorrect value");
        assertDoubleEquals(fuelCapacity,
            FieldHeader.of(Plane.class, "fuelCapacity").getValue(cargoPlaneInstance),
            context,
            result -> "Field fuelCapacity has incorrect value");
    }

    @Test
    public void testMass() {
        int getSumReturnValue = 123;
        Substitution.enable(MethodHeader.of(CargoStack.class, "getSum"), invocation -> getSumReturnValue);
        Delegation.disable(MethodHeader.of(CargoPlane.class, "mass"));

        int baseWeight = 100;
        CargoPlane cargoPlaneInstance = new CargoPlane("D-ABCD", baseWeight, FuelType.JetA, 1000);
        Context context = contextBuilder()
            .add("baseWeight", baseWeight)
            .add("containers.getSum()", getSumReturnValue)
            .build();

        double returnValue = callObject(cargoPlaneInstance::mass, context, result ->
            "An exception occurred while invoking mass()");
        assertDoubleEquals(baseWeight + getSumReturnValue, returnValue, context, result ->
            "Method mass() returned an incorrect value");
    }

    @Test
    public void testLoadContainer() {
        AtomicInteger lastPushedValue = new AtomicInteger(-1);
        Substitution.enable(MethodHeader.of(CargoStack.class, "push", int.class), invocation -> {
            lastPushedValue.set(invocation.getIntParameter(0));
            return invocation.callOriginalMethod(true);
        });
        Delegation.disable(MethodHeader.of(CargoPlane.class, "loadContainer", int.class));
        CargoPlane cargoPlaneInstance = new CargoPlane("D-ABCD", 100, FuelType.JetA, 1000);

        for (int i = 0; i < 10; i++) {
            Context context = contextBuilder()
                .add("cargoWeight", i)
                .build();
            final int finalI = i;
            call(() -> cargoPlaneInstance.loadContainer(finalI), context, result ->
                "An exception occurred while invoking loadContainer(int)");
            assertEquals(i, lastPushedValue.get(), context, result ->
                "Method did not call containers.push(int) with the correct value");
        }
    }

    @Test
    public void testHasFreightLoaded() {
        AtomicBoolean isEmpty = new AtomicBoolean();
        Substitution.enable(MethodHeader.of(CargoStack.class, "empty"), invocation -> isEmpty.get());
        Delegation.disable(MethodHeader.of(CargoPlane.class, "hasFreightLoaded"));
        CargoPlane cargoPlaneInstance = new CargoPlane("D-ABCD", 100, FuelType.JetA, 1000);

        for (boolean b : new boolean[] {true, false}) {
            Context context = contextBuilder()
                .add("containers.empty()", b)
                .build();
            isEmpty.set(b);
            boolean returnValue = callObject(cargoPlaneInstance::hasFreightLoaded, context, result ->
                "An exception occurred while invoking hasFreightLoaded()");
            assertEquals(!isEmpty.get(), returnValue, context, result ->
                "Method hasFreightLoaded() did not return the correct value");
        }
    }

    @Test
    public void testUnloadNextContainer() {
        List<Integer> stackElements = List.of(1, 2, 3, 4, 5);
        AtomicInteger stackIndex = new AtomicInteger(stackElements.size() - 1);
        Substitution.enable(MethodHeader.of(CargoStack.class, "pop"), invocation -> stackElements.get(stackIndex.getAndDecrement()));
        Delegation.disable(MethodHeader.of(CargoPlane.class, "unloadNextContainer"));
        CargoPlane cargoPlaneInstance = new CargoPlane("D-ABCD", 100, FuelType.JetA, 1000);

        for (int i = 0; i < stackElements.size(); i++) {
            Context context = contextBuilder()
                .add("containers.pop()", stackElements.get(stackIndex.get()))
                .build();
            int returnValue = callObject(cargoPlaneInstance::unloadNextContainer, context, result ->
                "An exception occurred while invoking unloadNextContainer()");
            assertEquals(stackElements.get(stackIndex.get() + 1), returnValue, context, result ->
                "Method unloadNextContainer() did not return the correct value");
        }
    }
}
