package com.controleestoque.api_estoque.repository;

import com.controleestoque.api_estoque.model.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    
}