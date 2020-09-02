package com.utfpr.ativadi.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "aula_aluno", schema = "ativadi")
public class AulaAluno implements Serializable {
    @Id
    protected long id;

    @ManyToOne
    @JoinColumn(name="id_atividade")
    private Atividade atividade;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

    private int pontuacao;

    private Date data_realizacao;

    private Date data_programacao;

    public AulaAluno() {
    }

    public AulaAluno(long id, Atividade atividade, Usuario usuario, int pontuacao, Date data_realizacao, Date data_programacao) {
        this.id = id;
        this.atividade = atividade;
        this.usuario = usuario;
        this.pontuacao = pontuacao;
        this.data_realizacao = data_realizacao;
        this.data_programacao = data_programacao;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public Date getData_realizacao() {
        return data_realizacao;
    }

    public void setData_realizacao(Date data_realizacao) {
        this.data_realizacao = data_realizacao;
    }

    public Date getData_programacao() {
        return data_programacao;
    }

    public void setData_programacao(Date data_programacao) {
        this.data_programacao = data_programacao;
    }
}
