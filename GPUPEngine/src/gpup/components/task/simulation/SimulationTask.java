package gpup.components.task.simulation;


import gpup.components.target.FinishResult;
import gpup.components.task.Task;

import java.util.Random;

public class SimulationTask implements Task {
    private String name;
    private int processingTimeInMs;
    private ProcessingTimeType processingTimeType;
    private float succesProb;
    private float successWithWarningsProb;
    private String dirPath;

    public long getSleepingTime() {
        return sleepingTime;
    }

    private long sleepingTime;
    Random random;

    public SimulationTask(String name, ProcessingTimeType processingTime, float succesProb, float ifSucces_withWarningsProb, int processingTimeInMs) {
        this.random = new Random();
        this.name = name;
        this.processingTimeInMs = processingTimeInMs;
        this.processingTimeType = processingTime;
        this.succesProb = succesProb;
        this.successWithWarningsProb = ifSucces_withWarningsProb;
        calcSingleTargetProcessingTimeInMs();


    }

    public FinishResult run() throws InterruptedException {
        float LuckyNumber = (float) Math.random();
        FinishResult res = LuckyNumber < succesProb ? FinishResult.SUCCESS : FinishResult.FAILURE;

        if (res == FinishResult.SUCCESS) {
            LuckyNumber = (float) Math.random();
            res = LuckyNumber < successWithWarningsProb ? FinishResult.WARNING : FinishResult.SUCCESS;
        }
        Thread.sleep(sleepingTime);
        return res;
    }

    @Override
    public String getDirectoryPath() {
        return dirPath;
    }

    @Override
    public void setDirectoryPath(String path) {
        dirPath = path;
    }

    @Override
    public long getProcessingTime() {
        return sleepingTime;
    }

    @Override
    public void updateData(ProcessingTimeType procTimeType, float successProb, float successWithWarningsProb, int targetProcessingTimeMs) {
        this.processingTimeInMs = processingTimeInMs;
        this.processingTimeType = procTimeType;
        this.succesProb = successProb;
        this.successWithWarningsProb = successWithWarningsProb;
    }

    private void calcSingleTargetProcessingTimeInMs() {
        int res = 0;
        switch (processingTimeType) {
            case Permanent:
                res = processingTimeInMs;
                break;
            case Random: {
                res = random.nextInt(processingTimeInMs);
                break;
            }
        }
        sleepingTime = res;
    }
}