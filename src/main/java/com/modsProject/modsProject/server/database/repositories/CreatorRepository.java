package com.modsProject.modsProject.server.database.repositories;

import com.modsProject.modsProject.server.database.models.Creator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Long> {
    Optional<Creator> findByName(String name);

    @Query(value = "SELECT * FROM creators WHERE name LIKE CONCAT('%', :name, '%')", nativeQuery = true)
    Page<Creator> findBySimilarName(@Param("name") String name, Pageable pageable);

    Optional<Creator> findByNameAndPassword(String name, String password);

    @Transactional
    @Modifying
    @Query(value = "UPDATE creators SET image_path = :image_path WHERE id = :id", nativeQuery = true)
    void updateImagePath(@Param("id") Long id, @Param("image_path") String imagePath);

    @Transactional
    @Modifying
    @Query(value = "UPDATE creators SET name = :name, description = :description WHERE id = :id", nativeQuery = true)
    void updateNameAndDescription(@Param("id") Long id, @Param("name") String name, @Param("description") String description);
}