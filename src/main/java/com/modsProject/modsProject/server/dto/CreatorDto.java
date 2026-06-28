package com.modsProject.modsProject.server.dto;

public class CreatorDto {
    private String name, password, description, imagePath;

    public CreatorDto() {

    }

    public CreatorDto(String name, String password, String description, String imagePath) {
        this.name = name;
        this.password = password;
        this.description = description;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
