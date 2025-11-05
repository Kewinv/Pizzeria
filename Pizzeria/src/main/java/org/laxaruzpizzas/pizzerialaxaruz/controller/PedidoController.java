package org.laxaruzpizzas.pizzerialaxaruz.controller;

import org.laxaruzpizzas.pizzerialaxaruz.entity.*;
import org.laxaruzpizzas.pizzerialaxaruz.repository.ItemCarritoRepository;
import org.laxaruzpizzas.pizzerialaxaruz.services.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PedidoController {

    private final PedidoService pedidoService;
    private final ItemCarritoRepository itemCarritoRepository;

    public PedidoController(PedidoService pedidoService, ItemCarritoRepository itemCarritoRepository) {
        this.pedidoService = pedidoService;
        this.itemCarritoRepository = itemCarritoRepository;
    }

    @PostMapping("/pedido/confirmar")
    public String confirmarPedido(HttpSession session, Model model) {
        // Obtener el usuario logueado
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login";
        }

        // Obtener el carrito actual
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/catalogo";
        }

        // Calcular total
        int total = carrito.stream().mapToInt(ItemCarrito::getSubtotal).sum();

        // Crear pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(usuario);
        pedido.setTotal(total);
        pedido.setEstado("Confirmado");

        // Guardar pedido (solo la cabecera)
        Pedido saved = pedidoService.save(pedido);

        // También persistir los items en la tabla items_carrito (si es requerido)
        for (ItemCarrito item : carrito) {
            if (item.getPizza() != null) {
                item.setTipo(item.getPizza().getTipo());
                item.setTamano(item.getPizza().getTamaño());
                item.setPrecioUnitario(item.getPizza().getPrecio());
                item.setCantidad(item.getCantidad());
            }
        }
        itemCarritoRepository.saveAll(carrito);

        // Pasar datos a la vista
        model.addAttribute("pedido", saved);

        // Limpiar carrito
        session.removeAttribute("carrito");

        return "confirmacion";
    }
}
