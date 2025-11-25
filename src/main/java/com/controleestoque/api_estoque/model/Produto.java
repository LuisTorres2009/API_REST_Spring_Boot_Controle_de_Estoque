package com.controleestoque.api_estoque.model;

import java.math.BigDecimal;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private BigDecimal preco;

    // Relacionamento 1:1 com Estoque
    @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Estoque estoque;

    // Relacionamento N:1 com Categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Relacionamento N:M com Fornecedores
    @ManyToMany
    @JoinTable(
        name = "tb_produto_fornecedor",
        joinColumns = @JoinColumn(name = "produto_id"),
        inverseJoinColumns = @JoinColumn(name = "fornecedor_id")
    )
    private Set<Fornecedor> fornecedores;

    // NOVO: Relacionamento 1:n com ItensVenda
    @OneToMany(mappedBy = "produto")
    @JsonIgnore
    private Set<ItemVenda> itensVenda; // É importante inicializar esta coleção para evitar NullPointerException

    // Construtores
    public Produto(){}

    // Construtor completo (Atualizado para incluir itensVenda)
    public Produto(String nome, BigDecimal preco, Estoque estoque, Categoria categoria,
                   Set<Fornecedor> fornecedores, Set<ItemVenda> itensVenda) {
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.categoria = categoria;
        this.fornecedores = fornecedores;
        this.itensVenda = itensVenda; // Novo campo
    }

    // Getters e Setters (Certifique-se de que estes estejam incluídos)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public Estoque getEstoque() { return estoque; }
    public void setEstoque(Estoque estoque) { this.estoque = estoque; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public Set<Fornecedor> getFornecedores() { return fornecedores; }
    public void setFornecedores(Set<Fornecedor> fornecedores) { this.fornecedores = fornecedores;}
    
    // Getter e Setter para o novo campo
    public Set<ItemVenda> getItensVenda() { return itensVenda; }
    public void setItensVenda(Set<ItemVenda> itensVenda) { this.itensVenda = itensVenda;} 
}