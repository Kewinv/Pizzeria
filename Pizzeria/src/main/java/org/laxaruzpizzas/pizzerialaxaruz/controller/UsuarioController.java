package org.laxaruzpizzas.pizzerialaxaruz.controller;

import org.laxaruzpizzas.pizzerialaxaruz.entity.Usuario;
import org.laxaruzpizzas.pizzerialaxaruz.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Página principal (redirige al login)
    @GetMapping("/")
    public String inicio() {
        return "redirect:/usuario/login";
    }

    // Formulario de registro
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    // Procesar registro
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model, HttpSession session) {
        if (usuarioService.existeCorreo(usuario.getCorreo())) {
            model.addAttribute("error", "El correo ya está registrado.");
            return "registro";
        }
        Usuario guardado = usuarioService.registrar(usuario);
        session.setAttribute("usuarioLogueado", guardado);
        return "redirect:/catalogo";
    }

    // Formulario de inicio de sesión
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    // Procesar inicio de sesión
    @PostMapping("/login")
    public String iniciarSesion(@ModelAttribute Usuario usuario, Model model, HttpSession session) {
        Usuario logueado = usuarioService.iniciarSesion(usuario.getCorreo(), usuario.getPassword());
        if (logueado == null) {
            model.addAttribute("error", "Credenciales inválidas.");
            return "login";
        }
        session.setAttribute("usuarioLogueado", logueado);
        return "redirect:/catalogo";
    }

    // Cerrar sesión
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/usuario/login";
    }
}
