package com.example.springbootwebapp;

import com.example.springbootwebapp.model.User;
import com.example.springbootwebapp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll(); // Clean DB before each test
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        userRepository.save(user);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Test User"));
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user = userRepository.save(user);

        mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User();
        user.setName("Old Name");
        user.setEmail("old@example.com");
        user = userRepository.save(user);

        user.setName("New Name");
        user.setEmail("new@example.com");

        mockMvc.perform(put("/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.email").value("new@example.com"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        User user = new User();
        user.setName("To Be Deleted");
        user.setEmail("delete@example.com");
        user = userRepository.save(user);

        mockMvc.perform(delete("/users/{id}", user.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
