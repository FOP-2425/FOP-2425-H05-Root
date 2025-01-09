package h05;

import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;

import java.lang.reflect.Modifier;

import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class CarriesCargoTest {

    @Test
    public void testClassHeader() {
        ClassHeader originalClassHeader = getOriginalClassHeader(CarriesCargo.class);

        assertTrue(Modifier.isInterface(originalClassHeader.modifiers()), emptyContext(), result ->
            "CarriesCargo was not declared to be an interface");
        assertTrue(Modifier.isPublic(originalClassHeader.modifiers()), emptyContext(), result ->
            "Interface CarriesCargo was not declared public");
    }

    @Test
    public void testMethodHeaders() {
        MethodHeader loadContainer = assertNotNull(getOriginalMethodHeader(CarriesCargo.class, "loadContainer", int.class),
            emptyContext(), result -> "Could not find method 'loadContainer(int)'");
        assertEquals(void.class, loadContainer.getReturnType(), emptyContext(), result ->
            "Method loadContainer(int) does not have return type void");

        MethodHeader hasFreightLoaded = assertNotNull(getOriginalMethodHeader(CarriesCargo.class, "hasFreightLoaded"),
            emptyContext(), result -> "Could not find method 'hasFreightLoaded()'");
        assertEquals(boolean.class, hasFreightLoaded.getReturnType(), emptyContext(), result ->
            "Method hasFreightLoaded() does not have return type boolean");

        MethodHeader unloadNextContainer = assertNotNull(getOriginalMethodHeader(CarriesCargo.class, "unloadNextContainer"),
            emptyContext(), result -> "Could not find method 'unloadNextContainer()'");
        assertEquals(int.class, unloadNextContainer.getReturnType(), emptyContext(), result ->
            "Method unloadNextContainer() does not have return type int");
    }
}
