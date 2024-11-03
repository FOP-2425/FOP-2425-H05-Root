package h05.transform.util;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

import java.util.Objects;

public record FieldHeader(String owner, Integer access, String name, String descriptor, String signature) {

    public FieldVisitor toFieldVisitor(ClassVisitor delegate, Object value) {
        return delegate.visitField(access, name, descriptor, signature, value);
    }

    // TODO: include owner and parent classes if possible
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
