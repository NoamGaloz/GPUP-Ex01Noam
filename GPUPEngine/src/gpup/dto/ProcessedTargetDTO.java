package gpup.dto;

import gpup.components.target.FinishResult;
import gpup.components.target.Target;

import java.util.List;

public class ProcessedTargetDTO implements ConsumerDTO {
    private final String name;
    private final String userData;
    private final FinishResult finishResult;
    private final String justOpenedList;
    private final String justSkippedList;
    private TaskOutputDTO taskOutput;

    public ProcessedTargetDTO(Target target) {
        this.name = target.getName();
        this.userData = target.getUserData() == null ? "No Data to show." : target.getUserData();
        this.finishResult = target.getFinishResult();
        justOpenedList = listToSting(target.getJustOpenedList());
        justSkippedList = listToSting(target.GetSkippedList());
    }

    private String listToSting(List<Target> list) {
        if (list.size() == 0) {
            return "None";
        } else {
            StringBuilder str = new StringBuilder();
            list.forEach(target -> {
                str.append(target.getName() + ",");
            });
            str.deleteCharAt(str.length() - 1);
            return str.toString();
        }
    }

    @Override
    public void setTaskOutput(TaskOutputDTO taskOutput) {
        this.taskOutput = taskOutput;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("START process target: " + name + "\n");
        output.append(taskOutput.toString());
        output.append("Target Data:\n   " + userData + "\n");
        output.append("FINISH process target: " + name + "\n");
        if (finishResult.equals(FinishResult.WARNING)) {
            output.append("Target -" + name + "- finished with: SUCCESS WITH " + finishResult + "\n");
        } else {
            output.append("Target -" + name + "- finished with: " + finishResult + "\n");
        }
        output.append("Targets that just opened: " + justOpenedList + "\n");
        output.append("Targets that had lost their chance to run: " + justSkippedList + "\n");
        output.append("---------------------------------");

        return output.toString();
    }
}
