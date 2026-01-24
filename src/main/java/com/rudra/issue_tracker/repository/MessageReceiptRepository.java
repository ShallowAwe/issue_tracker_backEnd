package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.MessageReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageReceiptRepository extends JpaRepository<MessageReceipt, Long> {

    List<MessageReceipt> findByMessageConversationIdAndUserId(
            Long conversationId,
            Long userId);

    List<MessageReceipt> findByUserAndMessage_Conversation(
            com.rudra.issue_tracker.model.User user,
            com.rudra.issue_tracker.model.Conversation conversation);
}
