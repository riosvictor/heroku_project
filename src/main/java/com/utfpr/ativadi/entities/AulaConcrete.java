package com.utfpr.ativadi.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity(name = "Aula")
@Table(name = "aula", schema = "ativadi")
public class AulaConcrete extends Aula implements PrototypeAula, Serializable {

    @Transient
    private static final long serialVersionUID = -2774691941039676822L;

    public AulaConcrete() {
    }

    public void invalidAula() {
        this.setStatus(Constants.FECHADO);
    }

    public void closeAula() {
        this.setStatus(Constants.CONCLUIDO);
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
