package gpup.components.task.simulation;


import gpup.components.target.FinishResult;
import gpup.components.task.Task;

import java.util.Random;

public class SimulationTask implements Task {
    private String name;
    private int processingTimeInMs;
    private ProcessingTimeType processingTimeType;
    private float succesProb;
    private float ifSucces_withWarningsProb;

    public SimulationTask(String name, ProcessingTimeType processingTime, float succesProb, float ifSucces_withWarningsProb, int processingTimeInMs) {
        this.name = name;
        this.processingTimeInMs= processingTimeInMs;
        this.processingTimeType = processingTime;
        this.succesProb = succesProb;
        this.ifSucces_withWarningsProb = ifSucces_withWarningsProb;
    }

    public FinishResult run() {
        float LuckyNumber =(float)Math.random();
        FinishResult res = LuckyNumber < succesProb ? FinishResult.SUCCESS : FinishResult.FAILURE;

        if(res==FinishResult.SUCCESS)
        {
            LuckyNumber=(float)Math.random();
            res = LuckyNumber < ifSucces_withWarningsProb ? FinishResult.WARNING : FinishResult.SUCCESS;
        }

        return res;
    }
    public int GetSingleTargetProcessingTimeInMs(){
        int res=0;
        switch (processingTimeType){
            case Permanent:
                res=processingTimeInMs;
                break;
            case Random: {
                Random random = new Random();
                res=random.nextInt(processingTimeInMs);
                break;
            }
        }
        return res;
    }
}