package org.cryptotrader.entity.currency;
//=================================-Imports-==================================
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.cryptotrader.entity.Identifiable;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "currency_history")
public class CurrencyHistory extends Identifiable {
    //============================-Variables-=================================
    @Column(name = "currency_name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "currency_code", nullable = false)
    private Currency currency;
    @Column(name = "currency_value", columnDefinition = "DECIMAL(34, 18)")
    private double value;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    //===========================-Constructors-===============================
    public CurrencyHistory() {
        super();
        this.currency = new Currency();
        this.name = "";
        this.value = 0;
        this.lastUpdated = LocalDateTime.now();
    }
    public CurrencyHistory(Currency currency, double value) {
        super();
        this.currency = currency;
        this.name = currency.getName();
        this.value = value;
        this.lastUpdated = LocalDateTime.now();
    }
    public CurrencyHistory(Currency currency, double value, LocalDateTime lastUpdated) {
        super();
        this.currency = currency;
        this.name = currency.getName();
        this.value = value;
        this.lastUpdated = lastUpdated;
    }
}
