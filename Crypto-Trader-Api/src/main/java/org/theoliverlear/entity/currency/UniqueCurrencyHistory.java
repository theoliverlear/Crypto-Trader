package org.theoliverlear.entity.currency;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.theoliverlear.entity.Identifiable;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "unique_currency_history")
@AllArgsConstructor
@NoArgsConstructor
public class UniqueCurrencyHistory extends Identifiable {
    @Column(name = "currency_name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "currency_code", nullable = false)
    private Currency currency;
    @Column(name = "currency_value", columnDefinition = "DECIMAL(34, 18)")
    private double value;
    @Column(name = "currency_value_formatted")
    private String formattedValue;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    public UniqueCurrencyHistory(Currency currency) {
        super();
        this.name = currency.getName();
        this.currency = currency;
        this.value = currency.getValue();
        this.formattedValue = currency.getFormattedValue();
        this.lastUpdated = LocalDateTime.now();
    }
}
