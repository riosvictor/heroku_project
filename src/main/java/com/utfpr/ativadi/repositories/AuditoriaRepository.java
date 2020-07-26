package com.utfpr.ativadi.repositories;

import com.utfpr.ativadi.entities.Auditoria;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuditoriaRepository extends CrudRepository<Auditoria, Long> {
    @Query(value = "SELECT * FROM ativadi.auditoria", nativeQuery = true)
    public List<Auditoria> findAll();

    @Query(value = "SELECT * FROM ativadi.auditoria m WHERE m.id = :id", nativeQuery = true)
    public Optional<Auditoria> findById(@Param("id") Long id);

    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM ativadi.auditoria", nativeQuery = true)
    public long getNewID();
}
