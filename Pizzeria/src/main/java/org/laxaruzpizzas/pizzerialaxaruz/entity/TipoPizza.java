package org.laxaruzpizzas.pizzerialaxaruz.entity;


public enum TipoPizza {
    HAWAIANA("Hawaiana", 18000, 26000, 32000),
    MEXICANA("Mexicana", 20000, 28000, 35000),
    NAPOLITANA("Napolitana", 17000, 25000, 31000),
    VEGETARIANA("Vegetariana", 19000, 27000, 34000),
    CUATRO_QUESOS("Cuatro Quesos", 21000, 30000, 37000),
    POLLO_CHAMPI("Pollo y Champiñón", 20000, 29000, 36000),
    JAMON_QUESO("Jamón y Queso", 18000, 26000, 32000),
    PEPERONI("Peperoni", 19000, 27000, 34000),
    MAR_TIERRA("Mar y Tierra", 23000, 32000, 40000),
    BBQ_ESPECIAL("BBQ Especial", 22000, 31000, 39000);

    private final String nombre;
    private final int precioPequeña;
    private final int precioMediana;
    private final int precioGrande;

    TipoPizza(String nombre, int pequeña, int mediana, int grande) {
        this.nombre = nombre;
        this.precioPequeña = pequeña;
        this.precioMediana = mediana;
        this.precioGrande = grande;
    }

    public String getNombre() { return nombre; }
    public int getPrecioPequeña() { return precioPequeña; }
    public int getPrecioMediana() { return precioMediana; }
    public int getPrecioGrande() { return precioGrande; }
    //FIn
}

