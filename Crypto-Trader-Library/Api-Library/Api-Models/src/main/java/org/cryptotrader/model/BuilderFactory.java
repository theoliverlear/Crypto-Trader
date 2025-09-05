package org.cryptotrader.model;

/**
 * Generic factory interface for building objects.
 *
 * @param <T> type of object constructed by the factory
 * @see org.cryptotrader.entity.portfolio.builder.models.AbstractPortfolio
 * @see org.cryptotrader.entity.user.builder.models.AbstractProfilePicture
 * @see org.cryptotrader.entity.portfolio.Portfolio
 * @see org.cryptotrader.entity.user.ProductUser
 * @author Oliver Lear Sigwarth (theoliverlear)
 */
public interface BuilderFactory<T> {
    T build();
}
