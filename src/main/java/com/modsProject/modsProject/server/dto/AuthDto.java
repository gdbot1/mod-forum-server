package com.modsProject.modsProject.server.dto;

public class AuthDto {
    private Long creatorId;
    private String newJwtToken;

    public AuthDto() {

    }

    public AuthDto(Long creatorId, String newJwtToken) {
        this.creatorId = creatorId;
        this.newJwtToken = newJwtToken;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getNewJwtToken() {
        return newJwtToken;
    }

    public void setNewJwtToken(String newJwtToken) {
        this.newJwtToken = newJwtToken;
    }

    public boolean hasNewJwt() {
        return newJwtToken != null;
    }

}