package pl.januaryewakasia.plxpodlasie.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.januaryewakasia.plxpodlasie.exception.ExceptionHandler;
import pl.januaryewakasia.plxpodlasie.service.infrastructure.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final Logger logger = LogManager.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/permitted")
    public ResponseEntity<?> isUserAdmin() {
        try {
            logger.info("Checking if user is admin");
            return ResponseEntity.ok(userService.isUserAdmin());
        } catch (Exception e) {
            logger.error(e);
            return ExceptionHandler.exceptionHandler(e);
        }
    }
}
