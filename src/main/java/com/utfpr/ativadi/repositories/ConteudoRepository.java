package com.utfpr.ativadi.repositories;

import com.utfpr.ativadi.entities.Conteudo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConteudoRepository extends CrudRepository<Conteudo, Long> {

    @Query(value = "SELECT * FROM ativadi.conteudo", nativeQuery = true)
    public List<Conteudo> findAll();

    @Query(value = "SELECT * FROM ativadi.conteudo a WHERE a.id = :id", nativeQuery = true)
    public Optional<Conteudo> findById(@Param("id") Long id);

    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM ativadi.conteudo", nativeQuery = true)
    public long getNewID();
}
