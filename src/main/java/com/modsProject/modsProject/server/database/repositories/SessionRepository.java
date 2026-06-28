package com.modsProject.modsProject.server.database.repositories;

import com.modsProject.modsProject.server.database.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
}