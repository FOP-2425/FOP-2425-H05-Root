package h05.transform.util;

import org.objectweb.asm.*;
import transform.ForceSignature;

import java.util.*;

public class ForceSignatureAnnotationProcessor {

    // Class / Type level
    private String forcedClassIdentifier;

    // Field level
    private final Map<FieldHeader, FieldHeader> forcedFieldIdentifiers = new HashMap<>();

    // Constructor / Method level
    private final Map<MethodHeader, MethodHeader> forcedMethodSignatures = new HashMap<>();

    public ForceSignatureAnnotationProcessor(ClassReader reader) {
        reader.accept(new ClassLevelVisitor(reader.getClassName()), 0);
    }

    public boolean classIdentifierIsForced() {
        return forcedClassIdentifier != null;
    }

    public String forcedClassIdentifier() {
        return forcedClassIdentifier.replace('.', '/');
    }

    public boolean fieldIdentifierIsForced(String identifier) {
        return forcedFieldHeader(identifier) != null;
    }

    public FieldHeader forcedFieldHeader(String identifier) {
        return forcedFieldIdentifiers.entrySet()
            .stream()
            .filter(entry -> identifier.equals(entry.getKey().name()))
            .findAny()
            .map(Map.Entry::getValue)
            .orElse(null);
    }

    public boolean methodSignatureIsForced(String identifier, String descriptor) {
        return forcedMethodHeader(identifier, descriptor) != null;
    }

    public MethodHeader forcedMethodHeader(String identifier, String descriptor) {
        return forcedMethodSignatures.entrySet()
            .stream()
            .filter(entry -> identifier.equals(entry.getKey().name()) && descriptor.equals(entry.getKey().descriptor()))
            .findAny()
            .map(Map.Entry::getValue)
            .orElse(null);
    }

    private class ClassLevelVisitor extends ClassVisitor {

        private final String name;

        private ForceSignatureAnnotationVisitor annotationVisitor;
        private final List<FieldLevelVisitor> fieldLevelVisitors = new ArrayList<>();
        private final List<MethodLevelVisitor> methodLevelVisitors = new ArrayList<>();

        private ClassLevelVisitor(String name) {
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

        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            FieldLevelVisitor fieldLevelVisitor = new FieldLevelVisitor(this.name, access, name, descriptor, signature);
            fieldLevelVisitors.add(fieldLevelVisitor);
            return fieldLevelVisitor;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodLevelVisitor methodLevelVisitor = new MethodLevelVisitor(this.name, access, name, descriptor, signature, exceptions);
            methodLevelVisitors.add(methodLevelVisitor);
            return methodLevelVisitor;
        }

        @Override
        public void visitEnd() {
            forcedClassIdentifier = annotationVisitor != null ? annotationVisitor.identifier : null;

            for (FieldLevelVisitor fieldLevelVisitor : fieldLevelVisitors) {
                ForceSignatureAnnotationVisitor annotationVisitor = fieldLevelVisitor.annotationVisitor;
                if (annotationVisitor == null) continue;
                forcedFieldIdentifiers.put(
                    new FieldHeader(fieldLevelVisitor.owner,
                        fieldLevelVisitor.access,
                        fieldLevelVisitor.name,
                        fieldLevelVisitor.descriptor,
                        fieldLevelVisitor.signature),
                    new FieldHeader(fieldLevelVisitor.owner,
                        fieldLevelVisitor.access,
                        annotationVisitor.identifier,
                        fieldLevelVisitor.descriptor,
                        fieldLevelVisitor.signature)
                );
            }

            for (MethodLevelVisitor methodLevelVisitor : methodLevelVisitors) {
                ForceSignatureAnnotationVisitor annotationVisitor = methodLevelVisitor.annotationVisitor;
                if (annotationVisitor == null) continue;
                forcedMethodSignatures.put(
                    new MethodHeader(methodLevelVisitor.owner,
                        methodLevelVisitor.access,
                        methodLevelVisitor.name,
                        methodLevelVisitor.descriptor,
                        methodLevelVisitor.signature,
                        methodLevelVisitor.exceptions),
                    new MethodHeader(methodLevelVisitor.owner,
                        methodLevelVisitor.access,
                        annotationVisitor.identifier,
                        annotationVisitor.descriptor,
                        methodLevelVisitor.signature,
                        methodLevelVisitor.exceptions)
                );
            }
        }
    }

    private static class FieldLevelVisitor extends FieldVisitor {

        private final String owner;
        private final int access;
        private final String name;
        private final String descriptor;
        private final String signature;
        private ForceSignatureAnnotationVisitor annotationVisitor;

        private FieldLevelVisitor(String owner, int access, String name, String descriptor, String signature) {
            super(Opcodes.ASM9);
            this.owner = owner;
            this.access = access;
            this.name = name;
            this.descriptor = descriptor;
            this.signature = signature;
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

        private final String owner;
        private final int access;
        private final String name;
        private final String descriptor;
        private final String signature;
        private final String[] exceptions;
        private ForceSignatureAnnotationVisitor annotationVisitor;

        private MethodLevelVisitor(String owner, int access, String name, String descriptor, String signature, String[] exceptions) {
            super(Opcodes.ASM9);
            this.owner = owner;
            this.access = access;
            this.name = name;
            this.descriptor = descriptor;
            this.signature = signature;
            this.exceptions = exceptions;
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
