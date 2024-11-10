package h05;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.EnumConstantLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.lang.reflect.Modifier;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class FuelTypeTest {

    @Test
    public void testHeader() {
        TypeLink fuelTypeLink = Links.FUEL_TYPE_LINK.get();

        assertTrue((fuelTypeLink.modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Class FuelType is not public");
        assertTrue(fuelTypeLink.reflection().isEnum(), emptyContext(), result ->
            "Class FuelType is not an enum");
    }

    @Test
    public void testFields() {
        FieldLink consumptionMultiplicatorLink = Links.FUEL_TYPE_CONSUMPTION_MULTIPLICATOR_LINK.get();

        assertEquals(double.class, consumptionMultiplicatorLink.type().reflection(), emptyContext(), result ->
            "Field consumptionMultiplicator does not have the correct type");
        assertTrue((consumptionMultiplicatorLink.modifiers() & Modifier.FINAL) != 0, emptyContext(), result ->
            "Field consumptionMultiplicator is not final");
    }

    @Test
    public void testConstructor() {
        call(Links.FUEL_TYPE_CONSTRUCTOR_LINK::get, emptyContext(), result ->
            "An exception occurred while getting the constructor of FuelType, it might have wrong parameter types");
    }

    @ParameterizedTest
    @JsonParameterSetTest("FuelTypeConstants.json")
    public void testEnumConstants(JsonParameterSet params) {
        String enumConstantName = params.getString("enumConstantName");
        double enumConstantValue = params.getDouble("enumConstantValue");

        FieldLink consumptionMultiplicatorLink = Links.FUEL_TYPE_CONSUMPTION_MULTIPLICATOR_LINK.get();
        EnumConstantLink enumConstantLink = callObject(
            () -> Links.FUEL_TYPE_LINK.get().getEnumConstant(Matcher.of(enumConstant -> enumConstant.name().equals(enumConstantName))),
            emptyContext(),
            result -> "Could not get enum constant " + enumConstantName);
        assertEquals(enumConstantValue, consumptionMultiplicatorLink.get(enumConstantLink.constant()), emptyContext(), result ->
            "Enum constant %s does not have the correct value".formatted(enumConstantName));
    }
}
