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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Set;

import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class PlaneTest {

    @AfterEach
    public void tearDown() {
        resetAll();
    }

    @Test
    public void testHeader() {
        ClassHeader originalClassHeader = getOriginalClassHeader(Plane.class);

        assertTrue(Modifier.isPublic(originalClassHeader.modifiers()), emptyContext(), result ->
            "Class Plane is not public");
        assertTrue(Modifier.isAbstract(originalClassHeader.modifiers()), emptyContext(), result ->
            "Class Plane is not abstract");
        assertTrue(originalClassHeader.getInterfaceTypes().contains(Flying.class), emptyContext(), result ->
            "Class Plane does not implement interface Flying");
    }

    @Test
    public void testFields() {
        FieldHeader aircraftRegistration = assertNotNull(getOriginalFieldHeader(Plane.class, "aircraftRegistration"),
            emptyContext(), result -> "Could not find field 'aircraftRegistration'");
        assertEquals(String.class, aircraftRegistration.getType(), emptyContext(), result ->
            "Field 'aircraftRegistration' does not have correct type");

        FieldHeader baseWeight = assertNotNull(getOriginalFieldHeader(Plane.class, "baseWeight"),
            emptyContext(), result -> "Could not find field 'baseWeight'");
        assertEquals(int.class, baseWeight.getType(), emptyContext(), result ->
            "Field 'baseWeight' does not have correct type");

        FieldHeader fuelType = assertNotNull(getOriginalFieldHeader(Plane.class, "fuelType"),
            emptyContext(), result -> "Could not find field 'fuelType'");
        assertEquals(FuelType.class, fuelType.getType(), emptyContext(), result ->
            "Field 'fuelType' does not have correct type");

        FieldHeader fuelCapacity = assertNotNull(getOriginalFieldHeader(Plane.class, "fuelCapacity"),
            emptyContext(), result -> "Could not find field 'fuelCapacity'");
        assertEquals(double.class, fuelCapacity.getType(), emptyContext(), result ->
            "Field 'fuelCapacity' does not have correct type");

        FieldHeader currentFuelLevel = assertNotNull(getOriginalFieldHeader(Plane.class, "currentFuelLevel"),
            emptyContext(), result -> "Could not find field 'currentFuelLevel'");
        assertEquals(double.class, currentFuelLevel.getType(), emptyContext(), result ->
            "Field 'currentFuelLevel' does not have correct type");
    }

    @Test
    public void testMass() {
        MethodHeader mass = assertNotNull(getOriginalMethodHeader(Plane.class, "mass"),
            emptyContext(), result -> "Could not find method 'mass()'");
        assertTrue(Modifier.isProtected(mass.modifiers()), emptyContext(), result ->
            "Method 'mass()' is not protected");
        assertTrue(Modifier.isAbstract(mass.modifiers()), emptyContext(), result ->
            "Method 'mass()' is not abstract");
        assertEquals(double.class, mass.getReturnType(), emptyContext(), result ->
            "Return type of method 'mass()' is incorrect");
    }

    @Test
    public void testRefuel() {
        Delegation.disable(MethodHeader.of(Plane.class, "refuel", double.class));
        String aircraftRegistration = "D-ABCD";
        double currentFuelLevel = 0;
        double fuelCapacity = 1000;
        Plane planeInstance = new Plane(aircraftRegistration, 0, FuelType.JetA, fuelCapacity) {
            @Override
            protected double mass() {
                return 0;
            }
        };

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
            call(() -> planeInstance.refuel(amount), context, result -> "An exception occurred while invoking refuel");
        } finally {
            System.setOut(old);
        }

        assertEquals(
            "The Tank of Plane %s has overflowed!".formatted(aircraftRegistration),
            outputStream.toString().strip(),
            context,
            result -> "The message printed by method refuel is incorrect"
        );
    }

    @Test
    public void testGetFuelConsumptionPerKilometer() throws ReflectiveOperationException {
        MethodHeader getFuelConsumptionPerKilometer = assertNotNull(
            getOriginalMethodHeader(Plane.class, "getFuelConsumptionPerKilometer"),
            emptyContext(), result -> "Could not find method 'getFuelConsumptionPerKilometer()'");
        assertTrue(Modifier.isProtected(getFuelConsumptionPerKilometer.modifiers()), emptyContext(),
            result -> "Method 'getFuelConsumptionPerKilometer()' is not protected");
        assertEquals(double.class, getFuelConsumptionPerKilometer.getReturnType(), emptyContext(),
            result -> "Return type of 'getFuelConsumptionPerKilometer()' is incorrect");

        Field fuelTypeField = Plane.class.getDeclaredField("fuelType");
        AtomicDouble massValue = new AtomicDouble();
        Plane planeInstance = new Plane("D-ABCD", 0, FuelType.JetA, 1000) {
            @Override
            protected double mass() {
                return massValue.get();
            }
        };

        Delegation.disable(MethodHeader.of(Plane.class, "getFuelConsumptionPerKilometer"));
        for (FuelType fuelType : FuelType.values()) {
            fuelTypeField.set(planeInstance, fuelType);
            for (double mass = 10000; mass < 30000; mass += 5000) {
                Context context = contextBuilder()
                    .add("fuelType", fuelType)
                    .add("mass", mass)
                    .build();
                massValue.set(mass);
                double expected = 1.1494e-4 * mass * fuelType.getConsumptionMultiplicator();
                double actual = callObject(planeInstance::getFuelConsumptionPerKilometer, context, result ->
                    "An exception occurred while invoking 'getFuelConsumptionPerKilometer()'");
                assertEquals(expected, actual, context, result ->
                    "Return value of method 'getFuelConsumptionPerKilometer()' is incorrect");
            }
        }
    }

    @Test
    public void testFly() {
        MethodHeader fly = assertNotNull(getOriginalMethodHeader(Plane.class, "fly", double.class),
            emptyContext(), result -> "Could not find method 'fly(double)'");
        assertTrue(Modifier.isPublic(fly.modifiers()), emptyContext(), result ->
            "Method 'fly(double)' is not public");
        assertEquals(void.class, fly.getReturnType(), emptyContext(), result ->
            "Return type of method 'fly(double)' is incorrect");
        Airspace airspace = Airspace.get();
        Set.copyOf(airspace.getFlyingInAirspace())
            .forEach(flying -> call(() -> airspace.deregister(flying), emptyContext(), result ->
                "An exception occurred while invoking 'Airspace.deregister(Flying)'"));

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        try {
            String aircraftRegistration = "D-ABCD";
            double currentFuelLevel = 5;
            Field currentFuelLevelField = Plane.class.getDeclaredField("currentFuelLevel");
            Plane planeInstance = new Plane(aircraftRegistration, 0, FuelType.JetA, 1000) {
                @Override
                protected double mass() {
                    return 0;
                }
            };

            Delegation.disable(MethodHeader.of(Plane.class, "fly", double.class));
            Substitution.enable(MethodHeader.of(Plane.class, "getFuelConsumptionPerKilometer"), invocation -> 1d);
            currentFuelLevelField.set(planeInstance, currentFuelLevel);
            for (double distance : new double[] {1_000_000, 1}) {
                Context context = contextBuilder()
                    .add("aircraftRegistration", aircraftRegistration)
                    .add("currentFuelLevel", currentFuelLevel)
                    .add("getFuelConsumptionPerKilometer() return value", 1)
                    .add("distance", distance)
                    .build();
                outputStream.reset();
                call(() -> planeInstance.fly(distance), context, result -> "An exception occurred while invoking method fly");
                if (distance > currentFuelLevel) {
                    assertEquals(
                        String.format((Locale) null, "Plane %s does not have enough fuel to fly %.1f km.", aircraftRegistration, distance),
                        outputStream.toString().trim(),
                        context,
                        result -> "Method fly did not print the correct string"
                    );
                    assertEquals(currentFuelLevel, currentFuelLevelField.get(planeInstance), context, result ->
                        "Plane does not have enough fuel to fly but currentFuelLevel was modified");
                } else {
                    assertEquals(
                        String.format((Locale) null, "Plane %s flew %.1f km and has %.1f liters of fuel left.", aircraftRegistration, distance, currentFuelLevel - 1),
                        outputStream.toString().trim(),
                        context,
                        result -> "Method fly did not print the correct string"
                    );
                    assertEquals(currentFuelLevel - 1, currentFuelLevelField.get(planeInstance), context, result ->
                        "Plane had enough fuel to fly but currentFuelLevel was not set to the correct value");
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testTakeOff() {
        Airspace airspace = Airspace.get();
        Plane planeInstance = new Plane("D-ABCD", 0, FuelType.JetA, 1000) {
            @Override
            protected double mass() {
                return 0;
            }
        };

        Delegation.disable(MethodHeader.of(Plane.class, "takeOff"));
        Set.copyOf(airspace.getFlyingInAirspace())
            .forEach(flying -> call(() -> airspace.deregister(flying), emptyContext(), result ->
                "An exception occurred while invoking 'Airspace.deregister(Flying)'"));
        call(planeInstance::takeOff, emptyContext(), result ->
            "An exception occurred while invoking method takeOff");
        Set<?> flyingInAirspace = airspace.getFlyingInAirspace();
        assertEquals(1, flyingInAirspace.size(), emptyContext(), result ->
            "Number of planes in airspace differs from expected value");
        assertSame(planeInstance, flyingInAirspace.iterator().next(), emptyContext(), result ->
            "Calling Plane (using 'this') was not added to airspace");
    }
}
