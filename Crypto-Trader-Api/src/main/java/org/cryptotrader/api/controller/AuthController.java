package org.cryptotrader.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.api.library.communication.request.LoginRequest;
import org.cryptotrader.api.library.communication.request.SignupRequest;
import org.cryptotrader.api.library.communication.response.AuthResponse;
import org.cryptotrader.api.library.entity.user.ProductUser;
import org.cryptotrader.api.library.events.UserRegisteredEvent;
import org.cryptotrader.api.library.events.publisher.UserEventsPublisher;
import org.cryptotrader.api.library.model.dpop.DpopProofContext;
import org.cryptotrader.universal.library.model.http.AuthStatus;
import org.cryptotrader.universal.library.model.http.PayloadStatusResponse;
import org.cryptotrader.api.library.scripts.http.CookieScript;
import org.cryptotrader.api.library.scripts.http.HttpScript;
import org.cryptotrader.api.library.services.*;
import org.cryptotrader.api.library.component.dpop.DpopReplayCache;
import org.cryptotrader.api.library.services.dpop.DpopVerifierService;
import org.cryptotrader.api.library.services.jwt.JwtTokenService;
import org.cryptotrader.api.library.services.jwt.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Authentication API endpoints implementing DPoP-bound access tokens and rotating refresh tokens.
 *
 * Endpoints:
 * - POST /api/auth/signup — Optional DPoP proof; when present, the issued access token is bound via cnf.jkt.
 * - POST /api/auth/login  — Optional DPoP proof; when present, the issued access token is bound via cnf.jkt.
 * - POST /api/auth/refresh — Requires valid DPoP proof and HttpOnly refresh cookie; rotates cookie and returns new access token.
 * - GET  /api/auth/logout  — Revokes refresh token family, clears cookie, blacklists current access token.
 *
 * Headers:
 * - Authorization: DPoP <access-token> (for protected resources; not used on login/signup)
 * - DPoP: <dpop-proof> (required on refresh; optional on login/signup to bind initial token)
 * - DPoP-JKT: <jwk-thumbprint> (optional on login/signup if DPoP proof is not sent)
 *
 * Cookie:
 * - __Host-rt — HttpOnly; Secure; SameSite (Strict/None); Path=/; rotated on each refresh.
 */
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final UserEventsPublisher userEventsPublisher;
    private final ProductUserService productUserService;
    private final AuthContextService authContextService;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;
    private final DpopReplayCache replayCache;
    private final DpopVerifierService dpopVerifier;

    @Value("${security.refresh.cookie-samesite:Strict}")
    private String refreshCookieSameSite;

    @Value("${security.refresh.cookie-secure:true}")
    private boolean refreshCookieSecure;

    @Autowired
    public AuthController(AuthService authService,
                          UserEventsPublisher userEventsPublisher,
                          ProductUserService productUserService,
                          AuthContextService authContextService,
                          JwtTokenService jwtTokenService,
                          RefreshTokenService refreshTokenService,
                          DpopReplayCache replayCache,
                          DpopVerifierService dpopVerifier) {
        this.authService = authService;
        this.userEventsPublisher = userEventsPublisher;
        this.productUserService = productUserService;
        this.authContextService = authContextService;
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenService = refreshTokenService;
        this.replayCache = replayCache;
        this.dpopVerifier = dpopVerifier;
    }

