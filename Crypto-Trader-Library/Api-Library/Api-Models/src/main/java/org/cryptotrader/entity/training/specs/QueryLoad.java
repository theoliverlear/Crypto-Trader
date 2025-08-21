package org.cryptotrader.entity.training.specs;

public enum QueryLoad {
    BATCHES("batches"),
    BULK("bulk");
    public final String load;

    QueryLoad(String load) {
        this.load = load;
    }

    public static QueryLoad from(String load) {
        return switch (load) {
            case "batches" -> QueryLoad.BATCHES;
            case "bulk" -> QueryLoad.BULK;
            default -> throw new IllegalArgumentException("Unknown query load: " + load);
        };
    }
}
