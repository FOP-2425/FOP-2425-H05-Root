package h05;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class MainTest {

    private List<Set<String>> airspaceScans;

    @AfterEach
    public void tearDown() {
        resetAll();
    }

    public void setupEnvironment() {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            Airspace airspace = Airspace.get();
            Set.copyOf(airspace.getFlyingInAirspace())
                .forEach(flying -> call(() -> airspace.deregister(flying), emptyContext(), result ->
                    "An exception occurred while invoking Airspace.deregister(Flying)"));

            Delegation.disable(MethodHeader.of(Main.class, "main", String[].class));
            call(() -> Main.main(new String[0]), emptyContext(), result -> "An exception occurred while invoking main method");
        } finally {
            System.setOut(oldOut);
        }
        airspaceScans = Arrays.stream(outputStream.toString().strip().split("Scanning\\.\\.\\.\n"))
            .map(s -> Set.of(s.split("\n")))
            .toList();
    }

    @ParameterizedTest
    @JsonParameterSetTest("MainTest_Airspace.json")
    public void testMain_FirstScans(JsonParameterSet params) {
        setupEnvironment();
        if (params.getInt("invocation") <= 2) {
            testMain(params);
        }
    }

    @ParameterizedTest
    @JsonParameterSetTest("MainTest_Airspace.json")
    public void testMain_AllScans(JsonParameterSet params) {
        setupEnvironment();
        testMain(params);
    }

    private void testMain(JsonParameterSet params) {
        int invocation = params.getInt("invocation");
        List<String> expectedAirspaceState = params.get("airspaceState");
        Set<String> actualAirspaceState = airspaceScans.get(invocation);

        for (String airspaceMessage : expectedAirspaceState) {
            Context context = contextBuilder().add("expected message", airspaceMessage).build();
            assertTrue(actualAirspaceState.contains(airspaceMessage), context, result ->
                "Airspace does not contain aircraft %s or message is incorrect".formatted(airspaceMessage.split(" is ", 2)[0]));
        }
    }
}
