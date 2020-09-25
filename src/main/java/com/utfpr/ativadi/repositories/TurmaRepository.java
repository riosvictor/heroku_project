package com.utfpr.ativadi.repositories;

import com.utfpr.ativadi.entities.Turma;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurmaRepository extends CrudRepository<Turma, Long> {
    @Query(value = "SELECT * FROM ativadi.turma ORDER BY descricao ASC ", nativeQuery = true)
    public List<Turma> findAll();

    @Query(value = "SELECT * FROM ativadi.turma t WHERE t.id = :id", nativeQuery = true)
    public Optional<Turma> findById(@Param("id") Long id);

    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM ativadi.turma", nativeQuery = true)
    public long getNewID();

    @Query(value = "SELECT * FROM ativadi.turma t " +
            "INNER JOIN ativadi.turma_professor tp " +
            "ON tp.id_turma = t.id " +
            "WHERE tp.id_professor = :id", nativeQuery = true)
    public List<Turma> findAllByProfessorId(@Param("id") Long id);

    @Query(value = "SELECT * FROM ativadi.turma t " +
            "INNER JOIN ativadi.turma_aluno ta " +
            "ON ta.id_turma = t.id " +
            "WHERE ta.id_aluno = :id LIMIT 1", nativeQuery = true)
    public Optional<Turma> findAllByAlunoId(@Param("id") Long id);

    /*
    * MODIFYING
    * */

    @Modifying
    @Query(value = "DELETE FROM ativadi.turma_professor tp WHERE tp.id_turma = :id", nativeQuery = true)
    public void deleteAllProfessores(@Param("id") Long turmaId);

    @Modifying()
    @Query(value = "DELETE FROM ativadi.turma_aluno tp WHERE tp.id_turma = :id", nativeQuery = true)
    public void deleteAllAlunos(@Param("id") Long turmaId);

    @Query(value = "INSERT INTO ativadi.turma_aluno (id_turma, id_aluno) VALUES (:turma, :aluno)", nativeQuery = true)
    public void insertAluno(@Param("turma") long turmaId, @Param("aluno") long alunoId);

    @Query(value = "INSERT INTO ativadi.turma_aluno (id_turma, id_professor) VALUES (:turma, :professor)", nativeQuery = true)
    public void insertProfessor(@Param("turma") long turmaId, @Param("professor") long professorId);
}
