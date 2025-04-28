package com.example.jds4800.jds.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.jds4800.jds.model.Jds;

@Repository
public interface JdsRepository extends CrudRepository<Jds, Long> {
    
}
