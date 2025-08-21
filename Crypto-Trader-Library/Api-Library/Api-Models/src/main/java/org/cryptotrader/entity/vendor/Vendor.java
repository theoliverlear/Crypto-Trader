package org.cryptotrader.entity.vendor;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cryptotrader.entity.Identifiable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class Vendor extends Identifiable {
    private String name;
    private double rate;
    public Vendor(String name, double rate) {
        this.name = name;
        this.rate = rate;
    }
    
    public double getAdjustedPrice(double price) {
        return price + (price * this.rate);
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object instanceof Vendor comparedVendor) {
            return this.name.equals(comparedVendor.name);
        }
        return false;
    }
}
