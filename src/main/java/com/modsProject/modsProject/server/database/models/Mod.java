package com.modsProject.modsProject.server.database.models;

import jakarta.persistence.*;

@Entity
@Table(name = "mods")
public class Mod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "image_path")
    private String imagePath;

    @Lob
    private String description;

    @Column(name = "creator_id", nullable = false)
    private long creatorId;

    public Mod() {

    }

    public Mod(String name, String imagePath, String description, long creatorId) {
        this.name = name;
        this.imagePath = imagePath;
        this.description = description;
        this.creatorId = creatorId;
    }

    public Mod(Long id, String name, String imagePath, String description, long creatorId) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.description = description;
        this.creatorId = creatorId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id:"+this.id+", name:"+this.name+", image_path:"+this.imagePath+", desc:"+this.description+", creator_id:"+this.creatorId+"]";
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }
}