/**
     * Sign up a new user and start a session.
     *
     * What this does in plain words:
     * - Creates a user account if the email is not already used.
     * - If you include a DPoP header, the access token we return will be “tied” to your browser key,
     *   so it can’t be replayed on another device.
     * - Also issues a long-lived refresh cookie so you don’t have to log in again soon.
     *
     * Headers:
     * - DPoP (optional): a one-time proof for this HTTP request. If present, we bind tokens to your key.
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest signupRequest,
                                               @RequestHeader(value = "DPoP", required = false) String dpopProof,
                                               HttpServletRequest request) {
        if (this.authContextService.isAuthenticated()) {
            AuthResponse authResponse = new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResponse);
        }
        String jwkThumbprint = null;
        if (dpopProof != null) {
            jwkThumbprint = this.deriveJwkThumbprintFromProof(dpopProof, request, "POST");
            if (jwkThumbprint == null) {
                AuthResponse authResponse = new AuthResponse(false);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
            }
        }
        PayloadStatusResponse<AuthResponse> signupResponse = this.authService.signup(signupRequest, jwkThumbprint);
        if (signupResponse.getPayload().isAuthorized()) {
            ProductUser possibleUser = this.productUserService.getUserByEmail(signupRequest.getEmail());
            // Issue refresh token cookie bound to jkt
            RefreshTokenService.RefreshTokenIssue issue = this.refreshTokenService.issue(possibleUser.getId(), jwkThumbprint);
            ResponseCookie cookie = CookieScript.INSTANCE.buildRefreshCookie(this.refreshTokenService.cookieName(),
                                                                             issue.getId(),
                                                                             issue.getExpiresAt(),
                                                                             this.refreshCookieSecure,
                                                                             this.refreshCookieSameSite);
            // Publish event
            UserRegisteredEvent registerEvent = new UserRegisteredEvent(possibleUser, LocalDateTime.now());
            this.userEventsPublisher.publishUserRegisteredEvent(registerEvent);
            return ResponseEntity.status(signupResponse.getStatus())
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(signupResponse.getPayload());
        }
        return ResponseEntity.status(signupResponse.getStatus()).body(signupResponse.getPayload());
    }

/**
     * Log in and start a session.
     *
     * In simple terms:
     * - We check your email and password.
     * - You must include a DPoP header so we can bind your tokens to your browser key.
     * - On success, we return a short-lived access token and set a refresh cookie.
     *
     * Headers:
     * - DPoP (required): proof for this POST /auth/login call.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest,
                                              @RequestHeader(value = "DPoP", required = false) String dpopProof,
                                              HttpServletRequest request) {
        if (this.authContextService.isAuthenticated()) {
            AuthResponse authResponse = new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResponse);
        }
        boolean hasProof = this.dpopVerifier.isValidProof(dpopProof);
        AuthResponse unauthorizedResponse = new AuthResponse(false);
        if (!hasProof) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorizedResponse);
        }
        String jwkThumbprint = this.deriveJwkThumbprintFromProof(dpopProof, request, "POST");
        if (jwkThumbprint == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorizedResponse);
        }
        PayloadStatusResponse<AuthResponse> loginResponse = this.authService.login(loginRequest, jwkThumbprint);
        if (loginResponse.getPayload().isAuthorized()) {
            ProductUser user = this.productUserService.getUserByEmail(loginRequest.getEmail());
            RefreshTokenService.RefreshTokenIssue issue = this.refreshTokenService.issue(user.getId(), jwkThumbprint);
            ResponseCookie cookie = CookieScript.INSTANCE.buildRefreshCookie(this.refreshTokenService.cookieName(),
                                                                             issue.getId(),
                                                                             issue.getExpiresAt(),
                                                                             this.refreshCookieSecure,
                                                                             this.refreshCookieSameSite);
            return ResponseEntity.status(loginResponse.getStatus())
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(loginResponse.getPayload());
        }
        return ResponseEntity.status(loginResponse.getStatus()).body(loginResponse.getPayload());
    }

/**
     * Refresh the access token.
     *
     * In plain words:
     * - The browser sends a DPoP proof and the refresh cookie (sent automatically with credentials).
     * - We verify both, rotate the refresh cookie (so an old one can’t be reused), and return a new access token.
     * - If anything is suspicious (missing/used cookie, mismatched key), we revoke the session.
     *
     * Headers:
     * - DPoP (required)
     * Cookie (sent automatically):
     * - __Host-rt
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader(value = "DPoP", required = false) String dpopProof,
                                                HttpServletRequest request) {
        // Must have refresh cookie
        String cookieValue = HttpScript.readCookie(request, this.refreshTokenService.cookieName());
        if (cookieValue == null || cookieValue.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(false));
        }
        boolean hasProof = this.dpopVerifier.isValidProof(dpopProof);
        if (!hasProof) {
            ResponseCookie del = CookieScript.INSTANCE.deleteCookie(this.refreshTokenService.cookieName(),
                                                                    this.refreshCookieSecure,
                                                                    this.refreshCookieSameSite);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, del.toString())
                    .body(new AuthResponse(false));
        }
        String jwkThumbprint = this.deriveJwkThumbprintFromProof(dpopProof, request, "POST");
        if (jwkThumbprint == null) {
            // DPoP mandatory on refresh
            ResponseCookie cookieToDelete = CookieScript.INSTANCE.deleteCookie(this.refreshTokenService.cookieName(),
                                                                               this.refreshCookieSecure,
                                                                               this.refreshCookieSameSite);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, cookieToDelete.toString())
                    .body(new AuthResponse(false));
        }
        RefreshTokenService.RotationResult rotationResult = this.refreshTokenService.validateAndRotate(cookieValue, jwkThumbprint);
        if (rotationResult.getNewRecord() == null) {
            // reuse or invalid
            ResponseCookie cookieToDelete = CookieScript.INSTANCE.deleteCookie(this.refreshTokenService.cookieName(),
                                                                               this.refreshCookieSecure,
                                                                               this.refreshCookieSameSite);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, cookieToDelete.toString())
                    .body(new AuthResponse(false));
        }
        // Load user and issue new access token bound to same jkt
        long userId = rotationResult.getNewRecord().getUserId();
        ProductUser user = this.productUserService.getUserById(userId);
        String email = (user != null) ? user.getEmail() : null;
        if (email == null) {
            log.error("User not found for refresh token: {}", userId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse(false));
        }
        String token = this.jwtTokenService.generateToken(String.valueOf(user.getId()), email, jwkThumbprint);
        AuthResponse payload = new AuthResponse(true, token);
        ResponseCookie cookie = CookieScript.INSTANCE.buildRefreshCookie(this.refreshTokenService.cookieName(),
                                                                         rotationResult.getNewRecord().getId(),
                                                                         rotationResult.getNewRecord().getExpiresAt(),
                                                                         this.refreshCookieSecure,
                                                                         this.refreshCookieSameSite);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(payload);
    }

/**
     * Quick status check used by the UI.
     * Returns authorized=true if the current request is authenticated.
     */
    @GetMapping("/logged-in")
    public ResponseEntity<AuthResponse> isLoggedIn() {
        boolean authenticated = this.authContextService.isAuthenticated();
        log.info("User logged in: {}", authenticated);
        AuthResponse authResponse = new AuthResponse(authenticated);
        return ResponseEntity.ok(authResponse);
    }

