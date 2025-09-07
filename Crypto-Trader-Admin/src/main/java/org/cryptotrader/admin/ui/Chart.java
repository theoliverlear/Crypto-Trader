package org.cryptotrader.admin.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.admin.component.DataPointFetcher;
import org.cryptotrader.admin.models.ChartDataPoint;
import org.cryptotrader.desktop.component.ComponentLoader;
import org.cryptotrader.desktop.component.config.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@Scope("prototype")
@Lazy
public class Chart extends HBox {
    @Autowired
    private DataPointFetcher dataPointFetcher;
    
    private List<ChartDataPoint<LocalDateTime, Double>> dataPoints;

    private final NumberAxis xAxis;
    private final NumberAxis yAxis;
    private final AreaChart<Number, Number> areaChart;
    private final XYChart.Series<Number, Number> series;


    public Chart() {
        SpringContext.getBean(ComponentLoader.class).loadWithFxRoot(this, this);
        xAxis = new NumberAxis();
        yAxis = new NumberAxis();

        xAxis.setForceZeroInRange(false);
        yAxis.setForceZeroInRange(false);

        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);

        xAxis.setTickLabelGap(6);
        yAxis.setTickLabelGap(6);

        // Format X as time "HH:mm:ss" (adjust formatter as desired)
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override public String toString(Number value) {
                long epochMillis = value.longValue();
                LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault());
                return timeFmt.format(ldt);
            }
            @Override public Number fromString(String string) { return 0L; }
        });

        // Chart
        areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setAnimated(false);
        areaChart.setLegendVisible(false);
        areaChart.setCreateSymbols(false);
        areaChart.setHorizontalGridLinesVisible(true);
        areaChart.setVerticalGridLinesVisible(false);

        // Series
        series = new XYChart.Series<>();
        areaChart.getData().add(series);

        // Appearance
        areaChart.getStyleClass().add("ct-area-chart");
        areaChart.setEffect(new DropShadow());
        setPadding(new Insets(8));
        getChildren().add(areaChart);
        VBox.setVgrow(areaChart, Priority.ALWAYS);
        
        this.initGraph();
    }

    public void setDataPoints(List<ChartDataPoint<LocalDateTime, Double>> points) {
        this.dataPoints = points;
        ObservableList<XYChart.Data<Number, Number>> items = FXCollections.observableArrayList();
        if (points != null && !points.isEmpty()) {
            for (ChartDataPoint<LocalDateTime, Double> p : points) {
                long x = p.getX().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                Double y = p.getY();
                if (y != null) {
                    items.add(new XYChart.Data<>(x, y));
                }
            }
            series.setData(items);

            // Adjust axis range a bit for nicer margins
            long minX = items.stream().mapToLong(d -> d.getXValue().longValue()).min().orElse(0L);
            long maxX = items.stream().mapToLong(d -> d.getXValue().longValue()).max().orElse(0L);
            if (minX < maxX) {
                double pad = (maxX - minX) * 0.05;
                xAxis.setAutoRanging(false);
                xAxis.setLowerBound(minX - pad);
                xAxis.setUpperBound(maxX + pad);
                xAxis.setTickUnit(Math.max(1, (maxX - minX) / 6.0));
            } else {
                xAxis.setAutoRanging(true);
            }

            double minY = items.stream().mapToDouble(d -> d.getYValue().doubleValue()).min().orElse(0.0);
            double maxY = items.stream().mapToDouble(d -> d.getYValue().doubleValue()).max().orElse(0.0);
            if (minY < maxY) {
                double padY = (maxY - minY) * 0.1;
                yAxis.setAutoRanging(false);
                yAxis.setLowerBound(minY - padY);
                yAxis.setUpperBound(maxY + padY);
                yAxis.setTickUnit(Math.max(0.0001, (maxY - minY) / 5.0));
            } else {
                yAxis.setAutoRanging(true);
            }
        } else {
            series.getData().clear();
            xAxis.setAutoRanging(true);
            yAxis.setAutoRanging(true);
        }
    }
    
    public void initGraph() {
        List<ChartDataPoint<LocalDateTime, Double>> dataPoints = this.dataPointFetcher.getLastDayCurrencyHistory("BTC");
        this.setDataPoints(dataPoints);
    }

    public Node getView() {
        return this;
    }

}
