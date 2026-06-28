package com.modsProject.modsProject.server.database.models;

import jakarta.persistence.*;

@Entity
@Table(name = "sources")
public class Source {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String link;

    @Column(name = "src_type", nullable = false)
    private String srcType;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    public Source() {

    }

    public Source(String link, String srcType, Long creatorId) {
        this.link = link;
        this.srcType = srcType;
        this.creatorId = creatorId;
    }

    public Source(Long id, String link, String srcType, Long creatorId) {
        this.id = id;
        this.link = link;
        this.srcType = srcType;
        this.creatorId = creatorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSrcType() {
        return srcType;
    }

    public void setSrcType(String srcType) {
        this.srcType = srcType;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
}
