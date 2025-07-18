package br.com.alura.forumHub.domain.resposta;

import java.time.LocalDateTime;

public record DadosListagemResposta(
        Long id,
        String mensagem,
        LocalDateTime dataCriacao,
        String autor,
        boolean solucao
) {
    public DadosListagemResposta(Resposta resposta){
        this(resposta.getId(),
                resposta.getMensagem(),
                resposta.getDataCriacao(),
                resposta.getAutor().getLogin(),
                resposta.getSolucao());
    }
}
