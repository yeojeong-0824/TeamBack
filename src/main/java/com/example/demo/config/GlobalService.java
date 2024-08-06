package com.example.demo.config;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

// T: DTO
// S: Id Type
public interface GlobalService<T, S> {
    void save(T data);

    Page<T> findAll();

    T findById(S id);

    void deleteById(S id);

    void updateById(T data, S id);
}
