package org.theoliverlear.entity.user.builder;

import org.theoliverlear.entity.portfolio.Portfolio;
import org.theoliverlear.entity.user.ProductUser;
import org.theoliverlear.entity.user.ProfilePicture;
import org.theoliverlear.entity.user.SafePassword;
import org.theoliverlear.entity.user.builder.models.AbstractProductUser;

import java.time.LocalDateTime;

public class ProductUserBuilder extends AbstractProductUser {
    private String username;
    private String email;
    private SafePassword safePassword;
    private Portfolio portfolio;
    private ProfilePicture profilePicture;
    private LocalDateTime lastLogin;
    public ProductUserBuilder() {
        this.username = null;
        this.email = null;
        this.safePassword = null;
        this.portfolio = null;
        this.lastLogin = null;
    }

    @Override
    public AbstractProductUser username(String username) {
        this.username = username;
        return this;
    }

    @Override
    public AbstractProductUser email(String email) {
        this.email = email;
        return this;
    }

    @Override
    public AbstractProductUser safePassword(SafePassword safePassword) {
        this.safePassword = safePassword;
        return this;
    }

    @Override
    public AbstractProductUser safePassword(String rawPassword) {
        this.safePassword = new SafePassword(rawPassword);
        return this;
    }

    @Override
    public AbstractProductUser portfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
        return this;
    }

    @Override
    public AbstractProductUser profilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    @Override
    public AbstractProductUser lastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }

    @Override
    public ProductUser build() {
        return new ProductUser(this.username,
                        this.email,
                        this.safePassword,
                        this.portfolio,
                        this.profilePicture,
                        this.lastLogin);
    }
}
