package com.example.store.controller;

import com.example.store.dto.ResponseDTO;
import com.example.store.dto.user.*;
import com.example.store.exception.AuthenticationFailException;
import com.example.store.exception.CustomException;
import com.example.store.model.User;
import com.example.store.repository.UserRepository;
import com.example.store.service.AuthenticationService;
import com.example.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public List<User> findAllUser(@RequestParam("token") String token) throws AuthenticationFailException {
        authenticationService.authenticate(token);
        return userRepository.findAll();
    }

    @PostMapping("/signup")
    public ResponseDTO SignUp(@RequestBody SignUpDTO signupDTO) throws CustomException {
        return userService.signUp(signupDTO);
    }

    //TODO token should be updated
    @PostMapping("/signin")
    public SignInResponseDTO SignIn(@RequestBody SignInDTO signInDTO) throws CustomException {
        return userService.signIn(signInDTO);
    }

        @PostMapping("/updateUser")
    public ResponseDTO updateUser(@RequestParam("token") String token, @RequestBody UserUpdateDTO userUpdateDTO) {
        authenticationService.authenticate(token);
        return userService.updateUser(token, userUpdateDTO);
    }


    @PostMapping("/createUser")
    public ResponseDTO createUser(@RequestParam("token") String token, @RequestBody UserCreateDTO userCreateDTO)
            throws CustomException, AuthenticationFailException {
        authenticationService.authenticate(token);
        return userService.createUser(token, userCreateDTO);
    }
}
