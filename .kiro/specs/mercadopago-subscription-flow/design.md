# Design Document: MercadoPago Subscription Flow

## Overview

This design implements a complete subscription flow that integrates MercadoPago payment processing with user subscription status management. The system handles payment confirmations through webhooks and automatically updates user profiles in the Auth Service to grant premium benefits.

The design follows a microservices architecture where the Monetization Service handles payment processing and the Auth Service manages user subscription status. Communication between services occurs through REST APIs with retry logic for resilience.

## Architecture

### High-Level Architecture

```
┌─────────────┐         ┌──────────────────┐         ┌─────────────┐
│   Frontend  │────────▶│  API Gateway     │────────▶│   Auth      │
│  (Angular)  │         │   (Port 8080)    │         │  Service    │
└─────────────┘         └──────────────────┘         └─────────────┘
       │                         │                           │
       │                         │                           │
       ▼                         ▼                           ▼
┌─────────────┐         ┌──────────────────┐         ┌─────────────┐
│  MercadoPago│◀────────│  Monetization    │────────▶│  Database   │
│   Checkout  │ Webhook │    Service       │         │  (MySQL)    │
└─────────────┘         └──────────────────┘         └─────────────┘
```

### Component Interaction Flow

1. **Payment Initiation**: Frontend → Monetization Service → MercadoPago
2. **Payment Completion**: MercadoPago → Monetization Service (Webhook)
3. **Subscription Activation**: Monetization Service → Auth Service
4. **Status Verification**: Frontend → Auth Service

## Components and Interfaces

### 1. MercadoPago Payment Controller

**Location**: `Backend/monetization-service/interfaces/rest/MercadoPagoController.java`

**Responsibilities**:
- Create payment preferences for subscriptions
- Handle webhook notifications from MercadoPago
- Process payment status updates

**New Endpoints**:
```java
POST /api/mercadopago/preferences
- Creates a payment preference with user metadata
- Returns preference ID and checkout URL
- Stores preference record in database

POST /api/mercadopago/webhook
- Receives payment notifications from MercadoPago
- Validates webhook authenticity
- Triggers subscription activation
```

### 2. Subscription Activation Service

**Location**: `Backend/monetization-service/application/service/SubscriptionActivationService.java` (NEW)

**Responsibilities**:
- Process successful payment notifications
- Call Auth Service to update user subscription status
- Handle retry logic for failed activations
- Maintain activation queue for resilience

**Key Methods**:
```java
void processPaymentNotification(PaymentNotification notification)
void activateUserSubscription(String userId, String userType, String paymentId)
void retryFailedActivation(ActivationRequest request)
```

### 3. Auth Service Subscription Controller

**Location**: `Backend/auth-service/interfaces/rest/SubscriptionController.java` (NEW)

**Responsibilities**:
- Provide endpoint to update user subscription status
- Validate user existence and type
- Update Ilustrador or Escritor subscription field
- Return updated user data

**New Endpoints**:
```java
POST /api/v1/subscriptions/activate
- Request: { userId, userType, paymentId }
- Updates user profile subscription field to true
- Returns updated user data

POST /api/v1/subscriptions/deactivate
- Request: { userId, userType }
- Updates user profile subscription field to false
- Returns updated user data

GET /api/v1/subscriptions/status/{userId}
- Returns current subscription status for user
```

### 4. User Subscription Repository

**Location**: `Backend/monetization-service/infrastructure/persistence/UserSubscriptionRepository.java`

**Responsibilities**:
- Store and retrieve UserSubscription records
- Query subscription history
- Track payment references

### 5. Payment Preference Repository

**Location**: `Backend/monetization-service/infrastructure/persistence/PaymentPreferenceRepository.java` (NEW)

**Responsibilities**:
- Store payment preference records
- Link preferences to users
- Track preference status

## Data Models

### PaymentPreference Entity

```java
@Entity
@Table(name = "payment_preferences")
public class PaymentPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String preferenceId;  // MercadoPago preference ID
    private String userId;
    private String userEmail;
    private String userType;  // "ILUSTRADOR" or "ESCRITOR"
    private BigDecimal amount;
    private String status;  // "pending", "completed", "failed"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### PaymentRecord Entity

```java
@Entity
@Table(name = "payment_records")
public class PaymentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String paymentId;  // MercadoPago payment ID
    private String preferenceId;
    private String userId;
    private String status;  // "approved", "rejected", "pending"
    private BigDecimal amount;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}
