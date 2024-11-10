package h05;

import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.lang.reflect.Modifier;

import static h05.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class CarriesCargoTest {

    @Test
    public void testClassHeader() {
        TypeLink carriesCargoLink = CARRIES_CARGO_LINK.get();
        assertTrue((carriesCargoLink.modifiers() & Modifier.INTERFACE) != 0, emptyContext(), result ->
            "CarriesCargo was not declared to be an interface");
        assertTrue((carriesCargoLink.modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Interface CarriesCargo was not declared public");
    }

    @Test
    public void testMethodHeaders() {
        MethodLink loadContainerLink = CARRIES_CARGO_LOAD_CONTAINER_LINK.get();
        assertEquals(void.class, loadContainerLink.returnType().reflection(), emptyContext(), result ->
            "Method loadContainer(int) does not have return type void");

        MethodLink hasFreightLoadedLink = CARRIES_CARGO_HAS_FREIGHT_LOADED_LINK.get();
        assertEquals(boolean.class, hasFreightLoadedLink.returnType().reflection(), emptyContext(), result ->
            "Method hasFreightLoaded() does not have return type boolean");

        MethodLink unloadNextContainerLink = CARRIES_CARGO_UNLOAD_NEXT_CONTAINER_LINK.get();
        assertEquals(int.class, unloadNextContainerLink.returnType().reflection(), emptyContext(), result ->
            "Method unloadNextContainer() does not have return type int");
    }
}