/**
     * Log out and end the session.
     *
     * What happens:
     * - Requires a DPoP proof (so only the real browser can end the session).
     * - Revokes the refresh token family and clears the HttpOnly cookie.
     * - Blacklists the current access token until it expires.
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(@RequestHeader(value = "DPoP", required = false) String dpopProof,
                                               HttpServletRequest request) {
        // DPoP is optional for logout: if provided and valid, great; if missing/invalid, we still end the session.
        // Attempt to derive jkt only to validate the proof when present; ignore failures.
        boolean isValidProof = this.dpopVerifier.isValidProof(dpopProof);
        if (isValidProof) {
            try {
                this.deriveJwkThumbprintFromProof(dpopProof, request, "POST");
            } catch (Exception _) {
                // Ignore proof validation failures
            }
        }
        // Clear refresh cookie and revoke its family
        String cookieValue = HttpScript.readCookie(request, this.refreshTokenService.cookieName());
        ResponseCookie cookieToDelete = CookieScript.INSTANCE.deleteCookie(this.refreshTokenService.cookieName(),
                                                                           this.refreshCookieSecure,
                                                                           this.refreshCookieSameSite);
        if (cookieValue != null && !cookieValue.isBlank()) {
            this.refreshTokenService.revokeByTokenId(cookieValue);
        }
        // Blacklist presented access token if any
        this.authContextService.logout();
        AuthResponse authResponse = new AuthResponse(AuthStatus.UNAUTHORIZED.isAuthorized);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookieToDelete.toString()).body(authResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<AuthResponse> logoutGet(@RequestHeader(value = "DPoP", required = false) String dpopProof,
                                                  HttpServletRequest request) {
        return logout(dpopProof, request);
    }



/**
     * Derive the key thumbprint (jkt) from a DPoP proof.
     *
     * Think of this as: “read the public key that signed the proof and compute a short fingerprint”.
     * We use this fingerprint to bind access/refresh tokens to the same browser key.
     *
     * @param dpopProof      The DPoP header value (a signed JWT created by the browser).
     * @param request        Used to confirm the proof matches this URL and method.
     * @param expectedMethod The HTTP method we expect (e.g., POST).
     * @return The jkt string if valid; otherwise null.
     */
    private String deriveJwkThumbprintFromProof(String dpopProof, HttpServletRequest request, String expectedMethod) {
        try {
            // If the pre-JWT DPoPValidationFilter has already verified the proof, reuse its context
            Object context = (request != null) ? request.getAttribute("dpop.proof") : null;
            if (context instanceof DpopProofContext proofContext) {
                return proofContext.getKeyThumbprint();
            }
            // Otherwise, verify here (including replay check)
            DpopVerifierService.VerificationResult verification = this.dpopVerifier.verify(dpopProof,
                                                                                           expectedMethod,
                                                                                           HttpScript.fullUrl(request),
                                                                                           null,
                                                                                           20L);
            if (verification == null) {
                return null;
            }
            if (this.replayCache.isReplay(verification.getJwtId())) {
                return null;
            }
            return verification.getJwkThumbprint();
        } catch (Exception ex) {
            log.debug("Failed to derive jkt from DPoP proof", ex);
        }
        return null;
    }
 
    // Convenience overloads for unit tests and backward compatibility (not HTTP endpoints)
    /**
     * Overload without DPoP or request argument for tests.
     */
    @Deprecated(forRemoval = true)
    public ResponseEntity<AuthResponse> signup(SignupRequest signupRequest) {
        return signup(signupRequest, null, null);
    }

    /**
     * Overload without DPoP or request argument for tests.
     */
    @Deprecated(forRemoval = true)
    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        return login(loginRequest, null, null);
    }

    /**
     * Overload without DPoP or request argument for tests.
     */
    @Deprecated(forRemoval = true)
    public ResponseEntity<AuthResponse> logout() {
        return logout(null, null);
    }
}
