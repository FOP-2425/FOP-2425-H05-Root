package h05;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.EnumConstant;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;
import org.tudalgo.algoutils.transform.util.headers.FieldHeader;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import java.lang.reflect.Modifier;

import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class FuelTypeTest {

    @Test
    public void testHeader() {
        ClassHeader originalClassHeader = getOriginalClassHeader(FuelType.class);

        assertTrue(Modifier.isPublic(originalClassHeader.modifiers()), emptyContext(), result ->
            "Class FuelType is not public");
        assertEquals(Enum.class, originalClassHeader.getSuperType(), emptyContext(),
            result -> "Class FuelType is not an enum");
    }

    @Test
    public void testFields() {
        FieldHeader consumptionMultiplicator = assertNotNull(getOriginalFieldHeader(FuelType.class, "consumptionMultiplicator"),
            emptyContext(), result -> "Could not find field 'consumptionMultiplicator'");

        assertTrue(Modifier.isFinal(consumptionMultiplicator.modifiers()), emptyContext(), result ->
            "Field consumptionMultiplicator is not final");
        assertEquals(double.class, consumptionMultiplicator.getType(), emptyContext(), result ->
            "Field consumptionMultiplicator does not have the correct type");
    }

    @Test
    public void testConstructor() {
        // Enums have special constructors: Enum(<name>, <ordinal>[, other args...])
        MethodHeader constructor = assertNotNull(getOriginalMethodHeader(FuelType.class, String.class, int.class, double.class),
            emptyContext(), result -> "Could not find constructor 'FuelType(double)'");
    }

    @ParameterizedTest
    @JsonParameterSetTest("FuelTypeConstants.json")
    public void testEnumConstants(JsonParameterSet params) {
        String enumConstantName = params.getString("enumConstantName");
        double enumConstantValue = params.getDouble("enumConstantValue");

        EnumConstant enumConstant = assertNotNull(getOriginalEnumConstant(FuelType.class, enumConstantName),
            emptyContext(), result -> "Could not find enum constant '" + enumConstantName + "'");
        assertEquals(1, enumConstant.values().length, emptyContext(), result ->
            "Enum constant '" + enumConstantName + "' does not have the expected amount of arguments");
        assertEquals(enumConstantValue, enumConstant.values()[0], emptyContext(), result ->
            "Enum constant '" + enumConstantName + "' does not have the correct value");
    }
}
