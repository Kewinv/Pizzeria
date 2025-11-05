package org.laxaruzpizzas.pizzerialaxaruz.controller;
import org.laxaruzpizzas.pizzerialaxaruz.entity.*;
import org.laxaruzpizzas.pizzerialaxaruz.repository.ItemCarritoRepository;

import org.laxaruzpizzas.pizzerialaxaruz.services.PizzaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;




@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final PizzaService pizzaService;
    private final ItemCarritoRepository itemCarritoRepository;

    public CarritoController(PizzaService pizzaService, ItemCarritoRepository itemCarritoRepository) {
        this.pizzaService = pizzaService;
        this.itemCarritoRepository = itemCarritoRepository;
    }

    @PostMapping("/agregar")
    public String agregarPizza(
            @RequestParam TipoPizza tipo,
            @RequestParam(name = "tamanio") TamañoPizza tamaño,
            @RequestParam(name = "cantidad", defaultValue = "1") int cantidad,
            HttpSession session
    ) {

        // Recuperar pizza persistida o crearla en BD si no existe
        Pizza pizza = pizzaService.getOrCreate(tipo, tamaño);

        // Obtener o crear el carrito en sesión
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }

        // Buscar si ya existe el mismo tipo y tamaño
        boolean combinado = false;
        for (ItemCarrito item : carrito) {
            if (item.getPizza().getTipo() == tipo && item.getPizza().getTamaño() == tamaño) {
                item.setCantidad(item.getCantidad() + cantidad);
                combinado = true;
                break;
            }
        }

        // Si no existía, añadir nuevo
        if (!combinado) {
            carrito.add(new ItemCarrito(pizza, cantidad));
        }

        return "redirect:/carrito/ver";
    }

    @GetMapping("/ver")
    public String verCarrito(Model model, HttpSession session) {
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) carrito = new ArrayList<>();
        int total = carrito.stream().mapToInt(ItemCarrito::getSubtotal).sum();

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        return "carrito";
    }

    @PostMapping("/eliminar")
    public String eliminarPizza(@RequestParam int index, HttpSession session) {
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito != null && index >= 0 && index < carrito.size()) {
            // Marcar el item para eliminación en la sesión en lugar de borrarlo inmediatamente
            ItemCarrito item = carrito.get(index);
            if (item != null) {
                // alternar la marca
                item.setMarcadoParaEliminar(!item.isMarcadoParaEliminar());
            }
        }
        return "redirect:/carrito/ver";
    }

    @PostMapping("/cancelar")
    public String cancelarPedido(HttpSession session) {
        session.removeAttribute("carrito");
        return "redirect:/catalogo";
    }

    @PostMapping("/guardar")
    public String guardarCarrito(HttpSession session, RedirectAttributes redirectAttributes) {
        // Requerir usuario logueado para asociar el carrito
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para guardar el carrito.");
            return "redirect:/usuario/login";
        }

        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null || carrito.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El carrito está vacío.");
            return "redirect:/carrito/ver";
        }

        // Sincronizar campos denormalizados
        for (ItemCarrito item : carrito) {
            if (item.getPizza() != null) {
                item.setTipo(item.getPizza().getTipo());
                item.setTamano(item.getPizza().getTamaño());
                item.setPrecioUnitario(item.getPizza().getPrecio());
                item.setCantidad(item.getCantidad()); // recalcula subtotal
            }
        }

        // Procesar eliminaciones marcadas y guardar nuevos
        List<Long> idsParaEliminar = new java.util.ArrayList<>();
        List<ItemCarrito> nuevos = new java.util.ArrayList<>();

        for (ItemCarrito item : carrito) {
            if (item.isMarcadoParaEliminar()) {
                if (item.getId() != null) idsParaEliminar.add(item.getId());
            } else {
                if (item.getId() == null) nuevos.add(item);
            }
        }

        // Borrar en BD los ids marcados
        if (!idsParaEliminar.isEmpty()) {
            try {
                itemCarritoRepository.deleteAllById(idsParaEliminar);
            } catch (Exception e) {
                // registrar error y continuar
                System.err.println("Error borrando items marcados: " + e.getMessage());
            }
        }

        // Guardar nuevos items
        if (!nuevos.isEmpty()) {
            List<ItemCarrito> guardados = itemCarritoRepository.saveAll(nuevos);
            // asignar ids generados a los objetos en sesión (por orden)
            int idx = 0;
            for (ItemCarrito item : carrito) {
                if (!item.isMarcadoParaEliminar() && item.getId() == null) {
                    item.setId(guardados.get(idx).getId());
                    idx++;
                }
            }
        }

        // Eliminar de la lista en sesión los items marcados (se han procesado)
        carrito.removeIf(ItemCarrito::isMarcadoParaEliminar);

        redirectAttributes.addFlashAttribute("mensaje", "Carrito sincronizado con la base de datos.");
        return "redirect:/carrito/ver";
    }
 }
