package org.theoliverlear.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "currency_history")
public class CurrencyHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "currency_code", nullable = false)
    private Currency currency;
    @Column(name = "currency_value")
    private double value;
    @Column(name = "currency_value_formatted")
    private String formattedValue;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    public CurrencyHistory() {
        this.currency = new Currency();
        this.value = 0;
        this.formattedValue = "";
        this.lastUpdated = LocalDateTime.now();
    }
    public CurrencyHistory(Currency currency, double value) {
        this.currency = currency;
        this.value = value;
        this.formattedValue = currency.getDecimalFormat().format(value);
        this.lastUpdated = LocalDateTime.now();
    }
    public CurrencyHistory(Currency currency, double value, LocalDateTime lastUpdated) {
        this.currency = currency;
        this.value = value;
        this.formattedValue = currency.getDecimalFormat().format(value);
        this.lastUpdated = lastUpdated;
    }
}