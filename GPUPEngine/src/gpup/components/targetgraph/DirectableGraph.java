package gpup.components.targetgraph;
// לשנות שם?
public interface DirectableGraph {

    void buildSuperGraph();

    int getConnectedComponentsCount();

    void buildTransposeGraph();

    //boolean isCircuit();

}