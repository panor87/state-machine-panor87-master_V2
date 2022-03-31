package com.imatia.statemachine.config;

public enum Estados {
    RECOGIDO_EN_ALMACÉN(1),
    EN_REPARTO(2),
    INCIDENCIA_EN_ENTREGA(3),
    ENTREGADO(4);

    int id;

    Estados(int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

