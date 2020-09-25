package com.utfpr.ativadi.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "jogo", schema = "ativadi")
public class Jogo implements Serializable {
    @Transient
    private static final long serialVersionUID = -4693196964195727273L;

    @Id
    protected long id;

    @NotBlank(message = "A Descrição é um campo obrigatório")
    @Column(name = "descricao")
    protected String descricao;

    @NotBlank(message = "A Url é um campo obrigatório")
    @Column(name = "url")
    protected String url;

    @ManyToMany(mappedBy = "jogos")
    private List<Conteudo> conteudos;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Jogo() {
    }

    public Jogo(long id, String descricao, String url) {
        this.id = id;
        this.descricao = descricao;
        this.url = url;
    }

    @Override
    public String toString() {

        return  "Jogo{" +
                ", id=" + id +
                ", descricao=" + descricao +
                ", url=" + url +
                '}';
    }
}
