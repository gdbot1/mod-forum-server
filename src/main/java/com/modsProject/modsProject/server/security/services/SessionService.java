package com.modsProject.modsProject.server.security.services;

import com.modsProject.modsProject.server.database.models.Session;
import com.modsProject.modsProject.server.database.repositories.SessionRepository;
import com.modsProject.modsProject.utils.GeneratorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class SessionService {
     public static final Logger LOGGER = LogManager.getLogger(SessionService.class);
    @Autowired
    SessionRepository sessionRepository;
    private final long expiration;

    public SessionService() {
        this.expiration = 1000 * 60 * 60 * 24; //1 день
    }

    public String generateUUID() {
        return GeneratorUtils.generateUUID();
    }

    public Session createSession(Long creator_id) {
        Session session = new Session(
                generateUUID(),
                creator_id,
                Instant.now(),
                Instant.now().plusMillis(expiration),
                false
        );

        sessionRepository.save(session);

        return session;
    }

    public Session get(String sessionKey) {
        Optional<Session> session = sessionRepository.findById(sessionKey);

        return session.orElse(null);
    }

    public Session getIfValid(String sessionKey) {
        if (sessionKey == null) {
            return null;
        }

        Optional<Session> raw_session = sessionRepository.findById(sessionKey);

        if (raw_session.isPresent()) {
            Session session = raw_session.get();

            if (session.isValid()) {
                return session;
            }
            else {
                LOGGER.debug("Session expired");
                return null;
            }
        }
        else {
            LOGGER.debug("Invalid session: {}", sessionKey);
            return null;
        }
    }

    public boolean isValid(Session session) {
        return session != null;
    }
}