package br.com.alura.forumHub.domain.topico;

import java.time.LocalDateTime;

public record DadosListagemTopico(
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        String status,
        String usuario,
        String curso
) {
    public DadosListagemTopico(Topico topico){
        this(topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getStatus(),
                topico.getUsuario() != null ? topico.getUsuario().getLogin(): null,
                topico.getCurso());
    }
}
