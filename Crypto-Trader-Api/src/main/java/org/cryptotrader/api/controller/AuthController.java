package org.cryptotrader.api.controller;

import org.cryptotrader.api.library.communication.request.LoginRequest;
import org.cryptotrader.api.library.communication.request.SignupRequest;
import org.cryptotrader.api.library.events.UserRegisteredEvent;
import org.cryptotrader.api.library.events.publisher.UserEventsPublisher;
import org.cryptotrader.api.library.services.AuthContextService;
import org.cryptotrader.api.library.services.AuthService;
import org.cryptotrader.api.library.services.ProductUserService;
import org.cryptotrader.api.library.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.cryptotrader.api.library.communication.response.AuthResponse;
import org.cryptotrader.api.library.model.http.AuthStatus;
import org.cryptotrader.api.library.model.http.PayloadStatusResponse;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserEventsPublisher userEventsPublisher;
    private final ProductUserService productUserService;
    private final AuthContextService authContextService;
    @Autowired
    public AuthController(AuthService authService,
                          UserEventsPublisher userEventsPublisher,
                          ProductUserService productUserService,
                          AuthContextService authContextService) {
        this.authService = authService;
        this.userEventsPublisher = userEventsPublisher;
        this.productUserService = productUserService;
        this.authContextService = authContextService;
    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest signupRequest) {
        if (this.authContextService.isAuthenticated()) {
            AuthResponse authResponse = new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResponse);
        }
        PayloadStatusResponse<AuthResponse> signupResponse = this.authService.signup(signupRequest);
        if (signupResponse.getPayload().isAuthorized()) {
            User possibleUser = this.productUserService.getUserByEmail(signupRequest.getEmail());
            UserRegisteredEvent registerEvent = new UserRegisteredEvent(possibleUser, LocalDateTime.now());
            this.userEventsPublisher.publishUserRegisteredEvent(registerEvent);
        }
        return ResponseEntity.status(signupResponse.getStatus()).body(signupResponse.getPayload());
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        if (this.authContextService.isAuthenticated()) {
            AuthResponse authResponse = new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResponse);
        }
        PayloadStatusResponse<AuthResponse> loginResponse = this.authService.login(loginRequest);
        return ResponseEntity.status(loginResponse.getStatus()).body(loginResponse.getPayload());
    }
    @GetMapping("/logged-in")
    public ResponseEntity<AuthResponse> isLoggedIn() {
        boolean authenticated = this.authContextService.isAuthenticated();
        AuthResponse authResponse = new AuthResponse(authenticated);
        return ResponseEntity.ok(authResponse);
    }
    @GetMapping("/logout")
    public ResponseEntity<AuthResponse> logout() {
        AuthResponse authResponse = new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized);
        boolean authenticated = this.authContextService.isAuthenticated();
        if (authenticated) {
            this.authContextService.logout();
        }
        return ResponseEntity.ok(authResponse);
    }
}
