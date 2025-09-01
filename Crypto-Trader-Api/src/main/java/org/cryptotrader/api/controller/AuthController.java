package org.cryptotrader.api.controller;

import jakarta.servlet.http.HttpSession;
import org.cryptotrader.api.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.cryptotrader.comm.request.UserRequest;
import org.cryptotrader.comm.response.AuthResponse;
import org.cryptotrader.model.http.AuthStatus;
import org.cryptotrader.model.http.PayloadStatusResponse;
import org.cryptotrader.api.service.AuthService;
import org.cryptotrader.api.service.CryptoTraderService;

@RestController
@RequestMapping("/api/authorize")
public class AuthController {
    private AuthService authService;
    private SessionService sessionService;
    @Autowired
    public AuthController(AuthService authService,
                          SessionService sessionService) {
        this.authService = authService;
        this.sessionService = sessionService;
    }
    @RequestMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody UserRequest userRequest, HttpSession session) {
        boolean userInSession = this.sessionService.userInSession(session);
        if (userInSession) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized));
        } else {
            PayloadStatusResponse<AuthResponse> signupResponse = this.authService.signup(userRequest);
            return ResponseEntity.status(signupResponse.getStatus()).body(signupResponse.getPayload());
        }
    }
    @RequestMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserRequest userRequest, HttpSession session) {
        boolean userInSession = this.sessionService.userInSession(session);
        if (userInSession) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized));
        } else {
            PayloadStatusResponse<AuthResponse> signupResponse = this.authService.login(userRequest);
            return ResponseEntity.status(signupResponse.getStatus()).body(signupResponse.getPayload());
        }
    }
    @RequestMapping("/isloggedin")
    public ResponseEntity<AuthResponse> isLoggedIn(HttpSession session) {
        boolean userInSession = this.sessionService.userInSession(session);
        if (userInSession) {
            return ResponseEntity.ok(new AuthResponse(AuthStatus.AUTHORIZED.isAuthorized));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized));
        }
    }
    @RequestMapping("/logout")
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
