package h05.transform;

import h05.transform.util.FieldHeader;
import h05.transform.util.ForceSignatureAnnotationProcessor;
import h05.transform.util.MethodHeader;
import h05.transform.util.TransformationContext;
import kotlin.Pair;
import kotlin.Triple;
import org.objectweb.asm.*;
import org.tudalgo.algoutils.tutor.general.match.MatchingUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.objectweb.asm.Opcodes.*;

public class SubmissionClassInfo extends ClassVisitor {

    private final TransformationContext transformationContext;
    private final String originalClassName;
    private final String computedClassName;
    private final Set<Triple<String, Map<FieldHeader, FieldHeader>, Map<MethodHeader, MethodHeader>>> superClassMembers = new HashSet<>();
    private final ForceSignatureAnnotationProcessor fsAnnotationProcessor;
    private final SolutionClassNode solutionClass;

    private String superClass;
    private String[] interfaces;

    // Mapping of fields in submission => usable fields
    private final Map<FieldHeader, FieldHeader> fields = new HashMap<>();

    // Mapping of methods in submission => usable methods
    private final Map<MethodHeader, MethodHeader> methods = new HashMap<>();

    public SubmissionClassInfo(TransformationContext transformationContext,
                               String className,
                               ForceSignatureAnnotationProcessor fsAnnotationProcessor) {
        super(Opcodes.ASM9);
        this.transformationContext = transformationContext;
        this.originalClassName = className;
        this.fsAnnotationProcessor = fsAnnotationProcessor;

        if (fsAnnotationProcessor.classIdentifierIsForced()) {
            this.computedClassName = fsAnnotationProcessor.forcedClassIdentifier();
        } else {
            // If not forced, get the closest matching solution class (at least 90% similarity)
            this.computedClassName = transformationContext.getSolutionClasses()
                .keySet()
                .stream()
                .map(s -> new Pair<>(s, MatchingUtils.similarity(originalClassName, s)))
                .filter(pair -> pair.getSecond() >= 0.90)
                .max(Comparator.comparing(Pair::getSecond))
                .map(Pair::getFirst)
                .orElse(originalClassName);
        }
        this.solutionClass = transformationContext.getSolutionClasses().get(computedClassName);
    }

    public String getComputedClassName() {
        return computedClassName;
    }

    public Optional<SolutionClassNode> getSolutionClass() {
        return Optional.ofNullable(solutionClass);
    }

    public FieldHeader getComputedFieldHeader(String name) {
        return fields.entrySet()
            .stream()
            .filter(entry -> entry.getKey().name().equals(name))
            .findAny()
            .map(Map.Entry::getValue)
            .orElse(null);
    }

