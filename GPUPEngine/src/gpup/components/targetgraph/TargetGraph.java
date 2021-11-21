package gpup.components.targetgraph;

import gpup.components.graph.DirectableGraph;
import gpup.components.graph.Graph;
import gpup.components.target.Target;
import gpup.components.target.TargetType;
import gpup.dto.TargetDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class TargetGraph implements DirectableGraph<Target> {

    private final String name;
    private Graph<Target> dependsOnGraph = null;
    private Graph<Target> requiredForGraph = null;
    private List<Target> leavesList = null;

    private String workingDirectory;

    public TargetGraph(String name) {
        this.name = name;
        dependsOnGraph = new Graph<>();
        requiredForGraph = new Graph<>();
        leavesList = new ArrayList<>();
    }

    public void buildGraph(Map<String, Target> targetList) {
        dependsOnGraph.buildGraph(targetList);
        for (Target t : dependsOnGraph.getKeySet()) {
            for (Target dependTarget : targetList.get(t.getName()).getDependsOnList()) {
                dependsOnGraph.addEdge(t, dependTarget);
            }
        }
        buildLeafList();
    }

    private void buildLeafList() {
        dependsOnGraph.getKeySet().stream()
                .filter(t -> t.getType() == TargetType.Leaf)
                .forEach(t -> leavesList.add(t));

    }

    @Override
    public void buildSuperGraph() {

    }

    @Override
    public int getConnectedComponentsCount() {
        return 0;
    }

    @Override
    public void buildTransposeGraph() {
        if (dependsOnGraph.count() != 0) {
            for (Target t : dependsOnGraph.getKeySet()) {
                for (Target s : dependsOnGraph.getAdjacencyList(t)) {
                    requiredForGraph.addEdge(s, t);
                }
            }
        }
        // Throws EXCEPTION ?
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public String getName() {
        return name;
    }

    public int getTotalTargetsNumber() {
        return dependsOnGraph.count();
    }

    public int getSpecificTypeOfTargetsNum(TargetType targetType) {

        int counter = 0;

        for (Target t : dependsOnGraph.getKeySet()) {
            if (t.getType() == targetType) counter++;
        }
        return counter;
    }

    public TargetDTO getTargetInfo(String name) {
        for (Target t : dependsOnGraph.getKeySet()) {
            if (t.getName().equals(name)) {
                return new TargetDTO(t);
            }
        }
        throw new NoSuchElementException("There is not a target named: " + name);
    }
}

