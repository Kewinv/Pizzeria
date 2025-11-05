package org.laxaruzpizzas.pizzerialaxaruz.services;

import org.laxaruzpizzas.pizzerialaxaruz.entity.Pizza;
import org.laxaruzpizzas.pizzerialaxaruz.entity.TipoPizza;
import org.laxaruzpizzas.pizzerialaxaruz.entity.TamañoPizza;
import org.laxaruzpizzas.pizzerialaxaruz.repository.PizzaRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PizzaService {

    private final PizzaRepository pizzaRepository;

    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    // Inicializa pizzas en la BD si no hay ninguna (útil en desarrollo)
    @PostConstruct
    public void init() {
        if (pizzaRepository.count() == 0) {
            long id = 1;
            List<Pizza> initial = new ArrayList<>();
            for (TipoPizza tipo : TipoPizza.values()) {
                for (TamañoPizza tam : TamañoPizza.values()) {
                    initial.add(new Pizza(null, tipo, tam));
                }
            }
            pizzaRepository.saveAll(initial);
        }
    }

    // Obtener pizza persistida por tipo y tamaño o crear y persistir si no existe
    public Pizza getOrCreate(TipoPizza tipo, TamañoPizza tamaño) {
        Optional<Pizza> p = pizzaRepository.findByTipoAndTamaño(tipo, tamaño);
        return p.orElseGet(() -> pizzaRepository.save(new Pizza(null, tipo, tamaño)));
    }

    public List<Pizza> findAll() {
        return pizzaRepository.findAll();
    }
}
