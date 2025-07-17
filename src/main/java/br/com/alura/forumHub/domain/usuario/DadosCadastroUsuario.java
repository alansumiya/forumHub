package br.com.alura.forumHub.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroUsuario(
        @NotBlank
        String login,
        @NotBlank
        String email,
        @NotBlank
        String senha,
        @NotBlank
        String perfis
) {
}
