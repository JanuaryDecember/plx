package pl.januaryewakasia.plxpodlasie.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.januaryewakasia.plxpodlasie.request.AuthenticationRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLoginGoodCreds() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("january");
        request.setPassword("Janek111");

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk()).andReturn();
        assert (res.getResponse().getCookies().length > 0);
        assert (Arrays.stream(res.getResponse().getCookies()).anyMatch(c -> c.getName().contains("SESSION")));
    }

    @Test
    public void testLoginBadCreds() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("january");
        request.setPassword("badpasswd");

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/logout")).andExpect(status().isOk());
    }
}
