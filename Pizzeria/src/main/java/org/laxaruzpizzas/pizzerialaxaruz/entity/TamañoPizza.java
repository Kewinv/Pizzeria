package org.laxaruzpizzas.pizzerialaxaruz.entity;

public enum TamañoPizza {
    PEQUEÑA("Pequeña"),
    MEDIANA("Mediana"),
    GRANDE("Grande");

    private final String etiqueta;

    TamañoPizza(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() { return etiqueta; }
}
