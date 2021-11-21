package gpup.console.app;


import gpup.components.target.TargetType;
import gpup.console.validation.ConsoleIOValidations;
import gpup.dto.TargetDTO;
import gpup.system.engine.Engine;

import javax.xml.bind.JAXBException;
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
                    showTargetInfo();
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
        if (!ConsoleIOValidations.isQuit(path)) {

            try {
                if (!Files.exists(Paths.get(path))) {
                    GPUPConsoleIO.printExceptionMessage("The File you try to load is not exist.");
                    return;
                } else if (!Files.probeContentType(Paths.get(path)).equals("text/xml")) {
                    GPUPConsoleIO.printExceptionMessage("The File you entered is not an xml file.");
                    return;
                } else {
                    engine.buildGraphFromXml(path);
                    GPUPConsoleIO.successLoading();
                }
            } catch (JAXBException ex) {
                GPUPConsoleIO.printExceptionMessage("The xml file you try to load is not valid for this system, try again.");
            } catch (FileNotFoundException ex) {
                GPUPConsoleIO.printExceptionMessage("Wrong file path - the file you entered is not exist, try again.");
            } catch (Exception e) {
                GPUPConsoleIO.printExceptionMessage(e.getMessage());
            }
        }
    }

    private void showTargetInfo() {
        boolean targetExist = false;
        if (engine.IsInitialized()) {
            do {
                GPUPConsoleIO.targetNameRequest();
                String name = GPUPConsoleIO.getStringInput("name");
                if (ConsoleIOValidations.isQuit(name)) {
                    break;
                }
                try {
                    TargetDTO targetDTO = engine.getTargetInfo(name);
                    GPUPConsoleIO.showTargetInfo(targetDTO);
                    targetExist = true;
                } catch (NoSuchElementException e) {
                    GPUPConsoleIO.printMsg(e.getMessage());
                }
            } while (!targetExist);
        } else {
            GPUPConsoleIO.unloadedSystem();
        }
    }

    private void showTargetsGraphInfo() {

        if (engine.IsInitialized()) {
            GPUPConsoleIO.ShowTargetsNum(engine.getTotalTargetsNumber());
            GPUPConsoleIO.ShowSpecificTargetsNum(TargetType.Independent, engine.getSpecificTypeOfTargetsNum(TargetType.Independent));
            GPUPConsoleIO.ShowSpecificTargetsNum(TargetType.Leaf, engine.getSpecificTypeOfTargetsNum(TargetType.Leaf));
            GPUPConsoleIO.ShowSpecificTargetsNum(TargetType.Middle, engine.getSpecificTypeOfTargetsNum(TargetType.Middle));
            GPUPConsoleIO.ShowSpecificTargetsNum(TargetType.Root, engine.getSpecificTypeOfTargetsNum(TargetType.Root));
        } else {
            GPUPConsoleIO.unloadedSystem();
        }
    }
}
