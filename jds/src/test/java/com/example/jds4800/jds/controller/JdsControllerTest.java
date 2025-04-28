package com.example.jds4800.jds.controller;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.jds4800.jds.exception.JdsNotFoundException;
import com.example.jds4800.jds.model.Jds;
import com.example.jds4800.jds.service.JdsService;

@SpringBootTest
@AutoConfigureMockMvc
public class JdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JdsService jdsService;

    @Test
    void createJds() throws Exception {
        Jds jds = new Jds();
        jds.setId(1L);
        jds.setTitle("walk");
        jds.setCompleted(false);

        when(jdsService.createJds(any(Jds.class))).thenReturn(jds);

        mockMvc.perform(post("/jds")
                .contentType("application/json")
                .content("{\"title\": \"walk\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("walk"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void getJds() throws Exception {
        Jds jds = new Jds();
        jds.setTitle("coding");

        when(jdsService.getJds(1L)).thenReturn(jds);

        mockMvc.perform(get("/jds/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("coding"));
    }

    @Test
    void getAllJds() throws Exception {
        Jds jds = new Jds();
        jds.setTitle("Running");
        
        when(jdsService.getAllJds()).thenReturn(Collections.singletonList(jds));

        
        mockMvc.perform(get("/jds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Running"));
    }

    @Test
    void updateJds_completedFalse() throws Exception {
        
        Jds jds = new Jds();
        jds.setTitle("Running");
        jds.setCompleted(false);

        Jds updatedJds = new Jds();
        updatedJds.setTitle("Running");
        updatedJds.setCompleted(true);

        when(jdsService.updateJds(eq(1L), any(Jds.class))).thenReturn(updatedJds);

        mockMvc.perform(put("/jds/{id}", 1L)
                .contentType("application/json")
                .content("{\"title\": \"Running\", \"completed\": false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Running"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void updateJds_completedTrue() throws Exception {
        Jds jds = new Jds();
        jds.setTitle("Coding");
        jds.setCompleted(true);

        Jds updatedJds = new Jds();
        updatedJds.setTitle("Coding");
        updatedJds.setCompleted(true);

        when(jdsService.updateJds(eq(1L), any(Jds.class))).thenReturn(updatedJds);

        mockMvc.perform(put("/jds/{id}", 1L)
                .contentType("application/json")
                .content("{\"title\": \"Coding\", \"completed\": false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Coding"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void createJds_emptyTitle_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/jds")
                .contentType("application/json")
                .content("{\"title\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is required"));
    }

    @Test
    void createJds_dueDateInPast_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/jds")
                .contentType("application/json")
                .content("{\"title\": \"Sample\", \"dueDate\": \"2000-01-01\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dueDate").value("Due date cannot be in the past"));
    }
    @Test
    void createJds_titleTooLong_shouldReturnBadRequest() throws Exception {
        String longTitle = "a".repeat(101);
        mockMvc.perform(post("/jds")
                .contentType("application/json")
                .content("{\"title\": \"" + longTitle + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title cannot exceed 100 characters"));
    }

    @Test
    void getJds_notFound_shouldReturn404() throws Exception {
        when(jdsService.getJds(999L)).thenThrow(new JdsNotFoundException("Value not found"));

        mockMvc.perform(get("/jds/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Value not found"));
    }

    @Test
    void deleteJds_notFound_shouldReturn404() throws Exception {
        doThrow(new JdsNotFoundException("Value not found")).when(jdsService).deleteJds(999L);

        mockMvc.perform(delete("/jds/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Value not found"));
    }

    @Test
    void updateJds_nullTitle_shouldReturnBadRequest() throws Exception {
        Jds jds = new Jds();
        jds.setTitle("Coding");
        

        Jds updatedJds = new Jds();
        updatedJds.setTitle(null);
        

        when(jdsService.updateJds(eq(1L), any(Jds.class))).thenReturn(updatedJds);

        mockMvc.perform(put("/jds/{id}", 1L)
                .contentType("application/json")
                .content("{\"title\": null}"))
                .andExpect(status().isBadRequest());
    }


}
