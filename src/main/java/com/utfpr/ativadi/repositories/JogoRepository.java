package com.utfpr.ativadi.repositories;

import com.utfpr.ativadi.entities.Jogo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JogoRepository extends CrudRepository<Jogo, Long> {

    @Query(value = "SELECT * FROM ativadi.jogo", nativeQuery = true)
    public List<Jogo> findAll();

    @Query(value = "SELECT * FROM ativadi.jogo a WHERE a.id = :id", nativeQuery = true)
    public Optional<Jogo> findById(@Param("id") Long id);

    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM ativadi.jogo", nativeQuery = true)
    public long getNewID();

    @Query(value = "SELECT * FROM ativadi.jogo a " +
            "INNER JOIN ativadi.conteudo_jogo cj " +
            "ON cj.id_jogo = a.id WHERE cj.id_conteudo = :conteudo", nativeQuery = true)
    public List<Jogo> findJogosByConteudo(@Param("conteudo") long conteudo);
}
