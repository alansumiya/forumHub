package br.com.alura.forumHub.domain.usuario;

public record DadosListagemUsuario(
        Long id,
        String login,
        String email,
        String perfis
) {

    public DadosListagemUsuario(Usuario usuario){
        this(usuario.getId(),
                usuario.getLogin(),
                usuario.getEmail(),
                usuario.getPerfis());
    }
}
