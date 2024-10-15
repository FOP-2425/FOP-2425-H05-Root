package h05;

import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.lang.reflect.Modifier;

import static h05.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class CarriesPassengersTest {

    @Test
    public void testClassHeader() {
        TypeLink carriesPassengersLink = CARRIES_PASSENGERS_LINK.get();
        assertTrue((carriesPassengersLink.modifiers() & Modifier.INTERFACE) != 0, emptyContext(), result ->
            "Class CarriesPassengers is not an interface");
        assertTrue((carriesPassengersLink.modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Interface CarriesPassengers was not declared public");
    }

    @Test
    public void testMethodHeaders() {
        MethodLink boardLink = CARRIES_PASSENGERS_BOARD_LINK.get();
        assertEquals(void.class, boardLink.returnType().reflection(), emptyContext(), result ->
            "Method board(int) does not have return type void");

        MethodLink disembarkLink = CARRIES_PASSENGERS_DISEMBARK_LINK.get();
        assertEquals(void.class, disembarkLink.returnType().reflection(), emptyContext(), result ->
            "Method disembark() does not have return type void");

        MethodLink getPassengerCountLink = CARRIES_PASSENGERS_GET_PASSENGER_COUNT_LINK.get();
        assertEquals(int.class, getPassengerCountLink.returnType().reflection(), emptyContext(), result ->
            "Method getPassengerCount() does not have return type int");
    }
}
