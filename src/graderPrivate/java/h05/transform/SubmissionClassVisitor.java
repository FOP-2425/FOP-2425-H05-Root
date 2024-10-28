package h05.transform;

import kotlin.Pair;
import kotlin.Triple;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.tudalgo.algoutils.tutor.general.match.MatchingUtils;

import java.util.*;

import static h05.transform.TransformationUtils.*;
import static org.objectweb.asm.Opcodes.*;

class SubmissionClassVisitor extends ClassVisitor {

    private final boolean defaultTransformationsOnly;
    private final String className;
    private final String projectPrefix;
    private final ForceSignatureAnnotationProcessor fsAnnotationProcessor;

    private final Map<String, FieldNode> fieldNodes;
    private final Map<String, String> fieldNameMapping = new HashMap<>();
    private final Map<String, FieldReplacement.FieldHeader> visitedFields = new HashMap<>();
    private final Map<String, Object> constantFieldValues = new HashMap<>();

    private final Map<String, Map<String, MethodNode>> methodNodes;
    private final Map<String, List<String>> visitedMethods = new HashMap<>();

    SubmissionClassVisitor(ClassVisitor classVisitor, String className, String projectPrefix) {
        this(classVisitor, className, projectPrefix, null, null);
    }

    SubmissionClassVisitor(ClassVisitor classVisitor,
                           String className,
                           String projectPrefix,
                           ForceSignatureAnnotationProcessor forceSignatureAnnotationProcessor,
                           SolutionClassNode solutionClassNode) {
        super(ASM9, classVisitor);
        this.className = className;
        this.projectPrefix = projectPrefix;
        this.fsAnnotationProcessor = forceSignatureAnnotationProcessor;

        if (forceSignatureAnnotationProcessor != null && solutionClassNode != null) {
            this.defaultTransformationsOnly = false;
            this.fieldNodes = solutionClassNode.getFields();
            this.methodNodes = solutionClassNode.getMethods();
        } else {
            this.defaultTransformationsOnly = true;
            this.fieldNodes = Collections.emptyMap();
            this.methodNodes = Collections.emptyMap();
        }
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (defaultTransformationsOnly) {
            return super.visitField(access, name, descriptor, signature, value);
        }

        String computedName;
        if (fsAnnotationProcessor.fieldIdentifierIsForced(name)) {
            computedName = fsAnnotationProcessor.forcedFieldIdentifier(name);
        } else {
            // find best matching field in solution
            computedName = fieldNodes.keySet()
                .stream()
                .map(s -> new Pair<>(s, MatchingUtils.similarity(name, s)))
                .filter(pair -> pair.getSecond() >= 0.90)
                .max(Comparator.comparing(Pair::getSecond))
                .map(Pair::getFirst)
                .orElse(name);  // if no match was found, assume the field was added by the submission's author
        }
        fieldNameMapping.put(name, computedName);
        // if field is static and has a default value (outside of <clinit>)
        if ((access & ACC_STATIC) != 0 && value != null) {
            constantFieldValues.put(computedName, value);  // record for initialization later
        }
        visitedFields.put(computedName, new FieldReplacement.FieldHeader(access, name, descriptor, signature));
        return super.visitField(access,
            computedName,
            FieldReplacement.INTERNAL_DESCRIPTOR,
            "L%s<%s>;".formatted(FieldReplacement.INTERNAL_NAME, signature),
            null);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        String methodName = name;
        visitedMethods.computeIfAbsent(name, k -> new ArrayList<>()).add(descriptor);

        return new MethodVisitor(ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
            @Override
            public void visitCode() {
                // if method is abstract, skip transformation
                if ((access & ACC_ABSTRACT) != 0) {
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
                    // replay instructions from solution, if present
                    Optional.of(methodNodes.get(name))
                        .map(map -> map.get(descriptor))
                        .ifPresent(methodNode -> methodNode.accept(getDelegate()));
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
                // else execute original code
                super.visitCode();
            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                // skip transformation if only default transformations are applied or owner is not part of the submission
                if (defaultTransformationsOnly || !owner.startsWith(projectPrefix)) {
                    super.visitFieldInsn(opcode, owner, name, descriptor);
                } else {
                    // writing to field
                    if (opcode == PUTFIELD || opcode == PUTSTATIC) {
                        boxType(getDelegate(), Type.getType(descriptor));
                        if (methodName.equals("<init>")) { // if in constructor, initialize fields
                            FieldReplacement.FieldHeader originalFieldHeader = visitedFields.get(name);

                            super.visitTypeInsn(NEW, FieldReplacement.INTERNAL_NAME);
                            super.visitInsn(DUP_X1);
                            super.visitInsn(SWAP); // swap to keep actual value on top of stack
                            super.visitLdcInsn(originalFieldHeader.access());
                            super.visitInsn(SWAP); // keep swapping...
                            super.visitLdcInsn(originalFieldHeader.descriptor());
                            super.visitInsn(SWAP); // and swapping...
                            if (originalFieldHeader.signature() == null) { // signature may be null, LDC doesn't like that
                                super.visitInsn(ACONST_NULL);
                            } else {
                                super.visitLdcInsn(originalFieldHeader.signature());
                            }
                            super.visitInsn(SWAP); // last one
                            super.visitMethodInsn(INVOKESPECIAL,
                                FieldReplacement.INTERNAL_NAME,
                                "<init>",
                                "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/Object;)V",
                                false);
                            super.visitFieldInsn(opcode, owner, fieldNameMapping.get(name), FieldReplacement.INTERNAL_DESCRIPTOR);
                        } else {
                            if (opcode == PUTFIELD) {
                                super.visitInsn(SWAP); // need the objectref to get the field
                                super.visitFieldInsn(GETFIELD, owner, fieldNameMapping.get(name), FieldReplacement.INTERNAL_DESCRIPTOR);
                            } else {
                                super.visitFieldInsn(GETSTATIC, owner, fieldNameMapping.get(name), FieldReplacement.INTERNAL_DESCRIPTOR);
                            }
                            super.visitInsn(SWAP); // swap value and fieldref for invocation
                            super.visitMethodInsn(INVOKEVIRTUAL,
                                FieldReplacement.INTERNAL_NAME,
                                "set",
                                "(Ljava/lang/Object;)V",
                                false);
                        }
                    } else { // reading from field
                        super.visitFieldInsn(opcode, owner, fieldNameMapping.getOrDefault(name, name), FieldReplacement.INTERNAL_DESCRIPTOR);
                        super.visitMethodInsn(INVOKEVIRTUAL,
                            FieldReplacement.INTERNAL_NAME,
                            "get",
                            "()Ljava/lang/Object;",
                            false);
                        unboxType(getDelegate(), Type.getType(descriptor));
                    }
                }
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            }

            @Override
            public void visitEnd() {
                // add initialization for static fields with Integer, Float, Long, Double or String values
                if (methodName.equals("<clinit>")) {
                    constantFieldValues.forEach((name, value) -> {
                        FieldReplacement.FieldHeader originalFieldHeader = visitedFields.get(name);

                        super.visitTypeInsn(NEW, FieldReplacement.INTERNAL_NAME);
                        super.visitInsn(DUP);
                        super.visitLdcInsn(originalFieldHeader.access());
                        super.visitLdcInsn(originalFieldHeader.descriptor());
                        super.visitLdcInsn(originalFieldHeader.signature());
                        super.visitLdcInsn(value);
                        boxType(getDelegate(), Type.getType(originalFieldHeader.descriptor()));
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
        // add missing methods (including lambdas)
        methodNodes.entrySet()
            .stream()
            .flatMap(entry -> entry.getValue()
                .entrySet()
                .stream()
                .map(subentry -> new Triple<>(entry.getKey(), subentry.getKey(), subentry.getValue())))
            .filter(triple -> !visitedMethods.containsKey(triple.getFirst()) || !visitedMethods.get(triple.getFirst()).contains(triple.getSecond()))
            .map(Triple::getThird)
            .forEach(methodNode -> methodNode.accept(getDelegate()));
        super.visitEnd();
    }
}
