package org.cryptotrader.api.controller;

import jakarta.servlet.http.HttpSession;
import org.cryptotrader.api.library.communication.request.LoginRequest;
import org.cryptotrader.api.library.communication.request.SignupRequest;
import org.cryptotrader.api.library.events.UserRegisteredEvent;
import org.cryptotrader.api.library.events.publisher.UserEventsPublisher;
import org.cryptotrader.api.library.services.AuthService;
import org.cryptotrader.api.library.services.ProductUserService;
import org.cryptotrader.api.library.services.SessionService;
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
    private final SessionService sessionService;
    private final UserEventsPublisher userEventsPublisher;
    private final ProductUserService productUserService;
    @Autowired
    public AuthController(AuthService authService,
                          SessionService sessionService,
                          UserEventsPublisher userEventsPublisher,
                          ProductUserService productUserService) {
        this.authService = authService;
        this.sessionService = sessionService;
        this.userEventsPublisher = userEventsPublisher;
        this.productUserService = productUserService;
    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest signupRequest, HttpSession session) {
        boolean userInSession = this.sessionService.userInSession(session);
        if (userInSession) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized));
        } else {
            PayloadStatusResponse<AuthResponse> signupResponse = this.authService.signup(signupRequest);
            if (signupResponse.getPayload().isAuthorized()) {
                User possibleUser = this.productUserService.getUserByEmail(signupRequest.getEmail());
                this.userEventsPublisher.publishUserRegisteredEvent(new UserRegisteredEvent(possibleUser, LocalDateTime.now()));
            }
            return ResponseEntity.status(signupResponse.getStatus()).body(signupResponse.getPayload());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        boolean userInSession = this.sessionService.userInSession(session);
        if (userInSession) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized));
        } else {
            PayloadStatusResponse<AuthResponse> signupResponse = this.authService.login(loginRequest);
            return ResponseEntity.status(signupResponse.getStatus()).body(signupResponse.getPayload());
        }
    }
    // TODO: Refactor to use HttpServletRequest to verify session.
    @GetMapping("/logged-in")
    public ResponseEntity<AuthResponse> isLoggedIn(HttpSession session) {
        boolean userInSession = this.sessionService.userInSession(session);
        if (userInSession) {
            return ResponseEntity.ok(new AuthResponse(AuthStatus.AUTHORIZED.isAuthorized));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized));
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        boolean userInSession = this.sessionService.userInSession(session);
        if (userInSession) {
            this.sessionService.removeSessionUser(session);
            return ResponseEntity.ok(new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized));
        }
    }
}
