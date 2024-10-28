package h05.transform;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static h05.transform.TransformationUtils.boxType;
import static h05.transform.TransformationUtils.unboxType;
import static org.objectweb.asm.Opcodes.*;

class SolutionClassNode extends ClassNode {

    private final String className;
    private final Map<String, FieldNode> fields = new HashMap<>();
    private final Map<String, Map<String, MethodNode>> methods = new HashMap<>();

    private final Map<String, String> fieldDescriptors = new HashMap<>();
    private final Map<String, Object> constantFieldValues = new HashMap<>();

    SolutionClassNode(String className) {
        super(Opcodes.ASM9);
        this.className = className;
    }

    public Map<String, FieldNode> getFields() {
        return fields;
    }

    public Map<String, Map<String, MethodNode>> getMethods() {
        return methods;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        fieldDescriptors.put(name, descriptor);
        // if field is static and has a default value (outside of <clinit>)
        if ((access & ACC_STATIC) != 0 && value != null) {
            constantFieldValues.put(name, value);  // record for initialization later
        }
        return super.visitField(access,
            name,
            FieldReplacement.INTERNAL_DESCRIPTOR,
            "L%s<%s>;".formatted(FieldReplacement.INTERNAL_NAME, signature),
            null);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodNode methodNode;
        if ((access & ACC_SYNTHETIC) != 0 && name.startsWith("lambda$")) {  // if method is lambda
            methodNode = getMethodNode(access, name + "$solution", descriptor, signature, exceptions);
        } else {
            methodNode = getMethodNode(access, name, descriptor, signature, exceptions);
        }
        super.methods.add(methodNode);
        return methodNode;
    }

    @Override
    public void visitEnd() {
        for (FieldNode fieldNode : super.fields) {
            fields.put(fieldNode.name, fieldNode);
        }
        for (MethodNode methodNode : super.methods) {
            methods.computeIfAbsent(methodNode.name, k -> new HashMap<>())
                .put(methodNode.desc, methodNode);
        }
    }

    private MethodNode getMethodNode(int access, String name, String descriptor, String signature, String[] exceptions) {
        String methodName = name;

        return new MethodNode(ASM9, access, name, descriptor, signature, exceptions) {
            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                // writing to field
                if (opcode == PUTFIELD || opcode == PUTSTATIC) {
                    boxType(this, Type.getType(descriptor));
                    if (methodName.equals("<init>")) { // if in constructor, initialize fields
                        super.visitTypeInsn(NEW, FieldReplacement.INTERNAL_NAME);
                        super.visitInsn(DUP_X1);
                        super.visitInsn(SWAP); // swap to keep actual value on top of stack
                        super.visitMethodInsn(INVOKESPECIAL,
                            FieldReplacement.INTERNAL_NAME,
                            "<init>",
                            "(Ljava/lang/Object;)V",
                            false);
                        super.visitFieldInsn(opcode, owner, name, FieldReplacement.INTERNAL_DESCRIPTOR);
                    } else {
                        if (opcode == PUTFIELD) {
                            super.visitInsn(SWAP); // need the objectref to get the field
                            super.visitFieldInsn(GETFIELD, owner, name, FieldReplacement.INTERNAL_DESCRIPTOR);
                        } else {
                            super.visitFieldInsn(GETSTATIC, owner, name, FieldReplacement.INTERNAL_DESCRIPTOR);
                        }
                        super.visitInsn(SWAP); // swap value and fieldref for invocation
                        super.visitMethodInsn(INVOKEVIRTUAL,
                            FieldReplacement.INTERNAL_NAME,
                            "set",
                            "(Ljava/lang/Object;)V",
                            false);
                    }
                } else { // reading from field
                    super.visitFieldInsn(opcode, owner, name, FieldReplacement.INTERNAL_DESCRIPTOR);
                    super.visitMethodInsn(INVOKEVIRTUAL,
                        FieldReplacement.INTERNAL_NAME,
                        "get",
                        "()Ljava/lang/Object;",
                        false);
                    unboxType(this, Type.getType(descriptor));
                }
            }

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

            @Override
            public void visitEnd() {
                if (methodName.equals("<clinit>")) {
                    constantFieldValues.forEach((name, value) -> {
                        super.visitTypeInsn(NEW, FieldReplacement.INTERNAL_NAME);
                        super.visitInsn(DUP);
                        super.visitLdcInsn(value);
                        boxType(this, Type.getType(fieldDescriptors.get(name)));
                        super.visitMethodInsn(INVOKESPECIAL,
                            FieldReplacement.INTERNAL_NAME,
                            "<init>",
                            "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/Object;)V",
                            false);
                        super.visitFieldInsn(PUTSTATIC, className, name, FieldReplacement.INTERNAL_DESCRIPTOR);
                    });
                }
                super.visitEnd();
            }
        };
    }
}
