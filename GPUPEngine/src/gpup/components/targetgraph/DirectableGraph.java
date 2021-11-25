package gpup.components.targetgraph;

import java.util.List;

// לשנות שם?
public interface DirectableGraph {

    void buildTransposeGraph();

    List<String> findCircuit(String src);

}