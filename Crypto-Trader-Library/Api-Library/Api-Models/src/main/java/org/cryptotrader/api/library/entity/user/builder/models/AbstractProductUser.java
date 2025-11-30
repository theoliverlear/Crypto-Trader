package org.cryptotrader.api.library.entity.user.builder.models;

import org.cryptotrader.api.library.entity.portfolio.Portfolio;
import org.cryptotrader.api.library.entity.user.ProductUser;
import org.cryptotrader.api.library.entity.user.ProfilePicture;
import org.cryptotrader.api.library.entity.user.SafePassword;
import org.cryptotrader.universal.library.model.BuilderFactory;

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
