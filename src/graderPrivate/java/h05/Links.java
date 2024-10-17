package h05;

import com.google.common.base.Suppliers;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Links {

    // Package
    public static final Supplier<PackageLink> BASE_PACKAGE_LINK = Suppliers.memoize(() -> BasicPackageLink.of("h05"));

    // Interface Flying
    public static final Supplier<TypeLink> FLYING_LINK = getTypeLinkByName("Flying");
    public static final Supplier<MethodLink> FLYING_GET_IDENTIFIER_LINK = getMethodLink(FLYING_LINK, "getIdentifier");

    // Enum FuelType
    public static final Supplier<TypeLink> FUEL_TYPE_LINK = getTypeLinkByName("FuelType");
    public static final Supplier<FieldLink> FUEL_TYPE_CONSUMPTION_MULTIPLICATOR_LINK = getFieldLinkByName(FUEL_TYPE_LINK, "consumptionMultiplicator");
    public static final Supplier<ConstructorLink> FUEL_TYPE_CONSTRUCTOR_LINK = getConstructorLink(FUEL_TYPE_LINK, double.class);
    public static final Supplier<EnumConstantLink> FUEL_TYPE_JET_A_LINK = getEnumConstantLinkByName(FUEL_TYPE_LINK, "JetA");
    public static final Supplier<EnumConstantLink> FUEL_TYPE_JET_B_LINK = getEnumConstantLinkByName(FUEL_TYPE_LINK, "JetB");
    public static final Supplier<EnumConstantLink> FUEL_TYPE_AV_GAS_LINK = getEnumConstantLinkByName(FUEL_TYPE_LINK, "AvGas");
    public static final Supplier<EnumConstantLink> FUEL_TYPE_BIOKEROSIN_LINK = getEnumConstantLinkByName(FUEL_TYPE_LINK, "Biokerosin");
    public static final Supplier<EnumConstantLink[]> FUEL_TYPE_CONSTANTS = Suppliers.memoize(() -> new EnumConstantLink[] {
        FUEL_TYPE_JET_A_LINK.get(),
        FUEL_TYPE_JET_B_LINK.get(),
        FUEL_TYPE_AV_GAS_LINK.get(),
        FUEL_TYPE_BIOKEROSIN_LINK.get()
    });

    // Class Plane
    public static final Supplier<TypeLink> PLANE_LINK = getTypeLinkByName("Plane");
    public static final Supplier<FieldLink> PLANE_AIRCRAFT_REGISTRATION_LINK = getFieldLinkByName(PLANE_LINK, "aircraftRegistration");
    public static final Supplier<FieldLink> PLANE_BASE_WEIGHT_LINK = getFieldLinkByName(PLANE_LINK, "baseWeight");
    public static final Supplier<FieldLink> PLANE_FUEL_TYPE_LINK = getFieldLinkByName(PLANE_LINK, "fuelType");
    public static final Supplier<FieldLink> PLANE_FUEL_CAPACITY_LINK = getFieldLinkByName(PLANE_LINK, "fuelCapacity");
    public static final Supplier<FieldLink> PLANE_CURRENT_FUEL_LEVEL_LINK = getFieldLinkByName(PLANE_LINK, "currentFuelLevel");
    public static final Supplier<MethodLink> PLANE_REFUEL_LINK = getMethodLink(PLANE_LINK, "refuel", double.class);
    public static final Supplier<MethodLink> PLANE_MASS_LINK = getMethodLink(PLANE_LINK, "mass");
    public static final Supplier<MethodLink> PLANE_GET_FUEL_CONSUMPTION_PER_KILOMETER_LINK = getMethodLink(PLANE_LINK, "getFuelConsumptionPerKilometer");
    public static final Supplier<MethodLink> PLANE_FLY_LINK = getMethodLink(PLANE_LINK, "fly", double.class);
    public static final Supplier<MethodLink> PLANE_TAKE_OFF_LINK = getMethodLink(PLANE_LINK, "takeOff");
    public static final Supplier<MethodLink> PLANE_LAND_LINK = getMethodLink(PLANE_LINK, "land");
    public static final Supplier<MethodLink> PLANE_GET_FUEL_TYPE_LINK = getMethodLink(PLANE_LINK, "getFuelType");
    public static final Supplier<MethodLink> PLANE_GET_FUEL_CAPACITY_LINK = getMethodLink(PLANE_LINK, "getFuelCapacity");
    public static final Supplier<MethodLink> PLANE_GET_CURRENT_FUEL_LEVEL_LINK = getMethodLink(PLANE_LINK, "getCurrentFuelLevel");
    public static final Supplier<MethodLink> PLANE_GET_IDENTIFIER_LINK = getMethodLink(PLANE_LINK, "getIdentifier");

    // Class WeatherBalloon
    public static final Supplier<TypeLink> WEATHER_BALLOON_LINK = getTypeLinkByName("WeatherBalloon");
    public static final Supplier<ConstructorLink> WEATHER_BALLOON_CONSTRUCTOR_LINK = getConstructorLink(WEATHER_BALLOON_LINK, int.class);
    public static final Supplier<MethodLink> WEATHER_BALLOON_START_LINK = getMethodLink(WEATHER_BALLOON_LINK, "start");
    public static final Supplier<MethodLink> WEATHER_BALLOON_POP_LINK = getMethodLink(WEATHER_BALLOON_LINK, "pop");
    public static final Supplier<MethodLink> WEATHER_BALLOON_GET_IDENTIFIER_LINK = getMethodLink(WEATHER_BALLOON_LINK, "getIdentifier");

    // Interface CarriesPassengers
    public static final Supplier<TypeLink> CARRIES_PASSENGERS_LINK = getTypeLinkByName("CarriesPassengers");
    public static final Supplier<MethodLink> CARRIES_PASSENGERS_BOARD_LINK = getMethodLink(CARRIES_PASSENGERS_LINK, "board", int.class);
    public static final Supplier<MethodLink> CARRIES_PASSENGERS_DISEMBARK_LINK = getMethodLink(CARRIES_PASSENGERS_LINK, "disembark");
    public static final Supplier<MethodLink> CARRIES_PASSENGERS_GET_PASSENGER_COUNT_LINK = getMethodLink(CARRIES_PASSENGERS_LINK, "getPassengerCount");

    // Class PassengerPlane
    public static final Supplier<TypeLink> PASSENGER_PLANE_LINK = getTypeLinkByName("PassengerPlane");
    public static final Supplier<FieldLink> PASSENGER_PLANE_AVERAGE_PEOPLE_WEIGHT_LINK = getFieldLinkByName(PASSENGER_PLANE_LINK, "AVERAGE_PEOPLE_WEIGHT");
    public static final Supplier<FieldLink> PASSENGER_PLANE_AVERAGE_LUGGAGE_WEIGHT_LINK = getFieldLinkByName(PASSENGER_PLANE_LINK, "AVERAGE_LUGGAGE_WEIGHT");
    public static final Supplier<FieldLink> PASSENGER_PLANE_PASSENGER_COUNT_LINK = getFieldLinkByName(PASSENGER_PLANE_LINK, "passengerCount");
    public static final Supplier<FieldLink> PASSENGER_PLANE_CREW_COUNT_LINK = getFieldLinkByName(PASSENGER_PLANE_LINK, "crewCount");
    public static final Supplier<ConstructorLink> PASSENGER_PLANE_CONSTRUCTOR_LINK = getConstructorLink(PASSENGER_PLANE_LINK,
        () -> BasicTypeLink.of(String.class), () -> BasicTypeLink.of(int.class), FUEL_TYPE_LINK, () -> BasicTypeLink.of(double.class), () -> BasicTypeLink.of(int.class));
    public static final Supplier<MethodLink> PASSENGER_PLANE_BOARD_LINK = getMethodLink(PASSENGER_PLANE_LINK, "board", int.class);
    public static final Supplier<MethodLink> PASSENGER_PLANE_DISEMBARK_LINK = getMethodLink(PASSENGER_PLANE_LINK, "disembark");
    public static final Supplier<MethodLink> PASSENGER_PLANE_GET_PASSENGER_COUNT_LINK = getMethodLink(PASSENGER_PLANE_LINK, "getPassengerCount");

    // Interface CarriesCargo
    public static final Supplier<TypeLink> CARRIES_CARGO_LINK = getTypeLinkByName("CarriesCargo");
    public static final Supplier<MethodLink> CARRIES_CARGO_LOAD_CONTAINER_LINK = getMethodLink(CARRIES_CARGO_LINK, "loadContainer", int.class);
    public static final Supplier<MethodLink> CARRIES_CARGO_HAS_FREIGHT_LOADED_LINK = getMethodLink(CARRIES_CARGO_LINK, "hasFreightLoaded");
    public static final Supplier<MethodLink> CARRIES_CARGO_UNLOAD_NEXT_CONTAINER_LINK = getMethodLink(CARRIES_CARGO_LINK, "unloadNextContainer");

    // Class CargoPlane
    public static final Supplier<TypeLink> CARGO_PLANE_LINK = getTypeLinkByName("CargoPlane");
    public static final Supplier<FieldLink> CARGO_PLANE_CONTAINERS_LINK = getFieldLinkByName(CARGO_PLANE_LINK, "containers");
    public static final Supplier<ConstructorLink> CARGO_PLANE_CONSTRUCTOR_LINK = getConstructorLink(CARGO_PLANE_LINK,
        () -> BasicTypeLink.of(String.class), () -> BasicTypeLink.of(int.class), FUEL_TYPE_LINK, () -> BasicTypeLink.of(double.class));
    public static final Supplier<MethodLink> CARGO_PLANE_LOAD_CONTAINER_LINK = getMethodLink(CARGO_PLANE_LINK, "loadContainer", int.class);
    public static final Supplier<MethodLink> CARGO_PLANE_HAS_FREIGHT_LOADED_LINK = getMethodLink(CARGO_PLANE_LINK, "hasFreightLoaded");
    public static final Supplier<MethodLink> CARGO_PLANE_UNLOAD_NEXT_CONTAINER_LINK = getMethodLink(CARGO_PLANE_LINK, "unloadNextContainer");
    public static final Supplier<MethodLink> CARGO_PLANE_MASS_LINK = getMethodLink(CARGO_PLANE_LINK, "mass");

    // Class CombinedPlane
    public static final Supplier<TypeLink> COMBINED_PLANE_LINK = getTypeLinkByName("CombinedPlane");
    public static final Supplier<ConstructorLink> COMBINED_PLANE_CONSTRUCTOR_LINK = getConstructorLink(COMBINED_PLANE_LINK,
        () -> BasicTypeLink.of(String.class), () -> BasicTypeLink.of(int.class), FUEL_TYPE_LINK, () -> BasicTypeLink.of(double.class), () -> BasicTypeLink.of(int.class));

    // Interface Refuelling
    public static final Supplier<TypeLink> REFUELLING_LINK = getTypeLinkByName("Refuelling");
    public static final Supplier<MethodLink> REFUELLING_REFUEL_PLANE_LINK = getMethodLink(REFUELLING_LINK, "refuelPlane", PLANE_LINK);

    // Class Tank
    public static final Supplier<TypeLink> TANK_LINK = getTypeLinkByName("Tank");
    public static final Supplier<FieldLink> TANK_FUEL_TYPE_LINK = getFieldLinkByName(TANK_LINK, "fuelType");
    public static final Supplier<ConstructorLink> TANK_CONSTRUCTOR_LINK = getConstructorLink(TANK_LINK, FUEL_TYPE_LINK);

    // Class TankerPlane
    public static final Supplier<TypeLink> TANKER_PLANE_LINK = getTypeLinkByName("TankerPlane");
    public static final Supplier<FieldLink> TANKER_PLANE_AVAILABLE_AMOUNT_LINK = getFieldLinkByName(TANKER_PLANE_LINK, "availableAmount");
    public static final Supplier<ConstructorLink> TANKER_PLANE_CONSTRUCTOR_LINK = getConstructorLink(TANKER_PLANE_LINK,
        () -> BasicTypeLink.of(String.class), () -> BasicTypeLink.of(int.class), FUEL_TYPE_LINK, () -> BasicTypeLink.of(double.class));
    public static final Supplier<MethodLink> TANKER_PLANE_LOAD_FUEL_LINK = getMethodLink(TANKER_PLANE_LINK, "loadFuel",
        FUEL_TYPE_LINK, () -> BasicTypeLink.of(double.class));

    // Class Runway
    public static final Supplier<TypeLink> RUNWAY_LINK = getTypeLinkByName("Runway");
    public static final Supplier<FieldLink> RUNWAY_RUNWAY_LENGTH_LINK = getFieldLinkByName(RUNWAY_LINK, "runwayLength");
    public static final Supplier<MethodLink> RUNWAY_LAND_LINK = getMethodLink(RUNWAY_LINK, "land", PLANE_LINK);

    private static Supplier<TypeLink> getTypeLinkByName(String name) {
        return Suppliers.memoize(() -> BASE_PACKAGE_LINK.get().getType(Matcher.of(typeLink -> typeLink.name().equals(name))));
    }

    private static Supplier<FieldLink> getFieldLinkByName(Supplier<TypeLink> owner, String name) {
        return Suppliers.memoize(() -> owner.get().getField(Matcher.of(fieldLink -> fieldLink.name().equals(name))));
    }

    private static Supplier<EnumConstantLink> getEnumConstantLinkByName(Supplier<TypeLink> owner, String name) {
        return Suppliers.memoize(() -> owner.get().getEnumConstant(Matcher.of(enumConstantLink -> enumConstantLink.name().equals(name))));
    }

    @SuppressWarnings("unchecked")
    private static Supplier<ConstructorLink> getConstructorLink(Supplier<TypeLink> owner) {
        return getConstructorLink(owner, new Supplier[0]);
    }

    @SuppressWarnings("unchecked")
    private static Supplier<ConstructorLink> getConstructorLink(Supplier<TypeLink> owner, Class<?>... parameterTypes) {
        return getConstructorLink(owner, Arrays.stream(parameterTypes)
            .map(parameterType -> (Supplier<TypeLink>) () -> BasicTypeLink.of(parameterType))
            .toArray(Supplier[]::new));
    }

    @SafeVarargs
    private static Supplier<ConstructorLink> getConstructorLink(Supplier<TypeLink> owner, Supplier<TypeLink>... parameterTypes) {
        return Suppliers.memoize(() ->
            owner.get().getConstructor(Matcher.of(constructorLink -> {
                List<? extends TypeLink> params = constructorLink.typeList();
                boolean found = params.size() == parameterTypes.length;
                for (int i = 0; found && i < parameterTypes.length; i++) {
                    found = parameterTypes[i].get().equals(params.get(i));
                }
                return found;
            })));
    }

    @SuppressWarnings("unchecked")
    private static Supplier<MethodLink> getMethodLink(Supplier<TypeLink> owner, String name) {
        return getMethodLink(owner, name, new Supplier[0]);
    }

    @SuppressWarnings("unchecked")
    private static Supplier<MethodLink> getMethodLink(Supplier<TypeLink> owner, String name, Class<?>... parameterTypes) {
        return getMethodLink(owner, name, Arrays.stream(parameterTypes)
            .map(parameterType -> (Supplier<TypeLink>) () -> BasicTypeLink.of(parameterType))
            .toArray(Supplier[]::new));
    }

    @SafeVarargs
    private static Supplier<MethodLink> getMethodLink(Supplier<TypeLink> owner, String name, Supplier<TypeLink>... parameterTypes) {
        return Suppliers.memoize(() ->
            owner.get().getMethod(Matcher.of(methodLink -> {
                List<? extends TypeLink> params = methodLink.typeList();
                boolean found = methodLink.name().equals(name) && params.size() == parameterTypes.length;
                for (int i = 0; found && i < parameterTypes.length; i++) {
                    found = parameterTypes[i].get().equals(params.get(i));
                }
                return found;
            })));
    }
}
