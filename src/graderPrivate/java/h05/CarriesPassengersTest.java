package h05;

import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;

import java.lang.reflect.Modifier;

import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class CarriesPassengersTest {

    @Test
    public void testClassHeader() {
        ClassHeader originalClassHeader = getOriginalClassHeader(CarriesPassengers.class);

        assertTrue(Modifier.isInterface(originalClassHeader.modifiers()), emptyContext(), result ->
            "Class CarriesPassengers is not an interface");
        assertTrue(Modifier.isPublic(originalClassHeader.modifiers()), emptyContext(), result ->
            "Interface CarriesPassengers was not declared public");
    }

    @Test
    public void testMethodHeaders() {
        MethodHeader board = assertNotNull(getOriginalMethodHeader(CarriesPassengers.class, "board", int.class),
            emptyContext(), result -> "Could not find method 'board(int)'");
        assertTrue(Modifier.isAbstract(board.modifiers()), emptyContext(), result ->
            "Method 'board(int)' was not declared abstract");
        assertEquals(void.class, board.getReturnType(), emptyContext(), result ->
            "Method 'board(int)' does not have return type void");

        MethodHeader disembark = assertNotNull(getOriginalMethodHeader(CarriesPassengers.class, "disembark"),
            emptyContext(), result -> "Could not find method 'disembark()'");
        assertTrue(Modifier.isAbstract(disembark.modifiers()), emptyContext(), result ->
            "Method 'disembark()' was not declared abstract");
        assertEquals(void.class, disembark.getReturnType(), emptyContext(), result ->
            "Method 'disembark()' does not have return type void");

        MethodHeader getPassengerCount = assertNotNull(getOriginalMethodHeader(CarriesPassengers.class, "getPassengerCount"),
            emptyContext(), result -> "Could not find method 'getPassengerCount()'");
        assertTrue(Modifier.isAbstract(getPassengerCount.modifiers()), emptyContext(), result ->
            "Method 'getPassengerCount()' was not declared abstract");
        assertEquals(int.class, getPassengerCount.getReturnType(), emptyContext(), result ->
            "Method 'getPassengerCount()' does not have return type int");
    }
}
