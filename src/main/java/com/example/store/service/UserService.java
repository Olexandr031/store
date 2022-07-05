package com.example.store.service;

import com.example.store.config.MessageStrings;
import com.example.store.dto.ResponseDTO;
import com.example.store.dto.user.*;
import com.example.store.enums.ResponseStatus;
import com.example.store.enums.Role;
import com.example.store.exception.AuthenticationFailException;
import com.example.store.exception.CustomException;
import com.example.store.model.AuthenticationToken;
import com.example.store.model.User;
import com.example.store.repository.UserRepository;
import com.example.store.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.example.store.config.MessageStrings.USER_CREATED;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    Logger logger = LoggerFactory.getLogger(UserService.class);


    public ResponseDTO signUp(SignUpDTO signupDTO)  throws CustomException {
        if (Helper.notNull(userRepository.findByEmail(signupDTO.getEmail()))) {
            throw new CustomException("User already exists");
        }
        String encryptedPassword = signupDTO.getPassword();
        try {
            encryptedPassword = hashPassword(signupDTO.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("hashing password failed {}", e.getMessage());
        }


        User user = new User(signupDTO.getFirstName(), signupDTO.getLastName(), signupDTO.getEmail(), Role.user, encryptedPassword );

        User createdUser;
        try {
            createdUser = userRepository.save(user);
            final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);
            authenticationService.saveConfirmationToken(authenticationToken);
            return new ResponseDTO(ResponseStatus.success.toString(), USER_CREATED);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    public SignInResponseDTO signIn(SignInDTO signInDTO) throws CustomException {
        User user = userRepository.findByEmail(signInDTO.getEmail());
        if(!Helper.notNull(user)){
            throw  new AuthenticationFailException("User not present");
        }
        try {
            if (!user.getPassword().equals(hashPassword(signInDTO.getPassword()))){
                throw  new AuthenticationFailException(MessageStrings.WRONG_PASSWORD);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("Hashing password failed {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }

        AuthenticationToken token = authenticationService.getToken(user);

        if(!Helper.notNull(token)) {
            throw new CustomException("token not present");
        }

        return new SignInResponseDTO ("success", token.getToken());
    }


    String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return myHash;
    }

    public ResponseDTO createUser(String token, UserCreateDTO userCreateDTO) throws CustomException, AuthenticationFailException {
        User creatingUser = authenticationService.getUser(token);
        if (!canCrudUser(creatingUser.getRole())) {
            throw  new AuthenticationFailException(MessageStrings.USER_NOT_PERMITTED);
        }
        String encryptedPassword = userCreateDTO.getPassword();
        try {
            encryptedPassword = hashPassword(userCreateDTO.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("hashing password failed {}", e.getMessage());
        }

        User user = new User(userCreateDTO.getFirstName(), userCreateDTO.getLastName(), userCreateDTO.getEmail(), userCreateDTO.getRole(), encryptedPassword );
        User createdUser;
        try {
            createdUser = userRepository.save(user);
            final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);
            authenticationService.saveConfirmationToken(authenticationToken);
            return new ResponseDTO(ResponseStatus.success.toString(), USER_CREATED);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }

    }

    boolean canCrudUser(Role role) {
        if (role == Role.admin || role == Role.manager) {
            return true;
        }
        return false;
    }

    boolean canCrudUser(User userUpdating, Integer userIdBeingUpdated) {
        Role role = userUpdating.getRole();
        if (role == Role.admin || role == Role.manager) {
            return true;
        }
        if (role == Role.user && userUpdating.getId() == userIdBeingUpdated) {
            return true;
        }
        return false;
    }

    public ResponseDTO updateUser(String token, UserUpdateDTO userUpdateDTO) { // from IDEA
        return (ResponseDTO) userRepository.findAll();
    }
}
