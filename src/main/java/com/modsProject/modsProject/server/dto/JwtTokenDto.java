package com.modsProject.modsProject.server.dto;

import java.time.Instant;

public class JwtTokenDto {
    private Long creatorId;
    private Instant issuedAt, expiration;

    public JwtTokenDto() {

    }

    public JwtTokenDto(Long creator_id, Instant issuedAt, Instant expiration) {
        this.creatorId = creator_id;
        this.issuedAt = issuedAt;
        this.expiration = expiration;
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
}
