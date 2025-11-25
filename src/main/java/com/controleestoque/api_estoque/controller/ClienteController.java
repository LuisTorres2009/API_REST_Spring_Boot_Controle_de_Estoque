package com.controleestoque.api_estoque.controller;

import com.controleestoque.api_estoque.model.Cliente;
import com.controleestoque.api_estoque.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteRepository clienteRepository;

    // 1. GET /api/clientes (Listar todos)
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    // 2. GET /api/clientes/{id} (Buscar por ID)
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        // Tenta encontrar o cliente; se encontrar, retorna 200 OK, senão 404 Not Found.
        return clienteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. POST /api/clientes (Criar novo cliente)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Retorna código 201 (Created)
    public Cliente createCliente(@RequestBody Cliente cliente) {
        // Salva e retorna o cliente criado.
        return clienteRepository.save(cliente);
    }

    // 4. PUT /api/clientes/{id} (Atualizar cliente existente)
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(
            @PathVariable Long id, @RequestBody Cliente clienteDetails) {

        return clienteRepository.findById(id)
                .map(cliente -> {
                    // Atualiza apenas os campos permitidos (nome, email)
                    cliente.setNome(clienteDetails.getNome());
                    // Adicione mais campos conforme necessário, por exemplo:
                    cliente.setEmail(clienteDetails.getEmail()); 
                    
                    Cliente updatedCliente = clienteRepository.save(cliente);
                    return ResponseEntity.ok(updatedCliente); // Retorna 200 OK
                })
                .orElse(ResponseEntity.notFound().build()); // Retorna 404 Not Found
    }

    // 5. DELETE /api/clientes/{id} (Excluir cliente)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        
        // Verifica se o cliente existe antes de tentar deletar
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found
        }

        clienteRepository.deleteById(id);
        // Retorna 204 No Content, que é o padrão para deleção bem-sucedida
        return ResponseEntity.noContent().build(); 
    }
}