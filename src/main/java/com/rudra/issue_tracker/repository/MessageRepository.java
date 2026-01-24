package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
        SELECT MAX(m.sequenceNo)
        FROM Message m
        WHERE m.conversation.id = :conversationId
    """)
    Optional<Long> findMaxSequenceByConversation(
            @Param("conversationId") Long conversationId
    );
}
