package com.utfpr.ativadi.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "usuario", schema = "ativadi")
public class Usuario implements Serializable {


    @Id
    private long id;

    @NotBlank(message = "O nome é um campo obrigatório")
    private String nome;

    @NotBlank(message = "O senha é um campo obrigatório")
    private String senha;

    @NotBlank(message = "O email é um campo obrigatório")
    private String email;

    @NotBlank(message = "O tipo é um campo obrigatório")
    private String tipo;

    @Column(nullable = true)
    private Integer grau;

    @Column(nullable = true)
    private Integer turno;

    public Usuario(){}

    public Usuario(long id, String nome, String senha, String email, String tipo, int grau, int turno) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.tipo = tipo;
        this.grau = grau;
        this.turno = turno;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public int getGrau() {
        return grau == null ? 0 : grau;
    }

    public void setGrau(Integer grau) {
        this.grau = grau;
    }

    public int getTurno() {
        return turno == null ? 0 : turno;
    }

    public void setTurno(Integer turno) {
        this.turno = turno;
    }
}

