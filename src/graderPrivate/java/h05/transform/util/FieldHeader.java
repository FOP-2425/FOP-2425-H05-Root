package h05.transform.util;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

import java.util.Objects;

/**
 * A record holding information on the header of a field as declared in java bytecode.
 *
 * @param owner      the field's owner or declaring class
 * @param access     the field's modifiers
 * @param name       the field's name
 * @param descriptor the field's descriptor / type
 * @param signature  the field's signature, if using type parameters
 * @author Daniel Mangold
 */
public record FieldHeader(String owner, Integer access, String name, String descriptor, String signature) {

    /**
     * Visits a field in the given class visitor using the information stored in this record.
     *
     * @param delegate the class visitor to use
     * @param value    an optional value for static fields
     *                 (see {@link ClassVisitor#visitField(int, String, String, String, Object)})
     * @return the resulting {@link FieldVisitor}
     */
    public FieldVisitor toFieldVisitor(ClassVisitor delegate, Object value) {
        return delegate.visitField(access, name, descriptor, signature, value);
    }

    /**
     * Two instances of {@link FieldHeader} are considered equal if their names are equal.
     * TODO: include owner and parent classes if possible
     * @inheritDoc
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldHeader that)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
