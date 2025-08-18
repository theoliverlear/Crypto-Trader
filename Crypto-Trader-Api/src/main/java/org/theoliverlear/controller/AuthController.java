package org.theoliverlear.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.theoliverlear.comm.request.UserRequest;
import org.theoliverlear.comm.response.AuthResponse;
import org.theoliverlear.model.http.AuthStatus;
import org.theoliverlear.model.http.PayloadStatusResponse;
import org.theoliverlear.service.AuthService;
import org.theoliverlear.service.CryptoTraderService;

@RestController
@RequestMapping("/api/authorize")
public class AuthController {
    private AuthService authService;
    private CryptoTraderService cryptoTraderService;
    @Autowired
    public AuthController(AuthService authService,
                          CryptoTraderService cryptoTraderService) {
        this.authService = authService;
        this.cryptoTraderService = cryptoTraderService;
    }
    @RequestMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody UserRequest userRequest, HttpSession session) {
        boolean userInSession = this.cryptoTraderService.userInSession(session);
        if (userInSession) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(AuthStatus.UNAUTHORIZED));
        } else {
            PayloadStatusResponse<AuthResponse> signupResponse = this.authService.signup(userRequest);
            return ResponseEntity.status(signupResponse.getStatus()).body(signupResponse.getPayload());
        }
    }
    @RequestMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserRequest userRequest, HttpSession session) {
        boolean userInSession = this.cryptoTraderService.userInSession(session);
        if (userInSession) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(AuthStatus.UNAUTHORIZED));
        } else {
            PayloadStatusResponse<AuthResponse> signupResponse = this.authService.login(userRequest);
            return ResponseEntity.status(signupResponse.getStatus()).body(signupResponse.getPayload());
        }
    }
    @RequestMapping("/isloggedin")
    public ResponseEntity<AuthResponse> isLoggedIn(HttpSession session) {
        boolean userInSession = this.cryptoTraderService.userInSession(session);
        if (userInSession) {
            return ResponseEntity.ok(new AuthResponse(AuthStatus.AUTHORIZED));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(AuthStatus.UNAUTHORIZED));
        }
    }
    @RequestMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        boolean userInSession = this.cryptoTraderService.userInSession(session);
        if (userInSession) {
            this.cryptoTraderService.removeSessionUser(session);
            return ResponseEntity.ok(new AuthResponse(AuthStatus.UNAUTHORIZED));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(AuthStatus.UNAUTHORIZED));
        }
    }
}
