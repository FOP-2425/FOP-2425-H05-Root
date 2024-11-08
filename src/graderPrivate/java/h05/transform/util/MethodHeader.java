package h05.transform.util;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * A record holding information on the header of a method as declared in java bytecode.
 *
 * @param owner      the method's owner or declaring class
 * @param access     the method's modifiers
 * @param name       the method's name
 * @param descriptor the method's descriptor / parameter types + return type
 * @param signature  the method's signature, if using type parameters
 * @param exceptions exceptions declared in the method's {@code throws} clause
 * @author Daniel Mangold
 */
public record MethodHeader(String owner, int access, String name, String descriptor, String signature, String[] exceptions) {

    public static final Type INTERNAL_TYPE = Type.getType(MethodHeader.class);
    public static final String INTERNAL_CONSTRUCTOR_DESCRIPTOR = Type.getMethodDescriptor(Type.VOID_TYPE,
        Type.getType(String.class),
        Type.INT_TYPE,
        Type.getType(String.class),
        Type.getType(String.class),
        Type.getType(String.class),
        Type.getType(String[].class));

    /**
     * Constructs a new method header using the given method.
     *
     * @param method a java reflection method
     */
    public MethodHeader(Method method) {
        this(Type.getInternalName(method.getDeclaringClass()),
            method.getModifiers(),
            method.getName(),
            Type.getMethodDescriptor(method),
            null,
            Arrays.stream(method.getExceptionTypes())
                .map(Type::getInternalName)
                .toArray(String[]::new));
    }

    /**
     * Visits a method in the given class visitor using the information stored in this record.
     *
     * @param delegate the class visitor to use
     * @return the resulting {@link MethodVisitor}
     */
    public MethodVisitor toMethodVisitor(ClassVisitor delegate) {
        return delegate.visitMethod(access, name, descriptor, signature, exceptions);
    }

    /**
     * Two instances of {@link MethodHeader} are considered equal if their names and descriptors are equal.
     * TODO: include owner and parent classes if possible
     * @inheritDoc
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodHeader that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(descriptor, that.descriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, descriptor);
    }
}
