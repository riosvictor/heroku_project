package com.utfpr.ativadi.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "atividade", schema = "ativadi")
public class Atividade implements AtividadeFlyweight, Serializable {

    @Transient
    private static final long serialVersionUID = 6857913359624129762L;

    @Id
    private long id;

    @NotBlank(message = "A Descrição é um campo obrigatório")
    private String descricao;

    @NotNull(message = "O Jogo é um campo obrigatório")
    @ManyToOne
    @JoinColumn(name="id_jogo")
    private Jogo jogo;

    @NotNull(message = "O Conteúdo é um campo obrigatório")
    @ManyToOne
    @JoinColumn(name="id_conteudo")
    private Conteudo conteudo;

    public Atividade(){}

    public Atividade(long id, String descricao, Jogo jogo, Conteudo conteudo) {
        this.id = id;
        this.descricao = descricao;
        this.jogo = jogo;
        this.conteudo = conteudo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public Conteudo getConteudo() {
        return conteudo;
    }

    public void setConteudo(Conteudo conteudo) {
        this.conteudo = conteudo;
    }

    @Override
    public String toString() {
        return "Atividade{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", jogo='" + jogo.getDescricao() + '\'' +
                '}';
    }
}
