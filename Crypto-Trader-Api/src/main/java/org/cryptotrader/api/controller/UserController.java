package org.cryptotrader.api.controller;
//=================================-Imports-==================================
import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.api.library.communication.response.OperationSuccessfulResponse;
import org.cryptotrader.api.library.communication.response.SubscriptionTierResponse;
import org.cryptotrader.api.library.entity.user.ProductUser;
import org.cryptotrader.api.library.services.AuthContextService;
import org.cryptotrader.api.library.services.PortfolioService;
import org.cryptotrader.api.library.services.ProductUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.cryptotrader.api.library.entity.user.User;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    //============================-Variables-=================================
    private final ProductUserService productUserService;
    private final PortfolioService portfolioService;
    private final AuthContextService authContextService;
    //===========================-Constructors-===============================
    @Autowired
    public UserController(ProductUserService productUserService,
                          PortfolioService portfolioService,
                          AuthContextService authContextService) {
        this.productUserService = productUserService;
        this.portfolioService = portfolioService;
        this.authContextService = authContextService;
    }
    //=============================-Methods-==================================

    @GetMapping("/me/tier")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SubscriptionTierResponse> getMySubscriptionTier(@AuthenticationPrincipal ProductUser user) {
        return ResponseEntity.ok(new SubscriptionTierResponse(user.getSubscriptionTier()));
    }

    /**
     * Permanently deletes the authenticated user's account and all associated data.
     *
     * @param user the currently authenticated user, resolved from the JWT principal
     * @return {@link ResponseEntity} with {@link OperationSuccessfulResponse};
     *         HTTP status {@code 200 OK} on success,
     *         {@code 401 UNAUTHORIZED} if no authenticated user is found
     * @see ProductUserService#deleteUserAndAllData(Long)
     */
    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OperationSuccessfulResponse> deleteMyAccount(@AuthenticationPrincipal ProductUser user) {
        if (user == null) {
            return new ResponseEntity<>(new OperationSuccessfulResponse(false), HttpStatus.UNAUTHORIZED);
        }
        boolean deleted = this.productUserService.deleteUserAndAllData(user.getId());
        if (!deleted) {
            return new ResponseEntity<>(new OperationSuccessfulResponse(false), HttpStatus.NOT_FOUND);
        }
        this.authContextService.logout();
        return ResponseEntity.ok(new OperationSuccessfulResponse(true));
    }

}
