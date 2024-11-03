package h05.transform.util;

import h05.transform.SolutionClassNode;
import h05.transform.SubmissionClassInfo;

import java.util.Map;

public class TransformationContext {

    private final String projectPrefix;
    private final Map<String, SolutionClassNode> solutionClasses;
    private final Map<String, SubmissionClassInfo> submissionClasses;

    public TransformationContext(String projectPrefix,
                                 Map<String, SolutionClassNode> solutionClasses,
                                 Map<String, SubmissionClassInfo> submissionClasses) {
        this.projectPrefix = projectPrefix;
        this.solutionClasses = solutionClasses;
        this.submissionClasses = submissionClasses;
    }

    public String getProjectPrefix() {
        return projectPrefix;
    }

    public Map<String, SolutionClassNode> getSolutionClasses() {
        return solutionClasses;
    }

    public Map<String, SubmissionClassInfo> getSubmissionClasses() {
        return submissionClasses;
    }

    public SubmissionClassInfo getSubmissionClassInfo(String submissionClassName) {
        return submissionClasses.computeIfAbsent(submissionClassName,
            className -> TransformationUtils.readSubmissionClass(this, className));
    }
}
