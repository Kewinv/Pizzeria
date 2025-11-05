package org.laxaruzpizzas.pizzerialaxaruz.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "items_carrito")
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Pizza pizza;

    @Enumerated(EnumType.STRING)
    private TipoPizza tipo;

    @Enumerated(EnumType.STRING)
    private TamañoPizza tamano;

    private int precioUnitario;

    private int cantidad;

    private int subtotal;

    @Transient
    private boolean marcadoParaEliminar = false;

    public ItemCarrito() {}

    public ItemCarrito(Pizza pizza, int cantidad) {
        this.pizza = pizza;
        this.cantidad = cantidad;
        if (pizza != null) {
            this.tipo = pizza.getTipo();
            this.tamano = pizza.getTamaño();
            this.precioUnitario = pizza.getPrecio();
            this.subtotal = this.precioUnitario * this.cantidad;
        }
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pizza getPizza() {
        return pizza;
    }
    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
        if (pizza != null) {
            this.tipo = pizza.getTipo();
            this.tamano = pizza.getTamaño();
            this.precioUnitario = pizza.getPrecio();
            this.subtotal = this.precioUnitario * this.cantidad;
        }
    }

    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = this.precioUnitario * this.cantidad;
    }

    public TipoPizza getTipo() { return tipo; }
    public void setTipo(TipoPizza tipo) { this.tipo = tipo; }

    public TamañoPizza getTamano() { return tamano; }
    public void setTamano(TamañoPizza tamano) { this.tamano = tamano; }

    public int getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(int precioUnitario) { this.precioUnitario = precioUnitario; }

    // Subtotal calculado
    public int getSubtotal() {
        return subtotal;
    }

    public boolean isMarcadoParaEliminar() {
        return marcadoParaEliminar;
    }

    public void setMarcadoParaEliminar(boolean marcadoParaEliminar) {
        this.marcadoParaEliminar = marcadoParaEliminar;
    }

    @Override
    public String toString() {
        return cantidad + "x " + pizza.toString() + " = $" + getSubtotal();
    }
}
