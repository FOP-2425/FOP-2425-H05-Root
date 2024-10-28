package h05.transform;

import kotlin.Pair;
import org.objectweb.asm.*;
import transform.ForceSignature;

import java.util.*;

class ForceSignatureAnnotationProcessor {

    // Class / Type level
    private String forcedClassIdentifier;

    // Field level
    private final Map<String, String> forcedFieldIdentifiers = new HashMap<>();

    // Constructor / Method level
    private final Map<String, Map<String, Pair<String, String>>> forcedMethodSignatures = new HashMap<>();

    public ForceSignatureAnnotationProcessor(ClassReader reader) {
        reader.accept(new ClassLevelVisitor(), 0);
    }

    public boolean classIdentifierIsForced() {
        return forcedClassIdentifier != null;
    }

    public String forcedClassIdentifier() {
        return forcedClassIdentifier.replace('.', '/');
    }

    public boolean fieldIdentifierIsForced(String identifier) {
        return forcedFieldIdentifiers.containsKey(identifier);
    }

    public String forcedFieldIdentifier(String identifier) {
        return forcedFieldIdentifiers.getOrDefault(identifier, null);
    }

    public boolean methodSignatureIsForced(String identifier, String descriptor) {
        return forcedMethodSignatures.containsKey(identifier) && forcedMethodSignatures.get(identifier).containsKey(descriptor);
    }

    public Pair<String, String> forcedMethodIdentifier(String identifier, String descriptor) {
        return forcedMethodSignatures.getOrDefault(identifier, Collections.emptyMap()).get(descriptor);
    }

    private class ClassLevelVisitor extends ClassVisitor {

        private ForceSignatureAnnotationVisitor annotationVisitor;
        private final List<FieldLevelVisitor> fieldLevelVisitors = new ArrayList<>();
        private final List<MethodLevelVisitor> methodLevelVisitors = new ArrayList<>();

        private ClassLevelVisitor() {
            super(Opcodes.ASM9);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            if (descriptor.equals(ForceSignature.INTERNAL_DESCRIPTOR)) {
                return annotationVisitor = new ForceSignatureAnnotationVisitor();
            } else {
                return null;
            }
        }

        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            FieldLevelVisitor fieldLevelVisitor = new FieldLevelVisitor(name);
            fieldLevelVisitors.add(fieldLevelVisitor);
            return fieldLevelVisitor;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodLevelVisitor methodLevelVisitor = new MethodLevelVisitor(name, descriptor);
            methodLevelVisitors.add(methodLevelVisitor);
            return methodLevelVisitor;
        }

        @Override
        public void visitEnd() {
            forcedClassIdentifier = annotationVisitor != null ? annotationVisitor.identifier : null;

            for (FieldLevelVisitor fieldLevelVisitor : fieldLevelVisitors) {
                if (fieldLevelVisitor.annotationVisitor == null) continue;
                forcedFieldIdentifiers.put(fieldLevelVisitor.name, fieldLevelVisitor.annotationVisitor.identifier);
            }

            for (MethodLevelVisitor methodLevelVisitor : methodLevelVisitors) {
                if (methodLevelVisitor.annotationVisitor == null) continue;
                Pair<String, String> methodSignature = new Pair<>(methodLevelVisitor.annotationVisitor.identifier, methodLevelVisitor.annotationVisitor.descriptor);
                forcedMethodSignatures.computeIfAbsent(methodLevelVisitor.name, k -> new HashMap<>())
                    .put(methodLevelVisitor.descriptor, methodSignature);
            }
        }
    }

    private static class FieldLevelVisitor extends FieldVisitor {

        private final String name;
        private ForceSignatureAnnotationVisitor annotationVisitor;

        private FieldLevelVisitor(String name) {
            super(Opcodes.ASM9);
            this.name = name;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            if (descriptor.equals(ForceSignature.INTERNAL_DESCRIPTOR)) {
                return annotationVisitor = new ForceSignatureAnnotationVisitor();
            } else {
                return null;
            }
        }
    }

    private static class MethodLevelVisitor extends MethodVisitor {

        private final String name;
        private final String descriptor;
        private ForceSignatureAnnotationVisitor annotationVisitor;

        private MethodLevelVisitor(String name, String descriptor) {
            super(Opcodes.ASM9);
            this.name = name;
            this.descriptor = descriptor;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            if (descriptor.equals(ForceSignature.INTERNAL_DESCRIPTOR)) {
                return annotationVisitor = new ForceSignatureAnnotationVisitor();
            } else {
                return null;
            }
        }
    }

    private static class ForceSignatureAnnotationVisitor extends AnnotationVisitor {

        private String identifier;
        private String descriptor;

        ForceSignatureAnnotationVisitor() {
            super(Opcodes.ASM9);
        }

        @Override
        public void visit(String name, Object value) {
            if (name.equals("identifier")) {
                identifier = (String) value;
            } else if (name.equals("descriptor")) {
                descriptor = (String) value;
            }
        }
    }
}
