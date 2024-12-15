package h05;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Set;

import static h05.TestUtils.assertStringEquals;
import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class AirspaceTest {

    @AfterEach
    public void tearDown() {
        resetAll();
    }

    @Test
    public void testScanAirspace_Empty() {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            Airspace airspace = Airspace.get();
            TestUtils.clearAirspace();

            Delegation.disable(MethodHeader.of(Airspace.class, "scanAirspace"));
            call(airspace::scanAirspace, emptyContext(), result -> "An exception occurred while invoking scanAirspace()");
            assertStringEquals("Scanning...\nAirspace is empty", outputStream.toString(), emptyContext(),
                result -> "scanAirspace() printed the wrong message to System.out");
        } finally {
            System.setOut(oldOut);
        }
    }

    @Test
    public void testScanAirspace_CargoPlane() {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            String aircraftRegistration = "D-FLOP";
            CargoPlane cargoPlaneInstance = new CargoPlane(aircraftRegistration, 0, FuelType.JetA, 1000);
            Context context = contextBuilder()
                .add("plane in airspace", cargoPlaneInstance)
                .build();
            Airspace airspace = Airspace.get();
            TestUtils.clearAirspace();

            Delegation.disable(MethodHeader.of(Airspace.class, "register", Flying.class));
            call(() -> airspace.register(cargoPlaneInstance), context, result ->
                "An exception occurred while invoking register(Flying)");
            call(airspace::scanAirspace, emptyContext(), result -> "An exception occurred while invoking scanAirspace()");
            assertStringEquals("Scanning...\n%s is flying in airspace".formatted(aircraftRegistration),
                outputStream.toString(),
                context,
                result -> "scanAirspace() printed the wrong message to System.out");
        } finally {
            System.setOut(oldOut);
        }
    }

    @Test
    public void testScanAirspace_PassengerPlane() {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            String aircraftRegistration = "D-FLOP";
            int passengerCount = 100;
            PassengerPlane passengerPlaneInstance = new PassengerPlane(aircraftRegistration, 0, FuelType.JetA, 1000, 5);
            passengerPlaneInstance.board(passengerCount);
            Context context = contextBuilder()
                .add("plane in airspace", passengerPlaneInstance)
                .build();
            Airspace airspace = Airspace.get();
            TestUtils.clearAirspace();

            Delegation.disable(MethodHeader.of(Airspace.class, "register", Flying.class));
            call(() -> airspace.register(passengerPlaneInstance), context, result ->
                "An exception occurred while invoking register(Flying)");
            call(airspace::scanAirspace, emptyContext(), result -> "An exception occurred while invoking scanAirspace()");
            assertStringEquals("Scanning...\n%s is flying in airspace (%d PAX)".formatted(aircraftRegistration, passengerCount),
                outputStream.toString(),
                context,
                result -> "scanAirspace() printed the wrong message to System.out");
        } finally {
            System.setOut(oldOut);
        }
    }
}
