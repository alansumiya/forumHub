package br.com.alura.forumHub.domain.topico;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizarTopico(
        String titulo,
        String mensagem,
        String curso

) {
}
