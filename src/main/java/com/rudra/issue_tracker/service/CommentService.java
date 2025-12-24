// CommentService.java
package com.rudra.issue_tracker.service;

import com.rudra.issue_tracker.exceptions.NotFoundException;
import com.rudra.issue_tracker.model.Issue;
import com.rudra.issue_tracker.model.IssueComment;
import com.rudra.issue_tracker.model.User;
import com.rudra.issue_tracker.repository.IssueCommentRepository;
import com.rudra.issue_tracker.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final IssueRepository issueRepository;
    private final IssueCommentRepository commentRepository;
    private final UserService userService;

    @Transactional
    public IssueComment addComment(String issueKey, Long authorId, String body) {
        Issue issue = issueRepository.findByKey(issueKey)
                .orElseThrow(() -> new NotFoundException("Issue not found: " + issueKey));

        User author = userService.findById(authorId);

        IssueComment comment = IssueComment.builder()
                .issue(issue)
                .author(author)
                .body(body)
                .build();

        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<IssueComment> getComments(String issueKey) {
        Issue issue = issueRepository.findByKey(issueKey)
                .orElseThrow(() -> new NotFoundException("Issue not found: " + issueKey));
        return commentRepository.findByIssueOrderByCreatedAtAsc(issue);
    }

    @Transactional
    public IssueComment editComment(Long commentId, Long authorId, String body) {
        IssueComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found: " + commentId));

        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new IllegalArgumentException("Only author can edit comment");
        }

        comment.setBody(body);
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long authorId) {
        IssueComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found: " + commentId));

        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new IllegalArgumentException("Only author can delete comment");
        }

        commentRepository.delete(comment);
    }
}
