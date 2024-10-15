package h05;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.ConstructorLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.lang.reflect.Modifier;

import static h05.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class PassengerPlaneTest {

    @Test
    public void testClassHeader() {
        TypeLink passengerPlaneLink = PASSENGER_PLANE_LINK.get();
        assertTrue(passengerPlaneLink.interfaces().contains(CARRIES_PASSENGERS_LINK.get()), emptyContext(), result ->
            "Class PassengerPlane does not implement interface CarriesPassengers");
    }

    @Test
    public void testConstants() {
        FieldLink averagePeopleWeightLink = PASSENGER_PLANE_AVERAGE_PEOPLE_WEIGHT_LINK.get();
        assertTrue((averagePeopleWeightLink.modifiers() & Modifier.PROTECTED) != 0, emptyContext(), result ->
            "Field AVERAGE_PEOPLE_WEIGHT was not declared protected");
        assertTrue((averagePeopleWeightLink.modifiers() & Modifier.STATIC) != 0, emptyContext(), result ->
            "Field AVERAGE_PEOPLE_WEIGHT was not declared static");
        assertTrue((averagePeopleWeightLink.modifiers() & Modifier.FINAL) != 0, emptyContext(), result ->
            "Field AVERAGE_PEOPLE_WEIGHT was not declared final");
        assertEquals(char.class, averagePeopleWeightLink.type().reflection(), emptyContext(), result ->
            "Field AVERAGE_PEOPLE_WEIGHT does not have type char");
        assertEquals((char) 100, averagePeopleWeightLink.get(), emptyContext(), result ->
            "Value of field AVERAGE_PEOPLE_WEIGHT is incorrect");

        FieldLink averageLuggageWeightLink = PASSENGER_PLANE_AVERAGE_LUGGAGE_WEIGHT_LINK.get();
        assertTrue((averageLuggageWeightLink.modifiers() & Modifier.PROTECTED) != 0, emptyContext(), result ->
            "Field AVERAGE_LUGGAGE_WEIGHT was not declared protected");
        assertTrue((averageLuggageWeightLink.modifiers() & Modifier.STATIC) != 0, emptyContext(), result ->
            "Field AVERAGE_LUGGAGE_WEIGHT was not declared static");
        assertTrue((averageLuggageWeightLink.modifiers() & Modifier.FINAL) != 0, emptyContext(), result ->
            "Field AVERAGE_LUGGAGE_WEIGHT was not declared final");
        assertEquals(char.class, averageLuggageWeightLink.type().reflection(), emptyContext(), result ->
            "Field AVERAGE_LUGGAGE_WEIGHT does not have type char");
        assertEquals((char) 15, averageLuggageWeightLink.get(), emptyContext(), result ->
            "Value of field AVERAGE_LUGGAGE_WEIGHT is incorrect");
    }

    @Test
    public void testConstructor() {
        String aircraftRegistration = "D-DFOP";
        int baseWeight = 100;
        Enum<?> fuelType = FUEL_TYPE_JET_A_LINK.get().constant();
        double fuelCapacity = 50;
        int crewCount = 2;
        Context context = contextBuilder()
            .add("aircraftRegistration", aircraftRegistration)
            .add("baseWeight", baseWeight)
            .add("fuelType", fuelType)
            .add("fuelCapacity", fuelCapacity)
            .add("crewCount", crewCount)
            .build();

        Object passengerPlaneInstance = callObject(
            () -> PASSENGER_PLANE_CONSTRUCTOR_LINK.get().invoke(aircraftRegistration, baseWeight, fuelType, fuelCapacity, crewCount),
            context,
            result -> "An exception occurred while invoking constructor of PassengerPlane");
        assertEquals(aircraftRegistration, PLANE_AIRCRAFT_REGISTRATION_LINK.get().get(passengerPlaneInstance), context, result ->
            "Field aircraftRegistration has incorrect value");
        assertEquals(baseWeight, PLANE_BASE_WEIGHT_LINK.get().get(passengerPlaneInstance), context, result ->
            "Field baseWeight has incorrect value");
        assertEquals(fuelType, PLANE_FUEL_TYPE_LINK.get().get(passengerPlaneInstance), context, result ->
            "Field fuelType has incorrect value");
        assertEquals(fuelCapacity, PLANE_FUEL_CAPACITY_LINK.get().get(passengerPlaneInstance), context, result ->
            "Field fuelCapacity has incorrect value");
        assertEquals(crewCount, PASSENGER_PLANE_CREW_COUNT_LINK.get().get(passengerPlaneInstance), context, result ->
            "Field crewCount has incorrect value");
    }

    @Test
    public void testBoard() {
        FieldLink passengerCountLink = PASSENGER_PLANE_PASSENGER_COUNT_LINK.get();
        MethodLink boardLink = PASSENGER_PLANE_BOARD_LINK.get();
        Object passengerPlaneMock = Mockito.mock(PASSENGER_PLANE_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);
        passengerCountLink.set(passengerPlaneMock, 0);

        int totalPassengersExpected = 0;
        for (int i = 0; i < 10; i++, totalPassengersExpected += i) {
            Context context = contextBuilder()
                .add("peopleCount", i)
                .add("passengerCount (before call)", totalPassengersExpected)
                .build();
            final int finalI = i;
            call(() -> boardLink.invoke(passengerPlaneMock, finalI), context, result ->
                "An exception occurred while invoking board(int)");
            assertEquals(totalPassengersExpected, passengerCountLink.get(passengerPlaneMock), context, result ->
                "board(int) did not add the given amount to field passengerCount");
        }
    }

    @Test
    public void testDisembark() {
        FieldLink passengerCountLink = PASSENGER_PLANE_PASSENGER_COUNT_LINK.get();
        MethodLink disembarkLink = PASSENGER_PLANE_DISEMBARK_LINK.get();
        Object passengerPlaneMock = Mockito.mock(PASSENGER_PLANE_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);

        for (int i = 0; i < 10; i++) {
            passengerCountLink.set(passengerPlaneMock, i);
            Context context = contextBuilder()
                .add("passengerCount (before call)", i)
                .build();
            call(() -> disembarkLink.invoke(passengerPlaneMock), context, result ->
                "An exception occurred while invoking disembark()");
            assertEquals(0, passengerCountLink.get(passengerPlaneMock), context, result ->
                "disembark() did not set field passengerCount to 0");
        }
    }

    @Test
    public void testGetPassengerCount() {
        FieldLink passengerCountLink = PASSENGER_PLANE_PASSENGER_COUNT_LINK.get();
        MethodLink getPassengerCountLink = PASSENGER_PLANE_GET_PASSENGER_COUNT_LINK.get();
        Object passengerPlaneMock = Mockito.mock(PASSENGER_PLANE_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);

        for (int i = 0; i < 10; i++) {
            passengerCountLink.set(passengerPlaneMock, i);
            Context context = contextBuilder()
                .add("passengerCount", i)
                .build();
            int returnValue = callObject(() -> getPassengerCountLink.invoke(passengerPlaneMock), context, result ->
                "An exception occurred while invoking getPassengerCount()");
            assertEquals(i, returnValue, context, result ->
                "getPassengerCount() did not return the correct value");
        }
    }
}
