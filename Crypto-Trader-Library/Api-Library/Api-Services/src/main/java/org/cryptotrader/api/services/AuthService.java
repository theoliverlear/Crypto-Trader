package org.cryptotrader.api.services;

import org.cryptotrader.comm.request.UserRequest;
import org.cryptotrader.comm.response.AuthResponse;
import org.cryptotrader.entity.portfolio.Portfolio;
import org.cryptotrader.entity.user.ProductUser;
import org.cryptotrader.entity.user.SafePassword;
import org.cryptotrader.entity.user.User;
import org.cryptotrader.model.http.AuthStatus;
import org.cryptotrader.model.http.PayloadStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private ProductUserService productUserService;
    private PortfolioService portfolioService;
    @Autowired
    public AuthService(ProductUserService productUserService,
                       PortfolioService portfolioService) {
        this.productUserService = productUserService;
        this.portfolioService = portfolioService;
    }
    public PayloadStatusResponse<AuthResponse> signup(UserRequest userRequest) {
        String username = userRequest.getUsername();
        String email = userRequest.getEmail();
        String password = userRequest.getPassword();
        boolean userExists = this.productUserService.userExistsByUsername(username);
        if (userExists) {
            return new PayloadStatusResponse<>(new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized), HttpStatus.CONFLICT);
        } else {
            SafePassword safePassword = new SafePassword(password);
            ProductUser user = ProductUser.builder()
                            .username(username)
                            .email(email)
                            .safePassword(safePassword)
                            .build();
            Portfolio portfolio = new Portfolio(user);
            user.setPortfolio(portfolio);
            this.productUserService.saveUser(user);
            this.portfolioService.savePortfolio(portfolio);
            this.portfolioService.addPortfolioToTraders(portfolio);
            return new PayloadStatusResponse<>(new AuthResponse(AuthStatus.AUTHORIZED.isAuthorized), HttpStatus.OK);
        }
    }
    public PayloadStatusResponse<AuthResponse> login(UserRequest userRequest) {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();
        User user = this.productUserService.getUserByUsername(username);
        if (user == null) {
            return new PayloadStatusResponse<>(new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized), HttpStatus.NOT_FOUND);
        } else {
            boolean passwordsMatch = this.productUserService.comparePassword(user, password);
            if (passwordsMatch) {
                return new PayloadStatusResponse<>(new AuthResponse(AuthStatus.AUTHORIZED.isAuthorized), HttpStatus.OK);
            } else {
                return new PayloadStatusResponse<>(new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized), HttpStatus.UNAUTHORIZED);
            }
        }
    }
}
