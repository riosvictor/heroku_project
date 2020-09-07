package com.utfpr.ativadi.repositories;

import com.utfpr.ativadi.entities.Materia;
import com.utfpr.ativadi.entities.Turma;
import com.utfpr.ativadi.entities.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MateriaRepository extends CrudRepository<Materia, Long> {
    @Query(value = "SELECT * FROM ativadi.materia", nativeQuery = true)
    public List<Materia> findAll();

    @Query(value = "SELECT * FROM ativadi.materia m WHERE m.ativo = true", nativeQuery = true)
    public List<Materia> findAllAtivas();

    @Query(value = "SELECT * FROM ativadi.materia m WHERE m.id = :id", nativeQuery = true)
    public Optional<Materia> findById(@Param("id") Long id);

    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM ativadi.materia", nativeQuery = true)
    public long getNewID();

    @Query(value = "SELECT * FROM ativadi.materia m WHERE m.ativo = true AND m.grau = :grau", nativeQuery = true)
    public List<Materia> findMateriaByGrau(@Param("grau") int grau);
}
