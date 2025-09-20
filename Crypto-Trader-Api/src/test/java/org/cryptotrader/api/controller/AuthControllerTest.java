package org.cryptotrader.api.controller;

import org.cryptotrader.api.library.entity.user.ProductUser;
import org.cryptotrader.api.library.services.AuthContextService;
import org.cryptotrader.api.library.services.AuthService;
import org.cryptotrader.api.library.services.ProductUserService;
import org.cryptotrader.api.library.events.publisher.UserEventsPublisher;
import org.cryptotrader.api.library.communication.request.SignupRequest;
import org.cryptotrader.api.library.communication.request.LoginRequest;
import org.cryptotrader.api.library.communication.response.AuthResponse;
import org.cryptotrader.api.library.model.http.PayloadStatusResponse;
import org.cryptotrader.api.library.entity.user.SafePassword;
import org.cryptotrader.test.CryptoTraderTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Auth Controller")
public class AuthControllerTest extends CryptoTraderTest {
    @InjectMocks
    private AuthController authController;
    
    @Mock
    private AuthService authService;

    @Mock
    private ProductUserService productUserService;

    @Mock
    private UserEventsPublisher userEventsPublisher;

    @Mock
    private AuthContextService authContextService;
    
    private SignupRequest signupRequest;
    private LoginRequest loginRequest;
    
    @BeforeEach
    void setup() {
        this.signupRequest = new SignupRequest("ollie@ollie.com", "password");
        this.loginRequest = new LoginRequest("ollie@ollie.com", "password");
    }
    
    @Test
    @DisplayName("Should not sign up users in session")
    public void signup_NotSignUp_UsersInSession() {
        when(this.authContextService.isAuthenticated()).thenReturn(true);
        ResponseEntity<AuthResponse> signupResponse = this.authController.signup(this.signupRequest);
        verify(this.authContextService).isAuthenticated();
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        AuthResponse expectedResponse = new AuthResponse(false);
        assertEquals(expectedStatus, signupResponse.getStatusCode());
        assertEquals(expectedResponse, signupResponse.getBody());
    }
    
    @Test
    @DisplayName("Should sign up users not in session")
    public void signup_SignUp_UsersNotInSession() {
        when(this.authContextService.isAuthenticated()).thenReturn(false);
        when(this.authService.signup(this.signupRequest)).thenReturn(new PayloadStatusResponse<>(new AuthResponse(true), HttpStatus.OK));
        when(this.productUserService.getUserByEmail("ollie@ollie.com")).thenReturn(new ProductUser("Ollie", new SafePassword("password")));
        ResponseEntity<AuthResponse> signupResponse = this.authController.signup(this.signupRequest);
        verify(this.authContextService).isAuthenticated();
        verify(this.authService).signup(this.signupRequest);
        HttpStatus expectedStatus = HttpStatus.OK;
        AuthResponse expectedResponse = new AuthResponse(true);
        assertEquals(expectedStatus, signupResponse.getStatusCode());
        assertEquals(expectedResponse, signupResponse.getBody());
    }
    
    @Test
    @DisplayName("Should not login users in session")
    public void login_NotLogin_UsersInSession() {
        when(this.authContextService.isAuthenticated()).thenReturn(true);
        ResponseEntity<AuthResponse> loginResponse = this.authController.login(this.loginRequest);
        verify(this.authContextService).isAuthenticated();
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        AuthResponse expectedResponse = new AuthResponse(false);
        assertEquals(expectedStatus, loginResponse.getStatusCode());
        assertEquals(expectedResponse, loginResponse.getBody());
    }
    
    @Test
    @DisplayName("Should login users not in session")
    public void login_Login_UsersNotInSession() {
        when(this.authContextService.isAuthenticated()).thenReturn(false);
        when(this.authService.login(this.loginRequest)).thenReturn(new PayloadStatusResponse<>(new AuthResponse(true), HttpStatus.OK));
        ResponseEntity<AuthResponse> loginResponse = this.authController.login(this.loginRequest);
        verify(this.authContextService).isAuthenticated();
        verify(this.authService).login(this.loginRequest);
        HttpStatus expectedStatus = HttpStatus.OK;
        AuthResponse expectedResponse = new AuthResponse(true);
        assertEquals(expectedStatus, loginResponse.getStatusCode());
        assertEquals(expectedResponse, loginResponse.getBody());
    }
    
    @Test
    @DisplayName("Should logout users in session")
    public void logout_Logout_UsersInSession() {
        when(this.authContextService.isAuthenticated()).thenReturn(true);
        ResponseEntity<AuthResponse> logoutResponse = this.authController.logout();
        verify(this.authContextService).isAuthenticated();
        HttpStatus expectedStatus = HttpStatus.OK;
        AuthResponse expectedResponse = new AuthResponse(false);
        assertEquals(expectedStatus, logoutResponse.getStatusCode());
        assertEquals(expectedResponse, logoutResponse.getBody());
    }
    
    @Test
    @DisplayName("Should not logout users not in session")
    public void logout_NotLogout_UsersNotInSession() {
        when(this.authContextService.isAuthenticated()).thenReturn(false);
        ResponseEntity<AuthResponse> logoutResponse = this.authController.logout();
        verify(this.authContextService).isAuthenticated();
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        AuthResponse expectedResponse = new AuthResponse(false);
        assertEquals(expectedStatus, logoutResponse.getStatusCode());
        assertEquals(expectedResponse, logoutResponse.getBody());
    }
    
    
    @Test
    @DisplayName("Should count session users as logged in")
    public void isLoggedIn_LoggedIn_UsersInSession() {
        when(this.authContextService.isAuthenticated()).thenReturn(true);
        ResponseEntity<AuthResponse> isLoggedInResponse = this.authController.isLoggedIn();
        verify(this.authContextService).isAuthenticated();
        HttpStatus expectedStatus = HttpStatus.OK;
        AuthResponse expectedResponse = new AuthResponse(true);
        assertEquals(expectedStatus, isLoggedInResponse.getStatusCode());
        assertEquals(expectedResponse, isLoggedInResponse.getBody());
    }
    
    @Test
    @DisplayName("Should not count session users as logged in")
    public void isLoggedIn_NotLoggedIn_UsersInSession() {
        when(this.authContextService.isAuthenticated()).thenReturn(false);
        ResponseEntity<AuthResponse> isLoggedInResponse = this.authController.isLoggedIn();
        verify(this.authContextService).isAuthenticated();
        HttpStatus expectedStatus = HttpStatus.UNAUTHORIZED;
        AuthResponse expectedResponse = new AuthResponse(false);
        assertEquals(expectedStatus, isLoggedInResponse.getStatusCode());
        assertEquals(expectedResponse, isLoggedInResponse.getBody());
    }
}
