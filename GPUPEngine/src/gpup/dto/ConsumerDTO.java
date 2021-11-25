package gpup.dto;

public interface ConsumerDTO {
    default void setTaskOutput(TaskOutputDTO taskOutput) {};

    default String getName() {
        return "Name";
    }
}
