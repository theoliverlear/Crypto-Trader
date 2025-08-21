package org.cryptotrader.entity.currency;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "unique_currencies")
@AllArgsConstructor
@NoArgsConstructor
public class UniqueCurrency {
    @Column(name = "currency_name")
    private String name;
    @Id
    @Column(name = "currency_code")
    private String currency;
    private String urlPath;
    @Column(name = "currency_value", columnDefinition = "DECIMAL(34, 18)")
    private double value;
    @Column(name = "currency_value_formatted")
    private String formattedValue;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    public UniqueCurrency(Currency currency) {
        this.name = currency.getName();
        this.currency = currency.getCurrencyCode();
        this.urlPath = currency.getUrlPath();
        this.value = currency.getValue();
        this.formattedValue = currency.getFormattedValue();
        this.lastUpdated = LocalDateTime.now();
    }
}
