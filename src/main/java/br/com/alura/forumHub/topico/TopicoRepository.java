package br.com.alura.forumHub.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    @Query("""
    SELECT t FROM Topico t
    WHERE t.curso = :nomeCurso AND YEAR(t.dataCriacao) = :ano
    """)
    Page<Topico> buscarPorCursoEAno(@Param("nomeCurso") String nomeCurso, @Param("ano") int ano, Pageable paginacao);
}
