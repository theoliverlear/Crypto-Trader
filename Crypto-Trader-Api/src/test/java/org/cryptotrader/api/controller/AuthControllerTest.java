package org.cryptotrader.api.controller;

import jakarta.servlet.http.HttpSession;
import org.cryptotrader.api.services.AuthService;
import org.cryptotrader.api.services.SessionService;
import org.cryptotrader.comm.request.UserRequest;
import org.cryptotrader.comm.response.AuthResponse;
import org.cryptotrader.model.http.PayloadStatusResponse;
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
    private SessionService sessionService;
    
    @Mock
    private HttpSession httpSession;
    
    private UserRequest userRequest;
    
    @BeforeEach
    void setup() {
        this.userRequest = new UserRequest("Ollie", "ollie@ollie.com", "password");
    }
    
    @Test
    @DisplayName("Should not sign up users in session")
    public void signup_NotSignUp_UsersInSession() {
        when(this.sessionService.userInSession(this.httpSession)).thenReturn(true);
        ResponseEntity<AuthResponse> signupResponse = this.authController.signup(this.userRequest, this.httpSession);
        verify(this.sessionService).userInSession(this.httpSession);
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        AuthResponse expectedResponse = new AuthResponse(false);
        assertEquals(expectedStatus, signupResponse.getStatusCode());
        assertEquals(expectedResponse, signupResponse.getBody());
    }
    
    @Test
    @DisplayName("Should sign up users not in session")
    public void signup_SignUp_UsersNotInSession() {
        when(this.sessionService.userInSession(this.httpSession)).thenReturn(false);
        when(this.authService.signup(this.userRequest)).thenReturn(new PayloadStatusResponse<>(new AuthResponse(true), HttpStatus.OK));
        ResponseEntity<AuthResponse> signupResponse = this.authController.signup(this.userRequest, this.httpSession);
        verify(this.sessionService).userInSession(this.httpSession);
        verify(this.authService).signup(this.userRequest);
        HttpStatus expectedStatus = HttpStatus.OK;
        AuthResponse expectedResponse = new AuthResponse(true);
        assertEquals(expectedStatus, signupResponse.getStatusCode());
        assertEquals(expectedResponse, signupResponse.getBody());
    }
    
    @Test
    @DisplayName("Should not login users in session")
    public void login_NotLogin_UsersInSession() {
        when(this.sessionService.userInSession(this.httpSession)).thenReturn(true);
        ResponseEntity<AuthResponse> loginResponse = this.authController.login(this.userRequest, this.httpSession);
        verify(this.sessionService).userInSession(this.httpSession);
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        AuthResponse expectedResponse = new AuthResponse(false);
        assertEquals(expectedStatus, loginResponse.getStatusCode());
        assertEquals(expectedResponse, loginResponse.getBody());
    }
    
    @Test
    @DisplayName("Should login users not in session")
    public void login_Login_UsersNotInSession() {
        when(this.sessionService.userInSession(this.httpSession)).thenReturn(false);
        when(this.authService.login(this.userRequest)).thenReturn(new PayloadStatusResponse<>(new AuthResponse(true), HttpStatus.OK));
        ResponseEntity<AuthResponse> loginResponse = this.authController.login(this.userRequest, this.httpSession);
        verify(this.sessionService).userInSession(this.httpSession);
        verify(this.authService).login(this.userRequest);
        HttpStatus expectedStatus = HttpStatus.OK;
        AuthResponse expectedResponse = new AuthResponse(true);
        assertEquals(expectedStatus, loginResponse.getStatusCode());
        assertEquals(expectedResponse, loginResponse.getBody());
    }
    
    @Test
    @DisplayName("Should logout users in session")
    public void logout_Logout_UsersInSession() {
        when(this.sessionService.userInSession(this.httpSession)).thenReturn(true);
        ResponseEntity<AuthResponse> logoutResponse = this.authController.logout(this.httpSession);
        verify(this.sessionService).userInSession(this.httpSession);
        verify(this.sessionService).removeSessionUser(this.httpSession);
        HttpStatus expectedStatus = HttpStatus.OK;
        AuthResponse expectedResponse = new AuthResponse(false);
        assertEquals(expectedStatus, logoutResponse.getStatusCode());
        assertEquals(expectedResponse, logoutResponse.getBody());
    }
    
    @Test
    @DisplayName("Should not logout users not in session")
    public void logout_NotLogout_UsersNotInSession() {
        when(this.sessionService.userInSession(this.httpSession)).thenReturn(false);
        ResponseEntity<AuthResponse> logoutResponse = this.authController.logout(this.httpSession);
        verify(this.sessionService).userInSession(this.httpSession);
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        AuthResponse expectedResponse = new AuthResponse(false);
        assertEquals(expectedStatus, logoutResponse.getStatusCode());
        assertEquals(expectedResponse, logoutResponse.getBody());
    }
    
    
    @Test
    @DisplayName("Should count session users as logged in")
    public void isLoggedIn_LoggedIn_UsersInSession() {
        when(this.sessionService.userInSession(this.httpSession)).thenReturn(true);
        ResponseEntity<AuthResponse> isLoggedInResponse = this.authController.isLoggedIn(this.httpSession);
        verify(this.sessionService).userInSession(this.httpSession);
        HttpStatus expectedStatus = HttpStatus.OK;
        AuthResponse expectedResponse = new AuthResponse(true);
        assertEquals(expectedStatus, isLoggedInResponse.getStatusCode());
        assertEquals(expectedResponse, isLoggedInResponse.getBody());
    }
    
    @Test
    @DisplayName("Should not count session users as logged in")
    public void isLoggedIn_NotLoggedIn_UsersInSession() {
        when(this.sessionService.userInSession(this.httpSession)).thenReturn(false);
        ResponseEntity<AuthResponse> isLoggedInResponse = this.authController.isLoggedIn(this.httpSession);
        verify(this.sessionService).userInSession(this.httpSession);
        HttpStatus expectedStatus = HttpStatus.UNAUTHORIZED;
        AuthResponse expectedResponse = new AuthResponse(false);
        assertEquals(expectedStatus, isLoggedInResponse.getStatusCode());
        assertEquals(expectedResponse, isLoggedInResponse.getBody());
    }
}
