package br.com.alura.forumHub.domain.usuario;

public record DadosDetalhamentoUsuario(
        Long id,
        String login,
        String email,
        String perfis
) {

    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getId(),
                usuario.getLogin(),
                usuario.getEmail(),
                usuario.getPerfis());
    }


}
