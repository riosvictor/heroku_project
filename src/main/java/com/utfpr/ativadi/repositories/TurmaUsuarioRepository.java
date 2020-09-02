package com.utfpr.ativadi.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Repository
//public interface TurmaUsuarioRepository extends CrudRepository<TurmaUsuario, Long> {
//    @Query(value = "SELECT * FROM ativadi.turma_usuario", nativeQuery = true)
//    public List<TurmaUsuario> findAll();
//
//    @Query(value = "SELECT * FROM ativadi.turma_usuario t WHERE t.id_turma = :id", nativeQuery = true)
//    public Optional<TurmaUsuario> findById(@Param("id") Long id);
//
//    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM ativadi.turma_usuario", nativeQuery = true)
//    public long getNewID();
//}
