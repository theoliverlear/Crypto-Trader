package org.theoliverlear.entity.user.builder.models;

import org.theoliverlear.entity.portfolio.Portfolio;
import org.theoliverlear.entity.user.ProductUser;
import org.theoliverlear.entity.user.User;
import org.theoliverlear.entity.user.ProfilePicture;
import org.theoliverlear.entity.user.SafePassword;
import org.theoliverlear.model.BuilderFactory;

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
