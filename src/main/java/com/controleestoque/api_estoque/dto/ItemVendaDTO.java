package com.controleestoque.api_estoque.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemVendaDTO {
    private Long produtoId;
    private Integer quantidade;
}