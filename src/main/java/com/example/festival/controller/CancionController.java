package com.example.festival.controller;

import com.example.festival.dto.NuevaAsignacionCancion;
import com.example.festival.entity.AsignacionCancion;
import com.example.festival.entity.Cancion;
import com.example.festival.entity.Usuario;
import com.example.festival.repository.AsignacionRepository;
import com.example.festival.repository.CancionRepository;
import com.example.festival.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Optional;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/canciones")
public class CancionController {

    final UsuarioRepository usuarioRepository;
    final AsignacionRepository asignacionRepository;
    final CancionRepository cancionRepository;

    public CancionController(UsuarioRepository usuarioRepository, AsignacionRepository asignacionRepository, CancionRepository cancionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.asignacionRepository = asignacionRepository;
        this.cancionRepository = cancionRepository;
    }

    @GetMapping
    public String verCancionAsignada(Model model, Authentication auth, HttpSession session) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName());

        AsignacionCancion asignacion = asignacionRepository.findByUsuario_Id(usuario.getId());

        if (asignacion != null) {

            if (asignacion.getCancion() == null) {
                model.addAttribute("solicitada", true);
            } else {
                model.addAttribute("solicitada", true);
                model.addAttribute("asignacion", asignacion);

                if (session.getAttribute("intentos") == null) {
                    session.setAttribute("intentos", 0);
                }
                model.addAttribute("intentos", session.getAttribute("intentos"));

                if (session.getAttribute("progreso") == null) {
                    char[] progreso = new char[asignacion.getCancion().getTitulo().length()];
                    String titulo = asignacion.getCancion().getTitulo();
                    for (int i = 0; i < titulo.length(); i++) {
                        if (titulo.charAt(i) == ' ') progreso[i] = ' ';
                        else progreso[i] = '_';
                    }
                    session.setAttribute("progreso", progreso);
                }
                model.addAttribute("progreso", session.getAttribute("progreso"));
                if (session.getAttribute("posCorrecta") == null) {
                    session.setAttribute("posCorrecta", 0);
                }
                model.addAttribute("posCorrecta", session.getAttribute("posCorrecta"));
                if (session.getAttribute("letrasTitutlo") == null) {
                    session.setAttribute("letrasTitutlo", 0);
                }
                model.addAttribute("letrasTitutlo", session.getAttribute("letrasTitutlo"));

            }
        } else {
            model.addAttribute("solicitada", false);
        }

        return "verCancionAsignada";
    }

    @PostMapping("/solicitar")
    public String solicitarAsignacionCancion(Model model, Authentication auth) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName());
        AsignacionCancion asignacion = asignacionRepository.findByUsuario_Id(usuario.getId());

        if (asignacion != null) {
          if (asignacion.getAdivinada() != null && !asignacion.getAdivinada()) {
              return "redirect:/canciones";
          }
        }

        AsignacionCancion nuevaAsignacion = new AsignacionCancion(usuario);
        asignacionRepository.save(nuevaAsignacion);

        return "redirect:/canciones";
    }

    @PostMapping("/adivinar")
    public String adivinarCancion(@RequestParam String adivinar, Model model, Authentication auth, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName());

        AsignacionCancion asignacion = asignacionRepository.findByUsuario_Id(usuario.getId());

        if (asignacion != null && asignacion.getCancion() != null) {
            String tituloCancion = asignacion.getCancion().getTitulo();
            Integer intentos = (Integer) session.getAttribute("intentos");
            char[] progreso = (char[]) session.getAttribute("progreso");

            int posCorrecta = 0;
            int letrasTitutlo = 0;
            if (tituloCancion.equals(adivinar)) {
                asignacion.setAdivinada(Boolean.TRUE);
                asignacion.setIntentos(intentos+1);
                asignacionRepository.save(asignacion);
                redirectAttributes.addFlashAttribute("msg", "Haz adivinado el título de tu cancion en "+(intentos+1)+" intentos");
            } else {

                int iter = Math.min(tituloCancion.length(), adivinar.length());
                for (int i = 0; i < iter; i++) {
                    if (tituloCancion.charAt(i) == adivinar.charAt(i)) {
                        progreso[i] = tituloCancion.charAt(i);
                        posCorrecta++;
                        continue;
                    }
                    for (int j = i + 1; j < iter; j++) {
                        if (tituloCancion.charAt(i) == adivinar.charAt(j)) {
                            letrasTitutlo++;
                        }
                    }
                }

            }

            intentos=intentos+1;

            session.setAttribute("letrasTitutlo", letrasTitutlo);
            session.setAttribute("posCorrecta", posCorrecta);
            session.setAttribute("progreso", progreso);
            session.setAttribute("intentos", intentos);
            session.setAttribute("asignacion", asignacion);

        } else return "redirect:/canciones";

        return "redirect:/canciones";
    }



    @GetMapping("/listaAsignaciones")
    @PreAuthorize("hasRole('ADMIN')")
    public String listaAsignaciones(Model model) {
        model.addAttribute("asignaciones", asignacionRepository.findAll());
        return "listaAsignaciones";
    }

    @GetMapping("/nuevaAsignacion")
    @PreAuthorize("hasRole('ADMIN')")
    public String crearAsignacionNueva(Model model) {

        model.addAttribute("nuevaAsignacionDto", new NuevaAsignacionCancion());
        model.addAttribute("listaCanciones", cancionRepository.findAll());
        model.addAttribute("listaUsuariosSolicitantes", asignacionRepository.findByCancionNull());

        return "nuevaAsignacion";
    }

    @PostMapping("/asignar")
    @PreAuthorize("hasRole('ADMIN')")
    public String creaOeditarAsignacion(Model model, Authentication auth, @ModelAttribute NuevaAsignacionCancion nuevaAsignacionDto, RedirectAttributes redirectAttributes) {

        Optional<Usuario> usuario = usuarioRepository.findById(nuevaAsignacionDto.getIdUsuario());
        Optional<Cancion> cancion = cancionRepository.findById(nuevaAsignacionDto.getIdCancion());

        if (usuario.isPresent() && cancion.isPresent()) {
            AsignacionCancion nuevaAsignacion = new AsignacionCancion(usuario.get(), cancion.get());
            asignacionRepository.save(nuevaAsignacion);
            redirectAttributes.addFlashAttribute("msg", "Asignación creada correctamente");

        } else {
            model.addAttribute("nuevaAsignacionDto", nuevaAsignacionDto);
            model.addAttribute("listaCanciones", cancionRepository.findAll());
            model.addAttribute("listaUsuariosSolicitantes", asignacionRepository.findByCancionNull());
            return "nuevaAsignacion";
        }

        return "redirect:/canciones/listaAsignaciones";
    }

}