```

### ActivationQueue Entity

```java
@Entity
@Table(name = "activation_queue")
public class ActivationQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String userId;
    private String userType;
    private String paymentId;
    private Integer retryCount;
    private String lastError;
    private LocalDateTime nextRetryAt;
    private LocalDateTime createdAt;
}
```

### SubscriptionLog Entity

```java
@Entity
@Table(name = "subscription_logs")
public class SubscriptionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String userId;
    private String action;  // "activated", "deactivated"
    private String source;  // "payment", "manual", "admin"
    private String performedBy;  // admin user ID if manual
    private String reason;
    private String paymentId;
    private LocalDateTime timestamp;
}
```

## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system-essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

### Property 1: Payment preference creation includes all required fields
*For any* valid user data and subscription amount, creating a payment preference should return a preference object containing preferenceId, initPoint, and all user metadata fields.
**Validates: Requirements 1.1, 5.1**

### Property 2: Payment preference response contains valid redirect URL
*For any* created payment preference, the response should contain an initPoint field that is a valid HTTPS URL pointing to mercadopago.com.
**Validates: Requirements 1.2**

### Property 3: Approved payment notifications trigger subscription activation
*For any* webhook notification with status "approved" and valid user metadata, processing the notification should result in a call to activate the user's subscription.
**Validates: Requirements 1.4, 2.3, 2.4**

### Property 4: Subscription activation updates user profile
*For any* valid user ID and type, calling the subscription activation endpoint should update the corresponding profile's subscription field to true in the database.
**Validates: Requirements 1.5, 3.3, 3.4**

### Property 5: Webhook payload parsing extracts payment details
*For any* valid MercadoPago webhook payload, the parser should correctly extract paymentId, status, and user metadata fields.
**Validates: Requirements 2.2**

### Property 6: Invalid user IDs are rejected
*For any* subscription activation request with a non-existent user ID, the Auth Service should return a 404 error response.
**Validates: Requirements 3.1**

### Property 7: User type determination is correct
*For any* valid user ID, the Auth Service should correctly identify whether the user is an Ilustrador or Escritor based on their profile relationships.
**Validates: Requirements 3.2**

### Property 8: Successful activation returns updated user data
*For any* successful subscription activation, the response should contain the user's updated subscription status set to true.
**Validates: Requirements 3.5**

### Property 9: Subscription status query returns current state
*For any* user ID, after updating their subscription status, querying the status endpoint should return the newly updated value.
**Validates: Requirements 4.2**

### Property 10: Active subscription grants premium access
*For any* user with subscripcion=true, requests to premium endpoints should return 200 OK responses.
**Validates: Requirements 4.3**

### Property 11: Inactive subscription denies premium access
*For any* user with subscripcion=false, requests to premium endpoints should return 403 Forbidden responses.
**Validates: Requirements 4.4**

### Property 12: Payment preference creation persists to database
*For any* created payment preference, querying the database by preferenceId should return a record containing userId, amount, and status fields.
**Validates: Requirements 5.1**

### Property 13: Payment completion creates payment record
*For any* processed payment webhook, the database should contain a PaymentRecord with paymentId, status, and timestamp.
**Validates: Requirements 5.2**

### Property 14: Subscription activation creates UserSubscription record
*For any* activated subscription, the database should contain a UserSubscription record with startDate, mercadoPagoSubscriptionId, and isActive=true.
**Validates: Requirements 5.3**

### Property 15: Subscription history query returns all records
*For any* user with multiple subscription records, querying their subscription history should return all records ordered by creation date.
**Validates: Requirements 5.4**

### Property 16: UserSubscription includes payment reference
*For any* created UserSubscription record, the mercadoPagoSubscriptionId field should be populated with a non-null value.
**Validates: Requirements 5.5**

### Property 17: Manual activation updates subscription without payment
*For any* admin-initiated activation request, the user's subscription should be activated without requiring a paymentId.
**Validates: Requirements 6.2**

### Property 18: Manual deactivation sets subscription to inactive
*For any* admin-initiated deactivation request, the user's subscription field should be set to false.
**Validates: Requirements 6.3**

### Property 19: Manual changes create audit logs
*For any* manual subscription change, a SubscriptionLog record should be created with performedBy, reason, and timestamp fields populated.
**Validates: Requirements 6.4**

### Property 20: Subscription logs include all change types
*For any* user with both automatic and manual subscription changes, querying their logs should return records with source="payment" and source="manual".
**Validates: Requirements 6.5**

### Property 21: Webhook processing failures are logged
*For any* webhook that fails to process, an error log entry should be created containing the full webhook payload.
**Validates: Requirements 7.1**

### Property 22: Auth Service unavailability triggers retry queue
*For any* subscription activation request that receives a 503 response from Auth Service, an ActivationQueue record should be created.
**Validates: Requirements 7.2**

### Property 23: Failed activations retry with exponential backoff
*For any* queued activation, the system should attempt exactly 3 retries with nextRetryAt values increasing exponentially (1min, 2min, 4min).
**Validates: Requirements 7.3**

### Property 24: Exhausted retries trigger admin alerts
*For any* activation that fails after 3 retries, an alert notification should be sent to the admin notification channel.
**Validates: Requirements 7.4**

### Property 25: Successful retries remove queue entries
*For any* queued activation that succeeds on retry, the corresponding ActivationQueue record should be deleted from the database.
**Validates: Requirements 7.5**

### Property 26: API errors return structured responses
*For any* API error condition, the response should be a JSON object containing errorCode and message fields.
**Validates: Requirements 8.3**

### Property 27: Invalid requests return validation errors
*For any* request with missing or invalid parameters, the response should be a 400 status with detailed validation error messages.
**Validates: Requirements 8.4**

### Property 28: Unauthenticated requests return 401
*For any* request to protected endpoints without valid authentication, the response should be 401 with an error message.
**Validates: Requirements 8.5**

## Error Handling

### Webhook Processing Errors

1. **Invalid Payload**: Log error, return 400 to MercadoPago
2. **Payment Not Found**: Log warning, return 200 (idempotent)
3. **User Not Found**: Log error, queue for manual review
4. **Auth Service Timeout**: Add to retry queue with exponential backoff
5. **Database Error**: Log error, return 500, trigger alert

### Retry Logic

```java
RetryPolicy:
- Max Attempts: 3
- Initial Delay: 1 minute
- Backoff Multiplier: 2
- Max Delay: 5 minutes
- Retry On: IOException, TimeoutException, 5xx responses
```

### Circuit Breaker

```java
CircuitBreaker for Auth Service:
- Failure Threshold: 5 consecutive failures
- Timeout: 10 seconds
- Reset Timeout: 30 seconds
- Half-Open Max Calls: 3
```

## Testing Strategy

### Unit Tests

1. **Payment Preference Creation**
   - Test preference creation with valid user data
   - Test preference creation with missing fields
   - Test preference ID generation

2. **Webhook Processing**
   - Test webhook payload parsing
   - Test payment status extraction
   - Test user metadata extraction

3. **Subscription Activation**
   - Test Auth Service API calls
   - Test retry queue management
   - Test error logging

4. **Auth Service Endpoints**
   - Test user type determination
   - Test profile subscription updates
   - Test response formatting

### Property-Based Tests

Property-based tests will use **JUnit 5** with **jqwik** library for Java.

Each property test should run a minimum of 100 iterations to ensure comprehensive coverage.

Property tests must be tagged with comments referencing the design document:
- Format: `// Feature: mercadopago-subscription-flow, Property X: [property description]`
- Example: `// Feature: mercadopago-subscription-flow, Property 1: Payment preference creation includes all required fields`

