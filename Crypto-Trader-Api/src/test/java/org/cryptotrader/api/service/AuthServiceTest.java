package org.cryptotrader.api.service;

import org.cryptotrader.comm.request.UserRequest;
import org.cryptotrader.comm.response.AuthResponse;
import org.cryptotrader.model.http.PayloadStatusResponse;
import org.cryptotrader.test.CryptoTraderTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Mock
    private UserRequest userRequest;
    
    @BeforeEach
    void setup() {
        this.userRequest = new UserRequest("Ollie", "ollie@ollie.com", "password");
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
}
