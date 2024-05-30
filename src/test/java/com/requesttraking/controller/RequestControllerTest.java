package com.requesttraking.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.requesttraking.dto.CreateRqDto;
import com.requesttraking.dto.UpdateRqDto;
import com.requesttraking.entity.Request;
import com.requesttraking.entity.common.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql(value = "/script/before-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/script/after-test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureMockMvc
class RequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(username = "operator", password = "operator", roles = {"OPERATOR"})
    void getSubmittedRequestsWithFullParams() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/requests")
                        .param("page", "0")
                        .param("size", "5")
                        .param("direction", "desc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Request> requests = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(requests);
        assertEquals(3, requests.size());
        assertEquals(Status.SENT, requests.get(0).getStatus());
        assertEquals(Status.SENT, requests.get(1).getStatus());
    }

    @Test
    @WithMockUser(roles = {"OPERATOR"})
    void getSubmittedRequestsWithoutParams() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/requests"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Request> requests = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(requests);
        assertEquals(3, requests.size());
        assertEquals(Status.SENT, requests.get(0).getStatus());
        assertEquals(Status.SENT, requests.get(1).getStatus());
    }

    @Test
    @WithMockUser
    void getSubmittedRequestsWithUserRole() throws Exception {
        mockMvc.perform(get("/api/requests"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getSubmittedRequestsWithAdminRole() throws Exception {
        mockMvc.perform(get("/api/requests"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"OPERATOR"})
    void getSubmittedUserRequestsWithFullParams() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/requests/filter")
                        .param("page", "0")
                        .param("size", "5")
                        .param("direction", "asc")
                        .param("userId", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Request> requests = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(requests);
        assertEquals(2, requests.size());
        assertEquals(Status.SENT, requests.get(0).getStatus());
        assertEquals(Status.SENT, requests.get(1).getStatus());
    }

    @Test
    @WithMockUser(roles = {"OPERATOR"})
    void getSubmittedUserRequestsWithoutUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/requests/filter")
                        .param("page", "0")
                        .param("size", "5")
                        .param("direction", "asc")
                        .param("userId", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Request> requests = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(requests);
        assertEquals(0, requests.size());
    }

    @Test
    @WithMockUser
    void getSubmittedUserRequestsWithRoleUser() throws Exception {
        mockMvc.perform(get("/api/requests/filter"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getSubmittedUserRequestsWithRoleAdmin() throws Exception {
        mockMvc.perform(get("/api/requests/filter"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user")
    void getUserRequestsWithFullParams() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/requests/user")
                        .param("page", "0")
                        .param("size", "5")
                        .param("direction", "desc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Request> requests = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(requests);
        assertEquals(4, requests.size());
        assertEquals(3, requests.get(0).getUserId());
        assertEquals(3, requests.get(1).getUserId());
    }

    @Test
    @WithMockUser(username = "user")
    void getUserRequestsWithoutParams() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/requests/user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Request> requests = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(requests);
        assertEquals(4, requests.size());
        assertEquals(3, requests.get(0).getUserId());
        assertEquals(3, requests.get(1).getUserId());
    }

    @Test
    @WithMockUser(username = "another user")
    void getUserRequestsWithNonExistentUser() throws Exception {
        mockMvc.perform(get("/api/requests/user")
                        .param("page", "0")
                        .param("size", "5")
                        .param("direction", "desc"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getUserRequestsWithRoleAdmin() throws Exception {
        mockMvc.perform(get("/api/requests/user"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "operator", roles = {"OPERATOR"})
    void getUserRequestsWithRoleOperator() throws Exception {
        mockMvc.perform(get("/api/requests/user"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user")
    void getUserRequestById() throws Exception {
        mockMvc.perform(get("/api/requests/user/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user")
    void getAnotherUserRequestById() throws Exception {
        mockMvc.perform(get("/api/requests/user/3"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "operator", roles = {"OPERATOR"})
    void getByIdForOperator() throws Exception {
        mockMvc.perform(get("/api/requests/2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "operator", roles = {"OPERATOR"})
    void getByIdForOperatorWithStatusDraft() throws Exception {
        mockMvc.perform(get("/api/requests/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user")
    void createRequest() throws Exception {
        CreateRqDto request = new CreateRqDto("Test Request", null, 3L);

        mockMvc.perform(post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(3)))
                .andExpect(jsonPath("$.text", is("Test Request")));
    }

    @Test
    @WithMockUser(username = "user")
    void editRequest() throws Exception {
        UpdateRqDto request = new UpdateRqDto(1L);
        request.setText("Test Request");
        request.setAssigneeId(null);
        request.setId(3L);

        mockMvc.perform(put("/api/requests/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(3)))
                .andExpect(jsonPath("$.text", is("Test Request")));
    }

    @Test
    @WithMockUser(username = "user")
    void sendRequest() throws Exception {
        mockMvc.perform(put("/api/requests/send/4")).andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(3)))
                .andExpect(jsonPath("$.text", is("random text")))
                .andExpect(jsonPath("$.status", is("SENT")));
    }

    @Test
    @WithMockUser(username = "user")
    void sendRequestForIncorrectStatus() throws Exception {
        mockMvc.perform(put("/api/requests/send/2")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "operator", roles = {"OPERATOR"})
    void acceptRequest() throws Exception {
        mockMvc.perform(put("/api/requests/accept/5")).andExpect(status().isOk())
                .andExpect(jsonPath("$.assigneeId", is(2)))
                .andExpect(jsonPath("$.userId", is(3)))
                .andExpect(jsonPath("$.text", is("random text")))
                .andExpect(jsonPath("$.status", is(Status.ACCEPTED.name())));
    }

    @Test
    @WithMockUser(username = "operator", roles = {"OPERATOR"})
    void acceptRequestForIncorrectStatus() throws Exception {
        mockMvc.perform(put("/api/requests/accept/1")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void acceptRequestForRoleAdmin() throws Exception {
        mockMvc.perform(put("/api/requests/accept/1")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user")
    void acceptRequestForRoleUser() throws Exception {
        mockMvc.perform(put("/api/requests/accept/1")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "operator", roles = {"OPERATOR"})
    void rejectRequest() throws Exception {
        mockMvc.perform(put("/api/requests/reject/5")).andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(3)))
                .andExpect(jsonPath("$.text", is("random text")))
                .andExpect(jsonPath("$.status", is(Status.REJECTED.name())));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void rejectRequestForRoleAdmin() throws Exception {
        mockMvc.perform(put("/api/requests/reject/5")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user")
    void rejectRequestForRoleUser() throws Exception {
        mockMvc.perform(put("/api/requests/reject/5")).andExpect(status().isForbidden());
    }

}