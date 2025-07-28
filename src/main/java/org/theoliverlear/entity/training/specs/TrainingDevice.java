package org.theoliverlear.entity.training.specs;

public enum TrainingDevice {
    CPU("cpu"),
    GPU_ZERO("AMD_6700_XT"),
    GPU_ONE("AMD_6900_XT");
    public final String device;

    TrainingDevice(String device) {
        this.device = device;
    }

    public static TrainingDevice from(String device) {
        return switch (device) {
            case "cpu" -> TrainingDevice.CPU;
            case "AMD_6700_XT", "gpu_0" -> TrainingDevice.GPU_ZERO;
            case "AMD_6900_XT", "gpu_1" -> TrainingDevice.GPU_ONE;
            default -> throw new IllegalArgumentException("Unknown training device: " + device);
        };
    }
}