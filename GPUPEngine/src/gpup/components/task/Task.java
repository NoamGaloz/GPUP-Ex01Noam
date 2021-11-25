package gpup.components.task;

import gpup.components.target.FinishResult;
import gpup.components.task.simulation.ProcessingTimeType;

public interface Task {
    FinishResult run() throws InterruptedException;

    String getDirectoryPath();

    void setDirectoryPath(String path);

    long getProcessingTime();

    default void updateData(ProcessingTimeType procTimeType, float successProb, float successWithWarningsProb, int targetProcessingTimeMs) {
    }
}
