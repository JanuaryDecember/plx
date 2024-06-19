package pl.januaryewakasia.plxpodlasie;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import pl.januaryewakasia.plxpodlasie.model.User;
import pl.januaryewakasia.plxpodlasie.request.AuthenticationRequest;
import pl.januaryewakasia.plxpodlasie.service.infrastructure.AuthenticationService;
import pl.januaryewakasia.plxpodlasie.service.infrastructure.UserService;

import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthenticationServiceTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void testRegister() {

        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("username");
        request.setPassword("password");
        request.setEmail("email");

        doNothing().when(userService).checkUserCredentialsAvailability(request);
        when(userService.save(request)).thenReturn(new User());

        authenticationService.register(request);

        verify(userService, times(1)).checkUserCredentialsAvailability(request);
        verify(userService, times(1)).save(request);
    }

}
