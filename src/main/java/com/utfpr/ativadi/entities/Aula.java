package com.utfpr.ativadi.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@MappedSuperclass
public abstract class Aula implements Serializable {

    @Transient
    private static final long serialVersionUID = -5924480645127534035L;

    @Id
    private long id;

    private int status;

    @NotNull(message = "A Data de Realização é um campo obrigatório")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date data;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date aberto_ate; //pode refazer valendo nota

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechado_em; //pode refazer sem valer nota

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date concluido_em; //não pode refazer

    @ManyToOne
    @JoinColumn(name="id_professor")
    @NotNull(message = "O Professor é um campo obrigatório")
    private Usuario professor;

    @ManyToOne
    @JoinColumn(name="id_turma")
    @NotNull(message = "A Turma é um campo obrigatório")
    private Turma turma;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "aula_atividade", schema = "ativadi",
            joinColumns = @JoinColumn(name = "id_aula", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_atividade", referencedColumnName = "id", table = "atividade"))
    @NotEmpty(message = "A Atividade é um campo obrigatório")
    protected List<Atividade> atividades;


    public Aula(){}

    public Aula(Aula target) {
        if (target != null) {
            this.id = target.id;
            this.professor = target.professor;
            this.turma = target.turma;
            this.atividades = target.atividades;
            this.data = target.data;
            this.status = Constants.ABERTO;
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

    public List<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<Atividade> atividade) {
        this.atividades = atividade;
    }

    @Override
    public String toString() {
        return "Aula{" +
                "id=" + id +
                ", data=" + data +
                ", professor=" + professor +
                ", turma=" + turma +
                ", atividades=" + atividades.stream().map(n-> String.valueOf(n.getDescricao())).collect(Collectors.joining(",","{","}")) +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object object2) {
        if (!(object2 instanceof Aula)) return false;
        Aula shape2 = (Aula) object2;
        return (shape2.data == this.data) &&
                Objects.equals(shape2.professor,this.professor) &&
                Objects.equals(shape2.turma, this.turma);
    }
}
