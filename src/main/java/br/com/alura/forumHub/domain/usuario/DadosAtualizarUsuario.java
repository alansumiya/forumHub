package br.com.alura.forumHub.domain.usuario;

public record DadosAtualizarUsuario(
        String senha,
        String email,
        String perfis
) {
}
