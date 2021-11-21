package gpup.console.app;


import gpup.components.target.TargetType;
import gpup.exceptions.TargetExistException;
import gpup.system.engine.Engine;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

public class GPUPApp {

    private Engine engine;

    public void init(Engine engine) {
        this.engine = engine;
    }

    public void run() {
        UserInput userInput = UserInput.INIT;
        while (userInput != UserInput.QUIT) {
            userInput = GPUPConsoleIO.mainMenu();
            switch (userInput) {
                case LOAD:
                    loadGPUPSystem();
                    break;
                case GRAPHINFO:
                    showTargetsGraphInfo();
                    break;
                case TARGETINFO:
                    break;
                case PATH:
                    break;
                case TASK:
                    break;
                case QUIT:
                    break;
            }
        }
    }

    private void loadGPUPSystem() {
        String path = GPUPConsoleIO.getXmlPath();

        try {
            if (!Files.probeContentType(Paths.get(path)).equals("text/xml")) {
                System.out.println("The File you entered is not an xml file.");
                return;
            } else if (!Files.exists(Paths.get(path))) {
                System.out.println("The File you try to laod is not exist.");
                return;
            } else {
                engine.buildGraphFromXml(path);
            }
        } catch (JAXBException ex) {
            System.out.println("The xml file you try to laod is not valid for this system, try again.");
        } catch (FileNotFoundException ex) {
            System.out.println("Wrong file path - the file you entered is not exist, try again.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void showTargetsGraphInfo() {

        if(engine.IsInitialized())
        {
            GPUPConsoleIO.ShowTargetsNum(engine.getTotalTargetsNumber());
            GPUPConsoleIO.ShowSpecificTargetsNum(TargetType.Independent, engine.getSpecificTypeOfTargetsNum(TargetType.Independent));
           GPUPConsoleIO.ShowSpecificTargetsNum(TargetType.Leaf, engine.getSpecificTypeOfTargetsNum(TargetType.Leaf));
            GPUPConsoleIO.ShowSpecificTargetsNum(TargetType.Middle, engine.getSpecificTypeOfTargetsNum(TargetType.Middle));
            GPUPConsoleIO.ShowSpecificTargetsNum(TargetType.Root, engine.getSpecificTypeOfTargetsNum(TargetType.Root));
        }
        else {
          System.out.println("Valid system not exist - please load valid xml first");
        }
    }

}
