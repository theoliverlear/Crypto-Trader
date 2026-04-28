package org.cryptotrader.api.controller;

import org.cryptotrader.api.library.communication.response.OperationSuccessfulResponse;
import org.cryptotrader.api.library.entity.user.ProductUser;
import org.cryptotrader.api.library.entity.user.SafePassword;
import org.cryptotrader.api.library.services.AuthContextService;
import org.cryptotrader.api.library.services.PortfolioService;
import org.cryptotrader.api.library.services.ProductUserService;
import org.cryptotrader.test.CryptoTraderTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@DisplayName("User Controller")
public class UserControllerTest extends CryptoTraderTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private ProductUserService productUserService;

    @Mock
    private PortfolioService portfolioService;

    @Mock
    private AuthContextService authContextService;

    private ProductUser testUser;

    @BeforeEach
    void setup() {
        this.testUser = new ProductUser("testuser", new SafePassword("password"));
    }

    @Nested
    @DisplayName("Delete My Account")
    class DeleteMyAccount {
        @Test
        @DisplayName("Should delete authenticated user account and return 200")
        public void deleteMyAccount_DeletesAccount_WhenAuthenticated() {
            doNothing().when(productUserService).deleteUserAndAllData(any());
            doNothing().when(authContextService).logout();
            ResponseEntity<OperationSuccessfulResponse> response = userController.deleteMyAccount(testUser);
            verify(productUserService).deleteUserAndAllData(any());
            verify(authContextService).logout();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("Should return 401 when principal is null")
        public void deleteMyAccount_ReturnsUnauthorized_WhenUserIsNull() {
            ResponseEntity<OperationSuccessfulResponse> response = userController.deleteMyAccount(null);
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertNotNull(response.getBody());
        }
    }
}
