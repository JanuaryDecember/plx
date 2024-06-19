package pl.januaryewakasia.plxpodlasie.unit;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import pl.januaryewakasia.plxpodlasie.model.User;
import pl.januaryewakasia.plxpodlasie.repository.UserRepository;
import pl.januaryewakasia.plxpodlasie.request.AuthenticationRequest;
import pl.januaryewakasia.plxpodlasie.service.infrastructure.UserService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCredentialsAvailability1() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("username");
        request.setPassword("password");
        request.setEmail("email");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);

        assertDoesNotThrow(() -> userService.checkUserCredentialsAvailability(request));
    }

    @Test
    public void testCredentialsAvailability2() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("username");
        request.setPassword("password");
        request.setEmail("email");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        assertThrows(AuthenticationServiceException.class, () -> userService.checkUserCredentialsAvailability(request));
    }

    @Test
    public void testGetCurrentUser() {
        User mockUser = new User("username", "password", "lala@la.pl");
        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUser, "password");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User currentUser = userService.getCurrentUser();

        assertNotNull(currentUser);
        assertEquals("username", currentUser.getUsername());
    }

    @Test
    public void testGetCurrentUser2() {
        assertThrows(AuthenticationServiceException.class, () -> userService.getCurrentUser());
    }

    @Test
    public void testGetUserById1() {
        User user = new User();
        user.setUsername("test");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertEquals(userService.getUserById(1L).getUsername(), user.getUsername());
    }

}
