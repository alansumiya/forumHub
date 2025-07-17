package br.com.alura.forumHub.domain.topico;

import br.com.alura.forumHub.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Topico")
@Table(name = "topico", uniqueConstraints = @UniqueConstraint(columnNames = {"titulo", "mensagem"}))
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataCriacao;
    private String status;
    private String curso;
    private int respostas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;




    public Topico(DadosCadastroTopico dados, Usuario usuario) {
        this.titulo = dados.titulo();
        this.mensagem = dados.mensagem();
        this.curso = dados.curso();
        this.dataCriacao = LocalDateTime.now();
        this.status = "Ativo";
        this.usuario = usuario;
    }

    public void atualizarInformacoes(@Valid DadosAtualizarTopico dados){
        if (dados.titulo() != null){
            this.titulo = dados.titulo();
        }
        if(dados.mensagem() != null){
            this.mensagem = dados.mensagem();
        }
        if(dados.curso() != null){
            this.curso = dados.curso();
        }
    }
}
