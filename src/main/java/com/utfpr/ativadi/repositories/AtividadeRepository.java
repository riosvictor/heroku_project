package com.utfpr.ativadi.repositories;

import com.utfpr.ativadi.entities.Atividade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtividadeRepository extends CrudRepository<Atividade, Long> {
    @Query(value = "SELECT * FROM ativadi.atividade", nativeQuery = true)
    public List<Atividade> findAll();

    @Query(value = "SELECT * FROM ativadi.atividade a WHERE a.id = :id", nativeQuery = true)
    public Optional<Atividade> findById(@Param("id") Long id);

    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM ativadi.atividade", nativeQuery = true)
    public long getNewID();

    @Query(value = "SELECT a.* FROM ativadi.atividade a " +
            "INNER JOIN ativadi.materia_conteudo mc " +
            "ON mc.id_conteudo = a.id_conteudo " +
            "INNER JOIN ativadi.materia m " +
            "ON m.id = mc.id_materia " +
            "INNER JOIN ativadi.turma_materia tm " +
            "ON tm.id_materia = m.id " +
            "WHERE tm.id_turma = :turma", nativeQuery = true)
    public List<Atividade> findAllByTurmaId(@Param("turma") Long turmaId);

    @Query(value = "SELECT at.* FROM ativadi.atividade at " +
            "INNER JOIN ativadi.aula_atividade aat " +
            "ON aat.id_atividade = at.id " +
            "INNER JOIN ativadi.aula a " +
            "ON a.id = aat.id_aula " +
            "WHERE a.id = :aula", nativeQuery = true)
    public List<Atividade> findAllByAulaId(@Param("aula") Long aulaId);
}
