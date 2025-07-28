package org.theoliverlear.entity.training;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.theoliverlear.entity.Identifiable;
import org.theoliverlear.entity.currency.Currency;
import org.theoliverlear.entity.prediction.ModelType;
import org.theoliverlear.entity.prediction.PricePrediction;
import org.theoliverlear.entity.training.builder.TrainingSessionBuilder;
import org.theoliverlear.entity.training.specs.QueryLoad;
import org.theoliverlear.entity.training.specs.TrainingDevice;
import org.theoliverlear.entity.training.specs.TrainingQueryType;


import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "training_sessions")
@NoArgsConstructor
public class TrainingSession extends Identifiable {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_code", nullable = false)
    private Currency currency;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "prediction_id", nullable = false)
    private PricePrediction prediction;

    @Column(name = "num_rows", nullable = false)
    private int numRows;

    @Column(name = "epochs_trained", nullable = false)
    private int epochsTrained;

    @Column(name = "max_epochs", nullable = false)
    private int maxEpochs;

    @Column(name = "starting_loss", nullable = false)
    private double startingLoss;

    @Column(name = "final_loss", nullable = false)
    private double finalLoss;

    @Enumerated(EnumType.STRING)
    @Column(name = "model_type", length = 50, nullable = false)
    private ModelType modelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "query_type", length = 50, nullable = false)
    private TrainingQueryType queryType;

    @Column(name = "training_start_time", nullable = false)
    private LocalDateTime trainingStartTime;

    @Column(name = "training_end_time")
    private LocalDateTime trainingEndTime;

    @Column(name = "query_start_time")
    private LocalDateTime queryStartTime;

    @Column(name = "query_end_time")
    private LocalDateTime queryEndTime;

    @Column(name = "sequence_length", nullable = false)
    private int sequenceLength;

    @Column(name = "batch_size", nullable = false)
    private int batchSize;

    @Column(name = "dimension_width", nullable = false)
    private int dimensionWidth;

    @Enumerated(EnumType.STRING)
    @Column(name = "query_load", length = 50, nullable = false)
    private QueryLoad queryLoad;

    @Column(name = "query_batch_size")
    private Integer queryBatchSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "training_device", length = 50, nullable = false)
    private TrainingDevice trainingDevice;

    public TrainingSession(Currency currency,
                           PricePrediction prediction,
                           int numRows,
                           int epochsTrained,
                           int maxEpochs,
                           double startingLoss,
                           double finalLoss,
                           ModelType modelType,
                           TrainingQueryType queryType,
                           LocalDateTime trainingStartTime,
                           LocalDateTime trainingEndTime,
                           LocalDateTime queryStartTime,
                           LocalDateTime queryEndTime,
                           int sequenceLength,
                           int batchSize,
                           int dimensionWidth,
                           QueryLoad queryLoad,
                           Integer queryBatchSize,
                           TrainingDevice trainingDevice) {
        this.currency = currency;
        this.prediction = prediction;
        this.numRows = numRows;
        this.epochsTrained = epochsTrained;
        this.maxEpochs = maxEpochs;
        this.startingLoss = startingLoss;
        this.finalLoss = finalLoss;
        this.modelType = modelType;
        this.queryType = queryType;
        this.trainingStartTime = trainingStartTime;
        this.trainingEndTime = trainingEndTime;
        this.queryStartTime = queryStartTime;
        this.queryEndTime = queryEndTime;
        this.sequenceLength = sequenceLength;
        this.batchSize = batchSize;
        this.dimensionWidth = dimensionWidth;
        this.queryLoad = queryLoad;
        this.queryBatchSize = queryBatchSize;
        this.trainingDevice = trainingDevice;
    }

    public static TrainingSessionBuilder builder() {
        return new TrainingSessionBuilder();
    }
}
