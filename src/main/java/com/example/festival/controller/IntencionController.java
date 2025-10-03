package com.example.festival.controller;

import com.example.festival.entity.Heroe;
import com.example.festival.entity.Intencion;
import com.example.festival.entity.Usuario;
import com.example.festival.repository.IntencionRepository;
import com.example.festival.repository.UsuarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/peticion")
public class IntencionController {

    final IntencionRepository intencionRepository;
    final UsuarioRepository usuarioRepository;

    public IntencionController(IntencionRepository intencionRepository, UsuarioRepository usuarioRepository) {
        this.intencionRepository = intencionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/")
    public String intencion(Model model, @ModelAttribute Intencion intencion, Authentication auth) {

        Usuario usuario = usuarioRepository.findByCorreo(auth.getName());

        if (intencionRepository.existsByUsuario_Id(usuario.getId())) {
            model.addAttribute("intencion", intencionRepository.findByUsuario_Id(usuario.getId()));
            return "verIntencion";
        } else {
            model.addAttribute("intencion", intencion);
            return "intencionForm";
        }


    }

    @PostMapping("/guardar")
    public String crearIntencion(Model model, @ModelAttribute Intencion intencion, RedirectAttributes redirectAttributes, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "intencionForm";
        }


        model.addAttribute("intencion", intencion);
        return "heroes";

    }

    @GetMapping("/lista")
    public String listaIntenciones(Model model) {
        model.addAttribute("intenciones", intencionRepository.findAll());
        return "intenciones";
    }
}
