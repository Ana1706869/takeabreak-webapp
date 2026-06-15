package com.takeabreak.web.model;

import java.time.LocalDate;

public record Funcionario(
        Long funcionarioId,
        String nome,
        String email,
        String password,
        String departamento,
        Integer escalao,
        String role,
        LocalDate dataAdmissao,
        String morada,
        String codigoPostal,
        String localidade,
        String concelho,
        String distrito,
        String telefone
) {
}
