package com.modsProject.modsProject.server.dto;

public class MessageRequestDto {
    private String message;
    private Long senderId;

    public MessageRequestDto() {

    }

    public MessageRequestDto(String message, Long senderId) {
        this.message = message;
        this.senderId = senderId;
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
}
