package org.theoliverlear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.theoliverlear.comm.request.UserRequest;
import org.theoliverlear.comm.response.AuthResponse;
import org.theoliverlear.comm.response.UserResponse;
import org.theoliverlear.entity.portfolio.Portfolio;
import org.theoliverlear.entity.user.SafePassword;
import org.theoliverlear.entity.user.User;
import org.theoliverlear.model.http.AuthStatus;
import org.theoliverlear.model.http.PayloadStatusResponse;

@Service
public class AuthService {
    private UserService userService;
    private PortfolioService portfolioService;
    @Autowired
    public AuthService(UserService userService,
                       PortfolioService portfolioService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
    }
    public PayloadStatusResponse<AuthResponse> signup(UserRequest userRequest) {
        String username = userRequest.getUsername();
        String email = userRequest.getEmail();
        String password = userRequest.getPassword();
        boolean userExists = this.userService.userExistsByUsername(username);
        if (userExists) {
            return new PayloadStatusResponse<>(new AuthResponse(AuthStatus.UNAUTHORIZED), HttpStatus.CONFLICT);
        } else {
            SafePassword safePassword = new SafePassword(password);
            User user = new User(username, email, safePassword);
            Portfolio portfolio = new Portfolio(user);
            user.setPortfolio(portfolio);
            this.userService.saveUser(user);
            this.portfolioService.savePortfolio(portfolio);
            this.portfolioService.addPortfolioToTraders(portfolio);
            return new PayloadStatusResponse<>(new AuthResponse(AuthStatus.AUTHORIZED), HttpStatus.OK);
        }
    }
    public PayloadStatusResponse<AuthResponse> login(UserRequest userRequest) {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();
        User user = this.userService.getUserByUsername(username);
        if (user == null) {
            return new PayloadStatusResponse<>(new AuthResponse(AuthStatus.UNAUTHORIZED), HttpStatus.NOT_FOUND);
        } else {
            boolean passwordsMatch = this.userService.comparePassword(user, password);
            if (passwordsMatch) {
                return new PayloadStatusResponse<>(new AuthResponse(AuthStatus.AUTHORIZED), HttpStatus.OK);
            } else {
                return new PayloadStatusResponse<>(new AuthResponse(AuthStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            }
        }
    }
}
