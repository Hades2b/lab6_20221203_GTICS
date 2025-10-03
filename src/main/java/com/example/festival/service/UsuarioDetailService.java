package com.example.festival.service;

import com.example.festival.entity.Usuario;
import com.example.festival.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        // Buscar usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if (usuario == null) {
            System.err.println("Usuario no encontrado con correo: " + correo);
            throw new UsernameNotFoundException("Credenciales inválidas");
        }

        String nombreRol = usuario.getRol().getNombreRol();
        System.out.println("Usuario encontrado: " + usuario.getCorreo() + " - Rol: " + nombreRol);

        return User.withUsername(usuario.getCorreo())
                .password(usuario.getPassword())
                .authorities(new SimpleGrantedAuthority("ROLE_" + nombreRol))
                .build();
    }
}
