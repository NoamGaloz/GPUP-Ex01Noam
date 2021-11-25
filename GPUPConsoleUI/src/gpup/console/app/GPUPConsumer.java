package gpup.console.app;

import gpup.dto.ConsumerDTO;

import java.util.function.Consumer;

public class GPUPConsumer implements Consumer<ConsumerDTO> {
    @Override
    public void accept(ConsumerDTO consumerDTO) {
        GPUPConsoleIO.printMsg(consumerDTO.toString());
    }
}
