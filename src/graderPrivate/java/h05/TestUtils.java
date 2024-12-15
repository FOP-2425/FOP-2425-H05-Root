package h05;

import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.assertions.ResultOfObject;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedObject;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedObjects;

import java.util.Set;

public final class TestUtils {

    private TestUtils() {}

    public static void clearAirspace() {
        Airspace airspace = Airspace.get();
        Set.copyOf(airspace.getFlyingInAirspace()).forEach(airspace::deregister);
    }

    public static void assertStringEquals(String expected,
                                          String actual,
                                          Context context,
                                          PreCommentSupplier<? super ResultOfObject<String>> preCommentSupplier) {
        Assertions2.<String>testOfObjectBuilder()
            .expected(ExpectedObjects.equalTo(expected.replaceAll("[^A-Za-z0-9-()]", "")))
            .build()
            .run(actual != null ? actual.replaceAll("[^A-Za-z0-9-()]", "") : null)
            .check(context, preCommentSupplier);
    }

    public static boolean stringEquals(String expected, String actual) {
        return expected.replaceAll("[^A-Za-z0-9-()]", "").equals(actual.replaceAll("[^A-Za-z0-9-()]", ""));
    }

    public static void assertDoubleEquals(double expected,
                                          double actual,
                                          Context context,
                                          PreCommentSupplier<? super ResultOfObject<Double>> preCommentSupplier) {
        Assertions2.<Double>testOfObjectBuilder()
            .expected(ExpectedObject.of(expected, a -> a > expected - 0.001 && a < expected + 0.001))
            .build()
            .run(actual)
            .check(context, preCommentSupplier);
    }
}
