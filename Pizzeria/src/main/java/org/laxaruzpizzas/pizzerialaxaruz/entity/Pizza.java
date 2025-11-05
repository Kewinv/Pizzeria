package org.laxaruzpizzas.pizzerialaxaruz.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pizzas")
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoPizza tipo;

    @Enumerated(EnumType.STRING)
    private TamañoPizza tamaño;

    private int precio;

    public Pizza() {}

    public Pizza(Long id, TipoPizza tipo, TamañoPizza tamaño) {
        this.id = id;
        this.tipo = tipo;
        this.tamaño = tamaño;
        this.precio = calcularPrecio();
    }

    // Método para calcular el precio según el tipo y tamaño
    private int calcularPrecio() {
        return switch (tamaño) {
            case PEQUEÑA -> tipo.getPrecioPequeña();
            case MEDIANA -> tipo.getPrecioMediana();
            case GRANDE  -> tipo.getPrecioGrande();
        };
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoPizza getTipo() { return tipo; }
    public void setTipo(TipoPizza tipo) {
        this.tipo = tipo;
        this.precio = calcularPrecio(); // recalcula al cambiar tipo
    }

    public TamañoPizza getTamaño() { return tamaño; }
    public void setTamaño(TamañoPizza tamaño) {
        this.tamaño = tamaño;
        this.precio = calcularPrecio(); // recalcula al cambiar tamaño
    }

    public int getPrecio() { return precio; }

    @Override
    public String toString() {
        return tipo.getNombre() + " (" + tamaño.getEtiqueta() + ") - $" + precio;
    }
}
