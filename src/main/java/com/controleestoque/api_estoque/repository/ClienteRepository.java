package com.controleestoque.api_estoque.repository;

import com.controleestoque.api_estoque.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
}