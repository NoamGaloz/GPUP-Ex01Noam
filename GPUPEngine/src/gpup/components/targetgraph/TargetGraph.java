package gpup.components.targetgraph;

import gpup.components.target.Target;
import gpup.components.target.TargetType;
import gpup.components.target.TargetsRelationType;
import gpup.dto.TargetDTO;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TargetGraph implements DirectableGraph, GraphActions {

    private final String name;
    private String workingDirectory;
    private Map<String, List<Target>> dependsOnGraph;
    private Map<String, Target> targetMap;
    //private Map<String, Target> requiredForGraph; // maybe there's no need ?
    private List<Target> leavesList;

    public TargetGraph(String name) {
        this.name = name;
        dependsOnGraph = new HashMap<>();
        //requiredForGraph = new HashMap<>();
        targetMap = new HashMap<>();
        leavesList = new ArrayList<>();
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

    public void buildGraph(Map<String, Target> targetList) {
        targetList.values().forEach(target -> {
            dependsOnGraph.put(target.getName(), new LinkedList<>());
            targetMap.put(target.getName(), target);
        });

        targetMap.values().forEach(target -> {
            target.getDependsOnList().forEach(target1 -> {
                addEdge(target.getName(), target1);
            });
        });

        buildLeafList();
    }

    @Override
    // This function return the count of targets
    public int count() {
        return targetMap.size();
    }

    @Override
    public void addEdge(String source, Target destination) {
        if (dependsOnGraph.containsKey(source)) {

            if (!dependsOnGraph.containsKey(destination.getName()))
                addTarget(destination);

            // adding the destination to the adjacency list of the source
            dependsOnGraph.get(source).add(destination);
        }
    }

    @Override
    public void addTarget(Target target) {
        dependsOnGraph.put(target.getName(), new LinkedList<>());
    }

    @Override
    public boolean isDependOn(String s, String d) {
        return dependsOnGraph.get(s).stream().anyMatch(target -> target.getName().equals(d));
    }

    @Override
    public boolean isTargetExist(String t) {
        return targetMap.containsKey(t);
    }

    @Override
    public int getEdgesCount() {
        AtomicInteger count = new AtomicInteger();
        dependsOnGraph.values().forEach(list -> {
            list.forEach(target -> {
                count.getAndIncrement();
            });
        });
        return count.get();
    }

    @Override
    public void findPaths(String src, String dest, TargetsRelationType type) {

    }

    private void buildLeafList() {
        targetMap.values().forEach(target -> {
            if (target.getType() == TargetType.Leaf) {
                leavesList.add(target);
            }
        });
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
        if (dependsOnGraph.size() != 0) {
            dependsOnGraph.forEach((s, targets) -> {
                targets.forEach(target -> {
                    //requiredForGraph.put(target.getName(), targetMap.get(s));
                });
            });
        }
        throw new RuntimeException("Can't build transpose graph (Required For)");
    }

    public int getSpecificTypeOfTargetsNum(TargetType targetType) {
        return (int) targetMap.values()
                .stream()
                .filter(target -> target.getType() == targetType)
                .count();
    }

    public TargetDTO getTargetInfo(String name) {
        if(targetMap.containsKey(name)){
            return new TargetDTO(targetMap.get(name));
        }
        else{
        throw new NoSuchElementException("There is not a target named: " + name);
    }}
}

