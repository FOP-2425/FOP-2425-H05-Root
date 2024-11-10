package h05;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static h05.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;

@TestForSubmission
public class CargoPlaneTest {

    @Test
    public void testClassHeader() {
        TypeLink cargoPlaneLink = CARGO_PLANE_LINK.get();
        assertTrue(cargoPlaneLink.interfaces().contains(CARRIES_CARGO_LINK.get()), emptyContext(), result ->
            "Class CargoPlane does not implement interface CarriesCargo");
    }

    @Test
    public void testConstructor() {
        String aircraftRegistration = "D-DFOP";
        int baseWeight = 100;
        Enum<?> fuelType = FUEL_TYPE_JET_A_LINK.get().constant();
        double fuelCapacity = 50;
        Context context = contextBuilder()
            .add("aircraftRegistration", aircraftRegistration)
            .add("baseWeight", baseWeight)
            .add("fuelType", fuelType)
            .add("fuelCapacity", fuelCapacity)
            .build();

        Object cargoPlaneInstance = callObject(
            () -> CARGO_PLANE_CONSTRUCTOR_LINK.get().invoke(aircraftRegistration, baseWeight, fuelType, fuelCapacity),
            context,
            result -> "An exception occurred while invoking constructor of CargoPlane");
        assertEquals(aircraftRegistration, PLANE_AIRCRAFT_REGISTRATION_LINK.get().get(cargoPlaneInstance), context, result ->
            "Field aircraftRegistration has incorrect value");
        assertEquals(baseWeight, PLANE_BASE_WEIGHT_LINK.get().get(cargoPlaneInstance), context, result ->
            "Field baseWeight has incorrect value");
        assertEquals(fuelType, PLANE_FUEL_TYPE_LINK.get().get(cargoPlaneInstance), context, result ->
            "Field fuelType has incorrect value");
        assertEquals(fuelCapacity, PLANE_FUEL_CAPACITY_LINK.get().get(cargoPlaneInstance), context, result ->
            "Field fuelCapacity has incorrect value");
    }

    @Test
    public void testMass() {
        FieldLink baseWeightLink = PLANE_BASE_WEIGHT_LINK.get();
        FieldLink containersLink = CARGO_PLANE_CONTAINERS_LINK.get();
        Object cargoPlaneInstance = Mockito.mock(CARGO_PLANE_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);
        CargoStack cargoStack = new CargoStack() {
            @Override
            public int getSum() {
                return 123;
            }
        };
        containersLink.set(cargoPlaneInstance, cargoStack);
        int baseWeight = 100;
        baseWeightLink.set(cargoPlaneInstance, baseWeight);
        Context context = contextBuilder()
            .add("baseWeight", baseWeight)
            .add("containers.getSum()", cargoStack.getSum())
            .build();

        double returnValue = callObject(() -> CARGO_PLANE_MASS_LINK.get().invoke(cargoPlaneInstance), context, result ->
            "An exception occurred while invoking constructor of CargoPlane");
        assertEquals((double) (baseWeight + cargoStack.getSum()), returnValue, context, result ->
            "Method mass() returned an incorrect value");
    }

    @Test
    public void testLoadContainer() {
        FieldLink containersLink = CARGO_PLANE_CONTAINERS_LINK.get();
        Object cargoPlaneInstance = Mockito.mock(CARGO_PLANE_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);
        AtomicInteger lastPushedValue = new AtomicInteger(-1);
        CargoStack cargoStack = new CargoStack() {
            @Override
            public void push(int value) {
                lastPushedValue.set(value);
                super.push(value);
            }
        };
        containersLink.set(cargoPlaneInstance, cargoStack);
        MethodLink loadContainerLink = CARGO_PLANE_LOAD_CONTAINER_LINK.get();

        for (int i = 0; i < 10; i++) {
            Context context = contextBuilder()
                .add("cargoWeight", i)
                .build();
            final int finalI = i;
            call(() -> loadContainerLink.invoke(cargoPlaneInstance, finalI), context, result ->
                "An exception occurred while invoking loadContainer(int)");
            assertEquals(i, lastPushedValue.get(), context, result ->
                "Method did not call containers.push(int) with the correct value");
        }
    }

    @Test
    public void testHasFreightLoaded() {
        FieldLink containersLink = CARGO_PLANE_CONTAINERS_LINK.get();
        Object cargoPlaneInstance = Mockito.mock(CARGO_PLANE_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);
        AtomicBoolean isEmpty = new AtomicBoolean();
        CargoStack cargoStack = new CargoStack() {
            @Override
            public boolean empty() {
                return isEmpty.get();
            }
        };
        containersLink.set(cargoPlaneInstance, cargoStack);
        MethodLink hasFreightLoadedLink = CARGO_PLANE_HAS_FREIGHT_LOADED_LINK.get();

        for (boolean b : new boolean[] {true, false}) {
            Context context = contextBuilder()
                .add("containers.empty()", b)
                .build();
            isEmpty.set(b);
            boolean returnValue = callObject(() -> hasFreightLoadedLink.invoke(cargoPlaneInstance), context, result ->
                "An exception occurred while invoking hasFreightLoaded()");
            assertEquals(!isEmpty.get(), returnValue, context, result ->
                "Method hasFreightLoaded() did not return the correct value");
        }
    }

    @Test
    public void testUnloadNextContainer() {
        FieldLink containersLink = CARGO_PLANE_CONTAINERS_LINK.get();
        Object cargoPlaneInstance = Mockito.mock(CARGO_PLANE_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);
        List<Integer> stackElements = List.of(1, 2, 3, 4, 5);
        AtomicInteger stackIndex = new AtomicInteger(stackElements.size() - 1);
        CargoStack cargoStack = new CargoStack() {
            @Override
            public int pop() {
                return stackElements.get(stackIndex.getAndDecrement());
            }
        };
        containersLink.set(cargoPlaneInstance, cargoStack);
        MethodLink unloadNextContainerLink = CARGO_PLANE_UNLOAD_NEXT_CONTAINER_LINK.get();

        for (int i = 0; i < stackElements.size(); i++) {
            Context context = contextBuilder()
                .add("containers.pop()", stackElements.get(stackIndex.get()))
                .build();
            int returnValue = callObject(() -> unloadNextContainerLink.invoke(cargoPlaneInstance), context, result ->
                "An exception occurred while invoking unloadNextContainer()");
            assertEquals(stackElements.get(stackIndex.get() + 1), returnValue, context, result ->
                "Method unloadNextContainer() did not return the correct value");
        }
    }
}
