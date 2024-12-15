package h05;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;
import org.tudalgo.algoutils.transform.util.headers.FieldHeader;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.lang.reflect.Modifier;

import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class PassengerPlaneTest {

    @AfterEach
    public void tearDown() {
        resetAll();
    }

    @Test
    public void testClassHeader() {
        ClassHeader originalClassHeader = getOriginalClassHeader(PassengerPlane.class);

        assertTrue(originalClassHeader.getInterfaceTypes().contains(CarriesPassengers.class), emptyContext(),
            result -> "Class PassengerPlane does not implement interface CarriesPassengers");
    }

    @Test
    public void testConstants() {
        FieldHeader averagePeopleWeight = assertNotNull(getOriginalFieldHeader(PassengerPlane.class, "AVERAGE_PEOPLE_WEIGHT"),
            emptyContext(), result -> "Could not find field 'AVERAGE_PEOPLE_WEIGHT'");
        assertTrue(Modifier.isProtected(averagePeopleWeight.modifiers()), emptyContext(), result ->
            "Field 'AVERAGE_PEOPLE_WEIGHT' was not declared protected");
        assertTrue(Modifier.isStatic(averagePeopleWeight.modifiers()), emptyContext(), result ->
            "Field 'AVERAGE_PEOPLE_WEIGHT' was not declared static");
        assertTrue(Modifier.isFinal(averagePeopleWeight.modifiers()), emptyContext(), result ->
            "Field 'AVERAGE_PEOPLE_WEIGHT' was not declared final");
        assertEquals(char.class, averagePeopleWeight.getType(), emptyContext(), result ->
            "Field 'AVERAGE_PEOPLE_WEIGHT' does not have type char");
        assertEquals((char) 100, getOriginalStaticFieldValue(PassengerPlane.class, "AVERAGE_PEOPLE_WEIGHT"),
            emptyContext(), result -> "Value of field 'AVERAGE_PEOPLE_WEIGHT' is incorrect");

        FieldHeader averageLuggageWeight = assertNotNull(getOriginalFieldHeader(PassengerPlane.class, "AVERAGE_LUGGAGE_WEIGHT"),
            emptyContext(), result -> "Could not find field 'AVERAGE_LUGGAGE_WEIGHT'");
        assertTrue(Modifier.isProtected(averageLuggageWeight.modifiers()), emptyContext(), result ->
            "Field 'AVERAGE_LUGGAGE_WEIGHT' was not declared protected");
        assertTrue(Modifier.isStatic(averageLuggageWeight.modifiers()), emptyContext(), result ->
            "Field 'AVERAGE_LUGGAGE_WEIGHT' was not declared static");
        assertTrue(Modifier.isFinal(averageLuggageWeight.modifiers()), emptyContext(), result ->
            "Field 'AVERAGE_LUGGAGE_WEIGHT' was not declared final");
        assertEquals(char.class, averageLuggageWeight.getType(), emptyContext(), result ->
            "Field 'AVERAGE_LUGGAGE_WEIGHT' does not have type char");
        assertEquals((char) 15, getOriginalStaticFieldValue(PassengerPlane.class, "AVERAGE_LUGGAGE_WEIGHT"),
            emptyContext(), result -> "Value of field 'AVERAGE_LUGGAGE_WEIGHT' is incorrect");
    }

    @Test
    public void testConstructor() {
        String aircraftRegistration = "D-DFOP";
        int baseWeight = 100;
        FuelType fuelType = FuelType.JetA;
        double fuelCapacity = 50;
        int crewCount = 2;
        Context context = contextBuilder()
            .add("aircraftRegistration", aircraftRegistration)
            .add("baseWeight", baseWeight)
            .add("fuelType", fuelType)
            .add("fuelCapacity", fuelCapacity)
            .add("crewCount", crewCount)
            .build();

        MethodHeader constructor = MethodHeader.of(PassengerPlane.class, String.class, int.class, FuelType.class, double.class, int.class);
        Delegation.disable(constructor);
        PassengerPlane passengerPlaneInstance = callObject(
            () -> new PassengerPlane(aircraftRegistration, baseWeight, fuelType, fuelCapacity, crewCount),
            context,
            result -> "An exception occurred while invoking constructor of PassengerPlane");

        assertEquals(aircraftRegistration,
            FieldHeader.of(Plane.class, "aircraftRegistration").getValue(passengerPlaneInstance),
            context,
            result -> "Field aircraftRegistration (in class Plane) has incorrect value");
        assertEquals(baseWeight,
            FieldHeader.of(Plane.class, "baseWeight").getValue(passengerPlaneInstance),
            context,
            result -> "Field baseWeight (in class Plane) has incorrect value");
        assertEquals(fuelType,
            FieldHeader.of(Plane.class, "fuelType").getValue(passengerPlaneInstance),
            context,
            result -> "Field fuelType (in class Plane) has incorrect value");
        assertEquals(fuelCapacity,
            FieldHeader.of(Plane.class, "fuelCapacity").getValue(passengerPlaneInstance),
            context,
            result -> "Field fuelCapacity (in class Plane) has incorrect value");
        assertEquals(crewCount,
            FieldHeader.of(PassengerPlane.class, "crewCount").getValue(passengerPlaneInstance),
            context,
            result -> "Field crewCount has incorrect value");
    }

    @Test
    public void testBoard() {
        FieldHeader passengerCountField = FieldHeader.of(PassengerPlane.class, "passengerCount");
        MethodHeader board = MethodHeader.of(PassengerPlane.class, "board", int.class);
        PassengerPlane passengerPlaneInstance = new PassengerPlane("D-ABCD", 0, FuelType.JetA, 1000, 2);

        Delegation.disable(board);
        int totalPassengersExpected = 0;
        for (int i = 0; i < 10; i++, totalPassengersExpected += i) {
            Context context = contextBuilder()
                .add("peopleCount", i)
                .add("passengerCount (before call)", totalPassengersExpected)
                .build();
            final int finalI = i;
            call(() -> passengerPlaneInstance.board(finalI), context, result ->
                "An exception occurred while invoking board(int)");
            assertEquals(totalPassengersExpected, passengerCountField.getValue(passengerPlaneInstance), context,
                result -> "board(int) did not add the given amount to field passengerCount");
        }
    }

    @Test
    public void testDisembark() {
        FieldHeader passengerCountField = FieldHeader.of(PassengerPlane.class, "passengerCount");
        MethodHeader disembark = MethodHeader.of(PassengerPlane.class, "disembark");
        PassengerPlane passengerPlaneInstance = new PassengerPlane("D-ABCD", 0, FuelType.JetA, 1000, 2);

        Delegation.disable(disembark);
        for (int i = 0; i < 10; i++) {
            passengerCountField.setValue(passengerPlaneInstance, i);
            Context context = contextBuilder()
                .add("passengerCount (before call)", i)
                .build();
            call(passengerPlaneInstance::disembark, context, result ->
                "An exception occurred while invoking disembark()");
            assertEquals(0, passengerCountField.getValue(passengerPlaneInstance), context, result ->
                "disembark() did not set field passengerCount to 0");
        }
    }

    @Test
    public void testGetPassengerCount() {
        FieldHeader passengerCountField = FieldHeader.of(PassengerPlane.class, "passengerCount");
        PassengerPlane passengerPlaneInstance = new PassengerPlane("D-ABCD", 0, FuelType.JetA, 1000, 2);

        Delegation.disable(MethodHeader.of(PassengerPlane.class, "getPassengerCount"));
        for (int i = 0; i < 10; i++) {
            passengerCountField.setValue(passengerPlaneInstance, i);
            Context context = contextBuilder()
                .add("passengerCount", i)
                .build();
            int returnValue = callObject(passengerPlaneInstance::getPassengerCount, context, result ->
                "An exception occurred while invoking getPassengerCount()");
            assertEquals(i, returnValue, context, result ->
                "getPassengerCount() did not return the correct value");
        }
    }
}
