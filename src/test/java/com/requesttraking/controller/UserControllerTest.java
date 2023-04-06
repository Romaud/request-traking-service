package com.requesttraking.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.requesttraking.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql(value = "/script/before-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/script/after-test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper;

    public UserControllerTest() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAllUsers() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<User> requests = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(requests);
        assertEquals(4, requests.size());
        assertEquals("admin", requests.get(0).getUsername());
        assertEquals("operator", requests.get(1).getUsername());
        assertEquals("user", requests.get(2).getUsername());
    }

    @Test
    @WithMockUser
    void getAllUsersForRoleUser() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"OPERATOR"})
    void getAllUsersForRoleOperator() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void searchUsersForRoleAdmin() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/users/search")
                        .param("filter", "user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<User> requests = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(requests);
        assertEquals(2, requests.size());
        assertEquals("user", requests.get(0).getUsername());
        assertEquals("new user", requests.get(1).getUsername());
    }

    @Test
    @WithMockUser(roles = {"OPERATOR"})
    void searchUsersForRoleOperator() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/users/search")
                        .param("filter", "user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<User> requests = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(requests);
        assertEquals(2, requests.size());
        assertEquals("user", requests.get(0).getUsername());
        assertEquals("new user", requests.get(1).getUsername());
    }

    @Test
    @WithMockUser
    void searchUsersForRoleUser() throws Exception {
        mockMvc.perform(get("/api/users/search")
                        .param("filter", "user"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void update() throws Exception {
        mockMvc.perform(put("/api/users/3")).andExpect(status().isOk())
                .andExpect(jsonPath("$.roles.length()", is(2)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateNonExistUser() throws Exception {
        mockMvc.perform(put("/api/users/5")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"OPERATOR", "USER"})
    void updateForRoleOperatorAndUser() throws Exception {
        mockMvc.perform(put("/api/users/3")).andExpect(status().isForbidden());
    }
}