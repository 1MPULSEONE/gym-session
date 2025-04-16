package com.lovejazz.gymsession;

import com.lovejazz.gymsession.config.WebSecurityConfig;
import com.lovejazz.gymsession.controller.TestController;
import com.lovejazz.gymsession.converters.JwtAuthConverter;
import com.lovejazz.gymsession.security.jwt.AuthEntryPointJwt;
import com.lovejazz.gymsession.security.jwt.JwtUtils;
import com.lovejazz.gymsession.security.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestController.class)
@Import({WebSecurityConfig.class, TestControllerTests.TestSecurityConfiguration.class})
public class TestControllerTests {

    @TestConfiguration
    static class TestSecurityConfiguration {
        @Bean
        public UserDetailsServiceImpl userDetailsService() {
            return Mockito.mock(UserDetailsServiceImpl.class);
        }

        @Bean
        public AuthEntryPointJwt authEntryPointJwt() {
            return Mockito.mock(AuthEntryPointJwt.class);
        }

        @Bean
        public JwtUtils jwtUtils() {
            return Mockito.mock(JwtUtils.class);
        }

        @Bean
        public JwtAuthConverter jwtAuthConverter() {
            return Mockito.mock(JwtAuthConverter.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenGetAll_thenReturnsOkAndPublicContent() throws Exception {
        mockMvc.perform(get("/api/test/all"))
                .andExpect(status().isOk())
                .andExpect(content().string("Public Content."));
    }

    @Test
    void whenGetUserWithoutAuth_thenReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenGetModWithoutAuth_thenReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/test/mod"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenGetAdminWithoutAuth_thenReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isUnauthorized());
    }

    /*
    @Test
    @WithMockUser(roles = "client_user")
    void whenGetUserWithUserRole_thenReturnsOk() throws Exception {
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isOk())
                .andExpect(content().string("User Content."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenGetAdminWithAdminRole_thenReturnsOk() throws Exception {
        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin Board."));
    }
    */
}
