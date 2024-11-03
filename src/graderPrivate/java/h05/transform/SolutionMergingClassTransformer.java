package h05.transform;

import h05.transform.util.TransformationContext;
import h05.transform.util.TransformationUtils;
import org.objectweb.asm.*;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SolutionMergingClassTransformer implements ClassTransformer {

    private final TransformationContext transformationContext;

    public SolutionMergingClassTransformer(String projectPrefix, List<String> availableSolutionClasses) {
        Map<String, SolutionClassNode> solutionClasses = new HashMap<>();
        Map<String, SubmissionClassInfo> submissionClasses = new ConcurrentHashMap<>();
        this.transformationContext = new TransformationContext(projectPrefix, solutionClasses, submissionClasses);
        availableSolutionClasses.stream()
            .map(s -> s.replace('.', '/'))
            .forEach(className -> solutionClasses.put(className, TransformationUtils.readSolutionClass(className)));
    }

    @Override
    public String getName() {
        return SolutionMergingClassTransformer.class.getSimpleName();
    }

    @Override
    public int getWriterFlags() {
        return ClassWriter.COMPUTE_MAXS;
    }

    @Override
    public void transform(ClassReader reader, ClassWriter writer) {
        String submissionClassName = reader.getClassName();
        reader.accept(new SubmissionClassVisitor(writer, transformationContext, submissionClassName), 0);
    }
}
