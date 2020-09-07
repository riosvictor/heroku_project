package com.utfpr.ativadi.repositories;

import com.utfpr.ativadi.entities.Atividade;
import com.utfpr.ativadi.entities.Materia;
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

    @Query(value = "SELECT * FROM ativadi.atividade a WHERE a.grau = :grau", nativeQuery = true)
    public List<Atividade> findAtividadeByGrau(@Param("grau") int grau);
}
