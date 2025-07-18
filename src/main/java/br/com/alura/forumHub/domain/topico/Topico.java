package br.com.alura.forumHub.domain.topico;

import br.com.alura.forumHub.domain.resposta.Resposta;
import br.com.alura.forumHub.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas = new ArrayList<>();




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

    //contagem dinâmica das respostas, não ficando armazenado fisicamente no banco de
    //dados, somente aparece a quantidade de respostas se você fazer a busca detalhada do
    //tópico
    public int getRespostas(){
        return respostas.size();
    }

}
