package com.utfpr.ativadi.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "turma", schema = "ativadi")
public class Turma implements Serializable {

    @Id
    private long id;

    @NotBlank(message = "A Descrição é um campo obrigatório")
    private String descricao;

    @NotNull(message = "O Grau Escolar é um campo obrigatório")
    private int grau;

    @NotNull(message = "O Turno é um campo obrigatório")
    private int turno;

    public Turma(){}

    public Turma(long id, String descricao, int grau, int turno) {
        this.id = id;
        this.descricao = descricao;
        this.grau = grau;
        this.turno = turno;
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

    public int getGrau() {
        return grau;
    }

    public void setGrau(int grau) {
        this.grau = grau;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    @Override
    public String toString() {
        return "Turma{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", grau=" + grau +
                ", turno=" + turno +
                '}';
    }
}
