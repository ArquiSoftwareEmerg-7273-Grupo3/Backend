package com.drawnet.artcollab.iam.interfaces.rest;

import com.drawnet.artcollab.iam.domain.model.aggregates.User;
import com.drawnet.artcollab.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.drawnet.artcollab.profiles.domain.model.aggregates.Escritor;
import com.drawnet.artcollab.profiles.domain.model.aggregates.Ilustrador;
import com.drawnet.artcollab.profiles.infrastructure.persistence.jpa.repositories.EscritorRepository;
import com.drawnet.artcollab.profiles.infrastructure.persistence.jpa.repositories.IlustradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/subscriptions")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "http://localhost:8080"})
@RequiredArgsConstructor
public class SubscriptionController {
    
    private final UserRepository userRepository;
    private final IlustradorRepository ilustradorRepository;
    private final EscritorRepository escritorRepository;
    
    /**
     * Activate subscription for a user
     * POST /api/v1/subscriptions/activate
     */
    @PostMapping("/activate")
    @Transactional
    public ResponseEntity<?> activateSubscription(@RequestBody Map<String, String> request) {
        try {
            String userIdStr = request.get("userId");
            String userType = request.get("userType");
            String paymentId = request.get("paymentId");
            
            if (userIdStr == null || userType == null) {
                return ResponseEntity.badRequest().body(createErrorResponse(
                    "INVALID_REQUEST",
                    "userId and userType are required"
                ));
            }
            
            Long userId = Long.parseLong(userIdStr);
            
            // Validate user exists
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(createErrorResponse(
                    "USER_NOT_FOUND",
                    "User with ID " + userId + " not found"
                ));
            }
            
            User user = userOpt.get();
            boolean subscriptionActivated = false;
            
            // Determine user type and activate subscription
            if ("ILUSTRADOR".equalsIgnoreCase(userType)) {
                Optional<Ilustrador> ilustradorOpt = ilustradorRepository.findByUserId(userId);
                if (ilustradorOpt.isPresent()) {
                    Ilustrador ilustrador = ilustradorOpt.get();
                    ilustrador.activarSubscripcion();
                    ilustradorRepository.save(ilustrador);
                    subscriptionActivated = true;
                } else {
                    return ResponseEntity.status(404).body(createErrorResponse(
                        "PROFILE_NOT_FOUND",
                        "Ilustrador profile not found for user " + userId
                    ));
                }
            } else if ("ESCRITOR".equalsIgnoreCase(userType)) {
                Optional<Escritor> escritorOpt = escritorRepository.findByUserId(userId);
                if (escritorOpt.isPresent()) {
                    Escritor escritor = escritorOpt.get();
                    escritor.activarSubscripcion();
                    escritorRepository.save(escritor);
                    subscriptionActivated = true;
                } else {
                    return ResponseEntity.status(404).body(createErrorResponse(
                        "PROFILE_NOT_FOUND",
                        "Escritor profile not found for user " + userId
                    ));
                }
            } else {
                return ResponseEntity.badRequest().body(createErrorResponse(
                    "INVALID_USER_TYPE",
                    "userType must be ILUSTRADOR or ESCRITOR"
                ));
            }
            
            if (subscriptionActivated) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Subscription activated successfully");
                response.put("userId", userId);
                response.put("userType", userType);
                response.put("subscriptionActive", true);
                if (paymentId != null) {
                    response.put("paymentId", paymentId);
                }
                
                return ResponseEntity.ok(response);
            }
            
            return ResponseEntity.status(500).body(createErrorResponse(
                "ACTIVATION_FAILED",
                "Failed to activate subscription"
            ));
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(
                "INVALID_USER_ID",
                "userId must be a valid number"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(createErrorResponse(
                "INTERNAL_ERROR",
                "Error activating subscription: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Deactivate subscription for a user
     * POST /api/v1/subscriptions/deactivate
     */
    @PostMapping("/deactivate")
    @Transactional
    public ResponseEntity<?> deactivateSubscription(@RequestBody Map<String, String> request) {
        try {
            String userIdStr = request.get("userId");
            String userType = request.get("userType");
            
            if (userIdStr == null || userType == null) {
                return ResponseEntity.badRequest().body(createErrorResponse(
                    "INVALID_REQUEST",
                    "userId and userType are required"
                ));
            }
            
            Long userId = Long.parseLong(userIdStr);
            
            // Validate user exists
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(createErrorResponse(
                    "USER_NOT_FOUND",
                    "User with ID " + userId + " not found"
                ));
            }
            
            boolean subscriptionDeactivated = false;
            
            // Determine user type and deactivate subscription
            if ("ILUSTRADOR".equalsIgnoreCase(userType)) {
                Optional<Ilustrador> ilustradorOpt = ilustradorRepository.findByUserId(userId);
                if (ilustradorOpt.isPresent()) {
                    Ilustrador ilustrador = ilustradorOpt.get();
                    ilustrador.desactivarSubscripcion();
                    ilustradorRepository.save(ilustrador);
                    subscriptionDeactivated = true;
                }
            } else if ("ESCRITOR".equalsIgnoreCase(userType)) {
                Optional<Escritor> escritorOpt = escritorRepository.findByUserId(userId);
                if (escritorOpt.isPresent()) {
                    Escritor escritor = escritorOpt.get();
                    escritor.desactivarSubscripcion();
                    escritorRepository.save(escritor);
                    subscriptionDeactivated = true;
                }
            }
            
            if (subscriptionDeactivated) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Subscription deactivated successfully");
                response.put("userId", userId);
                response.put("userType", userType);
                response.put("subscriptionActive", false);
                
                return ResponseEntity.ok(response);
            }
            
            return ResponseEntity.status(500).body(createErrorResponse(
                "DEACTIVATION_FAILED",
                "Failed to deactivate subscription"
            ));
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(
                "INVALID_USER_ID",
                "userId must be a valid number"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(createErrorResponse(
                "INTERNAL_ERROR",
                "Error deactivating subscription: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Get subscription status for a user
     * GET /api/v1/subscriptions/status/{userId}
     */
    @GetMapping("/status/{userId}")
    public ResponseEntity<?> getSubscriptionStatus(@PathVariable Long userId) {
        try {
            // Validate user exists
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(createErrorResponse(
                    "USER_NOT_FOUND",
                    "User with ID " + userId + " not found"
                ));
            }
            
            User user = userOpt.get();
            boolean hasSubscription = false;
            String userType = null;
            
            // Check Ilustrador
            Optional<Ilustrador> ilustradorOpt = ilustradorRepository.findByUserId(userId);
            if (ilustradorOpt.isPresent()) {
                hasSubscription = ilustradorOpt.get().getSubscripcion();
                userType = "ILUSTRADOR";
            } else {
                // Check Escritor
                Optional<Escritor> escritorOpt = escritorRepository.findByUserId(userId);
                if (escritorOpt.isPresent()) {
                    hasSubscription = escritorOpt.get().getSubscripcion();
                    userType = "ESCRITOR";
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("hasActiveSubscription", hasSubscription);
            response.put("subscriptionActive", hasSubscription);
            response.put("userType", userType);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(createErrorResponse(
                "INTERNAL_ERROR",
                "Error getting subscription status: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Helper method to create error responses
     */
    private Map<String, Object> createErrorResponse(String errorCode, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("errorCode", errorCode);
        error.put("message", message);
        return error;
    }
}
