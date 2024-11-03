package h05.transform;

import h05.transform.util.FieldHeader;
import h05.transform.util.MethodHeader;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class SolutionClassNode extends ClassNode {

    private final String className;
    private final Map<FieldHeader, FieldNode> fields = new HashMap<>();
    private final Map<MethodHeader, MethodNode> methods = new HashMap<>();

    public SolutionClassNode(String className) {
        super(Opcodes.ASM9);
        this.className = className;
    }

    public Map<FieldHeader, FieldNode> getFields() {
        return fields;
    }

    public Map<MethodHeader, MethodNode> getMethods() {
        return methods;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        FieldHeader fieldHeader = new FieldHeader(className, access, name, descriptor, signature);
        FieldNode fieldNode = (FieldNode) super.visitField(access, name, descriptor, signature, value);
        fields.put(fieldHeader, fieldNode);
        return fieldNode;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodNode methodNode;
        if ((access & ACC_SYNTHETIC) != 0 && name.startsWith("lambda$")) {  // if method is lambda
            methodNode = getMethodNode(access, name + "$solution", descriptor, signature, exceptions);
        } else {
            methodNode = getMethodNode(access, name, descriptor, signature, exceptions);
            methods.put(new MethodHeader(className, access, name, descriptor, signature, exceptions), methodNode);
        }
        return methodNode;
    }

    private MethodNode getMethodNode(int access, String name, String descriptor, String signature, String[] exceptions) {
        return new MethodNode(ASM9, access, name, descriptor, signature, exceptions) {
            @Override
            public void visitMethodInsn(int opcodeAndSource, String owner, String name, String descriptor, boolean isInterface) {
                super.visitMethodInsn(opcodeAndSource,
                    owner,
                    name + (name.startsWith("lambda$") ? "$solution" : ""),
                    descriptor,
                    isInterface);
            }

            @Override
            public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
                super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, Arrays.stream(bootstrapMethodArguments)
                    .map(o -> {
                        if (o instanceof Handle handle && handle.getName().startsWith("lambda$")) {
                            return new Handle(handle.getTag(),
                                handle.getOwner(),
                                handle.getName() + "$solution",
                                handle.getDesc(),
                                handle.isInterface());
                        } else {
                            return o;
                        }
                    })
                    .toArray());
            }
        };
    }
}