### Integration Tests

1. **End-to-End Payment Flow**
   - Create preference → Process webhook → Verify subscription active
   - Test with both Ilustrador and Escritor users

2. **Retry Mechanism**
   - Simulate Auth Service failures
   - Verify retry queue behavior
   - Verify successful retry removes queue entry

3. **Manual Subscription Management**
   - Test admin activation endpoint
   - Test admin deactivation endpoint
   - Verify audit log creation

### Test Data Generators

```java
// Generate random user data
Arbitrary<UserData> userDataGenerator()

// Generate random payment amounts (2.00 - 1000.00)
Arbitrary<BigDecimal> amountGenerator()

// Generate random webhook payloads
Arbitrary<WebhookPayload> webhookGenerator()

// Generate random user types
Arbitrary<String> userTypeGenerator() // "ILUSTRADOR" or "ESCRITOR"
```

## Security Considerations

1. **Webhook Validation**: Verify MercadoPago signature on all webhook requests
2. **API Authentication**: Require JWT tokens for all subscription endpoints
3. **Admin Endpoints**: Require ADMIN role for manual subscription management
4. **Rate Limiting**: Limit webhook endpoint to 100 requests/minute per IP
5. **Data Encryption**: Encrypt sensitive payment data at rest

## Performance Requirements

1. **Webhook Processing**: < 2 seconds response time
2. **Subscription Activation**: < 5 seconds end-to-end
3. **Status Query**: < 500ms response time
4. **Database Queries**: Indexed on userId, paymentId, preferenceId
5. **Retry Queue Processing**: Background job every 1 minute

## Monitoring and Observability

### Metrics to Track

1. **Payment Success Rate**: Percentage of approved payments
2. **Activation Success Rate**: Percentage of successful subscription activations
3. **Webhook Processing Time**: P50, P95, P99 latencies
4. **Retry Queue Size**: Number of pending retries
5. **Auth Service Availability**: Uptime percentage

### Logging

1. **Webhook Events**: Log all incoming webhooks with payload
2. **Activation Events**: Log all subscription activations with user ID
3. **Error Events**: Log all errors with stack traces
4. **Audit Events**: Log all manual subscription changes

### Alerts

1. **High Retry Queue Size**: Alert if queue > 10 items
2. **Auth Service Down**: Alert if circuit breaker opens
3. **Payment Failures**: Alert if failure rate > 10%
4. **Webhook Errors**: Alert if error rate > 5%

## Deployment Considerations

1. **Database Migrations**: Create new tables for PaymentPreference, PaymentRecord, ActivationQueue, SubscriptionLog
2. **API Gateway Routes**: Add routes for new subscription endpoints
3. **Environment Variables**: Configure MercadoPago webhook URL
4. **Background Jobs**: Deploy retry queue processor
5. **Rollback Plan**: Maintain old subscription check logic during transition period
