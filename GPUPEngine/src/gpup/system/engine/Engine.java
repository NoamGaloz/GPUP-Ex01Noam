package gpup.system.engine;

import gpup.component.task.ProcessingType;
import gpup.component.target.TargetsRelationType;
import gpup.dto.*;
import gpup.exception.TargetExistException;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.function.Consumer;

public interface Engine {
    void buildGraphFromXml(String path) throws JAXBException, FileNotFoundException, TargetExistException;

    TargetDTO getTargetInfo(String name);

    TargetGraphDTO getGraphInfo();

    boolean isInitialized();

    void initTask(int targetProcessingTimeMs, int taskProcessingTimeType, float successProb, float successWithWarningsProb, ProcessingType status);

    void setProcessingType(ProcessingType processingStartStatus);

    void runTask(Consumer<ConsumerDTO> consumer) throws InterruptedException, IOException;

    PathsDTO findPaths(String src, String dest, TargetsRelationType type);

    void createTaskDirectory(String path);

    boolean isFirstTaskRun();

    CircuitDTO findCircuit(String targetName);
}
