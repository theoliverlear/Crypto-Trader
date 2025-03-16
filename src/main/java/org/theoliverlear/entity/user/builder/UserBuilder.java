package org.theoliverlear.entity.user.builder;

import org.theoliverlear.entity.portfolio.Portfolio;
import org.theoliverlear.entity.user.ProfilePicture;
import org.theoliverlear.entity.user.SafePassword;
import org.theoliverlear.entity.user.User;
import org.theoliverlear.entity.user.builder.models.AbstractUser;

import java.time.LocalDateTime;

public class UserBuilder extends AbstractUser {
    private String username;
    private String email;
    private SafePassword safePassword;
    private Portfolio portfolio;
    private ProfilePicture profilePicture;
    private LocalDateTime lastLogin;
    public UserBuilder() {
        this.username = null;
        this.email = null;
        this.safePassword = null;
        this.portfolio = null;
        this.lastLogin = null;
    }

    @Override
    public AbstractUser username(String username) {
        this.username = username;
        return this;
    }

    @Override
    public AbstractUser email(String email) {
        this.email = email;
        return this;
    }

    @Override
    public AbstractUser safePassword(SafePassword safePassword) {
        this.safePassword = safePassword;
        return this;
    }

    @Override
    public AbstractUser safePassword(String rawPassword) {
        this.safePassword = new SafePassword(rawPassword);
        return this;
    }

    @Override
    public AbstractUser portfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
        return this;
    }

    @Override
    public AbstractUser profilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    @Override
    public AbstractUser lastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }

    @Override
    public User build() {
        return new User(this.username,
                        this.email,
                        this.safePassword,
                        this.portfolio,
                        this.profilePicture,
                        this.lastLogin);
    }
}
