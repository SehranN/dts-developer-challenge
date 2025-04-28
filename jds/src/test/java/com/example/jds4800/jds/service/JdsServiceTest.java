package com.example.jds4800.jds.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.jds4800.jds.model.Jds;
import com.example.jds4800.jds.repository.JdsRepository;

public class JdsServiceTest {

    @Mock
    private JdsRepository jdsRepository;

    @InjectMocks
    private JdsService jdsService;

    public JdsServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllJds() {
        Jds jds1 = new Jds();
        jds1.setTitle("Running");

        Jds jds2 = new Jds();
        jds2.setTitle("Bed time");

        when(jdsRepository.findAll()).thenReturn(Arrays.asList(jds1, jds2));
        
        List<Jds> data = jdsService.getAllJds();

        assertEquals(2, data.size());
        assertEquals("Running", data.get(0).getTitle());
    }

    @Test
    void getJdsById() {
        Jds jds = new Jds();
        jds.setId(1L);
        jds.setTitle("Walk");

        when(jdsRepository.findById(1L)).thenReturn(Optional.of(jds));

        Jds data = jdsService.getJds(1L);

        assertEquals("Walk", data.getTitle());
    }

    @Test
    void updateJds() {
        Jds jds = new Jds();
        jds.setId(1L);
        jds.setCompleted(false);
     

        when(jdsRepository.findById(1L)).thenReturn(Optional.of(jds));
        when(jdsRepository.save(any(Jds.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Jds jdsUpdate = new Jds();
        jdsUpdate.setId(1L);
        jdsUpdate.setCompleted(true);

        when(jdsRepository.existsById(1L)).thenReturn(true);

        Jds update = jdsService.updateJds(1L, jdsUpdate);
        assertEquals(true, update.isCompleted());
        verify(jdsRepository).save(any(Jds.class));
    }
    
}
