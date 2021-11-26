package gpup.component.target;

import java.time.Duration;
import java.util.*;

public class Target {

    private final String name;
    private String userData;
    private List<Target> requiredForList;
    private List<Target> dependsOnList;
    private TargetType type;
    private RunResult runResult;
    private FinishResult finishResult;
    private List<Target> justOpenedList = new ArrayList<>();
    private List<Target> skippedList = new ArrayList<>();
    private Duration taskRunDuration;

    public Target(String name) {
        this.name = name;
        requiredForList = new ArrayList<>();
        dependsOnList = new ArrayList<>();
        runResult = RunResult.FROZEN;
    }

    public String getName() {
        return name;
    }

    public TargetType getType() {
        return type;
    }

    public RunResult getRunResult() {
        return runResult;
    }

    public FinishResult getFinishResult() {
        return finishResult;
    }

    public List<Target> getRequiredForList() {
        return requiredForList;
    }

    public List<Target> getJustOpenedList() {
        return justOpenedList;
    }

    public List<Target> getDependsOnList() {
        return dependsOnList;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public void setTaskRunDuration(Duration taskRunDuration) {
        this.taskRunDuration = taskRunDuration;
    }

    public void setRunResult(RunResult runResult) {
        this.runResult = runResult;
    }

    public void setFinishResult(FinishResult finishResult) {
        this.finishResult = finishResult;
    }

    public void setType(TargetType type) {
        this.type = type;
    }

    public boolean isAllAdjFinished() {
        return dependsOnList.stream().allMatch(target -> (target.getRunResult().equals(RunResult.FINISHED)));
        //||target.getRunResult().equals(RunResult.SKIPPED)));
    }

    public boolean isAllAdjFinishedWithoutFailure() {
        if (isAllAdjFinished()) {
            return dependsOnList.stream().allMatch(target -> (target.getFinishResult().equals(FinishResult.SUCCESS) || target.getFinishResult().equals(FinishResult.WARNING)));
        } else {
            return false;
        }
    }

    public void addDependOnTarget(Target target) {
        if (!dependsOnList.contains(target))
            dependsOnList.add(target);
    }

    public void addRequiredForTarget(Target target) {
        if (!requiredForList.contains(target)) {
            requiredForList.add(target);
        }
    }

    public boolean isDependency(Target target, String type) {
        if (type.equals("dependsOn")) {
            return dependsOnList.contains(target);
        } else {
            return requiredForList.contains(target);
        }
    }

    public List<Target> getSkippedList() {
        return skippedList;
    }

    public void addToJustOpenedList(Target target) {
        justOpenedList.add(target);
    }

    public void clearHelpingLists() {
        justOpenedList.clear();
        skippedList.clear();
    }

    public Duration getTaskRunDuration() {
        return taskRunDuration;
    }
}
