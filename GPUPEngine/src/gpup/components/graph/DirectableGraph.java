package gpup.components.graph;
// לשנות שם?
public interface DirectableGraph<T> {

    void buildSuperGraph();

    int getConnectedComponentsCount();

    void buildTransposeGraph();

    //boolean isCircuit();

}