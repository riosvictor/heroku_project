package com.utfpr.ativadi.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "materia", schema = "ativadi")
public class Materia implements ComponenteMateria, Serializable {

    @Transient
    private static final long serialVersionUID = -9200165852939914800L;

    @Id
    @Column(name = "id")
    protected long id;

    @NotBlank(message = "A Descrição é um campo obrigatório")
    @Column(name = "descricao")
    protected String descricao;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "materia_conteudo", schema = "ativadi",
            joinColumns = @JoinColumn(name = "id_materia", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_conteudo", referencedColumnName = "id", table = "conteudo"))
    protected List<Conteudo> conteudos = new ArrayList<>();

    @NotNull(message = "A Situação é um campo obrigatório")
    protected boolean ativo;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "materia_grau", schema = "ativadi",
            joinColumns = @JoinColumn(name = "id_materia", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_grau", referencedColumnName = "id", table = "grau"))
    protected List<Grau> graus = new ArrayList<>();

    public Materia(){}

    public Materia(long id, String descricao, boolean ativo) {
        this.id = id;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }

    @Override
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public List<Conteudo> getConteudos() {
        return conteudos;
    }

    public void setConteudos(List<Conteudo> conteudos) {
        this.conteudos = conteudos;
    }

    public List<Grau> getGraus() {
        return graus;
    }

    public void setGraus(List<Grau> graus) {
        this.graus = graus;
    }

    public void Adicionar(Conteudo conteudo){
        conteudos.add(conteudo);
    }

    public void Remover(Conteudo conteudo){
        conteudos.remove(conteudo);
    }

    @Override
    public String toString() {
        return  "Materia{" +
                ", id=" + id +
                ", descricao=" + descricao +
                ", conteudos=" + conteudos.stream().map(n-> String.valueOf(n.getDescricao())).collect(Collectors.joining(",","{","}")) +
                ", graus=" + graus.stream().map(n-> String.valueOf(n.getDescricao())).collect(Collectors.joining(",","{","}")) +
                ", ativo=" + ativo +
                '}';
    }
}
