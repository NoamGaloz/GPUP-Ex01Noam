package gpup.components.targetgraph;

import gpup.components.target.Target;
import gpup.components.target.TargetsRelationType;

import java.util.List;
import java.util.Map;

public interface GraphActions {
    void buildGraph(Map<String, Target> targets);

    int count();

    void addEdge(String source, Target destination);

    void addTarget(Target target);

    boolean isDependOn(String s, String d);

    boolean isTargetExist(String t);

    int getEdgesCount();

    List<String> findPaths(String src, String dest, TargetsRelationType type);







}
