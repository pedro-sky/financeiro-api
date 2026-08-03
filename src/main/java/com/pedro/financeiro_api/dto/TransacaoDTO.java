package com.pedro.financeiro_api.dto;

import com.pedro.financeiro_api.model.Transacao;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransacaoDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "Descrição é obrigatória")
        @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
        private String descricao;

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        private BigDecimal valor;

        @NotNull(message = "Data é obrigatória")
        private LocalDate data;

        @NotNull(message = "Tipo é obrigatório")
        private Transacao.Tipo tipo;

        private Long categoriaId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String descricao;
        private BigDecimal valor;
        private LocalDate data;
        private String tipo;
        private CategoriaDTO.Response categoria;
    }
}