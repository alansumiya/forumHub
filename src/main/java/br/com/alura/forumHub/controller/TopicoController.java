package br.com.alura.forumHub.controller;

import br.com.alura.forumHub.infra.exception.ValidacaoException;
import br.com.alura.forumHub.topico.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
public class TopicoController {
    @Autowired
    private TopicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroTopico dados, UriComponentsBuilder uriBuilder){
        if(repository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())){
            throw new ValidacaoException("Já existe um tópico com o mesmo título e mensagem.");
        }

        var topico = new Topico(dados);
        repository.save(topico);
        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoTopico(topico));

    }

    //opcional 1
    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(@PageableDefault(size = 10, sort = {"dataCriacao"}, direction = Sort.Direction.ASC) Pageable paginacao){
        var page = repository.findAll(paginacao).map(DadosListagemTopico::new);
        return ResponseEntity.ok(page);
    }

    //opcional 2
    @GetMapping("/por-curso-e-ano")
    public ResponseEntity<Page<DadosListagemTopico>> listarPorCursoEAno(
            @RequestParam String nomeCurso,
            @RequestParam int ano,
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.ASC) Pageable paginacao
    ){
        var page = repository.buscarPorCursoEAno(nomeCurso, ano, paginacao).map(DadosListagemTopico::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity detalhar(@PathVariable Long id){
        var topico = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico com ID "+ id + " não encontrado"));
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizarTopico dados){
        var topicoOptional = repository.findById(id);
        if (topicoOptional.isEmpty()){
            throw new EntityNotFoundException("Tópico com ID "+ id + " não encontrado.");
        }

        var topico = topicoOptional.get();

        if(dados.titulo() != null && dados.mensagem() != null){
            if (repository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())){
                throw new ValidacaoException("Já existe um tópico com o mesmo título e mensagem.");
            }
        }
        topico.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

}
