package gpup.system.engine;

import gpup.components.target.TargetType;
import gpup.components.targetgraph.TargetGraph;
import gpup.components.task.Task;
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

// IMPLEMENT INTERFACE
public class GPUPEngine implements Engine {
    private TargetGraph targetGraph;
    private Task task; // can it run several tasks?

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
       return targetGraph.getTargetInfo(name);
    }

    @Override
    public TargetGraphDTO getGraphInfo() {
        return null;
    }

    @Override
    public boolean IsInitialized() { return targetGraph!=null; }

    @Override
    public int getTotalTargetsNumber() {return targetGraph.getTotalTargetsNumber();}

    @Override
    public int getSpecificTypeOfTargetsNum(TargetType targetType){
        return targetGraph.getSpecificTypeOfTargetsNum(targetType);
    }
}
