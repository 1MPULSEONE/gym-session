// package com.lovejazz.gymsession;

// // Импорты для обоих наборов тестов
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.lovejazz.gymsession.controller.AuthController;
// import com.lovejazz.gymsession.controller.TestAuthController;
// import com.lovejazz.gymsession.exception.UserAlreadyExistsException;
// import com.lovejazz.gymsession.payload.request.LoginRequest;
// import com.lovejazz.gymsession.payload.request.LoginViaGoogleRequest;
// import com.lovejazz.gymsession.payload.request.SignUpRequest;
// import com.lovejazz.gymsession.security.jwt.JwtUtils;
// import com.lovejazz.gymsession.service.AuthService;
// import org.junit.jupiter.api.Nested;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.test.web.servlet.MockMvc;

// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// // Основной класс может остаться пустым или содержать общие настройки, если нужно
// public class AuthTests {

//     // Определяем record Password здесь, если он используется в обоих контроллерах
//     // или импортируем его, если он в отдельном файле
//     record Password(String password) {}

//     // Вложенный класс для тестов AuthController
//     @Nested
//     @WebMvcTest(AuthController.class) // Указываем контроллер для этого набора тестов
//     class AuthControllerIntegrationTest {

//         @Autowired
//         private MockMvc mockMvc;

//         @Autowired
//         private ObjectMapper objectMapper;

//         @MockBean
//         private PasswordEncoder passwordEncoder;

//         @MockBean
//         private AuthenticationManager authenticationManager;

//         @MockBean
//         private JwtUtils jwtUtils;

//         @Test
//         void whenRequestEncode_thenReturnsEncodedPassword() throws Exception {
//             // Arrange
//             String rawPassword = "mysecretpassword";
//             String encodedPassword = "encodedMySecretPassword";
//             Password requestBody = new Password(rawPassword);

//             when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);

//             // Act & Assert
//             mockMvc.perform(get("/api/auth/encode")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(objectMapper.writeValueAsString(requestBody)))
//                     .andExpect(status().isOk())
//                     .andExpect(content().string(encodedPassword));
//         }
//     }

//     // Вложенный класс для тестов TestAuthController
//     @Nested
//     @WebMvcTest(TestAuthController.class) // Указываем контроллер для этого набора тестов
//     class TestAuthControllerIntegrationTest {

//         @Autowired
//         private MockMvc mockMvc;

//         @Autowired
//         private ObjectMapper objectMapper;

//         @MockBean
//         private AuthService authService;

//         // --- Тесты для /signin ---
//         @Test
//         void whenValidSignin_thenReturnsOkAndToken() throws Exception {
//             // Arrange
//             LoginRequest loginRequest = LoginRequest.builder()
//                 .username("testuser")
//                 .password("password")
//                 .build();
//             String fakeToken = "fake-jwt-token";
//             when(authService.authenticateAndGetToken("testuser", "password")).thenReturn(fakeToken);

//             // Act & Assert
//             mockMvc.perform(post("/api/keycloak_auth/signin")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(objectMapper.writeValueAsString(loginRequest)))
//                     .andExpect(status().isOk())
//                     .andExpect(cookie().exists("access_token"))
//                     .andExpect(cookie().value("access_token", fakeToken))
//                     .andExpect(cookie().httpOnly("access_token", true))
//                     .andExpect(cookie().path("access_token", "/"))
//                     .andExpect(jsonPath("$.message").value("Authentication successful"))
//                     .andExpect(jsonPath("$.token").value(fakeToken));
//         }

//         @Test
//         void whenSigninFails_thenReturnsUnauthorized() throws Exception {
//             // Arrange
//             LoginRequest loginRequest = new LoginRequest("wronguser", "wrongpass");
//             String errorMessage = "Bad credentials";
//             when(authService.authenticateAndGetToken("wronguser", "wrongpass"))
//                     .thenThrow(new RuntimeException(errorMessage));

//             // Act & Assert
//             mockMvc.perform(post("/api/keycloak_auth/signin")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(objectMapper.writeValueAsString(loginRequest)))
//                     .andExpect(status().isUnauthorized())
//                     .andExpect(jsonPath("$.error").value("Authentication failed"))
//                     .andExpect(jsonPath("$.message").value(errorMessage));
//         }

//         // --- Тесты для /token ---
//         @Test
//         void whenRequestToken_thenReturnsOkAndToken() throws Exception {
//             // Arrange
//             String fakeAdminToken = "fake-admin-jwt-token";
//             when(authService.getUserCreatorToken()).thenReturn(fakeAdminToken);

//             // Act & Assert
//             mockMvc.perform(post("/api/keycloak_auth/token"))
//                     .andExpect(status().isOk())
//                     .andExpect(jsonPath("$.token").value(fakeAdminToken));
//         }

//         @Test
//         void whenRequestTokenFails_thenReturnsUnauthorized() throws Exception {
//             // Arrange
//             String errorMessage = "Client credentials grant failed";
//             when(authService.getUserCreatorToken()).thenThrow(new RuntimeException(errorMessage));

