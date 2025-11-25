package com.controleestoque.api_estoque.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class VendaRequestDTO {
    private Long clienteId;
    private List<ItemVendaDTO> itens;
}