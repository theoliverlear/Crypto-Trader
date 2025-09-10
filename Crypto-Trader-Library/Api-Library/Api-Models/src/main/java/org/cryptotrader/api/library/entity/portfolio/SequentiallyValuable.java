package org.cryptotrader.api.library.entity.portfolio;

public interface SequentiallyValuable<T> {
    void calculateValueChange(T previous);
}
