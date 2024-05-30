package org.theoliverlear.entity.portfolio;

public interface SequentiallyValuable<T> {
    void calculateValueChange(T previous);
}
