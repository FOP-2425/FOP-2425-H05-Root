package h05;

import com.google.common.base.Suppliers;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.*;

import java.util.List;
import java.util.function.Supplier;

public class Links {

    // Package
    public static final Supplier<PackageLink> BASE_PACKAGE_LINK = Suppliers.memoize(() -> BasicPackageLink.of("h05"));

    // Interface Flying
    public static final Supplier<TypeLink> FLYING_LINK = Suppliers.memoize(() ->
        BASE_PACKAGE_LINK.get().getType(Matcher.of(typeLink -> typeLink.name().equals("Flying"))));
    public static final Supplier<MethodLink> FLYING_GET_IDENTIFIER_LINK = Suppliers.memoize(() ->
        FLYING_LINK.get().getMethod(Matcher.of(methodLink -> methodLink.name().equals("getIdentifier"))));

    // Enum FuelType
    public static final Supplier<TypeLink> FUEL_TYPE_LINK = Suppliers.memoize(() ->
        BASE_PACKAGE_LINK.get().getType(Matcher.of(typeLink -> typeLink.name().equals("FuelType"))));
    public static final Supplier<FieldLink> FUEL_TYPE_CONSUMPTION_MULTIPLICATOR_LINK = Suppliers.memoize(() ->
        FUEL_TYPE_LINK.get().getField(Matcher.of(fieldLink -> fieldLink.name().equals("consumptionMultiplicator"))));
    public static final Supplier<ConstructorLink> FUEL_TYPE_CONSTRUCTOR_LINK = Suppliers.memoize(() ->
        FUEL_TYPE_LINK.get().getConstructor(Matcher.of(constructorLink -> {
            List<? extends TypeLink> parameterTypes = constructorLink.typeList();
            return parameterTypes.size() == 1 && parameterTypes.get(0).reflection().equals(double.class);
        })));
    public static final Supplier<EnumConstantLink> FUEL_TYPE_JET_A_LINK = Suppliers.memoize(() ->
        FUEL_TYPE_LINK.get().getEnumConstant(Matcher.of(enumConstantLink -> enumConstantLink.name().equals("JetA"))));
    public static final Supplier<EnumConstantLink> FUEL_TYPE_JET_B_LINK = Suppliers.memoize(() ->
        FUEL_TYPE_LINK.get().getEnumConstant(Matcher.of(enumConstantLink -> enumConstantLink.name().equals("JetB"))));
    public static final Supplier<EnumConstantLink> FUEL_TYPE_AV_GAS_LINK = Suppliers.memoize(() ->
        FUEL_TYPE_LINK.get().getEnumConstant(Matcher.of(enumConstantLink -> enumConstantLink.name().equals("AvGas"))));
    public static final Supplier<EnumConstantLink> FUEL_TYPE_BIOKEROSIN_LINK = Suppliers.memoize(() ->
        FUEL_TYPE_LINK.get().getEnumConstant(Matcher.of(enumConstantLink -> enumConstantLink.name().equals("Biokerosin"))));
    public static final Supplier<EnumConstantLink[]> FUEL_TYPE_CONSTANTS = Suppliers.memoize(() -> new EnumConstantLink[] {
        FUEL_TYPE_JET_A_LINK.get(),
        FUEL_TYPE_JET_B_LINK.get(),
        FUEL_TYPE_AV_GAS_LINK.get(),
        FUEL_TYPE_BIOKEROSIN_LINK.get()
    });

    // Class Plane
    public static final Supplier<TypeLink> PLANE_LINK = Suppliers.memoize(() ->
        BASE_PACKAGE_LINK.get().getType(Matcher.of(typeLink -> typeLink.name().equals("Plane"))));
    public static final Supplier<FieldLink> PLANE_AIRCRAFT_REGISTRATION_LINK = Suppliers.memoize(() ->
        PLANE_LINK.get().getField(Matcher.of(fieldLink -> fieldLink.name().equals("aircraftRegistration"))));
    public static final Supplier<FieldLink> PLANE_BASE_WEIGHT_LINK = Suppliers.memoize(() ->
        PLANE_LINK.get().getField(Matcher.of(fieldLink -> fieldLink.name().equals("baseWeight"))));
    public static final Supplier<FieldLink> PLANE_FUEL_TYPE_LINK = Suppliers.memoize(() ->
        PLANE_LINK.get().getField(Matcher.of(fieldLink -> fieldLink.name().equals("fuelType"))));
    public static final Supplier<FieldLink> PLANE_FUEL_CAPACITY_LINK = Suppliers.memoize(() ->
        PLANE_LINK.get().getField(Matcher.of(fieldLink -> fieldLink.name().equals("fuelCapacity"))));
    public static final Supplier<FieldLink> PLANE_CURRENT_FUEL_LEVEL_LINK = Suppliers.memoize(() ->
        PLANE_LINK.get().getField(Matcher.of(fieldLink -> fieldLink.name().equals("currentFuelLevel"))));
    public static final Supplier<MethodLink> PLANE_REFUEL_LINK = Suppliers.memoize(() ->
        PLANE_LINK.get().getMethod(Matcher.of(methodLink -> {
            List<? extends TypeLink> parameterTypes = methodLink.typeList();
            return methodLink.name().equals("refuel") && parameterTypes.size() == 1 && parameterTypes.get(0).reflection().equals(double.class);
        })));
    public static final Supplier<MethodLink> PLANE_MASS_LINK = Suppliers.memoize(() ->
        PLANE_LINK.get().getMethod(Matcher.of(methodLink -> methodLink.name().equals("mass") && methodLink.typeList().isEmpty())));
    public static final Supplier<MethodLink> PLANE_GET_FUEL_CONSUMPTION_PER_KILOMETER_LINK = Suppliers.memoize(() ->
        PLANE_LINK.get().getMethod(Matcher.of(methodLink -> methodLink.name().equals("getFuelConsumptionPerKilometer") && methodLink.typeList().isEmpty())));
    public static final Supplier<MethodLink> PLANE_FLY_LINK = Suppliers.memoize(() ->
        PLANE_LINK.get().getMethod(Matcher.of(methodLink -> {
            List<? extends TypeLink> parameterTypes = methodLink.typeList();
            return methodLink.name().equals("fly") && parameterTypes.size() == 1 && parameterTypes.get(0).reflection().equals(double.class);
        })));
    public static final Supplier<MethodLink> PLANE_TAKE_OFF_LINK = Suppliers.memoize(() ->
        PLANE_LINK.get().getMethod(Matcher.of(methodLink -> methodLink.name().equals("takeOff") && methodLink.typeList().isEmpty())));
    public static final Supplier<MethodLink> PLANE_LAND_LINK = Suppliers.memoize(() ->
        PLANE_LINK.get().getMethod(Matcher.of(methodLink -> methodLink.name().equals("land") && methodLink.typeList().isEmpty())));
}