//             // Act & Assert
//             mockMvc.perform(post("/api/keycloak_auth/token"))
//                     .andExpect(status().isUnauthorized())
//                     .andExpect(jsonPath("$.error").value("Authentication failed"))
//                     .andExpect(jsonPath("$.message").value(errorMessage));
//         }

//         // --- Тесты для /signup ---
//         @Test
//         void whenValidSignup_thenReturnsOkAndToken() throws Exception {
//             // Arrange
//             SignUpRequest signUpRequest = new SignUpRequest("newuser", "new@example.com", "New", "User", "password123");
//             String fakeToken = "new-user-fake-token";
//             when(authService.registerAndGetToken(
//                     "newuser", "new@example.com", "New", "User", "password123"))
//                     .thenReturn(fakeToken);

//             // Act & Assert
//             mockMvc.perform(post("/api/keycloak_auth/signup")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(objectMapper.writeValueAsString(signUpRequest)))
//                     .andExpect(status().isOk())
//                     .andExpect(cookie().exists("access_token"))
//                     .andExpect(cookie().value("access_token", fakeToken))
//                     .andExpect(jsonPath("$.message").value("Authentication successful"))
//                     .andExpect(jsonPath("$.token").value(fakeToken));
//         }

//         @Test
//         void whenSignupFails_thenReturnsUnauthorized() throws Exception {
//             // Arrange
//             SignUpRequest signUpRequest = new SignUpRequest("failuser", "fail@example.com", "Fail", "User", "password");
//             String errorMessage = "Registration internal error";
//             when(authService.registerAndGetToken(anyString(), anyString(), anyString(), anyString(), anyString()))
//                     .thenThrow(new RuntimeException(errorMessage));

//             // Act & Assert
//             mockMvc.perform(post("/api/keycloak_auth/signup")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(objectMapper.writeValueAsString(signUpRequest)))
//                     .andExpect(status().isUnauthorized())
//                     .andExpect(jsonPath("$.error").value("Authentication failed"))
//                     .andExpect(jsonPath("$.message").value(errorMessage));
//         }

//         // --- Тесты для /signin-via-google ---
//         @Test
//         void whenValidSigninViaGoogle_thenReturnsOkAndToken() throws Exception {
//             // Arrange
//             LoginViaGoogleRequest googleRequest = new LoginViaGoogleRequest("google-access-token");
//             String fakeToken = "google-exchange-fake-token";
//             when(authService.authenticateViaGoogleAndGetToken("google-access-token")).thenReturn(fakeToken);

//             // Act & Assert
//             mockMvc.perform(post("/api/keycloak_auth/signin-via-google")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(objectMapper.writeValueAsString(googleRequest)))
//                     .andExpect(status().isOk())
//                     .andExpect(cookie().exists("access_token"))
//                     .andExpect(cookie().value("access_token", fakeToken))
//                     .andExpect(jsonPath("$.message").value("Authentication successful or user already existed"))
//                     .andExpect(jsonPath("$.token").value(fakeToken));
//         }

//         @Test
//         void whenSigninViaGoogleUserExists_thenReturnsConflict() throws Exception {
//             // Arrange
//             LoginViaGoogleRequest googleRequest = new LoginViaGoogleRequest("google-token-existing-user");
//             String conflictMessage = "Пользователь с username 'existingGoogleUser' уже существует.";
//             when(authService.authenticateViaGoogleAndGetToken("google-token-existing-user"))
//                     .thenThrow(new UserAlreadyExistsException(conflictMessage));

//             // Act & Assert
//             mockMvc.perform(post("/api/keycloak_auth/signin-via-google")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(objectMapper.writeValueAsString(googleRequest)))
//                     .andExpect(status().isConflict()) // Ожидаем 409 из-за @ExceptionHandler в TestAuthController
//                     .andExpect(jsonPath("$.error").value("Conflict"))
//                     .andExpect(jsonPath("$.message").value(conflictMessage));
//         }

//         @Test
//         void whenSigninViaGoogleFails_thenReturnsInternalServerError() throws Exception {
//             // Arrange
//             LoginViaGoogleRequest googleRequest = new LoginViaGoogleRequest("google-token-fail");
//             String errorMessage = "Some internal processing error";
//             when(authService.authenticateViaGoogleAndGetToken("google-token-fail"))
//                     .thenThrow(new RuntimeException(errorMessage));

//             // Act & Assert
//             mockMvc.perform(post("/api/keycloak_auth/signin-via-google")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(objectMapper.writeValueAsString(googleRequest)))
//                     .andExpect(status().isInternalServerError()) // Ожидаем 500 из-за общего catch в контроллере
//                     .andExpect(jsonPath("$.error").value("Authentication or processing failed"))
//                     .andExpect(jsonPath("$.message").value(errorMessage));
//         }
//     }
// }
