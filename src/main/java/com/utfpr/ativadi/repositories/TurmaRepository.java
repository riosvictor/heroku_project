package com.utfpr.ativadi.repositories;

import com.utfpr.ativadi.entities.Turma;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TurmaRepository extends CrudRepository<Turma, Long> {
    @Query(value = "SELECT * FROM ativadi.turma", nativeQuery = true)
    public List<Turma> findAll();

    @Query(value = "SELECT * FROM ativadi.turma t WHERE t.id = :id", nativeQuery = true)
    public Optional<Turma> findById(@Param("id") Long id);

    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM ativadi.turma", nativeQuery = true)
    public long getNewID();
}
