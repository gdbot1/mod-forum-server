package com.modsProject.modsProject.server.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @Column(name = "session_key", nullable = false)
    private String sessionKey;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    @Column(nullable = false)
    private Instant expiration;

    @Column(nullable = false)
    private boolean revoked;

    public Session() {

    }

    public Session(String sessionKey, Long creatorId, Instant issuedAt, Instant expiration, boolean revoked) {
        this.sessionKey = sessionKey;
        this.creatorId = creatorId;
        this.issuedAt = issuedAt;
        this.expiration = expiration;
        this.revoked = revoked;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public boolean isValid() {
        return !revoked && Instant.now().isBefore(expiration);
    }
}
