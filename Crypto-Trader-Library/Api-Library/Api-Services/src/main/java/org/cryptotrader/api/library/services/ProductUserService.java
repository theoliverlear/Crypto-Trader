package org.cryptotrader.api.library.services;
//=================================-Imports-==================================

import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset;
import org.cryptotrader.api.library.entity.portfolio.PortfolioAssetHistory;
import org.cryptotrader.api.library.entity.portfolio.PortfolioHistory;
import org.cryptotrader.api.library.entity.trade.TradeEvent;
import org.cryptotrader.api.library.entity.user.ProductUser;
import org.cryptotrader.api.library.entity.user.ProfilePicture;
import org.cryptotrader.api.library.entity.user.User;
import org.cryptotrader.api.library.entity.portfolio.Portfolio;
import org.cryptotrader.api.library.repository.PortfolioAssetHistoryRepository;
import org.cryptotrader.api.library.repository.PortfolioAssetRepository;
import org.cryptotrader.api.library.repository.PortfolioHistoryRepository;
import org.cryptotrader.api.library.repository.ProductUserRepository;
import org.cryptotrader.api.library.repository.ProfilePictureRepository;
import org.cryptotrader.api.library.repository.TradeEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Simple user service for reading and writing ProductUser data.
 *
 * This service is intentionally thin and delegates to ProductUserRepository. It is used by
 * authentication components (e.g. JWT filters and auth controllers) to look up users by id/email/username
 * and to persist new registrations.
 */
@Service
@Slf4j
public class ProductUserService {
    //============================-Variables-=================================
    private final ProductUserRepository productUserRepository;
    private final TradeEventRepository tradeEventRepository;
    private final PortfolioAssetHistoryRepository portfolioAssetHistoryRepository;
    private final PortfolioHistoryRepository portfolioHistoryRepository;
    private final PortfolioAssetRepository portfolioAssetRepository;
    private final ProfilePictureRepository profilePictureRepository;
    //===========================-Constructors-===============================
    @Autowired
    public ProductUserService(ProductUserRepository productUserRepository,
                              TradeEventRepository tradeEventRepository,
                              PortfolioAssetHistoryRepository portfolioAssetHistoryRepository,
                              PortfolioHistoryRepository portfolioHistoryRepository,
                              PortfolioAssetRepository portfolioAssetRepository,
                              ProfilePictureRepository profilePictureRepository) {
        this.productUserRepository = productUserRepository;
        this.tradeEventRepository = tradeEventRepository;
        this.portfolioAssetHistoryRepository = portfolioAssetHistoryRepository;
        this.portfolioHistoryRepository = portfolioHistoryRepository;
        this.portfolioAssetRepository = portfolioAssetRepository;
        this.profilePictureRepository = profilePictureRepository;
    }
    //============================-Methods-===================================

    //----------------------User-Exists-By-Username---------------------------
    /**
     * Check whether a user exists with the given username (case sensitivity depends on repository).
     * @param username the username to check
     * @return true if a user record exists
     */
    public boolean userExistsByUsername(String username) {
        return this.productUserRepository.existsByUsername(username);
    }
    
    /**
     * Find a ProductUser by email.
     * @param email email address
     * @return matching ProductUser or null if none found
     */
    public ProductUser getUserByEmail(String email) {
        return this.productUserRepository.getUserByEmail(email);
    }
    
    /**
     * Check whether a user exists with the given email.
     * @param email email address to test
     * @return true if a user with that email exists
     */
    public boolean userExistsByEmail(String email) {
        return this.productUserRepository.existsByEmail(email);
    }
    //------------------------Get-User-By-Username----------------------------
    /**
     * Fetch a ProductUser by username.
     * @param username username value
     * @return ProductUser or null if not found
     */
    public ProductUser getUserByUsername(String username) {
        return this.productUserRepository.getUserByUsername(username);
    }
    //--------------------------Compare-Password------------------------------
    /**
     * Safely compare a plaintext password against the user's stored password.
     * @param user the user whose password hash to compare
     * @param password the plaintext password to verify
     * @return true when the password matches
     */
    public boolean comparePassword(User user, String password) {
        boolean passwordsMatch = user.getSafePassword().compareUnencodedPassword(password);
        return passwordsMatch;
    }
    //-----------------------------Save-User----------------------------------
    /**
     * Persist a ProductUser.
     * @param user the entity to save
     */
    public void saveUser(ProductUser user) {
        log.info("Saving user: {}", user.getUsername());
        this.productUserRepository.save(user);
    }
    //---------------------------Get-User-By-Id-------------------------------
    /**
     * Fetch a ProductUser by database id.
     * @param id numeric id
     * @return ProductUser or null if none
     */
    public ProductUser getUserById(Long id) {
        return this.productUserRepository.getUserById(id);
    }
    //--------------------Delete-User-And-All-Data----------------------------
    /**
     * Permanently deletes a user and all data associated with their account.
     * Deletion order respects foreign-key constraints:
     * <ol>
     *   <li>Trade events (reference portfolio, portfolio asset, and asset history)</li>
     *   <li>Portfolio asset histories (reference portfolio asset)</li>
     *   <li>Portfolio histories (reference portfolio)</li>
     *   <li>Portfolio assets (reference portfolio)</li>
     *   <li>Profile picture (bidirectional FK between product_users and profile_pictures)</li>
     *   <li>User (cascade deletes the portfolio)</li>
     * </ol>
     *
     * @param userId the numeric ID of the user to delete
     */
    @Transactional
    public void deleteUserAndAllData(Long userId) {
        ProductUser user = this.productUserRepository.getUserById(userId);
        if (user == null) {
            log.warn("deleteUserAndAllData: user with id {} not found", userId);
            return;
        }
        Portfolio portfolio = user.getPortfolio();
        if (portfolio != null && portfolio.getId() != null) {
            Long portfolioId = portfolio.getId();
            List<TradeEvent> tradeEvents = this.tradeEventRepository.findAllByPortfolioId(portfolioId);
            this.tradeEventRepository.deleteAll(tradeEvents);
            List<PortfolioAssetHistory> assetHistories = this.portfolioAssetHistoryRepository.findAllByPortfolioId(portfolioId);
            this.portfolioAssetHistoryRepository.deleteAll(assetHistories);
            List<PortfolioHistory> portfolioHistories = this.portfolioHistoryRepository.findAllByPortfolioId(portfolioId);
            this.portfolioHistoryRepository.deleteAll(portfolioHistories);
            List<PortfolioAsset> portfolioAssets = this.portfolioAssetRepository.findAllByPortfolioId(portfolioId);
            this.portfolioAssetRepository.deleteAll(portfolioAssets);
        }
        ProfilePicture profilePicture = user.getProfilePicture();
        if (profilePicture != null) {
            user.setProfilePicture(null);
            this.productUserRepository.save(user);
            this.profilePictureRepository.deleteById(profilePicture.getId());
        }
        this.productUserRepository.delete(user);
        log.info("Deleted user and all associated data for userId: {}", userId);
    }
}
