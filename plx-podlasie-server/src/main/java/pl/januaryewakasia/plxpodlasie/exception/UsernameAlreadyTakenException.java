package pl.januaryewakasia.plxpodlasie.exception;

public class UsernameAlreadyTakenException extends RuntimeException {
    public UsernameAlreadyTakenException() {
        super("Username is already taken");
    }
}
