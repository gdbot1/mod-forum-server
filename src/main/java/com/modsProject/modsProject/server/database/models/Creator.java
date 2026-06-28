package com.modsProject.modsProject.server.database.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "creators")
public class Creator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String password;

    @Lob
    private String description;

    @Column(name = "image_path")
    private String imagePath;

    @Column(
            name = "registration_date",
            insertable = false,
            updatable = false
    )
    private LocalDateTime registrationDate;

    public Creator() {

    }

    public Creator(String name, String password, String description, String imagePath) {
        this.name = name;
        this.password = password;
        this.description = description;
        this.imagePath = imagePath;
    }

    public Creator(Long id, String name, String password, String description, String imagePath, LocalDateTime registrationDate) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.description = description;
        this.imagePath = imagePath;
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
}
