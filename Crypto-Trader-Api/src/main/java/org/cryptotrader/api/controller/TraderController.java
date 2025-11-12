package org.cryptotrader.api.controller;
//=================================-Imports-==================================
import org.cryptotrader.api.library.communication.response.TradeEventListResponse;
import org.cryptotrader.api.library.entity.user.ProductUser;
import org.cryptotrader.api.library.services.AuthContextService;
import org.cryptotrader.api.library.services.TraderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trader")
public class TraderController {
    private final TraderService traderService;
    private final AuthContextService authContextService;
    
    @Autowired
    public TraderController(TraderService traderService,
                            AuthContextService authContextService) {
        this.traderService = traderService;
        this.authContextService = authContextService;
    }
    
    @RequestMapping("/events/all")
    public ResponseEntity<TradeEventListResponse> getAllEvents() {
        boolean isAuthorized = this.authContextService.isAuthenticated();
        if (!isAuthorized) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ProductUser user = this.authContextService.getAuthenticatedProductUser();
        return new ResponseEntity<>(this.traderService.getTradeEvents(user), HttpStatus.OK);
    }
}
