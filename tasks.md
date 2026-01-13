# Backend Task: WebSocket Messaging System

## Phase 1: Core WebSocket Messaging

### 1. Design Data Models

#### Message
- id (int)
- conversationId (int)
- senderId (int)
- senderType (USER | SYSTEM | BOT)
- content (TEXT)
- messageType (TEXT | FILE | IMAGE | SYSTEM)
- status (SENT | DELIVERED | READ)
- createdAt (timestamp)
- editedAt (timestamp, nullable)
- isDeleted (boolean)

#### Conversation
- id (UUID)
- type (DIRECT | GROUP)
- createdBy (UUID)
- createdAt (timestamp)
- isArchived (boolean)

#### ConversationParticipant
- id (UUID)
- conversationId (UUID)
- userId (UUID)
- role (ADMIN | MEMBER)
- joinedAt (timestamp)
- lastReadMessageId (UUID)

---

### 2. WebSocket Configuration

- Enable WebSocket with STOMP protocol
- Define application destination prefix: `/app`
- Define broker destinations:
    - `/topic` for group conversations
    - `/user/queue` for user-specific messages

#### Required Configuration Classes
- WebSocketConfig
- WebSocketSecurityConfig
- JwtHandshakeInterceptor
- UserSessionRegistry

---

### 3. Authentication & Authorization

- JWT token must be validated during WebSocket handshake
- Reject connections with invalid or expired tokens
- Map authenticated userId to WebSocket session
- Authorize every message:
    - User must be a participant of the conversation
    - Conversation must not be archived

---

### 4. WebSocket Message Flow

#### Connection Lifecycle
1. Client opens WebSocket connection with JWT in headers
2. Handshake interceptor validates token
3. User session is registered
4. Client subscribes to:
    - `/topic/conversation/{conversationId}`
    - `/user/queue/messages`

#### Message Lifecycle
1. Client sends message to `/app/chat.send`
2. Backend validates message and permissions
3. Message is published to subscribers
4. Message is persisted asynchronously
5. Acknowledgement sent to sender

---

### 5. Controllers

#### WebSocket Controller
- sendMessage
- typingIndicator

Responsibilities:
- Input validation
- Authorization checks
- Message publishing only (no business logic)

#### REST Controller
- Create conversation
- Fetch conversation history (paginated)

---

### 6. Message Persistence

- Persist messages asynchronously to avoid blocking WebSocket threads
- Use pagination or cursor-based retrieval for history
- Do not load entire conversation history at once

---

### 7. Logging & Testing

#### Logging Requirements
- WebSocket connect events
- Disconnect events
- Subscription events
- Message publish events
- Authorization failures

#### Testing Scenarios
- Invalid JWT during handshake
- User sending message to unauthorized conversation
- Offline recipient
- Large message payload rejection

---

## Phase 2: Security Enhancements

- Rate limiting per user
- Message size limits
- Content sanitization to prevent XSS
- Optional message encryption at rest

---

## Phase 3: File Upload in Chat

- Do not send files through WebSocket
- Upload files via REST API
- Store files in object storage (S3 / MinIO / local)
- Send file metadata through WebSocket:
    - fileId
    - fileName
    - fileType
    - fileSize
    - downloadUrl

---

## Phase 4: Performance & Scalability

- Use Redis (optional) for:
    - Active user sessions
    - Recent messages cache
- Implement connection limits per node
- Archive old conversations
- Monitor WebSocket thread usage

---

## Non-Goals (Out of Scope)
- Audio/video calls
- Message reactions
- Message search
- End-to-end encryption (future consideration)

---

## Notes
WebSockets are transport mechanisms, not storage systems.
Security and persistence are mandatory, not optional add-ons.
Design assumes at-least-once delivery guarantee.
