package h05;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.lang.reflect.Modifier;
import java.util.Set;

import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class WeatherBalloonTest {

    @AfterEach
    public void tearDown() {
        resetAll();
    }

    @Test
    public void testHeader() {
        ClassHeader originalClassHeader = getOriginalClassHeader(WeatherBalloon.class);

        assertTrue(Modifier.isPublic(originalClassHeader.modifiers()), emptyContext(), result ->
            "WeatherBalloon is not public");
        assertTrue(originalClassHeader.getInterfaceTypes().contains(Flying.class), emptyContext(), result ->
            "WeatherBalloon does not implement interface Flying");
    }

    @Test
    public void testStart() {
        Airspace airspace = Airspace.get();
        Set.copyOf(airspace.getFlyingInAirspace())
            .forEach(flying -> call(() -> airspace.deregister(flying), emptyContext(), result ->
                "An exception occurred while invoking 'Airspace.deregister(Flying)'"));
        WeatherBalloon weatherBalloonInstance = new WeatherBalloon(0);

        Delegation.disable(MethodHeader.of(WeatherBalloon.class, "start"));
        call(weatherBalloonInstance::start, emptyContext(), result ->
            "An exception occurred while invoking method start");
        Set<?> flyingInAirspace = airspace.getFlyingInAirspace();
        assertEquals(1, flyingInAirspace.size(), emptyContext(), result ->
            "Number of aircraft in airspace differs from expected value");
        assertSame(weatherBalloonInstance, flyingInAirspace.iterator().next(), emptyContext(), result ->
            "Calling WeatherBalloon (using 'this') was not added to airspace");
    }

    @Test
    public void testPop() {
        Airspace airspace = Airspace.get();
        Set.copyOf(airspace.getFlyingInAirspace())
            .forEach(flying -> call(() -> airspace.deregister(flying), emptyContext(), result ->
                "An exception occurred while invoking 'Airspace.deregister(Flying)'"));

        WeatherBalloon weatherBalloonInstance = new WeatherBalloon(0);
        weatherBalloonInstance.start();

        Delegation.disable(MethodHeader.of(WeatherBalloon.class, "pop"));
        call(weatherBalloonInstance::pop, emptyContext(), result ->
            "An exception occurred while invoking method pop");
        Set<?> flyingInAirspace = airspace.getFlyingInAirspace();
        assertEquals(0, flyingInAirspace.size(), emptyContext(), result ->
            "Number of aircraft in airspace differs from expected value");
    }

    @Test
    public void testGetIdentifier() {
        int balloonNumber = 12345;
        Context context = contextBuilder()
            .add("balloonNumber", balloonNumber)
            .build();
        WeatherBalloon weatherBalloonInstance = new WeatherBalloon(balloonNumber);

        // TODO: Implementation and exercise sheet differ
        Delegation.disable(MethodHeader.of(WeatherBalloon.class, "getIdentifier"));
        assertCallEquals("WeatherBalloon " + balloonNumber, weatherBalloonInstance::getIdentifier, context,
            result -> "Identifier returned by getIdentifier is incorrect");
    }
}
