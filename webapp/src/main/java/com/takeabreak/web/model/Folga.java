package com.takeabreak.web.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Folga(
        Long folgaId,
        Long funcionarioId,
        String funcionarioNome,
        LocalDate dataPedido,
        LocalDate dataInicio,
        LocalDate dataFim,
        String motivo,
        String estado,
        BigDecimal remuneracao
) {
}
