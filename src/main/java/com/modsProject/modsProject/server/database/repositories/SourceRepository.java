package com.modsProject.modsProject.server.database.repositories;

import com.modsProject.modsProject.server.database.models.Source;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceRepository extends CrudRepository<Source, Long> {
    List<Source> findByCreatorId(long creator_id);

    int deleteByCreatorId(long creator_id);
}