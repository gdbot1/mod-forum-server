package com.modsProject.modsProject.server.security.services;

import com.modsProject.modsProject.server.database.models.Creator;
import com.modsProject.modsProject.server.database.repositories.CreatorRepository;
import com.modsProject.modsProject.server.dto.CreatorDto;
import com.modsProject.modsProject.server.errors.RegistrationDeniedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreatorService {
    private static final Logger LOGGER = LogManager.getLogger(CreatorService.class);

    @Autowired
    CreatorRepository creatorRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void registration(CreatorDto creatorDto) throws DataIntegrityViolationException {
        Creator creator = new Creator();

        creator.setName(creatorDto.getName());
        creator.setPassword(passwordEncoder.encode(creatorDto.getPassword()));//Хеширую пароль с помощью bcrypt
        creator.setDescription(creatorDto.getDescription());
        creator.setImagePath(creatorDto.getImagePath());

        //Сохраняю креатора
        creatorRepository.saveAndFlush(creator);
    }

    public Creator authenticate(String name, String password) throws AuthorizationDeniedException {
        Optional<Creator> raw_creator = creatorRepository.findByName(name);

        Creator creator = raw_creator.orElseThrow(() -> new AuthorizationDeniedException("Creator not found"));

        if (passwordEncoder.matches(password, creator.getPassword())) {
            return creator;
        }
        else {
            throw new AuthorizationDeniedException("Creator not found");
        }
    }
}
