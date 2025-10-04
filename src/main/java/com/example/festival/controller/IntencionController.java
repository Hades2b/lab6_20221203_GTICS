package com.example.festival.controller;

import com.example.festival.entity.Heroe;
import com.example.festival.entity.Intencion;
import com.example.festival.entity.Usuario;
import com.example.festival.repository.IntencionRepository;
import com.example.festival.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/intencion")
public class IntencionController {

    final UsuarioRepository usuarioRepository;
    final IntencionRepository intencionRepository;

    public IntencionController(UsuarioRepository usuarioRepository, IntencionRepository intencionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.intencionRepository = intencionRepository;
    }

    @GetMapping()
    public String intencion(Model model, HttpSession session, Authentication auth) {

        Usuario usuario = usuarioRepository.findByCorreo(auth.getName());

        Intencion intencion = (Intencion) session.getAttribute("intencion");

        if (intencion != null) {
            model.addAttribute("intencion", intencion);
        } else {
            Intencion nuevaIntencion = new Intencion(usuario, LocalDateTime.now());
            model.addAttribute("intencion", nuevaIntencion);
        }
        return "intencion";


    }

    @PostMapping("/guardar")
    public String crearIntencion(Model model, @Valid @ModelAttribute Intencion intencion, BindingResult bindingResult, HttpSession session, Authentication auth) {

        if (bindingResult.hasErrors()) {
            return "intencion";
        }
        List<String> badWords = Arrays.asList("odio", "pelea", "violencia", "matar");
        for (String bad : badWords) {
            if (intencion.getDescripcion().toLowerCase().contains(bad)) {
                bindingResult.rejectValue("descripcion", "badword", "La descripción contiene palabras no permitidas");
                return "intencion";
            }
        }

        Usuario usuario = usuarioRepository.findByCorreo(auth.getName());
        intencion.setUsuario(usuario);
        intencion.setFecha(LocalDateTime.now());

        session.setAttribute("intencion", intencion);
        intencionRepository.save(intencion);

        return "redirect:/intencion";

    }

    @GetMapping("/lista")
    @PreAuthorize("hasRole('ADMIN')")
    public String listaIntenciones(Model model) {
        model.addAttribute("intenciones", intencionRepository.findAll());
        return "listaIntenciones";
    }
}
