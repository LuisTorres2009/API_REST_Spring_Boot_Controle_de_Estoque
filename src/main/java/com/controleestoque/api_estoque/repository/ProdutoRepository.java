package com.controleestoque.api_estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.controleestoque.api_estoque.model.Produto;
import java.util.Optional;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{
    @Query("SELECT p FROM Produto p JOIN FETCH p.categoria WHERE p.id = :id")
    Optional<Produto> findByIdWithCategoria(Long id);
    
    @Query("SELECT p FROM Produto p JOIN FETCH p.categoria")
    List<Produto> findAllWithCategoria();
}
