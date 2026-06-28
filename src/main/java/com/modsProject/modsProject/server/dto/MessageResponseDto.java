package com.modsProject.modsProject.server.dto;

import java.time.LocalDateTime;

public class MessageResponseDto {
    private String message;
    private String senderName, senderImage;
    private Long senderId;
    private LocalDateTime date;

    public MessageResponseDto() {

    }

    public MessageResponseDto(String message, String senderName, String senderImage, Long senderId, LocalDateTime date) {
        this.message = message;
        this.senderName = senderName;
        this.senderImage = senderImage;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
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
}