    public MethodHeader getComputedMethodHeader(String name, String descriptor) {
        return methods.entrySet()
            .stream()
            .filter(entry -> entry.getKey().name().equals(name) && entry.getKey().descriptor().equals(descriptor))
            .findAny()
            .map(Map.Entry::getValue)
            .orElse(null);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        resolveSuperClassMembers(superClassMembers, this.superClass = superName, this.interfaces = interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        FieldHeader submissionFieldHeader = new FieldHeader(originalClassName, access, name, descriptor, signature);
        FieldHeader solutionFieldHeader;
        if (fsAnnotationProcessor.fieldIdentifierIsForced(name)) {
            solutionFieldHeader = fsAnnotationProcessor.forcedFieldHeader(name);
        } else if (solutionClass != null) {
            solutionFieldHeader = solutionClass.getFields()
                .keySet()
                .stream()
                .map(fieldHeader -> new Pair<>(fieldHeader, MatchingUtils.similarity(name, fieldHeader.name())))
                .filter(pair -> pair.getSecond() >= 0.90)
                .max(Comparator.comparing(Pair::getSecond))
                .map(Pair::getFirst)
                .orElse(submissionFieldHeader);
        } else {
            solutionFieldHeader = submissionFieldHeader;
        }

        fields.put(submissionFieldHeader, solutionFieldHeader);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodHeader submissionMethodHeader = new MethodHeader(originalClassName, access, name, descriptor, signature, exceptions);
        if ((access & ACC_SYNTHETIC) != 0 && name.startsWith("lambda$")) {
            methods.put(submissionMethodHeader, submissionMethodHeader);
            return null;
        }

        MethodHeader solutionMethodHeader;
        if (fsAnnotationProcessor.methodSignatureIsForced(name, descriptor)) {
            solutionMethodHeader = fsAnnotationProcessor.forcedMethodHeader(name, descriptor);
        } else if (solutionClass != null) {
            solutionMethodHeader = solutionClass.getMethods()
                .keySet()
                .stream()
                .map(methodHeader -> new Triple<>(methodHeader,
                    MatchingUtils.similarity(name, methodHeader.name()),
                    MatchingUtils.similarity(descriptor, methodHeader.descriptor())))
                .filter(triple -> triple.getSecond() >= 0.90 && triple.getThird() >= 0.90)
                .max(Comparator.comparing(Triple<MethodHeader, Double, Double>::getSecond).thenComparing(Triple::getThird))
                .map(Triple::getFirst)
                .orElse(submissionMethodHeader);
        } else {
            solutionMethodHeader = submissionMethodHeader;
        }

        methods.put(submissionMethodHeader, solutionMethodHeader);
        return null;
    }

    @Override
    public void visitEnd() {
        for (Triple<String, Map<FieldHeader, FieldHeader>, Map<MethodHeader, MethodHeader>> triple : superClassMembers) {
            triple.getSecond().forEach(fields::putIfAbsent);
            triple.getThird().forEach(methods::putIfAbsent);
        }
    }

    private void resolveSuperClassMembers(Set<Triple<String, Map<FieldHeader, FieldHeader>, Map<MethodHeader, MethodHeader>>> superClassMembers,
                                          String superClass,
                                          String[] interfaces) {
        resolveSuperClassMembers(superClassMembers, superClass);
        if (interfaces != null) {
            for (String interfaceName : interfaces) {
                resolveSuperClassMembers(superClassMembers, interfaceName);
            }
        }
    }

    private void resolveSuperClassMembers(Set<Triple<String, Map<FieldHeader, FieldHeader>, Map<MethodHeader, MethodHeader>>> superClassMembers,
                                          String className) {
        if (className.startsWith(transformationContext.getProjectPrefix())) {
            SubmissionClassInfo submissionClassInfo = transformationContext.getSubmissionClassInfo(className);
            superClassMembers.add(new Triple<>(className, submissionClassInfo.fields, submissionClassInfo.methods));
            resolveSuperClassMembers(superClassMembers, submissionClassInfo.superClass, submissionClassInfo.interfaces);
        } else {
            try {
                Class<?> clazz = Class.forName(className.replace('/', '.'));
                Map<FieldHeader, FieldHeader> fieldHeaders = new HashMap<>();
                for (Field field : clazz.getDeclaredFields()) {
                    if ((field.getModifiers() & Modifier.PRIVATE) != 0) continue;
                    FieldHeader fieldHeader = new FieldHeader(
                        className,
                        field.getModifiers(),
                        field.getName(),
                        Type.getDescriptor(field.getType()),
                        null
                    );
                    fieldHeaders.put(fieldHeader, fieldHeader);
                }
                Map<MethodHeader, MethodHeader> methodHeaders = new HashMap<>();
                for (Method method : clazz.getDeclaredMethods()) {
                    if ((method.getModifiers() & Modifier.PRIVATE) != 0) continue;
                    MethodHeader methodHeader = new MethodHeader(
                        className,
                        method.getModifiers(),
                        method.getName(),
                        Type.getMethodDescriptor(method),
                        null,
                        Arrays.stream(method.getExceptionTypes()).map(Type::getInternalName).toArray(String[]::new)
                    );
                    methodHeaders.put(methodHeader, methodHeader);
                }
                superClassMembers.add(new Triple<>(className, fieldHeaders, methodHeaders));
                if (clazz.getSuperclass() != null) {
                    resolveSuperClassMembers(superClassMembers,
                        Type.getInternalName(clazz.getSuperclass()),
                        Arrays.stream(clazz.getInterfaces()).map(Type::getInternalName).toArray(String[]::new));
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
