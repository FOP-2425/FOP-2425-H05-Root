package h05.transform.util;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.Objects;

public record MethodHeader(String owner, Integer access, String name, String descriptor, String signature, String[] exceptions) {

    public MethodVisitor toMethodVisitor(ClassVisitor delegate) {
        return delegate.visitMethod(access, name, descriptor, signature, exceptions);
    }

    // TODO: include owner and parent classes if possible
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
