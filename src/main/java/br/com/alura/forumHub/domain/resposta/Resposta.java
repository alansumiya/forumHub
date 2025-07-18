package br.com.alura.forumHub.domain.resposta;

import br.com.alura.forumHub.domain.topico.Topico;
import br.com.alura.forumHub.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Resposta")
@Table(name = "respostas")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    private Topico topico;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario autor;

    private Boolean solucao;

    public Resposta(String mensagem, Topico topico, Usuario autor){
        this.mensagem = mensagem;
        this.topico = topico;
        this.autor = autor;
        this.dataCriacao = LocalDateTime.now();
        this.solucao = false;
    }

    public void atualizarInformacoes(@Valid DadosAtualizarResposta dados){
        if (dados.mensagem() != null){
            this.mensagem = dados.mensagem();
        }
        if(dados.solucao() != null){
            this.solucao = dados.solucao();
        }
    }

    public void marcarComoSolucao(){
        this.solucao = true;
    }

}
