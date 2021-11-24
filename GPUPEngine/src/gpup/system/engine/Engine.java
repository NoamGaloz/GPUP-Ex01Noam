package gpup.system.engine;

import gpup.components.target.TargetType;
import gpup.components.task.ProcessingStartStatus;
import gpup.dto.TargetDTO;
import gpup.dto.TargetGraphDTO;
import gpup.exceptions.TargetExistException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface Engine {
    void buildGraphFromXml(String path) throws JAXBException, FileNotFoundException, TargetExistException;

    TargetDTO getTargetInfo(String name);

    TargetGraphDTO getGraphInfo();

    boolean IsInitialized();

    int getTotalTargetsNumber();

    int getSpecificTypeOfTargetsNum(TargetType targetType);











    void InitTask(int targetProccesingTimeMs,int taskProcossingTimeType,float succesProb,float ifSucces_withWarningsProb);

    void SetProcessingStartStatus(ProcessingStartStatus processingStartStatus);

    void RunTask();
}
