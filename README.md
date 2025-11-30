# 📚 ControleEstoque

Este projeto estende uma API REST de Controle de Estoque (Spring Boot + JPA) para incorporar um módulo de **Vendas** e outro de **Clientes**. A funcionalidade central é a **lógica  de baixa automática do estoque** e o tratamento de erros com *rollback* em caso de insuficiência.

Link do vídeo: https://drive.google.com/drive/folders/1iEr1cU-9HePPi7H_B7dBuqlfi_VsTIWo?hl=pt-br (Veja em 1080p o vídeo para melhor resolucao

---

## ⚙️ Tecnologias Utilizadas

* **Linguagem:** Java 17+
* **Framework:** Spring Boot 
* **Persistência:** Spring Data JPA / Hibernate
* **Ferramenta de Build:** Maven

---

## 🚀 Como Inicializar e Executar

### Pré-requisitos

1.  Java Development Kit (JDK) 17 ou superior.
2.  Apache Maven.
3.  Um cliente REST para testes (Postman, Insomnia, ou cURL).

### Passos de Configuração

1.  **Clone o Repositório:**
    ```bash
    git clone https://github.com/LuisTorres2009/ControleEstoque-20240356
    cd ControleEstoque-20240356
    ```

2.  **Compilar e Rodar a Aplicação:**
    Compile e execute o programa por meio da setinha `▷`  no canto superior direito.

    A aplicação estará acessível em **`http://localhost:8080`**.

---

## 🔬 Roteiro de Testes 

Os testes abaixo devem ser seguidos em ordem para demonstrar todos os requisitos.

### I. Preparação: Categoria, Produto e Cliente

| # | Ação | Método | Endpoint | Body (JSON) | Obs. |
|---|---|---|---|---|---|
| 1 | **Criar Categoria** | `POST` | `/api/categorias` | `{"nome": "Eletrônicos"}` | ID Categoria: **1** |
| 2 | **Criar Produto c/ Estoque** | `POST` | `/api/produtos` | `{"nome": "Iphone 16 Pro Max", "preco": 1999.50, "categoria": {"id": 1}, "estoque": {"quantidade": 10}}` | ID Produto: **1**. Estoque Inicial: **10** |
| 3 | **Criar Cliente** | `POST` | `/api/clientes` | `{"nome": "João da Silva", "email": "joao.silva@exemplo.com"}` | ID Cliente: **1** |

### II. Fluxo de Venda BEM-SUCEDIDA (Baixa Correta)

| # | Ação | Método | Endpoint | Body (JSON) | Verificação |
|---|---|---|---|---|---|
| 4 | **Consulta de Estoque ANTES** | `GET` | `/api/produtos/1` | - | Confirma **10** unidades. |
| 5 | **REGISTRAR VENDA** | `POST` | `/api/vendas` | `{"clienteId": 1, "itens": [{"produtoId": 1, "quantidade": 3}]}` | HTTP **201 Created**. |
| 6 | **Consulta de Estoque DEPOIS** | `GET` | `/api/produtos/1` | - | Confirma **7** unidades. **(Baixa Correta)** |

### III. Fluxo de Venda com FALHA (Rollback Transacional)

| # | Ação | Método | Endpoint | Body (JSON) | Verificação |
|---|---|---|---|---|---|
| 7 | **TENTAR VENDA (Insuficiente)** | `POST` | `/api/vendas` | `{"clienteId": 1, "itens": [{"produtoId": 1, "quantidade": 10}]}` | HTTP **400 Bad Request**. **MENSAGEM DE ERRO** detalhada. |
| 8 | **Consulta de Estoque DEPOIS da Falha** | `GET` | `/api/produtos/1` | - | Confirma que o estoque **PERMANECE 7**. **(Rollback OK)** |

### IV. Consultas Finais (Novas Entidades)

---

| # | Ação | Método | Endpoint | Objetivo |
|---|---|---|---|---|
| 9 | **Listar Clientes** | `GET` | `/api/clientes` | Demonstrar CRUD/Leitura. |
| 10 | **Listar Vendas** | `GET` | `/api/vendas` | Demonstrar o registro da Venda bem-sucedida. |

---

## 👨‍💻 Autor

Projeto desenvolvido por **Luis Ricardo**.

---
