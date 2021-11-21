package gpup.console.app;


import gpup.console.validation.ConsoleIOValidations;
import gpup.dto.TargetDTO;
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
            if (!Files.exists(Paths.get(path))) {
                GPUPConsoleIO.printExceptionMessege("The File you try to laod is not exist.");
                return;
            } else if (!Files.probeContentType(Paths.get(path)).equals("text/xml")) {
                GPUPConsoleIO.printExceptionMessege("The File you entered is not an xml file.");
                return;
            } else {
                engine.buildGraphFromXml(path);
                GPUPConsoleIO.successLoading();
            }
        } catch (JAXBException ex) {
            GPUPConsoleIO.printExceptionMessege("The xml file you try to laod is not valid for this system, try again.");
        } catch (FileNotFoundException ex) {
            GPUPConsoleIO.printExceptionMessege("Wrong file path - the file you entered is not exist, try again.");
        } catch (Exception e) {
            GPUPConsoleIO.printExceptionMessege(e.getMessage());
        }
    }

    private void showTargetInfo() {
        boolean targetExist = false;
        boolean quitAction = false;

        do {
            GPUPConsoleIO.targetInfoMenu();
            String name = GPUPConsoleIO.getStringInput("name");
            if (ConsoleIOValidations.isQuit(name)) {
                break;
            }
            try {
                //TargetDTO targetDTO = engine.getTargetInfo(name);
                //GPUPConsoleIO.showTargetInfo(targetDTO);
                System.out.println("target info");
                targetExist = true;
            } catch (Exception e){
            }
        } while (!targetExist);
    }
}
