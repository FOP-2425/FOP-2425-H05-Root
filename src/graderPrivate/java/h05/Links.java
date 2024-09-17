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

    // Class WeatherBalloon
    public static final Supplier<TypeLink> WEATHER_BALLOON_LINK = getTypeLinkByName("WeatherBalloon");
    public static final Supplier<ConstructorLink> WEATHER_BALLOON_CONSTRUCTOR_LINK = getConstructorLink(WEATHER_BALLOON_LINK, int.class);
    public static final Supplier<MethodLink> WEATHER_BALLOON_START_LINK = getMethodLink(WEATHER_BALLOON_LINK, "start");
    public static final Supplier<MethodLink> WEATHER_BALLOON_POP_LINK = getMethodLink(WEATHER_BALLOON_LINK, "pop");
    public static final Supplier<MethodLink> WEATHER_BALLOON_GET_IDENTIFIER_LINK = getMethodLink(WEATHER_BALLOON_LINK, "getIdentifier");

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
