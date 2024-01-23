package com.example.springbootblog.service.impl;

import com.example.springbootblog.entity.Roles;
import com.example.springbootblog.entity.Users;
import com.example.springbootblog.exception.BlogAPIException;
import com.example.springbootblog.payload.LoginDto;
import com.example.springbootblog.payload.RegisterDto;
import com.example.springbootblog.repository.RoleRepository;
import com.example.springbootblog.repository.UserRepository;
import com.example.springbootblog.security.JwtTokenProvider;
import com.example.springbootblog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;


    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository= userRepository;
        this.roleRepository= roleRepository;
        this.passwordEncoder= passwordEncoder;
        this.jwtTokenProvider= jwtTokenProvider;
    }

    @Override
    public String Login(LoginDto loginDto) {

      Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token= jwtTokenProvider.generateToken(authentication);

        //return "User Logged-in Successfully";
        return token;
    }

    @Override
    public String Register(RegisterDto registerDto) {

        //check if username exists in db or not
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Username already exists");
        }

        //check if email already exists in db
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        Users user = new Users();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());

        Set<Roles> roles = new HashSet<>();
        Roles userRole = roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully";
    }
}
