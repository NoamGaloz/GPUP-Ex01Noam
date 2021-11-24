package gpup.console.app;

import gpup.components.target.TargetType;
import gpup.components.target.TargetsRelationType;
import gpup.console.validation.IOValidations;
import gpup.dto.PathsDTO;
import gpup.dto.ProcessedTargetDTO;
import gpup.dto.TargetDTO;
import gpup.system.engine.Engine;
import gpup.system.engine.GPUPEngine;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
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
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection("C:\\Users\\guysh\\Downloads\\ex1-small.xml"), null);
        UserInput userInput = UserInput.INIT;
        GPUPConsoleIO.welcome();

        while (userInput != UserInput.QUIT) {
            userInput = GPUPConsoleIO.mainMenu();
            if (engine.IsInitialized() || userInput.equals(UserInput.LOAD) || userInput.equals(UserInput.QUIT)) {
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
                        findPaths();
                        break;
                    case TASK:
                        break;
                    case QUIT:
                        userInput = GPUPConsoleIO.exit();
                        break;
                }
            } else {
                GPUPConsoleIO.unloadedSystem();
            }
            GPUPConsoleIO.continueApp();

        }
        GPUPConsoleIO.printMsg("Goodbye!");
    }

    private void findPaths() {
        GPUPConsoleIO.printMsg("Please enter 2 targets by name\nFirst target (from):");
        String src = GPUPConsoleIO.getStringInput("name");
        if (!IOValidations.isQuit(src)) {
            GPUPConsoleIO.printMsg("Second target (To):");
            String dest = GPUPConsoleIO.getStringInput("name");
            if (!IOValidations.isQuit(dest)) {
                TargetsRelationType type = getRelationType();
                if (type != null) {
                    try {
                        PathsDTO paths = engine.findPaths(src, dest, type);
                        GPUPConsoleIO.printMsg(paths.toString());
                    } catch (RuntimeException e) {
                        GPUPConsoleIO.printMsg(e.getMessage());
                    }
                }
            }
        }
    }

    private TargetsRelationType getRelationType() {
        TargetsRelationType type;
        GPUPConsoleIO.printMsg("Dependency between the targets");
        GPUPConsoleIO.printMsg("  1. Depend On");
        GPUPConsoleIO.printMsg("  2. Required For");
        int choice = GPUPConsoleIO.getIntegerInput();
        switch (choice) {
            case 0:
                return null;
            case 1:
                type = TargetsRelationType.DependsOn;
                break;
            case 2:
                type = TargetsRelationType.RequiredFor;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + choice);
        }
        return type;
    }

    private void loadGPUPSystem() {
        String path = GPUPConsoleIO.getXmlPath();
        if (path == null) {
            GPUPConsoleIO.failedLoadSystem("The file path contains invalid letters.");
            return;
        }
        if (!IOValidations.isQuit(path)) {
            try {

                if (!Files.exists(Paths.get(path))) {
                    GPUPConsoleIO.failedLoadSystem("The File you try to load is not exist.");
                    return;
                } else if (!Files.probeContentType(Paths.get(path)).equals("text/xml")) {
                    GPUPConsoleIO.failedLoadSystem("The File you entered is not an xml file.");
                    return;
                } else {
                    engine.buildGraphFromXml(path);
                    GPUPConsoleIO.successLoading();
                }
            } catch (JAXBException ex) {
                GPUPConsoleIO.failedLoadSystem("The xml file you try to load is not valid for this GPUP system, try again.");
            } catch (FileNotFoundException ex) {
                GPUPConsoleIO.failedLoadSystem("Wrong file path - the file you entered is not exist, try again.");
            } catch (Exception e) {
                GPUPConsoleIO.failedLoadSystem(e.getMessage());
            }
        }
    }

    private void showTargetInfo() {
        boolean targetExist = false;
        do {
            GPUPConsoleIO.targetNameRequest();
            String name = GPUPConsoleIO.getStringInput("name");
            if (IOValidations.isQuit(name)) {
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
    }


    private void showTargetsGraphInfo() {

        //GPUPConsoleIO.showTargetsCount(engine.getTotalTargetsNumber());
        GPUPConsoleIO.printMsg(engine.getGraphInfo().toString());
//        GPUPConsoleIO.showTargetCountByType(TargetType.Independent, engine.getSpecificTypeOfTargetsNum(TargetType.Independent));
//        GPUPConsoleIO.showTargetCountByType(TargetType.Leaf, engine.getSpecificTypeOfTargetsNum(TargetType.Leaf));
//        GPUPConsoleIO.showTargetCountByType(TargetType.Middle, engine.getSpecificTypeOfTargetsNum(TargetType.Middle));
//        GPUPConsoleIO.showTargetCountByType(TargetType.Root, engine.getSpecificTypeOfTargetsNum(TargetType.Root));
    }


}
