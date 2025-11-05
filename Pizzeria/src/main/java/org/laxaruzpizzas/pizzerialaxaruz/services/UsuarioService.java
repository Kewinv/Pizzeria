package org.laxaruzpizzas.pizzerialaxaruz.services;

import org.laxaruzpizzas.pizzerialaxaruz.entity.Usuario;
import org.laxaruzpizzas.pizzerialaxaruz.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public boolean existeCorreo(String correo) {
        return usuarioRepository.findByCorreoIgnoreCase(correo).isPresent();
    }

    public Usuario iniciarSesion(String correo, String password) {
        Optional<Usuario> u = usuarioRepository.findByCorreoIgnoreCase(correo);
        if (u.isPresent() && u.get().getPassword().equals(password)) {
            return u.get();
        }
        return null;
    }
}
