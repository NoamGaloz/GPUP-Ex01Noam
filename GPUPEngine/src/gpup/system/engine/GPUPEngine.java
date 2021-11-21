package gpup.system.engine;

import gpup.components.targetgraph.TargetGraph;
import gpup.components.task.Task;
import gpup.exceptions.TargetExistException;
import gpup.jaxb.schema.generated.GPUPDescriptor;
import gpup.jaxb.schema.parser.GPUPParser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.NoSuchElementException;

// IMPLEMENT INTERFACE
public class GPUPEngine implements Engine {
    private TargetGraph targetGraph;
    private Task task; // can it run several tasks?

    public GPUPEngine() {
    }

    private void loadXmlToTargetGraph(String path) {
        GPUPDescriptor gpupDescriptor;
        try {
            InputStream inputStream = new FileInputStream(new File(path));
            JAXBContext jc = JAXBContext.newInstance("gpup.jaxb.schema.generated");
            Unmarshaller u = jc.createUnmarshaller();
            gpupDescriptor = (GPUPDescriptor) u.unmarshal(inputStream);
            targetGraph = GPUPParser.parseTargetGraph(gpupDescriptor);
        } catch (JAXBException ex) {
        } catch (FileNotFoundException ex) {
            //5555555555555555555555555555555555555555555555555555555555555555555555555555
            //5555555555555555555555555555555555555555555555555555555555555555555555555555
            //5555555555555555555555555555555555555555555555555555555555555555555555555555
            System.out.println(ex.getMessage());
        } catch (TargetExistException | NoSuchElementException e) {
            //5555555555555555555555555555555555555555555555555555555555555555555555555555
            //5555555555555555555555555555555555555555555555555555555555555555555555555555
            //5555555555555555555555555555555555555555555555555555555555555555555555555555
            System.out.println(e.getMessage());
        }

    }


    //C:\Users\guysh\Downloads\ex1-small.xml

    @Override
    public void buildGraphFromXml(String path) {
        loadXmlToTargetGraph(path);
    }
}
