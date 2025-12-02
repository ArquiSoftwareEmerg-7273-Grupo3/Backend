# Implementation Plan

- [ ] 1. Create database entities and repositories



  - Create PaymentPreference entity with all required fields
  - Create PaymentRecord entity for payment tracking
  - Create ActivationQueue entity for retry management
  - Create SubscriptionLog entity for audit trail
  - Create PaymentPreferenceRepository with query methods
  - Create PaymentRecordRepository with query methods
  - Create ActivationQueueRepository with query methods
  - Create SubscriptionLogRepository with query methods
  - _Requirements: 5.1, 5.2, 7.2, 6.4_

- [x] 1.1 Write property test for PaymentPreference entity


  - **Property 1: Payment preference creation includes all required fields**
  - **Validates: Requirements 5.1**

- [x] 1.2 Write property test for PaymentRecord entity


  - **Property 13: Payment completion creates payment record**
  - **Validates: Requirements 5.2**

- [ ] 2. Enhance MercadoPago Controller with preference storage
  - Update createPreference endpoint to store PaymentPreference in database
  - Add user metadata (userId, userEmail, userType) to MercadoPago preference
  - Return preference ID and checkout URL
  - _Requirements: 1.1, 1.2, 5.1_

- [ ] 2.1 Write property test for preference creation
  - **Property 1: Payment preference creation includes all required fields**
  - **Validates: Requirements 1.1, 5.1**

- [ ] 2.2 Write property test for redirect URL validation
  - **Property 2: Payment preference response contains valid redirect URL**
  - **Validates: Requirements 1.2**

- [ ] 2.3 Write property test for preference persistence
  - **Property 12: Payment preference creation persists to database**
  - **Validates: Requirements 5.1**

- [ ] 3. Implement webhook notification processing
  - Update webhook endpoint to parse payment notifications
  - Extract payment ID, status, and user metadata from webhook payload
  - Create PaymentRecord for completed payments
  - Trigger subscription activation for approved payments
  - _Requirements: 1.3, 1.4, 2.2, 2.3, 5.2_

- [ ] 3.1 Write property test for webhook payload parsing
  - **Property 5: Webhook payload parsing extracts payment details**
  - **Validates: Requirements 2.2**

- [ ] 3.2 Write property test for approved payment handling
  - **Property 3: Approved payment notifications trigger subscription activation**
  - **Validates: Requirements 1.4, 2.3, 2.4**

- [ ] 3.3 Write property test for payment record creation
  - **Property 13: Payment completion creates payment record**
  - **Validates: Requirements 5.2**

- [ ] 4. Create Subscription Activation Service
  - Implement SubscriptionActivationService with activation logic
  - Add method to call Auth Service subscription endpoint
  - Implement error handling and logging
  - Create UserSubscription record on successful activation
  - _Requirements: 1.4, 1.5, 2.4, 5.3_

- [ ] 4.1 Write property test for subscription activation
  - **Property 4: Subscription activation updates user profile**
  - **Validates: Requirements 1.5, 3.3, 3.4**

- [ ] 4.2 Write property test for UserSubscription record creation
  - **Property 14: Subscription activation creates UserSubscription record**
  - **Validates: Requirements 5.3**

- [ ] 4.3 Write property test for payment reference inclusion
  - **Property 16: UserSubscription includes payment reference**
  - **Validates: Requirements 5.5**

- [ ] 5. Implement retry queue mechanism
  - Add retry logic to SubscriptionActivationService
  - Create ActivationQueue record when Auth Service fails
  - Implement exponential backoff calculation (1min, 2min, 4min)
  - Create background job to process retry queue
  - Remove queue entry on successful retry
  - Send admin alert after 3 failed retries
  - _Requirements: 7.1, 7.2, 7.3, 7.4, 7.5_

- [ ] 5.1 Write property test for retry queue creation
  - **Property 22: Auth Service unavailability triggers retry queue**
  - **Validates: Requirements 7.2**

- [ ] 5.2 Write property test for exponential backoff
  - **Property 23: Failed activations retry with exponential backoff**
  - **Validates: Requirements 7.3**

- [ ] 5.3 Write property test for admin alerts
  - **Property 24: Exhausted retries trigger admin alerts**
  - **Validates: Requirements 7.4**

- [ ] 5.4 Write property test for queue cleanup
  - **Property 25: Successful retries remove queue entries**
  - **Validates: Requirements 7.5**

- [ ] 5.5 Write property test for error logging
  - **Property 21: Webhook processing failures are logged**
  - **Validates: Requirements 7.1**

- [x] 6. Create Auth Service Subscription Controller


  - Create SubscriptionController in auth-service
  - Implement POST /api/v1/subscriptions/activate endpoint
  - Implement POST /api/v1/subscriptions/deactivate endpoint
  - Implement GET /api/v1/subscriptions/status/{userId} endpoint
  - Validate user ID exists before processing
  - Determine user type (Ilustrador or Escritor)
  - Update corresponding profile's subscription field
  - Return updated user data in response
  - _Requirements: 3.1, 3.2, 3.3, 3.4, 3.5_

