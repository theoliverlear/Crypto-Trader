package org.cryptotrader.api.library.services;

import org.cryptotrader.api.library.communication.request.LoginRequest;
import org.cryptotrader.api.library.communication.request.SignupRequest;
import org.cryptotrader.api.library.events.publisher.UserEventsPublisher;
import org.cryptotrader.api.library.communication.request.UserRequest;
import org.cryptotrader.api.library.communication.response.AuthResponse;
import org.cryptotrader.api.library.entity.user.SafePassword;
import org.cryptotrader.api.library.entity.user.ProductUser;
import org.cryptotrader.api.library.model.http.PayloadStatusResponse;
import org.cryptotrader.test.CryptoTraderTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

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
    private UserEventsPublisher userEventsPublisher;
    
    private LoginRequest loginRequest;
    
    private SignupRequest signupRequest;
    
    private UserRequest userRequest;
    
    private ProductUser user;
    
    private String testUsername;
    private String testEmail;
    private String testPassword;
    
    @BeforeEach
    void setup() {
        this.testUsername = "Ollie";
        this.testEmail = "ollie@ollie.com";
        this.testPassword = "password";
        SafePassword clientSideHashedPassword = new SafePassword(this.testPassword);
        String encodedPassword = clientSideHashedPassword.getEncodedPassword();
        this.userRequest = new UserRequest(this.testUsername, this.testEmail, encodedPassword);
        this.loginRequest = new LoginRequest(this.testEmail, encodedPassword);
        this.signupRequest = new SignupRequest(this.testEmail, this.testUsername, encodedPassword);
        this.user = new ProductUser(this.testUsername, new SafePassword(this.testPassword));
    }
    
    @AfterAll
    static void tearDown() {

    }

    @Test
    @DisplayName("Constructor should instantiate object")
    public void constructor_InstantiatesAuthService_WhenValidArgumentsProvided() {
        ProductUserService productUserService = Mockito.mock(ProductUserService.class);
        PortfolioService portfolioService = Mockito.mock(PortfolioService.class);
        UserEventsPublisher userEventsPublisher = Mockito.mock(UserEventsPublisher.class);
        AuthService authService = new AuthService(productUserService, portfolioService, userEventsPublisher);
        assertNotNull(authService);
    }
    
    @Test
    @DisplayName("Should sign up users with valid requests")
    public void signup_SignUpUsers_WithValidRequests() {
        when(this.productUserService.userExistsByUsername(this.testUsername)).thenReturn(false);
        AuthResponse expectedResponse = new AuthResponse(true);
        PayloadStatusResponse<AuthResponse> actualResponse = this.authService.signup(this.signupRequest);
        verify(this.productUserService).userExistsByUsername(this.testUsername);
        AuthResponse actualAuthResponse = actualResponse.getPayload();
        assertEquals(expectedResponse, actualAuthResponse);
    }
    
    @Test
    @DisplayName("Should not sign up existing users")
    public void signup_NotSignUp_WhenUserExists() {
        when(this.productUserService.userExistsByUsername(this.testUsername)).thenReturn(true);
        AuthResponse expectedResponse = new AuthResponse(false);
        PayloadStatusResponse<AuthResponse> actualResponse = this.authService.signup(this.signupRequest);
        AuthResponse actualAuthResponse = actualResponse.getPayload();
        assertEquals(expectedResponse, actualAuthResponse);
    }
    
    @Test
    @DisplayName("Should login users with valid requests")
    public void login_LoginUsers_WithValidRequests() {
        when(this.productUserService.getUserByEmail(this.testEmail)).thenReturn(this.user);
        AuthResponse expectedResponse = new AuthResponse(true);
        when(this.productUserService.comparePassword(this.user, this.userRequest.getPassword())).thenReturn(true);
        PayloadStatusResponse<AuthResponse> actualResponse = this.authService.login(this.loginRequest);
        verify(this.productUserService).getUserByEmail(this.testEmail);
        verify(this.productUserService).comparePassword(this.user, this.userRequest.getPassword());
        assertEquals(expectedResponse, actualResponse.getPayload());
    }
    
    @Test
    @DisplayName("Should not login users with invalid requests")
    public void login_NotLoginUsers_WithInvalidRequests() {
        when(this.productUserService.getUserByEmail(this.testEmail)).thenReturn(null);
        AuthResponse expectedResponse = new AuthResponse(false);
        PayloadStatusResponse<AuthResponse> actualResponse = this.authService.login(this.loginRequest);
        verify(this.productUserService).getUserByEmail(this.testEmail);
        assertEquals(expectedResponse, actualResponse.getPayload());
    }
    
    @Test
    @DisplayName("Should not login users with mismatched passwords")
    public void login_NotLoginUsers_WithMismatchedPasswords() {
        when(this.productUserService.getUserByEmail(this.testEmail)).thenReturn(this.user);
        AuthResponse expectedResponse = new AuthResponse(false);
        when(this.productUserService.comparePassword(this.user, this.userRequest.getPassword())).thenReturn(false);
        PayloadStatusResponse<AuthResponse> actualResponse = this.authService.login(this.loginRequest);
        verify(this.productUserService).getUserByEmail(this.testEmail);
        verify(this.productUserService).comparePassword(this.user, this.userRequest.getPassword());
        assertEquals(expectedResponse, actualResponse.getPayload());
    }
}
