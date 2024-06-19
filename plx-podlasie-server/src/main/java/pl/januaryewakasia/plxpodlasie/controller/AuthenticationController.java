package pl.januaryewakasia.plxpodlasie.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.januaryewakasia.plxpodlasie.exception.ExceptionHandler;
import pl.januaryewakasia.plxpodlasie.request.AuthenticationRequest;
import pl.januaryewakasia.plxpodlasie.service.infrastructure.AuthenticationService;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final Logger logger = LogManager.getLogger(AuthenticationController.class);

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationService.register(authenticationRequest);
            logger.info("Created account: {}", authenticationRequest.getUsername());
            return ResponseEntity.ok("Account registered successfully");
        } catch (Exception e) {
            logger.error("Error creating account {}", authenticationRequest.getUsername(), e);
            return ExceptionHandler.exceptionHandler(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest loginRequest, HttpServletRequest request) {
        try {
            authenticationService.login(loginRequest, request);
            logger.info("User logged in: {}", loginRequest.getUsername());
            return ResponseEntity.ok("Logged in successfully");
        } catch (AuthenticationException e) {
            logger.error("Login failed", e);
            return ResponseEntity.status(401).body("Login failed! Please try again!");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authenticationService.logout(request);
        logger.info("Logged out successfully");
        return ResponseEntity.ok("Logged out successfully");
    }
}