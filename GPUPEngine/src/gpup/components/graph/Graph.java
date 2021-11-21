package gpup.components.graph;

import gpup.components.target.Target;

import java.util.*;

public class Graph<T> implements BasicGraphActions<T> {
    private Map<T, List<T>> graph = new HashMap<>();

    public Set<T> getKeySet() {
        return graph.keySet();
    }

    public List<T> getAdjacencyList(T vertex) {
        return graph.get(vertex);
    }

    @Override
    public void addVertex(T vertex) {
        graph.put(vertex, new LinkedList<T>());
    }

    @Override
    public void addEdge(T source, T destination) {
        if (!graph.containsKey(source))
            addVertex(source);

        if (!graph.containsKey(destination))
            addVertex(destination);

        // adding the destination to the adjacency list of the source
        graph.get(source).add(destination);
    }

    @Override
    public void buildGraph(Map<String, T> vertices) {
        // build a graph with only vertices and no edges:
        for (T t : vertices.values()) {
            graph.put(t, new ArrayList<>());
        }
    }

    @Override
    // This function return the count of vertices
    public int count() {
        return graph.keySet().size();
    }

    @Override
    // This function gives the count of edges
    public int getEdgesCount() {
        int count = 0;
        for (T v : graph.keySet()) {
            count += graph.get(v).size();
        }
        return count;
    }

    @Override
    public void findPaths(T source, T dest) {
        // run BelmanFord with weight 0 //
    }

    @Override
    // This function gives whether vertex is present or not.

    public boolean isVertexExist(T vertex) {
        if (graph.containsKey(vertex)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    // This function gives whether an edge is present or not.
    public boolean isEdgeExist(T s, T d) {
        if (graph.get(s).contains(d)) {
            return true;
        } else {
            return false;
        }
    }
}

