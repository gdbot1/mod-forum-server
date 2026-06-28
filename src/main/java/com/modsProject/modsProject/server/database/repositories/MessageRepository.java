package com.modsProject.modsProject.server.database.repositories;

import com.modsProject.modsProject.server.database.models.Creator;
import com.modsProject.modsProject.server.database.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT messages.* FROM messages JOIN creators ON creators.id = messages.context_id WHERE creators.name LIKE CONCAT('%', :name, '%') AND messages.context LIKE 'creator'", nativeQuery = true)
    Optional<Message> findByCreatorSimilarName(String name);

    @Query(value = "SELECT messages.* FROM messages JOIN mods ON mods.id = messages.context_id WHERE mods.name LIKE CONCAT('%', :name, '%') AND messages.context LIKE 'mod'", nativeQuery = true)
    Optional<Message> findByModSimilarName(String name);

    List<Message> findByContextIdAndContextType(Long id, String contextType);
}