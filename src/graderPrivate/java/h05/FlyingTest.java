package h05;

import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;

import java.lang.reflect.Modifier;

import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.getOriginalClassHeader;
import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.getOriginalMethodHeader;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class FlyingTest {

    @Test
    public void testHeader() {
        ClassHeader originalClassHeader = getOriginalClassHeader(Flying.class);

        assertTrue(Modifier.isPublic(originalClassHeader.modifiers()), emptyContext(),
            result -> "Class Flying is not public");
        assertTrue(Modifier.isInterface(originalClassHeader.modifiers()), emptyContext(),
            result -> "Class Flying is not an interface");
    }

    @Test
    public void testMethods() {
        MethodHeader getIdentifier = assertNotNull(getOriginalMethodHeader(Flying.class, "getIdentifier"),
            emptyContext(), result -> "Could not find method 'getIdentifier()'");

        assertTrue(Modifier.isAbstract(getIdentifier.modifiers()), emptyContext(),
            result -> "Method 'getIdentifier()' is not abstract");
        assertEquals(String.class, getIdentifier.getReturnType(), emptyContext(),
            result -> "Method 'getIdentifier()' has incorrect return type");
    }
}
