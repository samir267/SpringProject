package SpringProject.controller;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import SpringProject.dto.AuthResponseDto;
import SpringProject.dto.LoginDto;
import SpringProject.dto.RegisterDto;
import SpringProject.models.Roles;
import SpringProject.models.User;
import SpringProject.repository.RolesRepo;
import SpringProject.repository.UserRepo;
import SpringProject.security.JwtGenerator;

@Controller
@RequestMapping("/users/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserRepo userRepo;
    private RolesRepo rolesRepo;
    private PasswordEncoder passwordEncoder;

    private JwtGenerator jwtGenerator;
     @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepo userRepository,
                          RolesRepo roleRepository, PasswordEncoder passwordEncoder,JwtGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepository;
        this.rolesRepo = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator=jwtGenerator;
    }
private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


@PostMapping("login")
public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) throws AuthenticationException {
    logger.info("Attempting login for email: {}", loginDto.getEmail());
    logger.info("Attempting password for email: {}", loginDto.getPassword());

    try {
        logger.info("Starting authentication process...");
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()));
        
        logger.info("User successfully authenticated: {}", loginDto.getEmail());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        logger.info("Generated JWT Token: {}", token);

        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);

    
    } catch (AuthenticationException e) {
        logger.error("Authentication failed for email: {}", loginDto.getEmail(), e);
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); 
    }
}



    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if (userRepo.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>("Email is taken!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setFull_name(registerDto.getFull_name());
        user.setEmail(registerDto.getEmail());
        user.setAddress(registerDto.getAddress());
        user.setPassword(passwordEncoder.encode((registerDto.getPassword())));
        user.setPhone(registerDto.getPhone());
        user.setRole(registerDto.getRole());

        Roles roles = rolesRepo.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));

        userRepo.save(user);

        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }

}