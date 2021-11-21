package components.target;

import gpup.components.target.FinishResult;
import gpup.components.target.RunResult;
import gpup.components.target.Target;
import gpup.components.target.TargetType;

import java.util.ArrayList;
import java.util.List;

public class TargetDTO {
    private String name;
    private List<TargetDTO> requiredForList;
    private List<TargetDTO> dependsOnList;
    private TargetType type;
    private RunResult runResult;
    private FinishResult finishResult;

    public TargetDTO(Target target) {
        name = target.getName();
        type = target.getType();
        runResult = target.getRunResult();
        finishResult = target.getFinishResult();
        dependsOnList = updateList(target.getDependsOnList());
        requiredForList = updateList(target.getRequiredForList());
    }

    public String getName() {
        return name;
    }

    public List<TargetDTO> getRequiredForList() {
        return requiredForList;
    }

    public List<TargetDTO> getDependsOnList() {
        return dependsOnList;
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

    private List<TargetDTO> updateList(List<Target> targetList) {
        List<TargetDTO> list = new ArrayList<>();
        for (Target t : targetList) {
            list.add(new TargetDTO(t));
        }
        return list;
    }
}
