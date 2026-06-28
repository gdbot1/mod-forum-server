package com.modsProject.modsProject.server.security.services;

import com.modsProject.modsProject.server.database.models.Session;
import com.modsProject.modsProject.server.dto.AuthDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final Logger LOGGER = LogManager.getLogger(AuthService.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private SessionService sessionService;

    public AuthService() {

    }

    public String generateJwtToken(Long creator_id) {
        return jwtService.generateToken(creator_id);
    }

    public String generateSessionKey(Long creator_id) {
        return sessionService.createSession(creator_id).getSessionKey();
    }

    public String refreshToken(String sessionKey) {
        Session session = sessionService.getIfValid(sessionKey);

        if (session != null) {
            return jwtService.generateToken(session.getCreatorId());
        }

        LOGGER.debug("Invalid session key");

        return null;
    }

    public AuthDto authenticate(String accessJwtToken, String refreshSessionKey) {
        String jwt = accessJwtToken;
        String newJwt = null;

        if (accessJwtToken == null || !jwtService.isValid(accessJwtToken)) {
            String newJwtToken = refreshToken(refreshSessionKey);

            if (newJwtToken == null) {
                return null;
            }
            else {
                newJwt = jwt = newJwtToken;
            }
        }

        Long creatorId = jwtService.parse(jwt).getCreatorId();

        return new AuthDto(creatorId, newJwt);
    }
}