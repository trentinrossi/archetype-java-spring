package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByUserId(String userId);
    
    boolean existsByUserId(String userId);
    
    Page<User> findByUserType(String userType, Pageable pageable);
    
    Page<User> findByUserStatus(String userStatus, Pageable pageable);
    
    Page<User> findByUserTypeAndUserStatus(String userType, String userStatus, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userStatus = 'A' AND u.accountLocked = false AND (u.accountExpiryDate IS NULL OR u.accountExpiryDate > :currentTime)")
    Page<User> findActiveUsers(@Param("currentTime") LocalDateTime currentTime, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userStatus = 'A' AND u.accountLocked = false AND (u.accountExpiryDate IS NULL OR u.accountExpiryDate > :currentTime)")
    List<User> findAllActiveUsers(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT u FROM User u WHERE u.accountLocked = true")
    Page<User> findLockedAccounts(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.accountLocked = true")
    List<User> findAllLockedAccounts();
    
    @Query("SELECT u FROM User u WHERE u.userStatus = :status")
    List<User> findByUserStatus(@Param("status") String status);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType")
    List<User> findByUserType(@Param("userType") String userType);
    
    @Query("SELECT u FROM User u WHERE u.sessionToken = :sessionToken AND u.sessionExpiry > :currentTime")
    Optional<User> findByValidSessionToken(@Param("sessionToken") String sessionToken, @Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT u FROM User u WHERE u.sessionToken IS NOT NULL AND u.sessionExpiry > :currentTime")
    List<User> findUsersWithActiveSessions(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT u FROM User u WHERE u.sessionToken IS NOT NULL AND u.sessionExpiry <= :currentTime")
    List<User> findUsersWithExpiredSessions(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT u FROM User u WHERE u.lastLoginAt >= :since")
    List<User> findRecentlyLoggedInUsers(@Param("since") LocalDateTime since);
    
    @Query("SELECT u FROM User u WHERE u.lastLoginAt >= :since")
    Page<User> findRecentlyLoggedInUsers(@Param("since") LocalDateTime since, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.lastLoginAt < :before OR u.lastLoginAt IS NULL")
    List<User> findInactiveUsers(@Param("before") LocalDateTime before);
    
    @Query("SELECT u FROM User u WHERE u.lastLoginAt < :before OR u.lastLoginAt IS NULL")
    Page<User> findInactiveUsers(@Param("before") LocalDateTime before, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.passwordExpired = true OR u.passwordExpiryDate <= :currentTime")
    List<User> findUsersWithExpiredPasswords(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT u FROM User u WHERE u.passwordExpired = true OR u.passwordExpiryDate <= :currentTime")
    Page<User> findUsersWithExpiredPasswords(@Param("currentTime") LocalDateTime currentTime, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.passwordExpiryDate BETWEEN :currentTime AND :warningTime")
    List<User> findUsersWithPasswordsNearExpiry(@Param("currentTime") LocalDateTime currentTime, @Param("warningTime") LocalDateTime warningTime);
    
    @Query("SELECT u FROM User u WHERE u.forcePasswordChange = true")
    List<User> findUsersRequiringPasswordChange();
    
    @Query("SELECT u FROM User u WHERE u.forcePasswordChange = true")
    Page<User> findUsersRequiringPasswordChange(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.failedLoginAttempts >= :threshold")
    List<User> findUsersWithFailedAttempts(@Param("threshold") Integer threshold);
    
    @Query("SELECT u FROM User u WHERE u.failedLoginAttempts >= :threshold")
    Page<User> findUsersWithFailedAttempts(@Param("threshold") Integer threshold, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.accountExpiryDate <= :expiryDate AND u.accountExpiryDate IS NOT NULL")
    List<User> findUsersWithExpiringAccounts(@Param("expiryDate") LocalDateTime expiryDate);
    
    @Query("SELECT u FROM User u WHERE u.accountExpiryDate <= :currentTime AND u.accountExpiryDate IS NOT NULL")
    List<User> findUsersWithExpiredAccounts(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT u FROM User u WHERE u.twoFactorEnabled = true")
    List<User> findUsersWithTwoFactorEnabled();
    
    @Query("SELECT u FROM User u WHERE u.twoFactorEnabled = true")
    Page<User> findUsersWithTwoFactorEnabled(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.twoFactorEnabled = false")
    List<User> findUsersWithoutTwoFactor();
    
    @Query("SELECT u FROM User u WHERE u.createdAt >= :since")
    List<User> findRecentlyCreatedUsers(@Param("since") LocalDateTime since);
    
    @Query("SELECT u FROM User u WHERE u.createdAt >= :since")
    Page<User> findRecentlyCreatedUsers(@Param("since") LocalDateTime since, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.updatedAt >= :since")
    List<User> findRecentlyUpdatedUsers(@Param("since") LocalDateTime since);
    
    @Query("SELECT u FROM User u WHERE u.loginCount >= :minimumLogins")
    List<User> findFrequentUsers(@Param("minimumLogins") Long minimumLogins);
    
    @Query("SELECT u FROM User u WHERE u.loginCount >= :minimumLogins")
    Page<User> findFrequentUsers(@Param("minimumLogins") Long minimumLogins, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.lastIpAddress = :ipAddress")
    List<User> findUsersByLastIpAddress(@Param("ipAddress") String ipAddress);
    
    @Query("SELECT u FROM User u WHERE u.createdBy = :createdBy")
    List<User> findUsersByCreator(@Param("createdBy") String createdBy);
    
    @Query("SELECT u FROM User u WHERE u.createdBy = :createdBy")
    Page<User> findUsersByCreator(@Param("createdBy") String createdBy, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.updatedBy = :updatedBy")
    List<User> findUsersByUpdater(@Param("updatedBy") String updatedBy);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> findByNameContaining(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT u FROM User u WHERE u.securityQuestion IS NOT NULL AND u.securityAnswerHash IS NOT NULL")
    List<User> findUsersWithSecurityQuestions();
    
    @Query("SELECT u FROM User u WHERE u.securityQuestion IS NULL OR u.securityAnswerHash IS NULL")
    List<User> findUsersWithoutSecurityQuestions();
    
    @Query("SELECT u FROM User u WHERE u.failedLoginAttempts >= 3 OR u.lastLoginAt < :riskThreshold OR u.passwordExpiryDate <= :passwordRisk OR (u.accountExpiryDate IS NOT NULL AND u.accountExpiryDate <= :accountRisk)")
    List<User> findHighRiskAccounts(@Param("riskThreshold") LocalDateTime riskThreshold, @Param("passwordRisk") LocalDateTime passwordRisk, @Param("accountRisk") LocalDateTime accountRisk);
    
    @Query("SELECT u FROM User u WHERE u.failedLoginAttempts >= 3 OR u.lastLoginAt < :riskThreshold OR u.passwordExpiryDate <= :passwordRisk OR (u.accountExpiryDate IS NOT NULL AND u.accountExpiryDate <= :accountRisk)")
    Page<User> findHighRiskAccounts(@Param("riskThreshold") LocalDateTime riskThreshold, @Param("passwordRisk") LocalDateTime passwordRisk, @Param("accountRisk") LocalDateTime accountRisk, Pageable pageable);
    
    long countByUserStatus(String userStatus);
    
    long countByUserType(String userType);
    
    long countByUserTypeAndUserStatus(String userType, String userStatus);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.accountLocked = true")
    long countLockedAccounts();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userStatus = 'A' AND u.accountLocked = false")
    long countActiveUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.passwordExpired = true OR u.passwordExpiryDate <= :currentTime")
    long countUsersWithExpiredPasswords(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.sessionToken IS NOT NULL AND u.sessionExpiry > :currentTime")
    long countUsersWithActiveSessions(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.twoFactorEnabled = true")
    long countUsersWithTwoFactor();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :since")
    long countRecentlyCreatedUsers(@Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.lastLoginAt >= :since")
    long countRecentlyLoggedInUsers(@Param("since") LocalDateTime since);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.sessionToken = NULL, u.sessionExpiry = NULL WHERE u.sessionExpiry <= :currentTime")
    int invalidateExpiredSessions(@Param("currentTime") LocalDateTime currentTime);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.sessionToken = NULL, u.sessionExpiry = NULL WHERE u.userId = :userId")
    int invalidateUserSession(@Param("userId") String userId);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.accountLocked = true WHERE u.failedLoginAttempts >= :threshold")
    int lockAccountsWithFailedAttempts(@Param("threshold") Integer threshold);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.passwordExpired = true WHERE u.passwordExpiryDate <= :currentTime")
    int markExpiredPasswords(@Param("currentTime") LocalDateTime currentTime);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.userStatus = 'I' WHERE u.accountExpiryDate <= :currentTime AND u.accountExpiryDate IS NOT NULL")
    int deactivateExpiredAccounts(@Param("currentTime") LocalDateTime currentTime);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.failedLoginAttempts = 0, u.accountLocked = false WHERE u.userId = :userId")
    int resetFailedAttempts(@Param("userId") String userId);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLoginAt = :loginTime, u.lastIpAddress = :ipAddress, u.failedLoginAttempts = 0, u.loginCount = u.loginCount + 1 WHERE u.userId = :userId")
    int recordSuccessfulLogin(@Param("userId") String userId, @Param("loginTime") LocalDateTime loginTime, @Param("ipAddress") String ipAddress);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.failedLoginAttempts = u.failedLoginAttempts + 1 WHERE u.userId = :userId")
    int incrementFailedAttempts(@Param("userId") String userId);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.accountLocked = :locked WHERE u.userId = :userId")
    int updateAccountLockStatus(@Param("userId") String userId, @Param("locked") Boolean locked);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.userStatus = :status WHERE u.userId = :userId")
    int updateUserStatus(@Param("userId") String userId, @Param("status") String status);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.forcePasswordChange = true, u.passwordExpired = true WHERE u.userId = :userId")
    int forcePasswordChange(@Param("userId") String userId);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.twoFactorEnabled = :enabled WHERE u.userId = :userId")
    int updateTwoFactorStatus(@Param("userId") String userId, @Param("enabled") Boolean enabled);
    
    @Query("SELECT u FROM User u WHERE u.userType = 'A'")
    List<User> findAdministrators();
    
    @Query("SELECT u FROM User u WHERE u.userType = 'A'")
    Page<User> findAdministrators(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userType = 'R'")
    List<User> findRegularUsers();
    
    @Query("SELECT u FROM User u WHERE u.userType = 'R'")
    Page<User> findRegularUsers(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userStatus = 'A' AND u.accountLocked = false AND (u.accountExpiryDate IS NULL OR u.accountExpiryDate > :currentTime) AND (u.passwordExpired = false OR u.passwordExpired IS NULL)")
    List<User> findUsersEligibleForLogin(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT u FROM User u WHERE u.userStatus = 'A' AND u.accountLocked = false AND (u.accountExpiryDate IS NULL OR u.accountExpiryDate > :currentTime) AND (u.passwordExpired = false OR u.passwordExpired IS NULL)")
    Page<User> findUsersEligibleForLogin(@Param("currentTime") LocalDateTime currentTime, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userStatus IN ('I', 'S', 'D') OR u.accountLocked = true OR (u.accountExpiryDate IS NOT NULL AND u.accountExpiryDate <= :currentTime)")
    List<User> findIneligibleUsers(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT u FROM User u WHERE u.userStatus IN ('I', 'S', 'D') OR u.accountLocked = true OR (u.accountExpiryDate IS NOT NULL AND u.accountExpiryDate <= :currentTime)")
    Page<User> findIneligibleUsers(@Param("currentTime") LocalDateTime currentTime, Pageable pageable);
    
    @Query("SELECT DISTINCT u.lastIpAddress FROM User u WHERE u.lastIpAddress IS NOT NULL")
    List<String> findDistinctIpAddresses();
    
    @Query("SELECT u.userType, COUNT(u) FROM User u GROUP BY u.userType")
    List<Object[]> getUserTypeStatistics();
    
    @Query("SELECT u.userStatus, COUNT(u) FROM User u GROUP BY u.userStatus")
    List<Object[]> getUserStatusStatistics();
    
    @Query("SELECT DATE(u.createdAt), COUNT(u) FROM User u WHERE u.createdAt >= :since GROUP BY DATE(u.createdAt) ORDER BY DATE(u.createdAt)")
    List<Object[]> getUserCreationStatistics(@Param("since") LocalDateTime since);
    
    @Query("SELECT DATE(u.lastLoginAt), COUNT(u) FROM User u WHERE u.lastLoginAt >= :since GROUP BY DATE(u.lastLoginAt) ORDER BY DATE(u.lastLoginAt)")
    List<Object[]> getLoginStatistics(@Param("since") LocalDateTime since);
    
    @Query("SELECT u FROM User u WHERE u.userId IN :userIds")
    List<User> findByUserIdIn(@Param("userIds") List<String> userIds);
    
    @Query("SELECT u FROM User u WHERE u.userId IN :userIds")
    Page<User> findByUserIdIn(@Param("userIds") List<String> userIds, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType AND u.userStatus = :status AND u.accountLocked = false")
    List<User> findByUserTypeAndStatusNotLocked(@Param("userType") String userType, @Param("status") String status);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType AND u.userStatus = :status AND u.accountLocked = false")
    Page<User> findByUserTypeAndStatusNotLocked(@Param("userType") String userType, @Param("status") String status, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.lastPasswordChange < :threshold")
    List<User> findUsersWithOldPasswords(@Param("threshold") LocalDateTime threshold);
    
    @Query("SELECT u FROM User u WHERE u.lastPasswordChange < :threshold")
    Page<User> findUsersWithOldPasswords(@Param("threshold") LocalDateTime threshold, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.loginCount = 0 OR u.loginCount IS NULL")
    List<User> findUsersWhoNeverLoggedIn();
    
    @Query("SELECT u FROM User u WHERE u.loginCount = 0 OR u.loginCount IS NULL")
    Page<User> findUsersWhoNeverLoggedIn(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.sessionToken IS NOT NULL")
    List<User> findUsersWithSessions();
    
    @Query("SELECT u FROM User u WHERE u.sessionToken IS NOT NULL")
    Page<User> findUsersWithSessions(Pageable pageable);
    
    @Query("SELECT u FROM User u ORDER BY u.loginCount DESC")
    List<User> findUsersByLoginCountDesc();
    
    @Query("SELECT u FROM User u ORDER BY u.loginCount DESC")
    Page<User> findUsersByLoginCountDesc(Pageable pageable);
    
    @Query("SELECT u FROM User u ORDER BY u.lastLoginAt DESC NULLS LAST")
    List<User> findUsersByLastLoginDesc();
    
    @Query("SELECT u FROM User u ORDER BY u.lastLoginAt DESC NULLS LAST")
    Page<User> findUsersByLastLoginDesc(Pageable pageable);
    
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    List<User> findUsersByCreationDateDesc();
    
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    Page<User> findUsersByCreationDateDesc(Pageable pageable);
}