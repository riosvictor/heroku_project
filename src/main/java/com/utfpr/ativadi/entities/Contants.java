package com.utfpr.ativadi.entities;

enum StatusAula {
    ABERTO(-1), FECHADO(0), CONCLUIDO(1);

    int value;
    StatusAula(int val) {
        value = val;
    }
    int getValue() {
        return value;
    }
}