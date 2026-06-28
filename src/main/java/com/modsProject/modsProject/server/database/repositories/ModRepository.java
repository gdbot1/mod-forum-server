package com.modsProject.modsProject.server.database.repositories;

import com.modsProject.modsProject.server.database.models.Mod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModRepository extends JpaRepository<Mod, Long> {
    @Query(value = "SELECT * FROM mods WHERE name = :name", nativeQuery = true)
    Optional<Mod> findByName(@Param("name") String name);

    @Query(value = "SELECT * FROM mods WHERE name LIKE CONCAT('%', :name, '%')", nativeQuery = true)
    Page<Mod> findBySimilarName(@Param("name") String name, Pageable pageable);

    Page<Mod> findByCreatorId(long creator_id, Pageable pageable);

    @Query(value = "SELECT mods.* FROM mods JOIN creators ON mods.creator_id = creators.id WHERE creators.name LIKE CONCAT('%', :name, '%')", nativeQuery = true,
            countQuery = "SELECT COUNT(*) FROM mods JOIN creators ON mods.creator_id = creators.id WHERE creators.name LIKE CONCAT('%', :name, '%')")
    Page<Mod> findByCreatorSimilarName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT mods.* FROM mods JOIN creators ON mods.creator_id = creators.id WHERE mods.name LIKE CONCAT('%', :name, '%') AND creators.id = :creator_id", nativeQuery = true,
            countQuery = "SELECT COUNT(*) FROM mods JOIN creators ON mods.creator_id = creators.id WHERE mods.name LIKE CONCAT('%', :name, '%') AND creators.id = :creator_id")
    Page<Mod> findByCreatorIdAndSimilarName(@Param("creator_id") long creator_id, @Param("name") String name, Pageable pageable);

    int deleteByName(String name);

    int deleteByCreatorId(long creator_id);
}