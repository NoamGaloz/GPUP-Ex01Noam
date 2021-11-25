package gpup.dto;

import java.util.List;

public class CircuitDTO {
    private String circuit;

    @Override
    public String toString() {
        return "CircuitDTO{" +
                "circuit='" + circuit + '\'' +
                '}';
    }

    public CircuitDTO(List<String> circuit) {
        this.circuit = updateCircuit(circuit);
    }

    private String updateCircuit(List<String> circuit) {
        return null;
    }
}
