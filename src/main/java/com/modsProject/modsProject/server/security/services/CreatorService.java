package com.modsProject.modsProject.server.security.services;

import com.modsProject.modsProject.server.database.models.Creator;
import com.modsProject.modsProject.server.database.repositories.CreatorRepository;
import com.modsProject.modsProject.server.dto.CreatorDto;
import com.modsProject.modsProject.server.errors.InvalidPasswordException;
import com.modsProject.modsProject.server.errors.InvalidUsernameException;
import com.modsProject.modsProject.server.errors.RegistrationDeniedException;
import com.modsProject.modsProject.utils.web.ValidatorUtils;
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

    public void changeUsername(Long id, String username) throws DataIntegrityViolationException, InvalidUsernameException {
        if (ValidatorUtils.usernameIsValid(username)) {
            creatorRepository.updateName(id, username);
        }
        else {
            throw new InvalidUsernameException("Username must containing symbols A-Z, a-z, 0-9, _ or - only and length must be in interval from 6 to 32");
        }
    }

    public void registration(CreatorDto creatorDto) throws DataIntegrityViolationException, InvalidUsernameException, InvalidPasswordException {
        if (!ValidatorUtils.passwordIsValid(creatorDto.getPassword())) {
            throw new InvalidPasswordException("Password must containing symbols A-Z, a-z, 0-9, _, -, !, @, #, $, %, ^, &, *, (), +, =, {}, [], |, \\, :, ;, \", ', <>, ,, ., ?, /, ~ or ` only and length must be in interval from 6 to 32");
        }
        if (!ValidatorUtils.usernameIsValid(creatorDto.getName())) {
            throw new InvalidUsernameException("Username must containing symbols A-Z, a-z, 0-9, _ or - only and length must be in interval from 6 to 32");
        }

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
