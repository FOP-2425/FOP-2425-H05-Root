package h05;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

import static h05.Links.AIRSPACE_DEREGISTER_LINK;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class WeatherBalloonTest {

    @Test
    public void testHeader() {
        TypeLink weatherBalloonLink = Links.WEATHER_BALLOON_LINK.get();

        assertTrue((weatherBalloonLink.modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "WeatherBalloon is not public");
        assertNotNull(weatherBalloonLink.getInterface(Matcher.of(Links.FLYING_LINK.get()::equals)), emptyContext(), result ->
            "WeatherBalloon does not implement interface Flying");
    }

    @Test
    public void testStart() {
        Airspace airspace = Airspace.get();
        Set.copyOf(airspace.getFlyingInAirspace())
            .forEach(flying -> call(() -> AIRSPACE_DEREGISTER_LINK.get().invoke(airspace, flying), emptyContext(), result ->
                "An exception occurred while invoking deregister(Flying)"));
        Object weatherBalloonInstance = Mockito.mock(Links.WEATHER_BALLOON_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);

        call(() -> Links.WEATHER_BALLOON_START_LINK.get().invoke(weatherBalloonInstance), emptyContext(), result ->
            "An exception occurred while invoking method start");
        Set<?> flyingInAirspace = airspace.getFlyingInAirspace();
        assertEquals(1, flyingInAirspace.size(), emptyContext(), result ->
            "Number of aircraft in airspace differs from expected value");
        assertSame(weatherBalloonInstance, flyingInAirspace.iterator().next(), emptyContext(), result ->
            "Calling WeatherBalloon (using 'this') was not added to airspace");
    }

    @Test
    public void testPop() throws ReflectiveOperationException {
        Airspace airspace = Airspace.get();
        Set.copyOf(airspace.getFlyingInAirspace())
            .forEach(flying -> call(() -> AIRSPACE_DEREGISTER_LINK.get().invoke(airspace, flying), emptyContext(), result ->
                "An exception occurred while invoking deregister(Flying)"));
        Object weatherBalloonInstance = Mockito.mock(Links.WEATHER_BALLOON_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);
        Field flyingInAirspaceField = Airspace.class.getDeclaredField("flyingInAirspace");
        flyingInAirspaceField.trySetAccessible();
        Set<Object> set = (Set<Object>) flyingInAirspaceField.get(airspace);
        set.add(weatherBalloonInstance);

        call(() -> Links.WEATHER_BALLOON_POP_LINK.get().invoke(weatherBalloonInstance), emptyContext(), result ->
            "An exception occurred while invoking method pop");
        Set<?> flyingInAirspace = airspace.getFlyingInAirspace();
        assertEquals(0, flyingInAirspace.size(), emptyContext(), result ->
            "Number of aircraft in airspace differs from expected value");
    }

    @Test
    public void testGetIdentifier() throws Throwable {
        int balloonNumber = 12345;
        Context context = contextBuilder()
            .add("balloonNumber", balloonNumber)
            .build();
        Object weatherBalloonInstance = callObject(() -> Links.WEATHER_BALLOON_CONSTRUCTOR_LINK.get().invoke(balloonNumber),
            context,
            result -> "An exception occurred while invoking constructor of WeatherBalloon");

        // TODO: Implementation and exercise sheet differ
        assertCallEquals(
            "WeatherBalloon " + balloonNumber,
            () -> Links.WEATHER_BALLOON_GET_IDENTIFIER_LINK.get().invoke(weatherBalloonInstance),
            context,
            result -> "Identifier returned by getIdentifier is incorrect"
        );
    }
}
