package com.controleestoque.api_estoque.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.controleestoque.api_estoque.model.Fornecedor;
import com.controleestoque.api_estoque.model.Produto;
import com.controleestoque.api_estoque.repository.ProdutoRepository;
import com.controleestoque.api_estoque.repository.CategoriaRepository;
import com.controleestoque.api_estoque.repository.FornecedorRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;

    // GET /api/produtos
    @GetMapping
    public List<Produto> getAllProdutos() {
        return produtoRepository.findAll();
    }

    // GET /api/produtos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) { // Nome do método corrigido
        return produtoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/produtos
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Produto> createProduto(@RequestBody Produto produto) {

        // 1. Verifica e Seta Categoria
        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            return ResponseEntity.badRequest().body(null); // 400 Bad Request
        }

        produto.getCategoria().setId(produto.getCategoria().getId()); // Garante o ID no objeto Categoria
        categoriaRepository.findById(produto.getCategoria().getId())
                .ifPresent(produto::setCategoria);

        // 2. LIGAÇÃO BIDIRECIONAL DO ESTOQUE (ALTERAÇÃO CRUCIAL)
        // Se o Estoque vier no payload, precisamos ligá-lo de volta ao Produto
        if (produto.getEstoque() != null) {
            // O Produto é o lado 'proprietário' da FK no Estoque (JoinColumn no Estoque)
            produto.getEstoque().setProduto(produto); 
        }

        // 3. Verifica e Seta Fornecedores (Otimizado)
        if (produto.getFornecedores() != null && !produto.getFornecedores().isEmpty()) {
            Set<Fornecedor> fornecedoresEncontrados = new HashSet<>();
            
            // Busca cada fornecedor pelo ID e adiciona à nova coleção
            for (Fornecedor f : produto.getFornecedores()) {
                 fornecedorRepository.findById(f.getId())
                         .ifPresent(fornecedoresEncontrados::add);
            }
            
            // Substitui a coleção com as instâncias gerenciadas pelo JPA
            produto.setFornecedores(fornecedoresEncontrados);
        }

        // 4. Salva o produto
        // Devido ao CascadeType.ALL em Produto->Estoque, salvar o Produto salva o Estoque também.
        Produto savedProduto = produtoRepository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduto);
    }

    // PUT /api/produtos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto( @PathVariable Long id, @RequestBody Produto produtoDetails) {
        
        // Esta implementação básica apenas atualiza o nome. Para um PUT completo, 
        // você precisaria implementar a lógica de atualização da Categoria, Estoque, e Fornecedores.
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setNome(produtoDetails.getNome());
                    // ... (outros campos para atualização, se necessário)
                    
                    Produto updatedProduto = produtoRepository.save(produto);
                    return ResponseEntity.ok(updatedProduto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/produtos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {

        if (!produtoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}