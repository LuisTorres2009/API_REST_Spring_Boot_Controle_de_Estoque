package com.controleestoque.api_estoque.controller;

import com.controleestoque.api_estoque.dto.VendaRequestDTO;
import com.controleestoque.api_estoque.model.Venda;
import com.controleestoque.api_estoque.service.VendaService;
import com.controleestoque.api_estoque.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;
    private final VendaRepository vendaRepository; // Para consulta

    // POST /api/vendas (Endpoint de Registro de Venda)
    @PostMapping
    public ResponseEntity<Venda> registrarVenda(@RequestBody VendaRequestDTO vendaRequestDTO) {
        // A lógica de negócio e transação estão no Service
        Venda novaVenda = vendaService.registrarVenda(vendaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaVenda);
    }

    // GET /api/vendas
    @GetMapping
    public List<Venda> getAllVendas() {
        return vendaRepository.findAll();
    }

    // GET /api/vendas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Venda> getVendaById(@PathVariable Long id) {
        return vendaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}