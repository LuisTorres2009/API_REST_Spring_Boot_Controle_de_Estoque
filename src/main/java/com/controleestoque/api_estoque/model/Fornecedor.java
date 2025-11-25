package com.controleestoque.api_estoque.model;

import jakarta.persistence.*;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tb_fornecedores")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // --- Relacionamento N:M (Many-to-Many) ---
    // Mapeamento: Lado não dominante relacionado em Produto.
    // Produto que indica que o mapeamento da tabela de junção está na classe Produto.
    @ManyToMany(mappedBy = "fornecedores")
    @JsonIgnore
    private Set<Produto> produtos;

    // --- Construtores, Getters e Setters ---
    public Fornecedor() {}

    public Fornecedor(String nome, Set<Produto> produtos) {
        this.nome = nome;
        this.produtos = produtos;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Set<Produto> getProdutos() { return produtos; }
    public void setProdutos(Set<Produto> produtos) { this.produtos = produtos; }
}