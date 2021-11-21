package gpup.components.graph;

import gpup.components.target.Target;

import java.util.Map;

public interface BasicGraphActions<T> {
    void buildGraph(Map<String, T> targets);

    int count();

    void addEdge(T source, T destination);

    void addVertex(T vertex);

    boolean isEdgeExist(T s, T d);

    boolean isVertexExist(T vertex);

    int getEdgesCount();

    void findPaths(T source, T dest);


}
