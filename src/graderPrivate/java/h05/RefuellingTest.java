package h05;

import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.lang.reflect.Modifier;

import static h05.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class RefuellingTest {

    @Test
    public void testClassHeader() {
        TypeLink refuellingLink = REFUELLING_LINK.get();
        assertTrue((refuellingLink.modifiers() & Modifier.INTERFACE) != 0, emptyContext(), result ->
            "Class Refuelling is not an interface");
        assertTrue((refuellingLink.modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Interface Refuelling is not declared public");
    }

    @Test
    public void testMethodHeaders() {
        MethodLink refuelPlaneLink = REFUELLING_REFUEL_PLANE_LINK.get();
        assertEquals(void.class, refuelPlaneLink.returnType().reflection(), emptyContext(), result ->
            "Method refuelPlane(Plane) does not have correct return type");
    }
}
