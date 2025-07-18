package br.com.alura.forumHub.controller;

import br.com.alura.forumHub.domain.usuario.*;
import br.com.alura.forumHub.infra.exception.ValidacaoException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder){
        if (repository.existsByLogin(dados.login())){
            throw new ValidacaoException("Já existe um usuário com esse login.");
        }
        String senhaHash = passwordEncoder.encode(dados.senha());
        var usuario = new Usuario(dados, senhaHash);
        repository.save(usuario);
        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoUsuario(usuario));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>> listar(@PageableDefault(size = 10, sort = {"login"}, direction = Sort.Direction.ASC) Pageable paginacao){
        var page = repository.findAll(paginacao).map(DadosListagemUsuario::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity detalhar(@PathVariable Long id){
        var usuario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com ID "+ id + " não encontrado"));
        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizarUsuario dados){
        var usuarioOptional = repository.findById(id);
        if (usuarioOptional.isEmpty()){
            throw new EntityNotFoundException("Usuário com ID "+ id + " não encontrado.");
        }
        var usuario = usuarioOptional.get();

        String senhaHash = null;
        if (dados.senha() != null && !dados.senha().isBlank()){
            senhaHash = passwordEncoder.encode(dados.senha());
        }
        usuario.atualizarInformacoes(dados, senhaHash);
        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        var usuario = repository.findById(id);

        if (usuario.isEmpty()){
            throw new EntityNotFoundException("Usuário com ID "+ id + " não encontrado.");
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
