package org.cryptotrader.api.library.services;

import org.cryptotrader.api.library.comm.request.UserRequest;
import org.cryptotrader.api.library.comm.response.AuthResponse;
import org.cryptotrader.api.library.entity.portfolio.Portfolio;
import org.cryptotrader.api.library.entity.user.ProductUser;
import org.cryptotrader.api.library.entity.user.SafePassword;
import org.cryptotrader.api.library.entity.user.User;
import org.cryptotrader.api.library.model.http.AuthStatus;
import org.cryptotrader.api.library.model.http.PayloadStatusResponse;
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
            // TODO: Replace with event publishing or other method of talking
            //       between modules.
//            this.portfolioService.addPortfolioToTraders(portfolio);
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
