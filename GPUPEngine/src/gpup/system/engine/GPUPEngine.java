package gpup.system.engine;

import gpup.component.target.*;
import gpup.component.targetgraph.TargetGraph;
import gpup.component.task.*;
import gpup.component.task.simulation.ProcessingTimeType;
import gpup.component.task.simulation.SimulationTask;
import gpup.dto.*;
import gpup.exception.TargetExistException;
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
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.function.Consumer;


// IMPLEMENT INTERFACE
public class GPUPEngine implements Engine {
    private TargetGraph targetGraph;
    private Task task; // can it run several tasks?
    private ProcessingType processingType;

    private void loadXmlToTargetGraph(String path) throws FileNotFoundException, JAXBException, TargetExistException {
        final String PACKAGE_NAME = "gpup.jaxb.schema.generated";
        GPUPDescriptor gpupDescriptor;
        InputStream inputStream = new FileInputStream(path);
        JAXBContext jc = JAXBContext.newInstance(PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        gpupDescriptor = (GPUPDescriptor) u.unmarshal(inputStream);

        targetGraph = GPUPParser.parseTargetGraph(gpupDescriptor);
    }

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
    public boolean isInitialized() {
        return targetGraph != null;
    }

    public int getTargetsCount() {
        return targetGraph.count();
    }

    public int getSpecificTypeOfTargetsNum(TargetType targetType) {
        return targetGraph.getSpecificTypeOfTargetsNum(targetType);
    }

    @Override
    public void initTask(int targetProcessingTimeMs, int taskProcessingTimeType, float successProb, float successWithWarningsProb, ProcessingType status) {
        ProcessingTimeType procTimeType = taskProcessingTimeType == 1 ? ProcessingTimeType.Random : ProcessingTimeType.Permanent;
        task = new SimulationTask(targetGraph.getName(), procTimeType, successProb, successWithWarningsProb, targetProcessingTimeMs);
    }

    @Override
    public void setProcessingType(ProcessingType status) {
        this.processingType = status;
    }

    @Override
    public void runTask(Consumer<ConsumerDTO> consumer) throws InterruptedException, IOException {
        Instant totalStart, totalEnd, start, end;
        List<Target> waitingList;

        targetGraph.prepareGraphFromProcType(processingType, processingType == ProcessingType.FromScratch);
        targetGraph.buildTransposeGraph();
        targetGraph.clearJustOpenAndSkippedLists();
        waitingList = targetGraph.getAllWaitingTargets();

        totalStart = Instant.now();
        String dirPath = createDirectoryName();
        createTaskDirectory(dirPath);
        task.setDirectoryPath(dirPath);

        if (waitingList.isEmpty() && processingType.equals(ProcessingType.Incremental)) {
            throw new RuntimeException("The graph already had been processed completely, there is no need for 'Incremental' action");
        }

        while (!waitingList.isEmpty()) {
            start = Instant.now();
            Target currentTarget = waitingList.remove(0);
            currentTarget.setRunResult(RunResult.INPROCESS);

            task.updateProcessingTime();
            currentTarget.setFinishResult(task.run());
            currentTarget.setRunResult(RunResult.FINISHED);

            if (currentTarget.getFinishResult() == FinishResult.FAILURE) {
                targetGraph.dfsTravelToUpdateSkippedList(currentTarget);
                targetGraph.updateTargetAdjAfterFinishWithFailure(currentTarget);
            } else {
                targetGraph.updateTargetAdjAfterFinishWithoutFailure(waitingList, currentTarget);
            }
            end = Instant.now();
            currentTarget.setTaskRunDuration(Duration.between(start, end));

            ConsumerDTO consumerDTO = new ProcessedTargetDTO(currentTarget);
            consumerDTO.setTaskOutput(new SimulationOutputDTO(task.getProcessingTime()));
            //Writing to file
            writeTargetToFile(start, end, consumerDTO, task.getDirectoryPath());
            // Writing to console
            consumer.accept(consumerDTO);
        }

        totalEnd = Instant.now();
        Duration totalRunDuration = Duration.between(totalStart, totalEnd);
        StatisticsDTO statisticsDTO = calcStatistics(totalRunDuration);
        consumer.accept(statisticsDTO);
    }

    @Override
    public PathsDTO findPaths(String src, String dest, TargetsRelationType type) {
        if (!src.equals(dest)) {
            if (targetGraph.isTargetExist(src) && targetGraph.isTargetExist(dest)) {
                return new PathsDTO(targetGraph.findPaths(src, type, dest), src, dest, type);
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

    @Override
    public CircuitDTO findCircuit(String targetName) {
        if (targetGraph.isTargetExist(targetName)) {
            return new CircuitDTO(targetGraph.findCircuit(targetName));
        } else {
            throw new NoSuchElementException("Target " + targetName + " doesn't exist.");
        }
    }

    private void writeTargetToFile(Instant start, Instant end, ConsumerDTO target, String path) throws IOException {
        String fileName = target.getName() + ".log";
        try (Writer out = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(path + "/" + fileName)))) {

            // write to file:
            out.write(start.toString());
            out.write(target.toString());
            out.write(end.toString());
        }
    }

    private StatisticsDTO calcStatistics(Duration totalRunDuration) {

        List<StatisticsDTO.TargetRunDTO> targetsRunInfoList = targetGraph.getTargetsRunInfoList();

        return new StatisticsDTO(totalRunDuration, targetsRunInfoList);
    }
}
