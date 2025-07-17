package br.com.alura.forumHub.domain.topico;

import java.time.LocalDateTime;

public record DadosDetalhamentoTopico(
        Long id,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        String status,
        String usuario,
        String curso,
        int respostas

) {
    public DadosDetalhamentoTopico(Topico topico){
        this(topico.getId(),
        topico.getTitulo(),
        topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getStatus(),
                topico.getUsuario() != null ? topico.getUsuario().getLogin(): null,
                topico.getCurso(),
                topico.getRespostas()
        );
    }
}
