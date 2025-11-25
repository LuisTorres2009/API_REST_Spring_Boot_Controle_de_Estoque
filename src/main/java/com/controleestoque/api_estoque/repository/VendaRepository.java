package com.controleestoque.api_estoque.repository;

import com.controleestoque.api_estoque.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    
}