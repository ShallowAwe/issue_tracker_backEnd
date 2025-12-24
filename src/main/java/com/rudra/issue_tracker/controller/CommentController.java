package com.rudra.issue_tracker.controller;


import com.rudra.issue_tracker.controller.dto.*;
import com.rudra.issue_tracker.exceptions.NotFoundException;
import com.rudra.issue_tracker.model.IssueComment;
import com.rudra.issue_tracker.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> addComment(@Validated @RequestBody CreateCommentRequest request) {
        try {
            IssueComment comment = commentService.addComment(
                    request.getIssueKey(),
                    request.getAuthorId(),
                    request.getBody()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(comment));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage(), 404, LocalDateTime.now()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage(), 400, LocalDateTime.now()));
        }
    }

    @GetMapping("/issue/{issueKey}")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable String issueKey) {
        List<CommentResponse> list = commentService.getComments(issueKey)
                .stream().map(this::toResponse).toList();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editComment(@PathVariable Long id,
                                         @RequestBody CreateCommentRequest request) {
        try {
            IssueComment comment = commentService.editComment(id, request.getAuthorId(), request.getBody());
            return ResponseEntity.ok(toResponse(comment));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage(), 404, LocalDateTime.now()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(e.getMessage(), 403, LocalDateTime.now()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id,
                                           @RequestParam Long authorId) {
        try {
            commentService.deleteComment(id, authorId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage(), 404, LocalDateTime.now()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(e.getMessage(), 403, LocalDateTime.now()));
        }
    }

    private CommentResponse toResponse(IssueComment c) {
        return CommentResponse.builder()
                .id(c.getId())
                .issueKey(c.getIssue().getKey())
                .authorId(c.getAuthor().getId())
                .authorUsername(c.getAuthor().getUsername())
                .body(c.getBody())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }
}
