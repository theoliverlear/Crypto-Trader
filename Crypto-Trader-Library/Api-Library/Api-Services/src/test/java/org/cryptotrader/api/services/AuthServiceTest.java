package org.cryptotrader.api.services;

import org.cryptotrader.comm.request.UserRequest;
import org.cryptotrader.comm.response.AuthResponse;
import org.cryptotrader.entity.user.SafePassword;
import org.cryptotrader.entity.user.User;
import org.cryptotrader.model.http.PayloadStatusResponse;
import org.cryptotrader.test.CryptoTraderTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Auth Service")
public class AuthServiceTest extends CryptoTraderTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private ProductUserService productUserService;

    @Mock
    private PortfolioService portfolioService;
    
    private UserRequest userRequest;
    
    private User user;
    
    @BeforeEach
    void setup() {
        SafePassword clientSideHashedPassword = new SafePassword("password");
        String encodedPassword = clientSideHashedPassword.getEncodedPassword();
        this.userRequest = new UserRequest("Ollie", "ollie@ollie.com", encodedPassword);
        this.user = new User("Ollie", new SafePassword("password"));
    }
    
    @AfterAll
    static void tearDown() {

    }

    @Test
    @DisplayName("Should sign up users with valid requests")
    public void shouldSignUpUsersWithValidRequests() {
        when(this.productUserService.userExistsByUsername("Ollie")).thenReturn(false);
        AuthResponse expectedResponse = new AuthResponse(true);
        PayloadStatusResponse<AuthResponse> actualResponse = this.authService.signup(this.userRequest);
        verify(this.productUserService).userExistsByUsername("Ollie");
        AuthResponse actualAuthResponse = actualResponse.getPayload();
        assertEquals(expectedResponse, actualAuthResponse);
    }
    
    @Test
    @DisplayName("Should not sign up existing users")
    public void shouldNotSignUpExistingUsers() {
        when(this.productUserService.userExistsByUsername("Ollie")).thenReturn(true);
        AuthResponse expectedResponse = new AuthResponse(false);
        PayloadStatusResponse<AuthResponse> actualResponse = this.authService.signup(this.userRequest);
        AuthResponse actualAuthResponse = actualResponse.getPayload();
        assertEquals(expectedResponse, actualAuthResponse);
    }
    
    @Test
    @DisplayName("Should login users with valid requests")
    public void shouldLoginUsersWithValidRequests() {
        when(this.productUserService.getUserByUsername("Ollie")).thenReturn(this.user);
        AuthResponse expectedResponse = new AuthResponse(true);
        when(this.productUserService.comparePassword(this.user, this.userRequest.getPassword())).thenReturn(true);
        PayloadStatusResponse<AuthResponse> actualResponse = this.authService.login(this.userRequest);
        verify(this.productUserService).getUserByUsername("Ollie");
        verify(this.productUserService).comparePassword(this.user, this.userRequest.getPassword());
        assertEquals(expectedResponse, actualResponse.getPayload());
    }
    
    @Test
    @DisplayName("Should not login users with invalid requests")
    public void shouldNotLoginUsersWithInvalidRequests() {
        when(this.productUserService.getUserByUsername("Ollie")).thenReturn(null);
        AuthResponse expectedResponse = new AuthResponse(false);
        PayloadStatusResponse<AuthResponse> actualResponse = this.authService.login(this.userRequest);
        verify(this.productUserService).getUserByUsername("Ollie");
        assertEquals(expectedResponse, actualResponse.getPayload());
    }
    
    @Test
    @DisplayName("Should not login users with invalid passwords")
    public void shouldNotLoginUsersWithInvalidPasswords() {
        when(this.productUserService.getUserByUsername("Ollie")).thenReturn(this.user);
        AuthResponse expectedResponse = new AuthResponse(false);
        when(this.productUserService.comparePassword(this.user, this.userRequest.getPassword())).thenReturn(false);
        PayloadStatusResponse<AuthResponse> actualResponse = this.authService.login(this.userRequest);
        verify(this.productUserService).getUserByUsername("Ollie");
        verify(this.productUserService).comparePassword(this.user, this.userRequest.getPassword());
        assertEquals(expectedResponse, actualResponse.getPayload());
    }
}
