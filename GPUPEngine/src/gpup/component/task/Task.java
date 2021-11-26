package gpup.component.task;

import gpup.component.target.FinishResult;

public interface Task {
    FinishResult run() throws InterruptedException;

    String getDirectoryPath();

    void setDirectoryPath(String path);

    long getProcessingTime();

    default void updateProcessingTime() {
    }
}
