package com.modsProject.modsProject.server.database.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(
            insertable = false,
            updatable = false
    )
    private LocalDateTime date;

    @Column(name = "context_type", nullable = false)
    private String contextType;

    @Column(name = "context_id", nullable = false)
    private Long contextId;

    public Message() {

    }

    public Message(String message, Long senderId, String contextType, Long contextId) {
        this.message = message;
        this.senderId = senderId;
        this.contextType = contextType;
        this.contextId = contextId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }

    public Long getContextId() {
        return contextId;
    }

    public void setContextId(Long contextId) {
        this.contextId = contextId;
    }
}
