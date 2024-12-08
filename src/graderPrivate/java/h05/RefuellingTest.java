package h05;

import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;

import java.lang.reflect.Modifier;

import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class RefuellingTest {

    @Test
    public void testClassHeader() {
        ClassHeader originalClassHeader = getOriginalClassHeader(Refuelling.class);
        assertTrue(Modifier.isInterface(originalClassHeader.modifiers()), emptyContext(), result ->
            "Class Refuelling is not an interface");
        assertTrue(Modifier.isPublic(originalClassHeader.modifiers()), emptyContext(), result ->
            "Interface Refuelling is not declared public");
    }

    @Test
    public void testMethodHeaders() {
        MethodHeader refuelPlane = getOriginalMethodHeader(Refuelling.class, "refuelPlane", Plane.class);
        assertEquals(void.class, refuelPlane.getReturnType(), emptyContext(), result ->
            "Method refuelPlane(Plane) does not have correct return type");
    }
}
