package com.utfpr.ativadi.entities;

import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "auditoria", schema = "ativadi")
public class Auditoria implements Serializable {

    @Id
    private long id;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date data;

    public Auditoria(){}

    public Auditoria(long id, String descricao, Usuario usuario, Date data) {
        this.id = id;
        this.descricao = descricao;
        this.usuario = usuario;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
