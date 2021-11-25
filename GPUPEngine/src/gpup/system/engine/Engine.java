package gpup.system.engine;

import gpup.components.target.TargetType;
import gpup.components.task.ProcessingStartStatus;
import gpup.components.target.TargetsRelationType;
import gpup.dto.*;
import gpup.exceptions.TargetExistException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.function.Consumer;

public interface Engine {
    void buildGraphFromXml(String path) throws JAXBException, FileNotFoundException, TargetExistException;

    TargetDTO getTargetInfo(String name);

    TargetGraphDTO getGraphInfo();

    boolean IsInitialized();

    int getTargetsCount();

    int getSpecificTypeOfTargetsNum(TargetType targetType);

    void InitTask(int targetProcessingTimeMs, int taskProcessingTimeType, float successProb, float successWithWarningsProb, ProcessingStartStatus status);


    void SetProcessingStartStatus(ProcessingStartStatus processingStartStatus);

    void RunTask(Consumer<ConsumerDTO> consumer) throws InterruptedException, IOException;

    PathsDTO findPaths(String src, String dest, TargetsRelationType type);

    void createTaskDirectory(String path);

    boolean isFirstTaskRun();
}