- [ ] 6.1 Write property test for user validation
  - **Property 6: Invalid user IDs are rejected**
  - **Validates: Requirements 3.1**

- [ ] 6.2 Write property test for user type determination
  - **Property 7: User type determination is correct**
  - **Validates: Requirements 3.2**

- [ ] 6.3 Write property test for profile update
  - **Property 4: Subscription activation updates user profile**
  - **Validates: Requirements 3.3, 3.4**

- [ ] 6.4 Write property test for response format
  - **Property 8: Successful activation returns updated user data**
  - **Validates: Requirements 3.5**

- [ ] 6.5 Write property test for status query
  - **Property 9: Subscription status query returns current state**
  - **Validates: Requirements 4.2**

- [ ] 7. Add API Gateway routes for subscription endpoints
  - Add route for /api/v1/subscriptions/** to auth-service
  - Add route for /api/mercadopago/webhook to monetization-service
  - Configure CORS for new endpoints
  - _Requirements: All_

- [ ] 8. Implement premium feature access control
  - Create subscription check interceptor/filter
  - Add @RequiresPremium annotation for premium endpoints
  - Return 403 for users without active subscription
  - Allow access for users with active subscription
  - _Requirements: 4.3, 4.4_

- [ ] 8.1 Write property test for premium access with subscription
  - **Property 10: Active subscription grants premium access**
  - **Validates: Requirements 4.3**

- [ ] 8.2 Write property test for premium access without subscription
  - **Property 11: Inactive subscription denies premium access**
  - **Validates: Requirements 4.4**

- [ ] 9. Implement subscription history and logging
  - Add method to query UserSubscription history by userId
  - Create SubscriptionLog record for all subscription changes
  - Include source (payment/manual/admin) in logs
  - Add admin endpoints for subscription management
  - _Requirements: 5.4, 6.1, 6.2, 6.3, 6.4, 6.5_

- [ ] 9.1 Write property test for subscription history query
  - **Property 15: Subscription history query returns all records**
  - **Validates: Requirements 5.4**

- [ ] 9.2 Write property test for manual activation
  - **Property 17: Manual activation updates subscription without payment**
  - **Validates: Requirements 6.2**

- [ ] 9.3 Write property test for manual deactivation
  - **Property 18: Manual deactivation sets subscription to inactive**
  - **Validates: Requirements 6.3**

- [ ] 9.4 Write property test for audit log creation
  - **Property 19: Manual changes create audit logs**
  - **Validates: Requirements 6.4**

- [ ] 9.5 Write property test for log completeness
  - **Property 20: Subscription logs include all change types**
  - **Validates: Requirements 6.5**

- [ ] 10. Implement error handling and validation
  - Add structured error responses with errorCode and message
  - Add request validation for all endpoints
  - Return 400 for invalid requests with validation details
  - Return 401 for unauthenticated requests
  - Add circuit breaker for Auth Service calls
  - _Requirements: 8.3, 8.4, 8.5_

- [ ] 10.1 Write property test for error response structure
  - **Property 26: API errors return structured responses**
  - **Validates: Requirements 8.3**

- [ ] 10.2 Write property test for request validation
  - **Property 27: Invalid requests return validation errors**
  - **Validates: Requirements 8.4**

- [ ] 10.3 Write property test for authentication errors
  - **Property 28: Unauthenticated requests return 401**
  - **Validates: Requirements 8.5**

- [ ] 11. Update frontend to handle subscription flow
  - Update payment-gateway component to include user metadata in preference request
  - Create subscription success page to handle MercadoPago redirect
  - Add subscription status check to user profile
  - Display premium badge for subscribed users
  - Add subscription management UI
  - _Requirements: 1.1, 1.2, 4.2_

- [ ] 11.1 Write integration test for end-to-end payment flow
  - Test: Create preference → Complete payment → Verify subscription active
  - Test with both Ilustrador and Escritor users

- [ ] 12. Create database migrations
  - Create migration for payment_preferences table
  - Create migration for payment_records table
  - Create migration for activation_queue table
  - Create migration for subscription_logs table
  - Add indexes on userId, paymentId, preferenceId
  - _Requirements: All_

- [ ] 13. Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 14. Configure monitoring and alerts
  - Add metrics for payment success rate
  - Add metrics for activation success rate
  - Add metrics for webhook processing time
  - Add metrics for retry queue size
  - Configure alerts for high retry queue size
  - Configure alerts for Auth Service downtime
  - _Requirements: All_

- [ ] 14.1 Write integration test for retry mechanism
  - Test: Simulate Auth Service failures
  - Verify retry queue behavior
  - Verify successful retry removes queue entry

- [ ] 15. Final Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.
