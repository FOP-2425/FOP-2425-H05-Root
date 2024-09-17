package h05;

import com.google.common.util.concurrent.AtomicDouble;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.EnumConstantLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Set;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class PlaneTest {

    @Test
    public void testHeader() {
        TypeLink planeLink = Links.PLANE_LINK.get();
        TypeLink flyingLink = Links.FLYING_LINK.get();

        assertTrue((planeLink.modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Class Plane is not public");
        assertTrue((planeLink.modifiers() & Modifier.ABSTRACT) != 0, emptyContext(), result ->
            "Class Plane is not abstract");
        assertNotNull(planeLink.getInterface(Matcher.of(flyingLink::equals)), emptyContext(), result ->
            "Class Plane does not implement interface Flying");
    }

    @Test
    public void testFields() {
        FieldLink aircraftRegistrationLink = Links.PLANE_AIRCRAFT_REGISTRATION_LINK.get();
        FieldLink baseWeightLink = Links.PLANE_BASE_WEIGHT_LINK.get();
        FieldLink fuelTypeLink = Links.PLANE_FUEL_TYPE_LINK.get();
        FieldLink fuelCapacityLink = Links.PLANE_FUEL_CAPACITY_LINK.get();
        FieldLink currentFuelLevel = Links.PLANE_CURRENT_FUEL_LEVEL_LINK.get();

        assertEquals(String.class, aircraftRegistrationLink.type().reflection(), emptyContext(), result ->
            "Field aircraftRegistration does not have correct type");
        assertEquals(int.class, baseWeightLink.type().reflection(), emptyContext(), result ->
            "Field baseWeight does not have correct type");
        assertEquals(Links.FUEL_TYPE_LINK.get(), fuelTypeLink.type(), emptyContext(), result ->
            "Field fuelType does not have correct type");
        assertEquals(double.class, fuelCapacityLink.type().reflection(), emptyContext(), result ->
            "Field fuelCapacity does not have correct type");
        assertEquals(double.class, currentFuelLevel.type().reflection(), emptyContext(), result ->
            "Field currentFuelLevel does not have correct type");
    }

    @Test
    public void testMass() {
        MethodLink massLink = Links.PLANE_MASS_LINK.get();

        assertTrue((massLink.modifiers() & Modifier.PROTECTED) != 0, emptyContext(), result ->
            "Method mass is not protected");
        assertTrue((massLink.modifiers() & Modifier.ABSTRACT) != 0, emptyContext(), result ->
            "Method mass is not abstract");
        assertEquals(double.class, massLink.type().reflection(), emptyContext(), result ->
            "Return type of method mass is incorrect");
    }

    @Test
    public void testRefuel() {
        String aircraftRegistration = "D-ABCD";
        double currentFuelLevel = 0;
        double fuelCapacity = 1000;
        Object planeInstance = Mockito.mock(Links.PLANE_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);
        Links.PLANE_AIRCRAFT_REGISTRATION_LINK.get().set(planeInstance, aircraftRegistration);
        Links.PLANE_CURRENT_FUEL_LEVEL_LINK.get().set(planeInstance, currentFuelLevel);
        Links.PLANE_FUEL_CAPACITY_LINK.get().set(planeInstance, fuelCapacity);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream old = System.out;
        System.setOut(printStream);

        double amount = Double.MAX_VALUE;
        Context context = contextBuilder()
            .add("aircraftRegistration", aircraftRegistration)
            .add("currentFuelLevel", currentFuelLevel)
            .add("fuelCapacity", fuelCapacity)
            .add("amount", amount)
            .build();
        try {
            call(() -> Links.PLANE_REFUEL_LINK.get().invoke(planeInstance, amount), context, result ->
                "An exception occurred while invoking refuel");
        } finally {
            System.setOut(old);
        }

        assertEquals(
            "The Tank of Plane %s has overflowed!".formatted(aircraftRegistration),
            outputStream.toString(),
            context,
            result -> "The message printed by method refuel is incorrect"
        );
    }

    @Test
    public void testGetFuelConsumptionPerKilometer() {
        MethodLink getFuelConsumptionPerKilometerLink = Links.PLANE_GET_FUEL_CONSUMPTION_PER_KILOMETER_LINK.get();
        assertTrue((getFuelConsumptionPerKilometerLink.modifiers() & Modifier.PROTECTED) != 0, emptyContext(), result ->
            "Method getFuelConsumptionPerKilometer is not protected");
        assertEquals(double.class, getFuelConsumptionPerKilometerLink.returnType().reflection(), emptyContext(), result ->
            "Return type of getFuelConsumptionPerKilometer is incorrect");

        FieldLink consumptionMultiplicatorLink = Links.FUEL_TYPE_CONSUMPTION_MULTIPLICATOR_LINK.get();
        EnumConstantLink[] fuelTypeLinks = Links.FUEL_TYPE_CONSTANTS.get();
        FieldLink fuelTypeFieldLink = Links.PLANE_FUEL_TYPE_LINK.get();
        MethodLink massLink = Links.PLANE_MASS_LINK.get();
        AtomicDouble massValue = new AtomicDouble();
        Answer<?> answer = invocation -> {
            if (invocation.getMethod().equals(massLink.reflection())) {
                return massValue.get();
            } else if (invocation.getMethod().equals(getFuelConsumptionPerKilometerLink.reflection())) {
                return invocation.callRealMethod();
            } else {
                return Answers.RETURNS_DEFAULTS.answer(invocation);
            }
        };
        Object planeInstance = Mockito.mock(Links.PLANE_LINK.get().reflection(), answer);

        for (EnumConstantLink fuelTypeLink : fuelTypeLinks) {
            fuelTypeFieldLink.set(planeInstance, fuelTypeLink.constant());
            for (double mass = 10000; mass < 30000; mass += 5000) {
                Context context = contextBuilder()
                    .add("fuelType", fuelTypeLink.constant())
                    .add("mass", mass)
                    .build();
                massValue.set(mass);
                double expected = 1.1494e-4 * mass * consumptionMultiplicatorLink.<Double>get(fuelTypeLink.constant());
                double actual = callObject(() -> getFuelConsumptionPerKilometerLink.invoke(planeInstance), context, result ->
                    "An exception occurred while invoking getFuelConsumptionPerKilometer");
                assertEquals(expected, actual, context, result -> "Return value of method getFuelConsumptionPerKilometer is incorrect");
            }
        }
    }

    @Test
    public void testFly() {
        MethodLink flyLink = Links.PLANE_FLY_LINK.get();
        assertTrue((flyLink.modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Method fly is not public");
        assertEquals(void.class, flyLink.returnType().reflection(), emptyContext(), result ->
            "Return type of method fly is incorrect");

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        try {
            Answer<?> answer = invocation -> {
                if (invocation.getMethod().equals(Links.PLANE_GET_FUEL_CONSUMPTION_PER_KILOMETER_LINK.get().reflection())) {
                    return 1d;
                } else if (invocation.getMethod().equals(flyLink.reflection())) {
                    return invocation.callRealMethod();
                } else {
                    return Answers.RETURNS_DEFAULTS.answer(invocation);
                }
            };
            Object planeInstance = Mockito.mock(Links.PLANE_LINK.get().reflection(), answer);

            String aircraftRegistration = "D-ABCD";
            double currentFuelLevel = 5;
            Links.PLANE_AIRCRAFT_REGISTRATION_LINK.get().set(planeInstance, aircraftRegistration);
            Links.PLANE_CURRENT_FUEL_LEVEL_LINK.get().set(planeInstance, currentFuelLevel);
            for (double distance : new double[] {1_000_000, 1}) {
                Context context = contextBuilder()
                    .add("aircraftRegistration", aircraftRegistration)
                    .add("currentFuelLevel", currentFuelLevel)
                    .add("getFuelConsumptionPerKilometer() return value", 1)
                    .add("distance", distance)
                    .build();
                outputStream.reset();
                call(() -> flyLink.invoke(planeInstance, distance), context, result ->
                    "An exception occurred while invoking method fly");
                if (distance > currentFuelLevel) {
                    assertEquals(
                        String.format((Locale) null, "Plane %s does not have enough fuel to fly %.1f km.", aircraftRegistration, distance),
                        outputStream.toString().trim(),
                        context,
                        result -> "Method fly did not print the correct string"
                    );
                    assertEquals(currentFuelLevel, Links.PLANE_CURRENT_FUEL_LEVEL_LINK.get().get(planeInstance), context, result ->
                        "Plane does not have enough fuel to fly but currentFuelLevel was modified");
                } else {
                    assertEquals(
                        String.format((Locale) null, "Plane %s flew %.1f km and has %.1f liters of fuel left.", aircraftRegistration, distance, currentFuelLevel - 1),
                        outputStream.toString().trim(),
                        context,
                        result -> "Method fly did not print the correct string"
                    );
                    assertEquals(currentFuelLevel - 1, Links.PLANE_CURRENT_FUEL_LEVEL_LINK.get().get(planeInstance), context, result ->
                        "Plane had enough fuel to fly but currentFuelLevel was not set to the correct value");
                }
            }
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testTakeOff() {
        Airspace airspace = Airspace.get();
        Object planeInstance = Mockito.mock(Links.PLANE_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);

        call(() -> Links.PLANE_TAKE_OFF_LINK.get().invoke(planeInstance), emptyContext(), result ->
            "An exception occurred while invoking method takeOff");
        Set<?> flyingInAirspace = airspace.getFlyingInAirspace();
        assertEquals(1, flyingInAirspace.size(), emptyContext(), result ->
            "Number of planes in airspace differs from expected value");
        assertSame(planeInstance, flyingInAirspace.iterator().next(), emptyContext(), result ->
            "Calling Plane (using 'this') was not added to airspace");
    }
}
