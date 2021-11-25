package gpup.dto;

public interface ConsumerDTO {
    void setTaskOutput(TaskOutputDTO taskOutput);

    default String getName() {
        return "Name";
    }
}
