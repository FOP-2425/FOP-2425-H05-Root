package h05;

import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.lang.reflect.Modifier;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class FlyingTest {

    @Test
    public void testHeader() {
        TypeLink flyingTypeLink = Links.FLYING_LINK.get();

        assertTrue((flyingTypeLink.modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result -> "Class Flying is not public");
        assertTrue(flyingTypeLink.reflection().isInterface(), emptyContext(), result -> "Class Flying is not an interface");
    }

    @Test
    public void testMethods() {
        MethodLink getIdentifierLink = Links.FLYING_GET_IDENTIFIER_LINK.get();

        assertEquals(0, getIdentifierLink.typeList().size(), emptyContext(), result ->
            "Method getIdentifier has at least one parameter");
        assertEquals(String.class, getIdentifierLink.returnType().reflection(), emptyContext(), result ->
            "Method getIdentifier has incorrect return type");
    }
}
