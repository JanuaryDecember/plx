package pl.januaryewakasia.plxpodlasie.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;

public class ExceptionHandler {
    public static ResponseEntity<?> exceptionHandler(Exception ex) {
        return switch (ex) {
            case AuthenticationServiceException e -> ResponseEntity.status(403).body(ex.getMessage());
            case NotFoundException e -> ResponseEntity.status(404).body(ex.getMessage());
            case UsernameAlreadyTakenException e -> ResponseEntity.status(409).body(ex.getMessage());
            case UnauthorizedException e -> ResponseEntity.status(401).body(ex.getMessage());
            default -> ResponseEntity.internalServerError().body(ex.getMessage());
        };
    }
}
