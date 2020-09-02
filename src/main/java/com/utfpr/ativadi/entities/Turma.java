package com.utfpr.ativadi.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "turma_aluno", schema = "ativadi",
            joinColumns = @JoinColumn(name = "id_turma", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_aluno", referencedColumnName = "id", table = "usuario"))
    protected List<Usuario> alunos;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "turma_professor", schema = "ativadi",
            joinColumns = @JoinColumn(name = "id_turma", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_professor", referencedColumnName = "id", table = "usuario"))
    protected List<Usuario> professores;

    public Turma(){}

    public Turma(long id, String descricao, int grau, int turno) {
        this.id = id;
        this.descricao = descricao;
        this.grau = grau;
        this.turno = turno;
        this.alunos = new ArrayList<>();
        this.professores = new ArrayList<>();
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

    public List<Usuario> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Usuario> alunos) {
        this.alunos = alunos;
    }

    public List<Usuario> getProfessores() {
        return professores;
    }

    public void setProfessores(List<Usuario> professores) {
        this.professores = professores;
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
