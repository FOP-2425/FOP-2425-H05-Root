package h05;

import com.google.common.base.Suppliers;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.function.Supplier;

public class Links {

    public static final Supplier<PackageLink> BASE_PACKAGE_LINK = Suppliers.memoize(() -> BasicPackageLink.of("h05"));

    public static final Supplier<TypeLink> FLYING_LINK = Suppliers.memoize(() ->
        BASE_PACKAGE_LINK.get().getType(Matcher.of(typeLink -> typeLink.name().equals("Flying"))));
    public static final Supplier<MethodLink> FLYING_GET_IDENTIFIER_LINK = Suppliers.memoize(() ->
        FLYING_LINK.get().getMethod(Matcher.of(methodLink -> methodLink.name().equals("getIdentifier"))));
}
