package com.utfpr.ativadi.repositories;

import com.utfpr.ativadi.entities.Grau;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrauRepository extends CrudRepository<Grau, Long> {

    @Query(value = "SELECT * FROM ativadi.grau", nativeQuery = true)
    public List<Grau> findAll();

    @Query(value = "SELECT * FROM ativadi.grau a WHERE a.id = :id", nativeQuery = true)
    public Optional<Grau> findById(@Param("id") Long id);

    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM ativadi.grau", nativeQuery = true)
    public long getNewID();
}
