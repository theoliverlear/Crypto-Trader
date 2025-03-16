package org.theoliverlear.entity.user.builder.models;

import org.theoliverlear.entity.portfolio.Portfolio;
import org.theoliverlear.entity.user.ProfilePicture;
import org.theoliverlear.entity.user.SafePassword;
import org.theoliverlear.entity.user.User;
import org.theoliverlear.model.BuilderFactory;

import java.time.LocalDateTime;

public abstract class AbstractUser implements BuilderFactory<User> {
    public abstract AbstractUser username(String username);
    public abstract AbstractUser email(String email);
    public abstract AbstractUser safePassword(SafePassword safePassword);
    public abstract AbstractUser safePassword(String rawPassword);
    public abstract AbstractUser portfolio(Portfolio portfolio);
    public abstract AbstractUser profilePicture(ProfilePicture profilePicture);
    public abstract AbstractUser lastLogin(LocalDateTime lastLogin);
}
