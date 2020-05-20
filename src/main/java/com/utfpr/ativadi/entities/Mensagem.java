package com.utfpr.ativadi.entities;

import java.io.Serializable;

public class Mensagem implements Serializable {
    public enum Funcao {
        ALTERAR,
        REMOVER,
        ADICIONAR,
        LOGIN
    }

    private static Mensagem instance;
    public boolean success;
    public Funcao function;

    private Mensagem(boolean success, Funcao function) {
        this.success = success;
        this.function = function;
    }

    public static Mensagem getInstance(boolean success, Funcao function) {
        if (instance == null) {
            instance = new Mensagem(success, function);
        }else{
            instance.success = success;
            instance.function = function;
        }

        return instance;
    }

    public String show(){
        String result = "";

        if (success){
            if (function == Funcao.ADICIONAR)
                result = "Adicionado com Sucesso!";
            else if (function == Funcao.ALTERAR)
                result = "Alterado com Sucesso!";
            else if (function == Funcao.REMOVER)
                result = "Removido com Sucesso!";
            else if (function == Funcao.LOGIN)
                result = "Login realizado com Sucesso!";
        }else{
            if (function == Funcao.ADICIONAR)
                result = "Erro ao Adicionar.";
            else if (function == Funcao.ALTERAR)
                result = "Erro ao Alterar.";
            else if (function == Funcao.REMOVER)
                result = "Erro ao Remover.";
            else if (function == Funcao.LOGIN)
                result = "Erro ao realizar o Login.";
        }

        return result;
    }
}
