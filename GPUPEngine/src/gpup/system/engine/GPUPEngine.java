package gpup.system.engine;

import gpup.components.target.FinishResult;
import gpup.components.target.RunResult;

import gpup.components.target.Target;
import gpup.components.target.TargetType;
import gpup.components.target.TargetsRelationType;
import gpup.components.targetgraph.TargetGraph;
import gpup.components.task.ProcessingStartStatus;
import gpup.components.task.Task;

import gpup.components.task.simulation.ProcessingTimeType;
import gpup.components.task.simulation.SimulationTask;
import gpup.dto.TargetDTO;
import gpup.dto.TargetGraphDTO;
import gpup.exceptions.TargetExistException;
import gpup.jaxb.schema.generated.GPUPDescriptor;
import gpup.jaxb.schema.parser.GPUPParser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;




// IMPLEMENT INTERFACE
public class GPUPEngine implements Engine {
    private TargetGraph targetGraph;
    private Task task; // can it run several tasks?
    private ProcessingStartStatus processingStartStatus;
    private boolean isFirstRunTask = true;



    public GPUPEngine() {
    }

    private void loadXmlToTargetGraph(String path) throws FileNotFoundException, JAXBException, TargetExistException {
        final String PACKAGE_NAME = "gpup.jaxb.schema.generated";
        GPUPDescriptor gpupDescriptor;
        InputStream inputStream = new FileInputStream(path);
        JAXBContext jc = JAXBContext.newInstance(PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        gpupDescriptor = (GPUPDescriptor) u.unmarshal(inputStream);

        targetGraph = GPUPParser.parseTargetGraph(gpupDescriptor);
    }

    //C:\Users\guysh\Downloads\ex1-small.xml

    @Override
    public void buildGraphFromXml(String path) throws JAXBException, FileNotFoundException, TargetExistException {
        loadXmlToTargetGraph(path);
    }

    @Override
    public TargetDTO getTargetInfo(String name) {
        if (targetGraph.isTargetExist(name)) {
            return targetGraph.getTargetInfo(name);
        }
        throw new NoSuchElementException("There is not a target named: " + name + "\n");
    }

    @Override
    public TargetGraphDTO getGraphInfo() {
        return new TargetGraphDTO(targetGraph);
    }

    @Override
    public boolean IsInitialized() {
        return targetGraph != null;
    }

    @Override
    public int getTargetsCount() {
        return targetGraph.count();
    }

    @Override
    public int getSpecificTypeOfTargetsNum(TargetType targetType) {
        return targetGraph.getSpecificTypeOfTargetsNum(targetType);
    }









































































































































    @Override
    public void InitTask(int targetProccesingTimeMs,int taskProcossingTimeType,float succesProb,float ifSucces_withWarningsProb)
    {
        ProcessingTimeType procTimeType = taskProcossingTimeType == 1 ? ProcessingTimeType.Random : ProcessingTimeType.Permanent;
        task = new SimulationTask("TARGETNAME",procTimeType, succesProb, ifSucces_withWarningsProb, targetProccesingTimeMs);
    }

@Override
    public void SetProcessingStartStatus(ProcessingStartStatus processingStartStatus) {
        this.processingStartStatus = processingStartStatus;
    }

    @Override
    public void RunTask(){

        String output;
        Instant start, end;
        Duration singleTargetRunDuration;
        long sleepingTime=0;
        List<Target> waitingList;
        targetGraph.PrepareGraphAccordingToProcessingStartStatus(processingStartStatus, isFirstRunTask);
        targetGraph.buildTransposeGraph();
        targetGraph.clearAllTargetsHelpingLists();
        waitingList = targetGraph.getAllWaitingTargets();

        try {

            while (!waitingList.isEmpty()) {
                Target currentTarget = waitingList.remove(0);
                currentTarget.setRunResult(RunResult.INPROCCESS);
                sleepingTime = (long) task.GetSingleTargetProcessingTimeInMs();

                output = "Task START running on Target " + currentTarget.getName() + "\n";
                output += currentTarget.getUserData() + "\n";
                output += "The predicted sleeping time is : " + sleepingTime + "\n";
                output += "Target " + currentTarget.getName() + " is about to sleep" + "\n";
                start = Instant.now();
                Thread.sleep(sleepingTime);
                end = Instant.now();
                singleTargetRunDuration = Duration.between(start, end);
                output += "Target " + currentTarget.getName() + " just woke up" + "\n";
                output += "Task END running on Target " + currentTarget.getName() + "\n";

                currentTarget.setFinishResult(task.run());
                currentTarget.setRunResult(RunResult.FINISHED);

                output += "Target finished with " + currentTarget.getFinishResult();

                if (currentTarget.getFinishResult() == FinishResult.FAILURE) {
                    targetGraph.DfsTravelToUpdateSkippedList(currentTarget);
                    targetGraph.UpdateTargetAdjAfterFinishWithFailure(currentTarget);
                } else {
                    targetGraph.UpdateTargetAdjAfterFinishWithoutFailure(waitingList,currentTarget);
                }

                /////to enter document

         //       if(!justOpenedList.isEmpty())
         //           output+= "\nTargets that -just opened- :" + printList(justOpenedList);

          //      if (!skippedList.isEmpty())
          //          output+= "\nTargets that lost their chance to run :" + printList(skippedList);
                /////to enter document

                System.out.println(output);
            }
        }
            catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String printList(List<Target> justOpenedList) {
        String res = "";
        for(Target t: justOpenedList){
            res += "\n"+t.getName();
        }
       // justOpenedList.forEach((target ->{res += "\n"+target.getName();}));
        return res;

      
    @Override
    public PathsDTO findPaths(String src, String dest, TargetsRelationType type) {
        if (!src.equals(dest)) {
            if (targetGraph.isTargetExist(src) && targetGraph.isTargetExist(dest)) {
                return new PathsDTO(targetGraph.findPaths(src, dest, type), src, dest, type);
            } else {
                throw new NoSuchElementException("The required targets aren't exist");
            }
        } else {
            throw new RuntimeException("The target you entered are the same.");
        }
    }

    @Override
    public void createTaskDirectory() {
        final String PATH = "GPUPEngine/src/tasks/directories/";
        String creationTime = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss").format(Calendar.getInstance().getTime());
        String dirName = PATH + "SimTaskName" + " - " + creationTime;
        //task.setDirectoryName(dirName);
        File taskDirectory = new File(dirName);
        if (!taskDirectory.exists()) {
            if (!taskDirectory.mkdir()) {
                throw new RuntimeException("Failure with creating the Task's Directory");
            }
        }
    }

    private void writeTargetToFile(Instant start, Instant end, long sleepTime, Target target) throws IOException {
        String fileName = target.getName() + ".log";
        Writer out = null;
        try {
            out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream("dirname" + "/" + fileName))); // task.getDirName()
            // write to file:
            out.write("BLABKABKAB");

        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
