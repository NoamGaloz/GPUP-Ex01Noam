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
import gpup.dto.*;
import gpup.exceptions.TargetExistException;
import gpup.jaxb.schema.generated.GPUPDescriptor;
import gpup.jaxb.schema.parser.GPUPParser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.function.Consumer;


// IMPLEMENT INTERFACE
public class GPUPEngine implements Engine {
    private TargetGraph targetGraph;
    private Task task; // can it run several tasks?
    private ProcessingStartStatus processingStartStatus;
    //    private boolean isFirstRunTask = true;
    private Duration totalRunDuration;


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
    public void InitTask(int targetProcessingTimeMs, int taskProcessingTimeType, float successProb, float successWithWarningsProb, ProcessingStartStatus status) {
        ProcessingTimeType procTimeType = taskProcessingTimeType == 1 ? ProcessingTimeType.Random : ProcessingTimeType.Permanent;
        task = new SimulationTask(targetGraph.getName(), procTimeType, successProb, successWithWarningsProb, targetProcessingTimeMs);
    }

    @Override
    public void SetProcessingStartStatus(ProcessingStartStatus status) {
        this.processingStartStatus = status;
    }

    @Override
    public void RunTask(Consumer<ConsumerDTO> consumer) throws InterruptedException, IOException {

        String output;
        Instant totalStart, totalEnd, start, end;
        List<Target> waitingList;

        targetGraph.PrepareGraphAccordingToProcessingStartStatus(processingStartStatus, processingStartStatus == ProcessingStartStatus.FromScratch);
        targetGraph.buildTransposeGraph();
        targetGraph.clearAllTargetsHelpingLists();
        waitingList = targetGraph.getAllWaitingTargets();

        totalStart = Instant.now();
        String dirPath = createDirectoryName();
        createTaskDirectory(dirPath);
        task.setDirectoryPath(dirPath);

        if (waitingList.isEmpty() && processingStartStatus.equals(ProcessingStartStatus.Incremental)) {
            throw new RuntimeException("The graph already had been processed completely, there is no need for 'Incremental' action");
        }

        while (!waitingList.isEmpty()) {
            start = Instant.now();
            Target currentTarget = waitingList.remove(0);
            currentTarget.setRunResult(RunResult.INPROCCESS);

            currentTarget.setFinishResult(task.run());
            currentTarget.setRunResult(RunResult.FINISHED);

            if (currentTarget.getFinishResult() == FinishResult.FAILURE) {
                targetGraph.DfsTravelToUpdateSkippedList(currentTarget);
                targetGraph.UpdateTargetAdjAfterFinishWithFailure(currentTarget);
            } else {
                targetGraph.UpdateTargetAdjAfterFinishWithoutFailure(waitingList, currentTarget);
            }
            end = Instant.now();
            currentTarget.setTaskRunDuration(Duration.between(start, end));

            ConsumerDTO consumerDTO = new ProcessedTargetDTO(currentTarget);
            consumerDTO.setTaskOutput(new SimulationOutputDTO(task.getProcessingTime()));
            //Writing to file
            writeTargetToFile(start, end, consumerDTO, task.getDirectoryPath());
            // calling to consumer.accept
            consumer.accept(consumerDTO);
        }

        totalEnd = Instant.now();
        totalRunDuration = Duration.between(totalStart, totalEnd);
        // StatDTO statisticsDTO = calcStatistics();
        // consumer.accept(statisticsDTO);


    }


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

    private String createDirectoryName() {
        String path = targetGraph.getWorkingDirectory() + "/";
        String creationTime = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss").format(Calendar.getInstance().getTime());
        path = path + targetGraph.getName() + " - " + creationTime;
        return path;
    }

    @Override
    public void createTaskDirectory(String path) {
        File taskDirectory = new File(path);
        if (!taskDirectory.exists()) {
            if (!taskDirectory.mkdir()) {
                throw new RuntimeException("Failure with creating the Task's Directory");
            }
        }
    }

    @Override
    public boolean isFirstTaskRun() {
        return task == null;
    }

    private void writeTargetToFile(Instant start, Instant end, ConsumerDTO target, String path) throws IOException {
        String fileName = target.getName() + ".log";
        Writer out = null;
        try {
            out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(path + "/" + fileName))); // task.getDirName()
            // write to file:
            out.write(start.toString());
            out.write(target.toString());
            out.write(end.toString());


        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
