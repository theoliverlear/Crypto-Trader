package org.cryptotrader.api.library.services;

import org.cryptotrader.api.library.communication.request.LoginRequest;
import org.cryptotrader.api.library.communication.request.SignupRequest;
import org.cryptotrader.api.library.communication.response.AuthResponse;
import org.cryptotrader.api.library.entity.portfolio.Portfolio;
import org.cryptotrader.api.library.entity.user.ProductUser;
import org.cryptotrader.api.library.entity.user.SafePassword;
import org.cryptotrader.api.library.entity.user.User;
import org.cryptotrader.api.library.events.UserRegisteredEvent;
import org.cryptotrader.api.library.events.publisher.UserEventsPublisher;
import org.cryptotrader.api.library.model.http.AuthStatus;
import org.cryptotrader.api.library.model.http.PayloadStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final ProductUserService productUserService;
    private final PortfolioService portfolioService;
    private final UserEventsPublisher userEventsPublisher;
    private final JwtService jwtService;
    @Autowired
    public AuthService(ProductUserService productUserService,
                       PortfolioService portfolioService,
                       UserEventsPublisher userEventsPublisher,
                       JwtService jwtService) {
        this.productUserService = productUserService;
        this.portfolioService = portfolioService;
        this.userEventsPublisher = userEventsPublisher;
        this.jwtService = jwtService;
    }
    public PayloadStatusResponse<AuthResponse> signup(SignupRequest signupRequest) {
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();
        boolean userExists = this.productUserService.userExistsByEmail(email);
        if (userExists) {
            return new PayloadStatusResponse<>(new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized), HttpStatus.CONFLICT);
        } else {
            SafePassword safePassword = new SafePassword(password);
            ProductUser user = ProductUser.builder()
                            .email(email)
                            .safePassword(safePassword)
                            .build();
            Portfolio portfolio = new Portfolio(user);
            user.setPortfolio(portfolio);
            this.productUserService.saveUser(user);
            this.portfolioService.savePortfolio(portfolio);
            this.userEventsPublisher.publishUserRegisteredEvent(new UserRegisteredEvent(user, LocalDateTime.now()));
            // TODO: Replace with event publishing or other method of talking
            //       between modules.
//            this.portfolioService.addPortfolioToTraders(portfolio);
            return new PayloadStatusResponse<>(new AuthResponse(AuthStatus.AUTHORIZED.isAuthorized), HttpStatus.OK);
        }
    }
    public PayloadStatusResponse<AuthResponse> login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        User user = this.productUserService.getUserByEmail(email);
        if (user == null) {
            return new PayloadStatusResponse<>(new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized), HttpStatus.NOT_FOUND);
        } else {
            boolean passwordsMatch = this.productUserService.comparePassword(user, password);
            if (passwordsMatch) {
                String subject = String.valueOf(user.getId());
                String token = this.jwtService.generateToken(subject, email);
                return new PayloadStatusResponse<>(new AuthResponse(AuthStatus.AUTHORIZED.isAuthorized, token), HttpStatus.OK);
            } else {
                return new PayloadStatusResponse<>(new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized), HttpStatus.UNAUTHORIZED);
            }
        }
    }
}
