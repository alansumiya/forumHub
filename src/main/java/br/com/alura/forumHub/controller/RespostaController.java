package br.com.alura.forumHub.controller;

import br.com.alura.forumHub.domain.resposta.*;
import br.com.alura.forumHub.domain.topico.DadosAtualizarTopico;
import br.com.alura.forumHub.domain.topico.TopicoRepository;
import br.com.alura.forumHub.domain.usuario.Usuario;
import br.com.alura.forumHub.domain.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/respostas")
public class RespostaController {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroResposta dados, UriComponentsBuilder uriBuilder){
        var topico = topicoRepository.findById(dados.idTopico())
                .orElseThrow(()->new EntityNotFoundException("Tópico não encontrado"));

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Usuario usuarioLogado)) {
            throw new RuntimeException("Usuário não autenticado ou tipo de principal inválido.");
        }
        var resposta = new Resposta(dados.mensagem(), topico, usuarioLogado);
        respostaRepository.save(resposta);
        var uri = uriBuilder.path("/respostas/{id}").buildAndExpand(resposta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoResposta(resposta));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemResposta>> listarTodos(@PageableDefault(size = 10, sort = {"dataCriacao"}, direction = Sort.Direction.ASC)Pageable paginacao){
        var page = respostaRepository.findAll(paginacao).map(DadosListagemResposta::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/topico/{idTopico}")
    public ResponseEntity<List<DadosListagemResposta>> listarPorTopico(@PathVariable Long idTopico){
        var respostas = respostaRepository.findByTopicoId(idTopico)
                .stream()
                .map(DadosListagemResposta::new)
                .toList();
        return ResponseEntity.ok(respostas);
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizarResposta dados){
        var respostaOptional = respostaRepository.findById(id);
        if(respostaOptional.isEmpty()){
            throw new EntityNotFoundException("Resposta com ID "+ id + " não encontrado.");
        }
        var resposta = respostaOptional.get();

        resposta.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoResposta(resposta));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        var resposta = respostaRepository.findById(id);

        if (resposta.isEmpty()){
            throw new EntityNotFoundException("Resposta com ID "+ id + " não encontrado.");
        }
        respostaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }



}
