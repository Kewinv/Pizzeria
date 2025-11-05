package org.laxaruzpizzas.pizzerialaxaruz.repository;

import org.laxaruzpizzas.pizzerialaxaruz.entity.Pizza;
import org.laxaruzpizzas.pizzerialaxaruz.entity.TipoPizza;
import org.laxaruzpizzas.pizzerialaxaruz.entity.TamañoPizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    Optional<Pizza> findByTipoAndTamaño(TipoPizza tipo, TamañoPizza tamaño);
}
