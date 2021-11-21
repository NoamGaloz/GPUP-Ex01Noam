package gpup.console.app;


import gpup.system.engine.Engine;

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
        boolean fileExist = false;

        String path = GPUPConsoleIO.getXmlPath();
        try {
            engine.buildGraphFromXml(path);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


}
