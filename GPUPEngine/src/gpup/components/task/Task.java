package gpup.components.task;

import gpup.components.target.FinishResult;

public interface Task {
    FinishResult run();
    int GetSingleTargetProcessingTimeInMs();



}
