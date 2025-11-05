package org.laxaruzpizzas.pizzerialaxaruz.controller;

import org.laxaruzpizzas.pizzerialaxaruz.entity.Pizza;
import org.laxaruzpizzas.pizzerialaxaruz.entity.TipoPizza;
import org.laxaruzpizzas.pizzerialaxaruz.services.PizzaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PizzaController {

    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/catalogo";
    }

    @GetMapping("/catalogo")
    public String verCatalogo(Model model, HttpSession session) {
        // Si el usuario no está logueado, redirigir al login
        Object usuario = session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/usuario/login";
        }

        // Cargar las pizzas disponibles
        List<Pizza> pizzas = pizzaService.findAll();
        model.addAttribute("pizzas", pizzas);

        // Añadir lista de tipos para renderizar tarjetas por tipo (más cómodo en la vista)
        model.addAttribute("tipos", TipoPizza.values());

        Map<String, String> imagenPorTipo = new HashMap<>();
        imagenPorTipo.put("HAWAIANA", "HAWAIANA.jpg");
        imagenPorTipo.put("MEXICANA", "MEXICANA.jpg");
        imagenPorTipo.put("NAPOLITANA", "NAPOLITANA.jpg");
        imagenPorTipo.put("VEGETARIANA", "VEGETARIANA.png");
        imagenPorTipo.put("CUATRO_QUESOS", "CUATRO_QUESOS.jpg");
        imagenPorTipo.put("POLLO_CHAMPI", "POLLO_CHAMP.png"); // fichero disponible con nombre ligeramente distinto
        imagenPorTipo.put("JAMON_QUESO", "JAMON_QUESO.jpg");
        imagenPorTipo.put("PEPERONI", "PEPERONI.jpg");
        imagenPorTipo.put("MAR_TIERRA", "MAR_TIERRA.png");
        imagenPorTipo.put("BBQ_ESPECIAL", "BBQ_ESPECIAL.jpg");

        model.addAttribute("imagenPorTipo", imagenPorTipo);

        return "catalogo";
    }
}
