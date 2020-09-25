package com.utfpr.ativadi.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "aula_aluno", schema = "ativadi")
public class AulaAluno implements Serializable {

    @Transient
    private static final long serialVersionUID = -5955728501664086754L;

    @Id
    protected long id;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "aula_aluno_atividade", schema = "ativadi",
            joinColumns = @JoinColumn(name = "id_aula_aluno", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_atividade", referencedColumnName = "id", table = "atividade"))
    protected List<Atividade> atividades;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

    private int pontuacao;

    private Date data_realizacao;

    private Date data_programacao;

    public AulaAluno() {
    }

    public AulaAluno(long id, List<Atividade> atividades, Usuario usuario, int pontuacao, Date data_realizacao, Date data_programacao) {
        this.id = id;
        this.atividades = atividades;
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

    public List<Atividade> getAtividade() {
        return atividades;
    }

    public void setAtividade(List<Atividade> atividade) {
        this.atividades = atividade;
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
