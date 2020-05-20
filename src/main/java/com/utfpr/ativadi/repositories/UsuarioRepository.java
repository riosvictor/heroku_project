package com.utfpr.ativadi.repositories;

import com.utfpr.ativadi.entities.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    @Query(value = "SELECT * FROM ativadi.usuario", nativeQuery = true)
    public List<Usuario> findAll();

    @Query(value = "SELECT * FROM ativadi.usuario u WHERE u.id = :id", nativeQuery = true)
    public Optional<Usuario> findById(@Param("id") Long id);

    @Query(value = "SELECT * FROM ativadi.usuario u WHERE u.email = :email", nativeQuery = true)
    public Optional<Usuario> findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM ativadi.usuario u WHERE u.email = :email and u.senha = :senha", nativeQuery = true)
    public Optional<Usuario> findByEmailSenha(@Param("email") String email, @Param("senha") String senha);

    @Query(value = "SELECT * FROM ativadi.usuario u WHERE u.tipo = 'professor'", nativeQuery = true)
    public List<Usuario> findProfessorAll();

    @Query(value = "SELECT * FROM ativadi.usuario u WHERE u.id = :id AND u.tipo = 'professor'", nativeQuery = true)
    public Optional<Usuario> findProfessorById(@Param("id") Long id);

    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM ativadi.usuario", nativeQuery = true)
    public long getNewID();
}
