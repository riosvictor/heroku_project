package com.utfpr.ativadi.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "grau", schema = "ativadi")
public class Grau implements Serializable {

    @Transient
    private static final long serialVersionUID = -7205370707550999979L;

    @Id
    @Column(name = "id")
    protected long id;

    @NotBlank(message = "A Descrição é um campo obrigatório")
    @Column(name = "descricao")
    protected String descricao;

    public Grau() {
    }

    public Grau(long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
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

    @Override
    public String toString() {
        return getDescricao();
    }
}

/*
 * # Inserção Incial
 * INSERT INTO ativadi.grau VALUES (1, 'Pré-Escolar');
 * INSERT INTO ativadi.grau VALUES (2, 'Escolar');
 * */
