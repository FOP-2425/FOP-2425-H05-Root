package h05;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Set;

import static h05.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class AirspaceTest {

    @Test
    public void testScanAirspace_Empty() {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            Airspace airspace = Airspace.get();
            Set.copyOf(airspace.getFlyingInAirspace())
                .forEach(flying -> call(() -> AIRSPACE_DEREGISTER_LINK.get().invoke(airspace, flying), emptyContext(), result ->
                    "An exception occurred while invoking deregister(Flying)"));
            call(airspace::scanAirspace, emptyContext(), result -> "An exception occurred while invoking scanAirspace()");
            assertEquals("Scanning...\nAirspace is empty", outputStream.toString().strip(), emptyContext(), result ->
                "scanAirspace() printed to wrong message to System.out");
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
            Object cargoPlaneInstance = Mockito.mock(CARGO_PLANE_LINK.get().reflection(), invocation -> {
                if (invocation.getMethod().equals(PLANE_GET_IDENTIFIER_LINK.get().reflection())) {
                    return aircraftRegistration;
                } else {
                    return Mockito.RETURNS_DEFAULTS.answer(invocation);
                }
            });
            Context context = contextBuilder()
                .add("plane in airspace", cargoPlaneInstance)
                .build();
            Airspace airspace = Airspace.get();
            Set.copyOf(airspace.getFlyingInAirspace())
                .forEach(flying -> call(() -> AIRSPACE_DEREGISTER_LINK.get().invoke(airspace, flying), emptyContext(), result ->
                    "An exception occurred while invoking deregister(Flying)"));
            call(() -> AIRSPACE_REGISTER_LINK.get().invoke(airspace, cargoPlaneInstance), context, result ->
                "An exception occurred while invoking register(Flying)");
            call(airspace::scanAirspace, emptyContext(), result -> "An exception occurred while invoking scanAirspace()");
            assertEquals("Scanning...\n%s is flying in airspace".formatted(aircraftRegistration),
                outputStream.toString().strip(),
                emptyContext(),
                result -> "scanAirspace() printed to wrong message to System.out");
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
            Object passengerPlaneInstance = Mockito.mock(PASSENGER_PLANE_LINK.get().reflection(), invocation -> {
                if (invocation.getMethod().equals(PLANE_GET_IDENTIFIER_LINK.get().reflection())) {
                    return aircraftRegistration;
                } else if (invocation.getMethod().equals(PASSENGER_PLANE_GET_PASSENGER_COUNT_LINK.get().reflection())) {
                    return passengerCount;
                } else {
                    return Mockito.RETURNS_DEFAULTS.answer(invocation);
                }
            });
            Context context = contextBuilder()
                .add("plane in airspace", passengerPlaneInstance)
                .build();
            Airspace airspace = Airspace.get();
            Set.copyOf(airspace.getFlyingInAirspace())
                .forEach(flying -> call(() -> AIRSPACE_DEREGISTER_LINK.get().invoke(airspace, flying), emptyContext(), result ->
                    "An exception occurred while invoking deregister(Flying)"));
            call(() -> AIRSPACE_REGISTER_LINK.get().invoke(airspace, passengerPlaneInstance), context, result ->
                "An exception occurred while invoking register(Flying)");
            call(airspace::scanAirspace, emptyContext(), result -> "An exception occurred while invoking scanAirspace()");
            assertEquals("Scanning...\n%s is flying in airspace (%d PAX)".formatted(aircraftRegistration, passengerCount),
                outputStream.toString().strip(),
                emptyContext(),
                result -> "scanAirspace() printed to wrong message to System.out");
        } finally {
            System.setOut(oldOut);
        }
    }
}
