package com.controleestoque.api_estoque.service;

import com.controleestoque.api_estoque.model.*;
import com.controleestoque.api_estoque.repository.*;
import com.controleestoque.api_estoque.exception.EstoqueInsuficienteException;
import com.controleestoque.api_estoque.dto.VendaRequestDTO; // Vamos criar este DTO
import com.controleestoque.api_estoque.dto.ItemVendaDTO; // Vamos criar este DTO
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository; // Usaremos para salvar a baixa

    @Transactional // GARANTE O ROLLBACK EM CASO DE EXCEÇÃO!
    public Venda registrarVenda(VendaRequestDTO vendaRequestDTO) {

        // 1. Busca o Cliente
        Cliente cliente = clienteRepository.findById(vendaRequestDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        // 2. Cria a Venda (Cabeçalho)
        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setItens(new HashSet<>());

        // 3. Processa e VAlida cada Item
        Set<ItemVenda> itensVenda = new HashSet<>();

        for (ItemVendaDTO itemDto : vendaRequestDTO.getItens()) {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemDto.getProdutoId()));

            // A. VERIFICAÇÃO CRÍTICA DE ESTOQUE
            Estoque estoque = produto.getEstoque();
            if (estoque == null || estoque.getQuantidade() < itemDto.getQuantidade()) {
                // Se o estoque for insuficiente, lança exceção -> ROLLBACK
                throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para o produto: " + produto.getNome() + 
                    ". Disponível: " + (estoque != null ? estoque.getQuantidade() : 0)
                );
            }

            // B. CRIAÇÃO DO ITEM DE VENDA
            ItemVenda itemVenda = new ItemVenda();
            itemVenda.setVenda(venda); // Liga o item à venda que está sendo criada
            itemVenda.setProduto(produto);
            itemVenda.setQuantidadeVendida(itemDto.getQuantidade());
            itemVenda.setPrecoUnitario(produto.getPreco()); // Pega o preço atual do produto

            itensVenda.add(itemVenda);

            // C. BAIXA NO ESTOQUE
            estoque.setQuantidade(estoque.getQuantidade() - itemDto.getQuantidade());
            estoqueRepository.save(estoque); // Salva a alteração no estoque
        }

        // 4. Finaliza a Venda
        venda.setItens(itensVenda);

        return vendaRepository.save(venda);
    }
}