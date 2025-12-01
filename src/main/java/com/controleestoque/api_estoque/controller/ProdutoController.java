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
        // CORREÇÃO: Usando JOIN FETCH para carregar a categoria na listagem (resolve o null)
        return produtoRepository.findAllWithCategoria();
    }

    // GET /api/produtos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
        // CORREÇÃO: Usando JOIN FETCH para carregar a categoria no detalhe (resolve o null)
        return produtoRepository.findByIdWithCategoria(id)
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

        produto.getCategoria().setId(produto.getCategoria().getId());
        categoriaRepository.findById(produto.getCategoria().getId())
                .ifPresent(produto::setCategoria);

        // 2. LIGAÇÃO BIDIRECIONAL DO ESTOQUE
        if (produto.getEstoque() != null) {
            produto.getEstoque().setProduto(produto); 
        }

        // 3. Verifica e Seta Fornecedores (Otimizado)
        if (produto.getFornecedores() != null && !produto.getFornecedores().isEmpty()) {
            Set<Fornecedor> fornecedoresEncontrados = new HashSet<>();
            
            for (Fornecedor f : produto.getFornecedores()) {
                 fornecedorRepository.findById(f.getId())
                         .ifPresent(fornecedoresEncontrados::add);
            }
            
            produto.setFornecedores(fornecedoresEncontrados);
        }

        // 4. Salva o produto
        Produto savedProduto = produtoRepository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduto);
    }

    // PUT /api/produtos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto( @PathVariable Long id, @RequestBody Produto produtoDetails) {
        
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
