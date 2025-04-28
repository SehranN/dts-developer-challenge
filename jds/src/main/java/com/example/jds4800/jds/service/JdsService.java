package com.example.jds4800.jds.service;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jds4800.jds.exception.JdsNotFoundException;
import com.example.jds4800.jds.model.Jds;
import com.example.jds4800.jds.repository.JdsRepository;

@Service
public class JdsService {
        private final JdsRepository jdsRepository;

    @Autowired
    public JdsService(JdsRepository jdsRepository) {
        this.jdsRepository = jdsRepository;
    }

    public Jds createJds(Jds jds) {
        jds.setTitle(sanitize(jds.getTitle()));
        return jdsRepository.save(jds);
    }

    public List<Jds> getAllJds() {
        return (List<Jds>) jdsRepository.findAll();
    }

    public Jds getJds(Long id) {
        return jdsRepository.findById(id).orElseThrow(() -> new JdsNotFoundException("Value not found"));
    }

    public Jds updateJds(Long id, Jds newJdsData) {
        return jdsRepository.findById(id).map(jds -> {
            jds.setTitle(sanitize(newJdsData.getTitle()));
            jds.setCompleted(newJdsData.isCompleted());
            jds.setDueDate(newJdsData.getDueDate());
            return jdsRepository.save(jds);
        }).orElse(null);
    }

    public void deleteJds(Long id) {
        if (!jdsRepository.existsById(id)) {
            throw new JdsNotFoundException("Value not found");
        }
        jdsRepository.deleteById(id);
    }

    private String sanitize(String input) {
        return StringEscapeUtils.escapeHtml4(input);
    }
    
}
