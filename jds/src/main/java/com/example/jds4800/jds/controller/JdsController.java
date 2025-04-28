package com.example.jds4800.jds.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jds4800.jds.model.Jds;
import com.example.jds4800.jds.service.JdsService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost")
@RestController
@RequestMapping("/jds")
public class JdsController {

    private final JdsService jdsService;
    // private final ObjectMapper objectMapper;

    @Autowired
    public JdsController(JdsService jdsService) {
        this.jdsService = jdsService;
        
    }

     @PostMapping
    public ResponseEntity<Jds> createJds(@Valid @RequestBody Jds jds) {
        Jds createdJds = jdsService.createJds(jds);
        return ResponseEntity.status(201).body(createdJds);
    }

    @GetMapping
    public List<Jds> getAllJds() {
        return jdsService.getAllJds();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jds> getJds(@PathVariable Long id) {
        Jds jds = jdsService.getJds(id);
        if (jds != null) {
            return ResponseEntity.ok(jds);
        }
        return ResponseEntity.notFound().build();
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Jds> updateJds(@PathVariable Long id,@Valid @RequestBody Jds jds) {
        jds.setId(id);
        Jds updatedJds = jdsService.updateJds(id, jds);
        
        if (updatedJds != null) {
            System.out.println("Not null");
            return ResponseEntity.ok(updatedJds);
        }
        return ResponseEntity.notFound().build(); 
    }

    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJds(@PathVariable Long id) {
        jdsService.deleteJds(id);
        return ResponseEntity.noContent().build();
    }
    
    
}
