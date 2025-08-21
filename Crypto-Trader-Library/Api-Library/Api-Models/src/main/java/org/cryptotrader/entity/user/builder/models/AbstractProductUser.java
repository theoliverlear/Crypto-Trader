package org.cryptotrader.entity.user.builder.models;

import org.cryptotrader.entity.portfolio.Portfolio;
import org.cryptotrader.entity.user.ProductUser;
import org.cryptotrader.entity.user.ProfilePicture;
import org.cryptotrader.entity.user.SafePassword;
import org.cryptotrader.model.BuilderFactory;

import java.time.LocalDateTime;

public abstract class AbstractProductUser implements BuilderFactory<ProductUser> {
    public abstract AbstractProductUser username(String username);
    public abstract AbstractProductUser email(String email);
    public abstract AbstractProductUser safePassword(SafePassword safePassword);
    public abstract AbstractProductUser safePassword(String rawPassword);
    public abstract AbstractProductUser portfolio(Portfolio portfolio);
    public abstract AbstractProductUser profilePicture(ProfilePicture profilePicture);
    public abstract AbstractProductUser lastLogin(LocalDateTime lastLogin);
}
