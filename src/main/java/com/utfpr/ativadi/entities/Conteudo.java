package com.utfpr.ativadi.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conteudo", schema = "ativadi")
public class Conteudo implements ComponenteMateria, Serializable {

    @Transient
    private static final long serialVersionUID = -273031246708025331L;

    @Id
    @Column(name = "id")
    protected long id;

    @NotBlank(message = "A Descrição é um campo obrigatório")
    @Column(name = "descricao")
    protected String descricao;

    @NotBlank(message = "O Objetivo é um campo obrigatório")
    @Column(name = "objetivos")
    protected String objetivos;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "conteudo_jogo", schema = "ativadi",
            joinColumns = @JoinColumn(name = "id_conteudo", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_jogo", referencedColumnName = "id", table = "jogo"))
    protected List<Jogo> jogos;

    public Conteudo() {
    }

    public Conteudo(long id, String descricao, String objetivos) {
        this.id = id;
        this.descricao = descricao;
        this.objetivos = objetivos;
        this.jogos = new ArrayList<>();
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

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public List<Jogo> getJogos() {
        return jogos;
    }

    public void setJogos(List<Jogo> jogos) {
        this.jogos = jogos;
    }

    public String allJogos() {
        StringBuilder sb = new StringBuilder();
        for (Jogo j : jogos) {
            sb.append(j.getDescricao());
        }

        return sb.toString();
    }

    @Override
    public String toString() {

        return "Conteudo{" +
                ", id=" + id +
                ", descricao=" + descricao +
                ", jogos=" + allJogos() +
                '}';
    }
}
