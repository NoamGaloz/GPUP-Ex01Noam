package gpup.system.engine;

import gpup.dto.TargetDTO;
import gpup.dto.TargetGraphDTO;
import gpup.exceptions.TargetExistException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface Engine {
    void buildGraphFromXml(String path) throws JAXBException, FileNotFoundException, TargetExistException;

    TargetDTO getTargetInfo(String name);

    TargetGraphDTO getGraphInfo();
}
