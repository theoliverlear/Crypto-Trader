package org.cryptotrader.entity.portfolio;

public interface SequentiallyValuable<T> {
    void calculateValueChange(T previous);
}
