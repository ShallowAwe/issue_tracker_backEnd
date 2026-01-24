package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.Conversation;
import com.rudra.issue_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("""
        SELECT c
        FROM Conversation c
        WHERE c.participantA = :a
          AND c.participantB = :b
          AND c.type = 'DIRECT'
    """)
    Optional<Conversation> findDirectConversation(
            @Param("a") User participantA,
            @Param("b") User participantB
    );
}
