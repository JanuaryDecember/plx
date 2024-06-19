package pl.januaryewakasia.plxpodlasie.service.infrastructure;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.januaryewakasia.plxpodlasie.Role;
import pl.januaryewakasia.plxpodlasie.exception.NotFoundException;
import pl.januaryewakasia.plxpodlasie.model.User;
import pl.januaryewakasia.plxpodlasie.repository.UserRepository;
import pl.januaryewakasia.plxpodlasie.request.AuthenticationRequest;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User getCurrentUser() throws AuthenticationServiceException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user;
        }
        throw new AuthenticationServiceException("No user info found. Please log in.");
    }

    public void checkUserCredentialsAvailability(AuthenticationRequest request) throws AuthenticationServiceException {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AuthenticationServiceException("Username is already taken.");
    }

    public User save(AuthenticationRequest authenticationRequest) {
        return userRepository.save(new User(authenticationRequest.getUsername(), authenticationRequest.getEmail(), passwordEncoder.encode(authenticationRequest.getPassword())));
    }

    public Boolean isUserAdmin() {
        return getCurrentUser().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(Role.ADMIN.name()));
    }
}
