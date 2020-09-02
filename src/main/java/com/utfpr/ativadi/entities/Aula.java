package com.utfpr.ativadi.entities;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@MappedSuperclass
public abstract class Aula implements Serializable {

    @Id
    private long id;

    private int status;

    public final int ABERTO = -1;
    public final int FECHADO = 0;
    public final int CONCLUIDO = 1;

    @NotNull(message = "A Data de Realização é um campo obrigatório")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date data;

    @NotNull(message = "O Professor é um campo obrigatório")
    @ManyToOne
    @JoinColumn(name="id_professor")
    private Usuario professor;

    @NotNull(message = "A Matéria é um campo obrigatório")
    @ManyToOne
    @JoinColumn(name="id_materia")
    private Materia materia;

    @NotNull(message = "A Turma é um campo obrigatório")
    @ManyToOne
    @JoinColumn(name="id_turma")
    private Turma turma;

    @NotNull(message = "A Atividade é um campo obrigatório")
    @ManyToOne
    @JoinColumn(name="id_atividade")
    private Atividade atividade;


    public Aula(){}

    public Aula(Aula target) {
        if (target != null) {
            this.id = target.id;
            this.professor = target.professor;
            this.materia = target.materia;
            this.turma = target.turma;
            this.atividade = target.atividade;
            this.data = target.data;
            this.status = ABERTO;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Usuario getProfessor() {
        return professor;
    }

    public void setProfessor(Usuario professor) {
        this.professor = professor;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    @Override
    public String toString() {
        return "Aula{" +
                "id=" + id +
                ", data=" + data +
                ", professor=" + professor +
                ", materia=" + materia +
                ", turma=" + turma +
                ", atividade=" + atividade +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object object2) {
        if (!(object2 instanceof Aula)) return false;
        Aula shape2 = (Aula) object2;
        return (shape2.data == this.data) &&
                Objects.equals(shape2.professor,this.professor) &&
                Objects.equals(shape2.turma, this.turma) &&
                Objects.equals(shape2.atividade, this.atividade);
    }
}
