package gpup.components.target;

import java.util.*;



public class Target {

    private final String name;
    private String userData;
    private List<Target> requiredForList;
    private List<Target> dependsOnList;
    private TargetType type;
    private RunResult runResult;
    private FinishResult finishResult;



    public Target(String name) {
        this.name = name;
        requiredForList = new ArrayList<>();
        dependsOnList = new ArrayList<>();
        runResult = RunResult.FROZEN;
    }

    // Setters & Getters:
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

    public List<Target> getDependsOnList() {
        return dependsOnList;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
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

    // Methods:
    public boolean isAllAdjFinished() {
        return dependsOnList.stream().allMatch(target -> target.getRunResult().equals(RunResult.FINISHED));
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Target target = (Target) o;
//        return name.equals(target.name) && Objects.equals(requiredForList, target.requiredForList) && Objects.equals(dependsOnList, target.dependsOnList) && type == target.type;
//    }

//    @Override
//    pub lic int hashCode() {
//        return Objects.hash(name, requiredForList, dependsOnList, type, runResult, finishResult);
//    }

    public boolean isDependency(Target target, String type) {
        if (type.equals("dependsOn")) {
            return dependsOnList.contains(target);
        } else {
            return requiredForList.contains(target);
        }
    }
}
