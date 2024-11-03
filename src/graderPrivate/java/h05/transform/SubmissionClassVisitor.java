package h05.transform;

import h05.transform.util.FieldHeader;
import h05.transform.util.MethodHeader;
import h05.transform.util.TransformationContext;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;

import static h05.transform.util.TransformationUtils.*;
import static org.objectweb.asm.Opcodes.*;

class SubmissionClassVisitor extends ClassVisitor {

    private final boolean defaultTransformationsOnly;
    private final TransformationContext transformationContext;
    private final String className;
    private final SubmissionClassInfo submissionClassInfo;

    private final Set<FieldHeader> visitedFields = new HashSet<>();
    private final Map<FieldHeader, FieldNode> solutionFieldNodes;

    private final Set<MethodHeader> visitedMethods = new HashSet<>();
    private final Map<MethodHeader, MethodNode> solutionMethodNodes;

    SubmissionClassVisitor(ClassVisitor classVisitor,
                           TransformationContext transformationContext,
                           String submissionClassName) {
        super(ASM9, classVisitor);
        this.transformationContext = transformationContext;
        this.className = transformationContext.getSubmissionClassInfo(submissionClassName).getComputedClassName();
        this.submissionClassInfo = transformationContext.getSubmissionClassInfo(submissionClassName);

        Optional<SolutionClassNode> solutionClass = submissionClassInfo.getSolutionClass();
        if (solutionClass.isPresent()) {
            this.defaultTransformationsOnly = false;
            this.solutionFieldNodes = solutionClass.get().getFields();
            this.solutionMethodNodes = solutionClass.get().getMethods();
        } else {
            System.err.printf("No corresponding solution class found for %s. Only applying default transformations.%n", submissionClassName);
            this.defaultTransformationsOnly = true;
            this.solutionFieldNodes = Collections.emptyMap();
            this.solutionMethodNodes = Collections.emptyMap();
        }
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (defaultTransformationsOnly) {
            return super.visitField(access, name, descriptor, signature, value);
        }

        FieldHeader fieldHeader = submissionClassInfo.getComputedFieldHeader(name);
        visitedFields.add(fieldHeader);
        return fieldHeader.toFieldVisitor(getDelegate(), value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodHeader methodHeader = submissionClassInfo.getComputedMethodHeader(name, descriptor);
        visitedMethods.add(methodHeader);

        return new MethodVisitor(ASM9, methodHeader.toMethodVisitor(getDelegate())) {
            @Override
            public void visitCode() {
                // if method is abstract or lambda, skip transformation
                if ((access & ACC_ABSTRACT) != 0 || ((access & ACC_SYNTHETIC) != 0 && name.startsWith("lambda$"))) {
                    super.visitCode();
                    return;
                }

                Type[] argumentTypes = Type.getArgumentTypes(descriptor);
                Label substitutionCheckLabel = new Label();
                Label delegationCheckLabel = new Label();
                Label submissionCodeLabel = new Label();

                super.visitMethodInsn(INVOKESTATIC,
                    SubmissionExecutionHandler.INTERNAL_NAME,
                    "getInstance",
                    "()" + SubmissionExecutionHandler.INTERNAL_DESCRIPTOR,
                    false);

                // check if invocation should be logged
                super.visitInsn(DUP);
                super.visitLdcInsn(className);
                super.visitLdcInsn(name);
                super.visitLdcInsn(descriptor);
                super.visitMethodInsn(INVOKEVIRTUAL,
                    SubmissionExecutionHandler.INTERNAL_NAME,
                    "logInvocation",
                    "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z",
                    false);
                super.visitJumpInsn(IFEQ, name.equals("<init>") ? delegationCheckLabel : substitutionCheckLabel); // jump to label if logInvocation(...) == false
                // intercept parameters
                super.visitInsn(DUP); // duplicate SubmissionExecutionHandler reference
                super.visitLdcInsn(className);
                super.visitLdcInsn(name);
                super.visitLdcInsn(descriptor);
                buildInvocation(argumentTypes);
                super.visitMethodInsn(INVOKEVIRTUAL,
                    SubmissionExecutionHandler.INTERNAL_NAME,
                    "addInvocation",
                    "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;L" + SubmissionExecutionHandler.Invocation.INTERNAL_NAME + ";)V",
                    false);

                // check if substitution exists for this method if not constructor (because waaay too complex right now)
                if (!name.equals("<init>")) {
                    super.visitFrame(F_SAME1, 0, null, 1, new Object[]{SubmissionExecutionHandler.INTERNAL_NAME});
                    super.visitLabel(substitutionCheckLabel);
                    super.visitInsn(DUP);
                    super.visitLdcInsn(className);
                    super.visitLdcInsn(name);
                    super.visitLdcInsn(descriptor);
                    super.visitMethodInsn(INVOKEVIRTUAL,
                        SubmissionExecutionHandler.INTERNAL_NAME,
                        "useSubstitution",
                        "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z",
                        false);
                    super.visitJumpInsn(IFEQ, delegationCheckLabel); // jump to label if useSubstitution(...) == false
                    super.visitLdcInsn(className);
                    super.visitLdcInsn(name);
                    super.visitLdcInsn(descriptor);
                    super.visitMethodInsn(INVOKEVIRTUAL,
                        SubmissionExecutionHandler.INTERNAL_NAME,
                        "getSubstitution",
                        "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)L" + SubmissionExecutionHandler.MethodSubstitution.INTERNAL_NAME + ";",
                        false);
                    buildInvocation(argumentTypes);
                    super.visitMethodInsn(INVOKEINTERFACE,
                        SubmissionExecutionHandler.MethodSubstitution.INTERNAL_NAME,
                        "execute",
                        "(L" + SubmissionExecutionHandler.Invocation.INTERNAL_NAME + ";)Ljava/lang/Object;",
                        true);
                    Type returnType = Type.getReturnType(descriptor);
                    if (returnType.getSort() == Type.ARRAY || returnType.getSort() == Type.OBJECT) {
                        super.visitTypeInsn(CHECKCAST, returnType.getInternalName());
                    } else {
                        unboxType(getDelegate(), returnType);
                    }
                    super.visitInsn(returnType.getOpcode(IRETURN));
                }

                // else check if call should be delegated to solution or not
                super.visitFrame(F_SAME1, 0, null, 1, new Object[] {SubmissionExecutionHandler.INTERNAL_NAME});
                super.visitLabel(delegationCheckLabel);
                // if only default transformations are applied, disable delegation
                if (defaultTransformationsOnly) {
                    super.visitInsn(POP); // remove SubmissionExecutionHandler instance from stack
                } else {
                    super.visitLdcInsn(className);
                    super.visitLdcInsn(name);
                    super.visitLdcInsn(descriptor);
                    super.visitMethodInsn(INVOKEVIRTUAL,
                        SubmissionExecutionHandler.INTERNAL_NAME,
                        "useStudentImpl",
                        "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z",
                        false);
                    super.visitJumpInsn(IFNE, submissionCodeLabel); // jump to label if useStudentImpl(...) == true
                    // replay instructions from solution
                    solutionMethodNodes.get(methodHeader).accept(getDelegate());
                }

                // calculate the frame for the beginning of the submission code
                Object[] parameterTypes = Arrays.stream(argumentTypes)
                    .map(type -> switch (type.getSort()) {
                        case Type.BOOLEAN, Type.BYTE, Type.SHORT, Type.CHAR, Type.INT -> INTEGER;
                        case Type.FLOAT -> FLOAT;
                        case Type.LONG -> LONG;
                        case Type.DOUBLE -> DOUBLE;
                        default -> type.getInternalName();
                    })
                    .toArray();
                if ((access & ACC_STATIC) == 0) { // if method is not static
                    Object[] types = new Object[parameterTypes.length + 1];
                    types[0] = name.equals("<init>") ? UNINITIALIZED_THIS : className;
                    System.arraycopy(parameterTypes, 0, types, 1, parameterTypes.length);
                    super.visitFrame(F_FULL, types.length, types, 0, null);
                } else {
                    super.visitFrame(F_FULL, parameterTypes.length, parameterTypes, 0, null);
                }
                super.visitLabel(submissionCodeLabel);
                // visit original code
                super.visitCode();
            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                // skip transformation if only default transformations are applied or owner is not part of the submission
                if (defaultTransformationsOnly || !owner.startsWith(transformationContext.getProjectPrefix())) {
                    super.visitFieldInsn(opcode, owner, name, descriptor);
                } else {
                    FieldHeader fieldHeader = transformationContext.getSubmissionClassInfo(owner).getComputedFieldHeader(name);
                    super.visitFieldInsn(opcode, fieldHeader.owner(), fieldHeader.name(), fieldHeader.descriptor());
                }
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                // skip transformation if only default transformations are applied or owner is not part of the submission
                if (defaultTransformationsOnly || !owner.startsWith(transformationContext.getProjectPrefix())) {
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                } else {
                    MethodHeader methodHeader = transformationContext.getSubmissionClassInfo(owner).getComputedMethodHeader(name, descriptor);
                    super.visitMethodInsn(opcode, methodHeader.owner(), methodHeader.name(), methodHeader.descriptor(), isInterface);
                }
            }

            private void buildInvocation(Type[] argumentTypes) {
                super.visitTypeInsn(NEW, SubmissionExecutionHandler.Invocation.INTERNAL_NAME);
                super.visitInsn(DUP);
                if ((access & ACC_STATIC) == 0 && !name.equals("<init>")) {
                    super.visitVarInsn(ALOAD, 0);
                    super.visitMethodInsn(INVOKESPECIAL, SubmissionExecutionHandler.Invocation.INTERNAL_NAME, "<init>", "(Ljava/lang/Object;)V", false);
                } else {
                    super.visitMethodInsn(INVOKESPECIAL, SubmissionExecutionHandler.Invocation.INTERNAL_NAME, "<init>", "()V", false);
                }
                for (int i = 0; i < argumentTypes.length; i++) {
                    super.visitInsn(DUP);
                    // load parameter with opcode (ALOAD, ILOAD, etc.) for type and ignore "this", if it exists
                    super.visitVarInsn(argumentTypes[i].getOpcode(ILOAD), getLocalsIndex(argumentTypes, i) + ((access & ACC_STATIC) == 0 ? 1 : 0));
                    boxType(getDelegate(), argumentTypes[i]);
                    super.visitMethodInsn(INVOKEVIRTUAL,
                        SubmissionExecutionHandler.Invocation.INTERNAL_NAME,
                        "addParameter",
                        "(Ljava/lang/Object;)V",
                        false);
                }
            }
        };
    }

    @Override
    public void visitEnd() {
        if (!defaultTransformationsOnly) {
            // add missing fields
            solutionFieldNodes.entrySet()
                .stream()
                .filter(entry -> !visitedFields.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(fieldNode -> fieldNode.accept(getDelegate()));
            // add missing methods (including lambdas)
            solutionMethodNodes.entrySet()
                .stream()
                .filter(entry -> !visitedMethods.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(methodNode -> methodNode.accept(getDelegate()));
        }
        super.visitEnd();
    }
}
