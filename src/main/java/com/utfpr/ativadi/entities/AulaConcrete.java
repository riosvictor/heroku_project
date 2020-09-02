package com.utfpr.ativadi.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "Aula")
@Table(name = "aula", schema = "ativadi")
public class AulaConcrete extends Aula implements PrototypeAula{

    public AulaConcrete() {
    }

    public void invalidAula() {
        this.setStatus(this.FECHADO);
    }

    public void closeAula() {
        this.setStatus(this.CONCLUIDO);
    }

    public AulaConcrete(AulaConcrete target) {
        super(target);
    }

    @Override
    public Aula clone() {
        return new AulaConcrete(this);
    }

    @Override
    public boolean equals(Object object2) {
        if (!(object2 instanceof AulaConcrete) || !super.equals(object2)) return false;
        else return true;
    }
}
